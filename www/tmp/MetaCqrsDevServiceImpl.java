/**
 * Copyright (C) 2011 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.metacqrs.zdev;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.axonframework.domain.DomainEvent;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventstore.EventStoreManagement;
import org.axonframework.eventstore.EventVisitor;
import org.fuin.metacqrs.query.server.lib.base.ManagerOfQuery;
import org.fuin.metacqrs.query.server.lib.base.QueryManager;
import org.fuin.metacqrs.query.server.lib.manager.FindAggregatePropertiesQueryManager;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Administration service.
 */
@Named
public class MetaCqrsDevServiceImpl implements MetaCqrsDevService {

    private static final Logger LOG = LoggerFactory.getLogger(MetaCqrsDevServiceImpl.class);

    @Inject
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private ApplicationContext ctx;

    /**
     * Creates a reflection object for finding query manager classes.
     * 
     * @param packageName
     *            Name of the package to scan.
     * 
     * @return Reflection object.
     */
    private static Reflections createReflections(final String packageName) {
        final ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        final Set<URL> urls = getUrls(packageName);
        configurationBuilder.setUrls(urls);
        configurationBuilder.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner());
        final FilterBuilder filterBuilder = new FilterBuilder();
        filterBuilder.include(FilterBuilder.prefix(packageName));
        configurationBuilder.filterInputsBy(filterBuilder);
        final Reflections reflections = new Reflections(configurationBuilder);
        return reflections;
    }

    /**
     * Returns the URLs for the given package. This is a workaround for a
     * strange behavior of
     * {@link ClasspathHelper#getUrlsForPackagePrefix(String)}. It does not work
     * out-of-the-box when run as Maven test. It contains the package name in
     * the URL. That package name part is stripped by this method.
     * 
     * @param packageName
     *            Name of the package to find URLs for.
     * 
     * @return List of URLs.
     */
    private static Set<URL> getUrls(final String packageName) {
        final Set<URL> urls = new HashSet<URL>();
        final String packagePath = packageName.replace('.', '/');
        final int packageLen = packagePath.length();
        final Set<URL> foundUrls = ClasspathHelper.getUrlsForPackagePrefix(packageName);
        for (URL url : foundUrls) {
            final String urlStr = url.toString();
            if (urlStr.endsWith(packagePath)) {
                try {
                    final URL newUrl = new URL(urlStr.substring(0, urlStr.length() - packageLen));
                    urls.add(newUrl);
                } catch (final MalformedURLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                urls.add(url);
            }
        }
        return urls;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void populateAllQueries() {
        LOG.debug("populateAllQueries()");

        final Reflections reflections = createReflections(FindAggregatePropertiesQueryManager.class
                .getPackage().getName());
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ManagerOfQuery.class);

        // Create query manager info list
        final List<QueryManagerInfo> queryManagerInfos = new ArrayList<QueryManagerInfo>();
        for (final Class<?> clasz : classes) {
            final QueryManagerInfo qmi = createManagerInfo(clasz);
            queryManagerInfos.add(qmi);
        }

        // TODO Replay should NOT be done one query by one (Performance!)...

        for (final QueryManagerInfo qmi : queryManagerInfos) {
            qmi.queryManager.deleteData();
        }
        entityManager.flush();
        for (final QueryManagerInfo qmi : queryManagerInfos) {
            replayEvents(qmi);
        }

    }

    @SuppressWarnings("unchecked")
    private QueryManagerInfo createManagerInfo(final Class<?> queryManagerClass) {

        final QueryManager queryManager = (QueryManager) ctx.getBean(queryManagerClass);
        if (queryManager == null) {
            throw new IllegalStateException("Query manager not found: "
                    + queryManagerClass.getName());
        }

        final Class<?> realQueryManagerClass;
        if (queryManager instanceof Advised) {
            final Advised advised = (Advised) queryManager;
            realQueryManagerClass = advised.getTargetClass();
        } else {
            realQueryManagerClass = queryManager.getClass();
        }

        final QueryManagerInfo info = new QueryManagerInfo(queryManager);

        final Method[] methods = realQueryManagerClass.getMethods();
        for (final Method method : methods) {
            if (method.getAnnotation(EventHandler.class) != null) {
                final Class<?> arg = method.getParameterTypes()[0];
                info.eventList.add((Class<? extends DomainEvent>) arg);
                info.eventMethodMap.put((Class<? extends DomainEvent>) arg, method.getName());
            }
        }

        if (info.eventList.size() == 0) {
            throw new IllegalArgumentException("Query manager class '"
                    + realQueryManagerClass.getSimpleName() + "' has no methods annotated with '@"
                    + EventHandler.class.getSimpleName() + "'!");
        }

        return info;

    }

    private void replayEvents(final QueryManagerInfo info) {
        final EventStoreManagement eventStore = ctx.getBean("eventStore",
                EventStoreManagement.class);
        eventStore.visitEvents(new EventVisitor() {
            @Override
            public void doWithEvent(final DomainEvent domainEvent) {
                if (info.eventList.contains(domainEvent.getClass())) {
                    handleEvent(info, domainEvent);
                } else {
                    LOG.trace("Ignored " + domainEvent.getClass());
                }
            }
        });
    }

    private void handleEvent(final QueryManagerInfo info, final DomainEvent event) {
        LOG.debug("handleEvent(" + info.getClass().getSimpleName() + ", "
                + event.getClass().getSimpleName() + ")");

        final String methodName = info.eventMethodMap.get(event.getClass());
        try {
            final Method method = info.queryManager.getClass().getMethod(methodName,
                    event.getClass());
            method.invoke(info.queryManager, event);
        } catch (final IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (final IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (final InvocationTargetException ex) {
            throw new RuntimeException(ex);
        } catch (final SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (final NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Helper object for holding events & methods relevant for a query manager.
     */
    // CHECKSTYLE:OFF Internal helper (Record Styl"... ;-)
    private static class QueryManagerInfo {

        public final QueryManager queryManager;

        public final List<Class<? extends DomainEvent>> eventList = new ArrayList<Class<? extends DomainEvent>>();

        public final Map<Class<? extends DomainEvent>, String> eventMethodMap = new HashMap<Class<? extends DomainEvent>, String>();

        public QueryManagerInfo(final QueryManager queryManager) {
            super();
            this.queryManager = queryManager;
        }

    }
    // CHECKSTYLE:ON

}
