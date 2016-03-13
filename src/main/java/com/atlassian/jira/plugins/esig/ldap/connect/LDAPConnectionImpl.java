package com.atlassian.jira.plugins.esig.ldap.connect;

import java.security.GeneralSecurityException;

import javax.net.SocketFactory;

import com.atlassian.jira.plugins.esig.ldap.Connection;
import com.atlassian.system.exception.Verifier;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

/**
 * This class implements the {@link Connection} interface which provides the necessary
 * methods for connecting and authenticating the user.
 * 
 * @author SK030637
 * @version 0.1
 */
public class LDAPConnectionImpl implements Connection {

	private LDAPConnection connection;
	final String CERNER_DOT_NET = "@ATLASSIAN.NET";

	/**
	 * Sets the server address and port for the LDAPConnection.
	 * 
	 * @throws GeneralSecurityException
	 *             Problem occurs when creating or initializing SSLSocket
	 *             factory
	 */
	public LDAPConnectionImpl() throws GeneralSecurityException {

		SSLUtil sslUtil = new SSLUtil(null, new TrustAllTrustManager());

		SocketFactory socketFactory = sslUtil.createSSLSocketFactory();
		connection = new LDAPConnection(socketFactory);
	}

	/**
	 * Sets the LDAP connection object with the provided {@link LDAPConnection}
	 * object.
	 */
	LDAPConnectionImpl(LDAPConnection connection) {

		this.connection = connection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean connect(String serverAddress, int serverPort)
			throws LDAPException {

		Verifier.verifyNotBlank(serverAddress,
				"Server Address must be provided.");
		Verifier.verifyPositive(serverPort, "Server port must be provided.");

		connection.connect(serverAddress, serverPort);

		return connection.isConnected();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean authenticate(String userName, String password) throws LDAPException {

		Verifier.verifyNotBlank(userName, "User name must be provided.");
		Verifier.verifyNotBlank(password, "Password must be provided.");

		String concatenatedUserName = userName.toUpperCase() + CERNER_DOT_NET;

		BindResult bindResult = connection.bind(concatenatedUserName, password);
		if (bindResult == null){
			return false;
		}

		return ResultCode.SUCCESS.equals(bindResult.getResultCode());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean disconnect() {

		if (connection != null) {
			connection.close();
			return true;
		}
		return false;
	}

}