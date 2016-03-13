package com.cerner.jira.plugins.esig.ldap.connect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.security.GeneralSecurityException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cerner.jira.plugins.esig.ldap.connect.LDAPConnectionImpl;
import com.cerner.system.exception.VerifyException;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;

/**
 * This class {@link LDAPConnectionImplTest} is unit UsernamePasswordValidation
 * {@link LDAPConnectionImpl} class functionality.
 * 
 * @author Varun Javalkar
 */

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.net.*")
@PrepareForTest({ LDAPConnection.class })
public class LDAPConnectionImplTest {

	private final int SERVER_PORT_VALID = 636;

	private final String SERVERNAME_VALID = "servername";
	private final String USERNAME_VALID = "USERNAME";
	private final String USERNAME_INVALID = "invalid_username";
	private final String PASSWORD_VALID = "password";
	private final String PASSWORD_INVALID = "invalid_password";
	private final String STRING_EMPTY = "";
	private final String STRING_BLANK = "     ";
	private final String STRING_NULL = null;

	/**
	 * Test whether the LDAPConnection object is null or not. Test method for
	 * {@link LDAPConnectionImpl#LDAPConnectionImpl()}
	 * 
	 * @throws GeneralSecurityException
	 *             Problem occurs when creating or initializing SSLSocket
	 *             factory
	 */
	@Test
	public void testLDAPConnectionImplConstructorDefaultNotNull() throws GeneralSecurityException {

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl();

		assertNotNull(ldapConnection);
	}

	/**
	 * Test whether the LDAPConnection object is null or not. Test method for
	 * {@link LDAPConnectionImpl#LDAPConnectionImpl(LDAPConnection)}
	 */
	@Test
	public void testLDAPConnectionImplConstructorLDAPConnectionNotNull() {

		LDAPConnection connection = new LDAPConnection();
		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl(connection);

		assertNotNull(ldapConnection);
	}

	/**
	 * Test case to check weather the method behaves for null server address.
	 * Test method for {@link LDAPConnectionImpl#connect(String, int)}
	 * 
	 * @throws LDAPException
	 *             When there is error establishing connection.
	 * @throws GeneralSecurityException
	 *             Problem occurs when creating or initializing SSLSocket
	 *             factory
	 */
	@Test(expected = VerifyException.class)
	public void testLDAPConnectionImplConnectUsernameNull()
			throws LDAPException, GeneralSecurityException {

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl();
		ldapConnection.connect(STRING_NULL, SERVER_PORT_VALID);
	}

	/**
	 * Test case to check weather the method behaves for empty server address.
	 * Test method for {@link LDAPConnectionImpl#connect(String, int)}
	 * 
	 * @throws LDAPException
	 *             When there is error establishing connection.
	 * @throws GeneralSecurityException
	 *             Problem occurs when creating or initializing SSLSocket
	 *             factory
	 */
	@Test(expected = VerifyException.class)
	public void testLDAPConnectionImplConnectUsernameEmpty()
			throws LDAPException, GeneralSecurityException {

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl();
		ldapConnection.connect(STRING_EMPTY, SERVER_PORT_VALID);
	}

	/**
	 * Test case to check weather the method behaves for blank server address.
	 * Test method for {@link LDAPConnectionImpl#connect(String, int)}
	 * 
	 * @throws LDAPException
	 *             When there is error establishing connection.
	 * @throws GeneralSecurityException
	 *             Problem occurs when creating or initializing SSLSocket
	 *             factory
	 */
	@Test(expected = VerifyException.class)
	public void testLDAPConnectionImplConnectUsernameBlank()
			throws LDAPException, GeneralSecurityException {

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl();
		ldapConnection.connect(STRING_BLANK, SERVER_PORT_VALID);
	}

	/**
	 * Test case to check weather the method behaves for negative server port
	 * number. Test method for {@link LDAPConnectionImpl#connect(String, int)}
	 * 
	 * @throws LDAPException
	 *             When there is error establishing connection.
	 * @throws GeneralSecurityException
	 *             Problem occurs when creating or initializing SSLSocket
	 *             factory
	 */
	@Test(expected = VerifyException.class)
	public void testLDAPConnectionImplConnectServerPort() throws LDAPException, GeneralSecurityException {

		final int SERVER_PORT_INVALID = -1;
		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl();
		ldapConnection.connect(STRING_BLANK, SERVER_PORT_INVALID);
	}

	/**
	 * Test case to check whether the LDAP Connection is successful with Good
	 * Attributes. Test method for {@link LDAPConnectionImpl#connect(String, int)}
	 * 
	 * @throws LDAPException
	 *             When there is error establishing a connection.
	 */
	@Test
	public void testLDAPConnectionImplConnectGood() throws LDAPException {

		LDAPConnection connection = mock(LDAPConnection.class);
		when(connection.isConnected()).thenReturn(true);

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl(connection);		
		boolean result = ldapConnection.connect(SERVERNAME_VALID, SERVER_PORT_VALID);

		assertTrue(result);
	}

	/**
	 * Test case to check whether the LDAP Connection is failing with Bad
	 * Attributes. Test method for {@link LDAPConnectionImpl#connect(String, int)}
	 * 
	 * @throws LDAPException
	 *             When there is error establishing a connection.
	 */
	@Test
	public void testLDAPConnectionImplConnectBad() throws LDAPException {

		LDAPConnection connection = mock(LDAPConnection.class);
		when(connection.isConnected()).thenReturn(false);

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl(connection);		
		boolean result = ldapConnection.connect(SERVERNAME_VALID, SERVER_PORT_VALID);

		assertFalse(result);
	}

	/**
	 * Test case to check the how the method behaves for null username. Test
	 * method for
	 * {@link LDAPConnectionImpl#authenticate(String, String)}
	 * 
	 * @throws LDAPException
	 *             Error while performing authentication.
	 * @throws GeneralSecurityException
	 *             Problem occurs when creating or initializing SSLSocket
	 *             factory
	 */
	@Test(expected = VerifyException.class)
	public void testLDAPConnectionImplAuthenticateUsernameNull()
			throws LDAPException, GeneralSecurityException {

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl();
		ldapConnection.authenticate(STRING_NULL, PASSWORD_VALID);
	}

	/**
	 * Test case to check the how the method behaves for empty username. Test
	 * method for
	 * {@link LDAPConnectionImpl#authenticate(String, String)}
	 * 
	 * @throws LDAPException
	 *             Error while performing authentication.
	 * @throws GeneralSecurityException
	 *             Problem occurs when creating or initializing SSLSocket
	 *             factory
	 */
	@Test(expected = VerifyException.class)
	public void testLDAPConnectionImplAuthenticateUsernameEmpty()
			throws LDAPException, GeneralSecurityException {

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl();
		ldapConnection.authenticate(STRING_EMPTY, PASSWORD_VALID);
	}

	/**
	 * Test case to check the how the method behaves for blank username. Test
	 * method for
	 * {@link LDAPConnectionImpl#authenticate(String, String)}
	 * 
	 * @throws LDAPException
	 *             Error while performing authentication.
	 * @throws GeneralSecurityException
	 *             Problem occurs when creating or initializing SSLSocket
	 *             factory
	 */
	@Test(expected = VerifyException.class)
	public void testLDAPConnectionImplAuthenticateUsernameBlank()
			throws LDAPException, GeneralSecurityException {

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl();
		ldapConnection.authenticate(STRING_BLANK, PASSWORD_VALID);
	}

	/**
	 * Test case to check the how the method behaves for null password. Test
	 * method for
	 * {@link LDAPConnectionImpl#authenticate(String, String)}
	 * 
	 * @throws LDAPException
	 *             Error while performing authentication.
	 * @throws GeneralSecurityException
	 *             Problem occurs when creating or initializing SSLSocket
	 *             factory
	 */
	@Test(expected = VerifyException.class)
	public void testLDAPConnectionImplAuthenticatePasswordNull()
			throws LDAPException, GeneralSecurityException {

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl();
		ldapConnection.authenticate(USERNAME_VALID, STRING_NULL);
	}

	/**
	 * Test case to check the how the method behaves for empty password. Test
	 * method for
	 * {@link LDAPConnectionImpl#authenticate(String, String)}
	 * 
	 * @throws LDAPException
	 *             Error while performing authentication.
	 * @throws GeneralSecurityException
	 *             Problem occurs when creating or initializing SSLSocket
	 *             factory
	 */
	@Test(expected = VerifyException.class)
	public void testLDAPConnectionImplAuthenticatePasswordEmpty()
			throws LDAPException, GeneralSecurityException {

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl();
		ldapConnection.authenticate(USERNAME_VALID, STRING_EMPTY);
	}

	/**
	 * Test case to check the how the method behaves for empty password. Test
	 * method for
	 * {@link LDAPConnectionImpl#authenticate(String, String)}
	 * 
	 * @throws LDAPException
	 *             Error while performing authentication.
	 * @throws GeneralSecurityException
	 *             Problem occurs when creating or initializing SSLSocket
	 *             factory
	 */
	@Test(expected = VerifyException.class)
	public void testLDAPConnectionImplAuthenticatePasswordBlank()
			throws LDAPException, GeneralSecurityException {

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl();
		ldapConnection.authenticate(USERNAME_VALID, STRING_BLANK);
	}

	/**
	 * Test case to check whether the authentication is behaving with Good
	 * Attributes. Test method for
	 * {@link LDAPConnectionImpl#authenticate(String, String)}
	 * 
	 * @throws LDAPException
	 *             Error while performing authentication.
	 */
	@Test
	public void testLDAPConnectionImplAuthenticateGood() throws LDAPException {

		LDAPConnection connection = mock(LDAPConnection.class);
		BindResult bindResult = new BindResult(new LDAPResult(1,
				ResultCode.SUCCESS));
		when(connection.bind(eq(USERNAME_VALID+"@CERNER.NET"), eq(PASSWORD_VALID))).thenReturn(
				bindResult);

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl(connection);
		boolean result = ldapConnection.authenticate(USERNAME_VALID, PASSWORD_VALID);

		assertTrue(result);
	}

	/**
	 * Test case to check whether the authentication is behaving with Bad
	 * Attributes. Test method for
	 * {@link LDAPConnectionImpl#authenticate(String, String)}
	 * 
	 * @throws LDAPException
	 *             Error while performing authentication.
	 */
	@Test
	public void testLDAPConnectionImplAuthenticateBad() throws Exception {

		LDAPConnection connection = mock(LDAPConnection.class);
		BindResult bindResult = new BindResult(new LDAPResult(1,
				ResultCode.INVALID_CREDENTIALS));
		when(connection.bind(eq(USERNAME_INVALID), eq(PASSWORD_INVALID))).thenReturn(
				bindResult);

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl(connection);
		boolean result = ldapConnection.authenticate(USERNAME_INVALID,
				PASSWORD_INVALID);

		assertFalse(result);
	}

	/**
	 * Test case to check whether the authentication is behaving with Bad
	 * Attributes. Test method for
	 * {@link LDAPConnectionImpl#authenticate(String, String)}
	 * 
	 * @throws LDAPException
	 *             Error while performing authentication.
	 */
	@Test
	public void testLDAPConnectionImplAuthenticateBindResultNull() throws Exception {

		LDAPConnection connection = mock(LDAPConnection.class);
		when(connection.bind(eq(USERNAME_INVALID), eq(PASSWORD_INVALID))).thenReturn(
				null);

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl(connection);
		boolean result = ldapConnection.authenticate(USERNAME_INVALID,
				PASSWORD_INVALID);

		assertFalse(result);
	}

	/**
	 * Test case to check how the LDAP Connection is disconnecting under good
	 * condition. Test method for {@link LDAPConnectionImpl#disconnect()}
	 */
	@Test
	public void testLDAPConnectionImplDisconnectGood() {

		LDAPConnection connection = mock(LDAPConnection.class);

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl(connection);
		boolean result = ldapConnection.disconnect();

		assertTrue(result);
	}

	/**
	 * Test case to check how the LDAP Connection is disconnecting under bad
	 * condition. Test method for {@link LDAPConnectionImpl#disconnect()}
	 */
	@Test
	public void testLDAPConnectionImplDisconnectBad() {

		LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl(null);
		boolean result = ldapConnection.disconnect();

		assertFalse(result);
	}

}