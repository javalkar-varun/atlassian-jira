package com.atlassian.jira.plugins.esig.ldap;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;

/**
 * Any LDAP connection class must implement this interface in order to connect to
 * LDAP hosted server and to authenticate the user.
 * 
 * Every LDAP connection class has to connect to server, on successful
 * establishment of connection user will be authenticated. The connection needs
 * to be disconnected once the authentication been finished.
 * 
 * @author SK030637
 * @version 0.1
 */
public interface Connection {

	/**
	 * Creates and establishes LDAPConnection so that users can be authenticated
	 * on success.
	 * @param serverAddress LDAP server address.
	 * @param serverPort LDAP server port number.
	 * 
	 * @return boolean On successful connection returns true if not returns false.
	 * @throws LDAPException
	 *             If there is any error while establishing the connection.
	 */
	public boolean connect(String serverAddress, int serverPort) throws LDAPException;

	/**
	 * Creates the LDAPConnection for the specific user logging in, and binds
	 * the user's credentials to {@link LDAPConnection}.
	 * 
	 * @param userName
	 *            The user name to authenticate with the LDAP system.
	 * @param password
	 *            The user's password to check with the LDAP system.
	 * @return boolean The connection status showing whether the user has been
	 *         successfully authenticated or not.
	 * @throws LDAPException
	 *             If there is an error authenticating with the username and
	 *             password supplied.
	 */
	public boolean authenticate(String userName, String password)
			throws LDAPException;

	/**
	 * Disconnects the LDAP connection.
	 * 
	 * @return boolean On successful disconnection returns true if not returns false.
	 */
	public boolean disconnect();

}