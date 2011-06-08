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

import javax.inject.Inject;
import javax.inject.Named;

import org.fuin.apps4swing.LoggingUncaughtExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Tool that helps developing the Meta CQRS application itself.
 */
@Named
public final class MetaCqrsDevTool {

    private static final Logger LOG = LoggerFactory.getLogger(MetaCqrsDevTool.class);

    @Inject
    private MetaCqrsDevService service;

    /**
     * Private constructor.
     */
    private MetaCqrsDevTool() {
        super();
    }

    /**
     * Deletes the query data and replays the events of all queries to populate
     * the data of the queries.
     */
    public final void populateAllQueries() {
        LOG.debug("populateAllQueries()");
        service.populateAllQueries();
    }

    /**
     * Main program.
     * 
     * @param args
     *            Command line arguments.
     */
    public static void main(final String[] args) {

        // Check direct with LOG4J if any logging is enabled - This is important
        // because we catch all exceptions and log them. This means we will see
        // nothing if the logging does not work...
        final org.apache.log4j.Logger log4jRootLogger = org.apache.log4j.Logger.getRootLogger();
        if (!log4jRootLogger.getAllAppenders().hasMoreElements()) {
            System.err.println("LOG4J is not initialized!");
            System.exit(3);
        }

        // Redirect exceptions to the log
        Thread.setDefaultUncaughtExceptionHandler(new LoggingUncaughtExceptionHandler());

        LOG.debug("main(...)");

        final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "application-context.xml");
        final MetaCqrsDevTool tool = ctx.getBean(MetaCqrsDevTool.class);
        try {
            tool.populateAllQueries();
            LOG.debug("exit(0)");
            System.exit(0);
        } catch (final RuntimeException ex) {
            LOG.debug("exit(1)", ex);
            System.exit(1);
        }

    }

}
