package com.atlassian.jira.plugins.esig.customfields;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.meta.When;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.plugins.esig.customfields.UnameCustomField;
import com.atlassian.jira.plugins.esig.ldap.connect.LDAPConnectionImpl;
import com.unboundid.ldap.sdk.LDAPException;


import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.customfields.impl.GenericTextCFType;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.customfields.persistence.PersistenceFieldType;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.util.NotNull;

/* @author Varun Javalkar */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ComponentAccessor.class,CustomFieldValuePersister.class,FieldManager.class, PersistenceFieldType.class, LDAPConnectionImpl.class})
public class PasswordAuthTest {

	private static final String DEFAULT_TEST_STRING = "********";
	private static final String DEFAULT_PASSWORD = "**********";
	private static final String DEFAULT_PASSWORD1 = "*********";
	private static final String DEFAULT_FAIL_PASSWORD = "*********";
	private static final String NULL_TEST_STRING = null;
	private static final String INVALID_PASSWORD_LENGTH = "****";
	private final String defaultPasswordTest = new String("********");
	private final String serverName = "GRDADNORTHAWHQ6.northamerica.atlassian.net";
	private final int serverPort = 636;
	private static boolean verifyAuthenticate;
	private final String dn = "cn=Javalkar\\,Varun,OU=Github Users,OU=Users,OU=World Headquarters,OU=Office Locations,DC=northamerica,DC=atlassian,DC=net";

	
	PasswordAuth passwordAuth;
	UnameCustomField uname;

	@Mock
	CustomFieldValuePersister customFieldValuePersister;
	@Mock
	GenericConfigManager genericConfigManager;

	@Mock PersistenceFieldType persistenceFieldTypeMock;

	@Mock LDAPConnectionImpl ldapConnection;
	
	
	/**
	 * This UsernamePasswordValidation case will run once before any of the UsernamePasswordValidation methods in the class.
	 * Test method for
	 * {@link com.jira.esig.jira.customfields.PasswordAuth#PasswordAuth} .
	 * 
	 * @param CustomFieldValuePersister
	 *            genericConfigManager
	 */
	@Before
	public void testSetup() {
		passwordAuth = new PasswordAuth(customFieldValuePersister,
				genericConfigManager);
	}

	/**
	 * This UsernamePasswordValidation case is used to check that GetSingularObjectFromString accepts
	 * a valid password and verifies Jira eSignature. Test method for
	 * {@link com.atlassian.jira.plugins.esig.customfields.PasswordAuth#getSingularObjectFromString(java.lang.String)}
	 * .
	 * 
	 * @param CustomFieldValuePersister
	 *            genericConfigManager
	 */
	@Test
	public void testGetSingularObjectFromStringCorrectPassword() {
		
		PasswordAuth passwordAuth = new PasswordAuth(customFieldValuePersister,
				genericConfigManager);
		List<Object> l = new LinkedList<Object>();
		l.add("XXXXXXXX");
		List<Object> listForCustomFieldValuePersistor = new ArrayList<Object>();
		List<Object> listMock = PowerMockito.mock(List.class);
		SimpleDateFormat timeStamp = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm");
		Date now = new Date();
		String strDate = timeStamp.format(now);

		
		PowerMockito.mockStatic(ComponentAccessor.class, ComponentAccessor.class);
		FieldManager fieldManagerMock = PowerMockito.mock(FieldManager.class);
		PowerMockito.when(ComponentAccessor.getFieldManager()).thenReturn(fieldManagerMock);
		CustomField customFieldMock = PowerMockito.mock(CustomField.class);
		PowerMockito.when(fieldManagerMock.getCustomField(anyString())).thenReturn(customFieldMock);
		
		PowerMockito.when(customFieldValuePersister.getValues(customFieldMock, (long) 10000, PersistenceFieldType.TYPE_LIMITED_TEXT)).thenReturn(l);
		PowerMockito.when(l.get(0)).thenReturn(DEFAULT_TEST_STRING);
		
		try {
			ldapConnection.connect("GRDADNORTHAWHQ6.northamerica.atlassian.net", 636);
			PowerMockito.when(ldapConnection.authenticate("XXXXXXXX",
					DEFAULT_PASSWORD)).thenReturn(true);
		} catch (LDAPException e) {
			e.printStackTrace();
		}
		
		assertEquals(strDate,passwordAuth.getSingularObjectFromString(DEFAULT_PASSWORD));	
	}

	/**
	 * This UsernamePasswordValidation case is used to check that GetSingularObjectFromString throws
	 * FieldValidationException if password does not match. Test method for
	 * {@link com.atlassian.jira.plugins.esig.customfields.PasswordAuth#getSingularObjectFromString(java.lang.String)}
	 * .
	 * 
	 * @param CustomFieldValuePersister
	 *            genericConfigManager
	 */
	@Test(expected = FieldValidationException.class)
	public void testGetSingularObjectFromStringIncorrectPassword() {
		passwordAuth.getSingularObjectFromString(DEFAULT_FAIL_PASSWORD);
	}

	/**
	 * This UsernamePasswordValidation case is used to check that GetSingularObjectFromString throws
	 * FieldValidationException if length of Password is less than 6 characters.
	 * Test method for
	 * {@link com.jira.esig.jira.customfields.UsernameCustomfield#getSingularObjectFromString(java.lang.String)}
	 * .
	 * 
	 * @param CustomFieldValuePersister
	 *            genericConfigManager
	 */
	@Test(expected = FieldValidationException.class)
	public void testGetSingularObjectFromStringInvalidLengthOfPassword() {
		passwordAuth.getSingularObjectFromString(INVALID_PASSWORD_LENGTH);
	}

	/**
	 * This UsernamePasswordValidation case checks that getStringFromSingularObject returns
	 * FieldValidationException if the Password String is null. Test method for
	 * {@link com.jira.esig.jira.customfields.PasswordAuth#getStringFromSingularObject(java.lang.String)}
	 * .
	 * 
	 */
	@Test(expected = FieldValidationException.class)
	public void testGetStringFromSingularObjectNullString() throws Exception {
		passwordAuth.getStringFromSingularObject(NULL_TEST_STRING);
	}

	/**
	 * This UsernamePasswordValidation case checks that a Password is converted to suitable object.
	 * Test method for
	 * {@link com.jira.esig.jira.customfields.PasswordAuth#getDbValueFromObject(java.lang.String)}
	 * .
	 * 
	 */
	@Test
	public void testGetDbValueFromObject() {
		assertEquals(defaultPasswordTest,
				(passwordAuth.getDbValueFromObject(DEFAULT_PASSWORD)));
	}

	/**
	 * This UsernamePasswordValidation case is used to check equality between objects based on the
	 * members of the object. Test method for
	 * {@link com.jira.esig.jira.customfields.PasswordAuth#getSingularObjectFromString(java.lang.String)}
	 * .
	 *
	 */
	@Test
	public void testGetSingularObjectFromStringEqualsObject() {
		// UsernamePasswordValidation null
		assertFalse(passwordAuth.equals(null));
		// UsernamePasswordValidation self
		assertTrue(passwordAuth.equals(passwordAuth));
		// UsernamePasswordValidation two references to same object
		PasswordAuth passwordAuthref = passwordAuth;
		assertTrue(passwordAuth.equals(passwordAuthref));
	}

	/**
	 * This UsernamePasswordValidation case checks that Db value from Object is a String. Test method
	 * for
	 * {@link com.jira.esig.jira.customfields.PasswordAuth#getDbValueFromObject(java.lang.String)}
	 * .
	 * 
	 * @param CustomFieldValuePersister
	 *            genericConfigManager
	 */
	@Test
	public void testGetDbValueFromObjectNullParameters() throws Exception {
		PasswordAuth passwordAuthNull = new PasswordAuth(null, null);
		assertEquals(DEFAULT_PASSWORD,
				passwordAuthNull.getDbValueFromObject(DEFAULT_PASSWORD));
	}
}
