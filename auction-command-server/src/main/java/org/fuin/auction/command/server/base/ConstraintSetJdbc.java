/*
 * Copyright (c) 2010. Axon Auction Example
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fuin.auction.command.server.base;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.inject.Named;
import javax.sql.DataSource;

import org.fuin.auction.command.api.exceptions.InternalErrorException;
import org.fuin.auction.command.api.exceptions.UserEmailAlreadyExistException;
import org.fuin.auction.command.api.exceptions.UserNameAlreadyExistException;
import org.fuin.auction.command.api.exceptions.UserNameEmailCombinationAlreadyExistException;
import org.fuin.auction.common.Utils;
import org.fuin.objects4j.EmailAddress;
import org.fuin.objects4j.UserName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Pure JDBC constraint service (Apache Derby!). Could be done more nice
 * (database independent for example...) but it's just to show that there is no
 * need that for "full-blown" Hibernate or JPA here...
 */
@Named
public final class ConstraintSetJdbc implements ConstraintSet {

	private static final Logger LOG = LoggerFactory.getLogger(ConstraintSetJdbc.class);

	private final DataSource dataSource;

	/**
	 * Default constructor.
	 */
	public ConstraintSetJdbc() {
		try {
			final Properties jdbcProperties = Utils.loadProperties(this.getClass(),
			        "/jdbc.properties");
			final ComboPooledDataSource cpds = new ComboPooledDataSource();
			cpds.setDriverClass(jdbcProperties.getProperty("driverclass"));
			cpds.setJdbcUrl(jdbcProperties.getProperty("url"));
			cpds.setUser(jdbcProperties.getProperty("commandserver"));
			cpds.setPassword(jdbcProperties.getProperty("secret"));
			this.dataSource = cpds;
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		} catch (final PropertyVetoException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public final void add(final UserName userName, final EmailAddress email)
	        throws UserNameEmailCombinationAlreadyExistException, UserNameAlreadyExistException,
	        UserEmailAlreadyExistException {

		try {

			if (!insert(userName, email)) {

				final String userNameStr = userName.toString();
				final String emailStr = email.toString();

				final UserNameEmail row = select(userName, email);
				if (row.getUserName().equals(userNameStr)) {
					if (row.getEmail().equals(emailStr)) {
						throw new UserNameEmailCombinationAlreadyExistException(userName, email);
					}
					throw new UserNameAlreadyExistException(userName);
				} else {
					if (row.getEmail().equals(emailStr)) {
						throw new UserEmailAlreadyExistException(email);
					}
					// This should never happen because it doesn't fit to there
					// where clause.
					throw new InternalErrorException("Unexpected result - Requested: userName='"
					        + userName + "', email='" + email + "' / Returned: userName='"
					        + row.getUserName() + "', email='" + row.getEmail() + "'");
				}
			}

		} catch (final SQLException ex) {
			final String message = "Error adding userName/email constraint!";
			LOG.error(message + " [SQLState=" + ex.getSQLState() + ", ErrorCode="
			        + ex.getErrorCode() + "]", ex);
			throw new InternalErrorException(message);
		}

	}

	@Override
	public final void remove(final UserName userName, final EmailAddress email) {

		try {
			final Connection con = dataSource.getConnection();
			try {
				final PreparedStatement stmt = con
				        .prepareStatement("delete from COMMANDSERVER.USERNAME_EMAIL "
				                + "where USER_NAME=? and EMAIL=?");
				try {
					stmt.setString(1, userName.toString());
					stmt.setString(2, email.toString());
					stmt.executeUpdate();
				} finally {
					stmt.close();
				}
			} finally {
				con.close();
			}
		} catch (final SQLException ex) {
			final String message = "Error removing userName/email constraint!";
			LOG.error(message + " [SQLState=" + ex.getSQLState() + ", ErrorCode="
			        + ex.getErrorCode() + "]", ex);
			throw new InternalErrorException(message);
		}

	}

	/**
	 * Tries to insert the userid/email combination and wraps a
	 * "duplicate primary key" exception into a <code>false</code> return value.
	 * 
	 * @param userName
	 *            User's unique name.
	 * @param email
	 *            User's email address.
	 * 
	 * @return If the combination was inserted <code>true</code> else
	 *         <code>false</code> ("duplicate primary key").
	 * 
	 * @throws SQLException
	 *             Other error than "duplicate primary key".
	 */
	private boolean insert(final UserName userName, final EmailAddress email) throws SQLException {
		final Connection con = dataSource.getConnection();
		try {
			final PreparedStatement stmt = con
			        .prepareStatement("insert into COMMANDSERVER.USERNAME_EMAIL (USER_NAME, EMAIL) "
			                + "values (?, ?)");
			try {
				stmt.setString(1, userName.toString());
				stmt.setString(2, email.toString());
				try {
					stmt.executeUpdate();
					return true;
				} catch (final SQLException ex) {
					// Duplicate key value in primary key
					if (!ex.getSQLState().equals("23505")) {
						throw ex;
					}
					return false;
				}
			} finally {
				stmt.close();
			}
		} finally {
			con.close();
		}
	}

	/**
	 * Selects user name and email based on user name or email address.
	 * 
	 * @param userName
	 *            User's unique name to find.
	 * @param email
	 *            Email address to find.
	 * 
	 * @return User name and email.
	 * 
	 * @throws SQLException
	 *             Error executing the select statement.
	 */
	private UserNameEmail select(final UserName userName, final EmailAddress email)
	        throws SQLException {
		final Connection con = dataSource.getConnection();
		try {
			final PreparedStatement stmt = con
			        .prepareStatement("select * from COMMANDSERVER.USERNAME_EMAIL where "
			                + "USER_NAME=? or EMAIL=?");
			try {
				stmt.setString(1, userName.toString());
				stmt.setString(2, email.toString());
				final ResultSet rs = stmt.executeQuery();
				try {
					if (!rs.next()) {
						throw new InternalErrorException("Neither user name '" + userName
						        + "' nor email '" + email + "' found!");
					}
					return new UserNameEmail(rs.getString("USER_NAME"), rs.getString("EMAIL"));
				} finally {
					rs.close();
				}
			} finally {
				stmt.close();
			}
		} finally {
			con.close();
		}
	}

	/**
	 * Helper class to return result.
	 */
	private static final class UserNameEmail {

		private final String userName;

		private final String email;

		/**
		 * Constructor with user name and email.
		 * 
		 * @param userName
		 *            User name.
		 * @param email
		 *            Email address.
		 */
		public UserNameEmail(final String userName, final String email) {
			super();
			this.userName = userName;
			this.email = email;
		}

		/**
		 * Returns the user name.
		 * 
		 * @return user name.
		 */
		public String getUserName() {
			return userName;
		}

		/**
		 * Returns the email.
		 * 
		 * @return Email address.
		 */
		public String getEmail() {
			return email;
		}

	}

}
