package com.atlassian.jira.plugins.esig.customfields;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.anyString;


import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.plugins.esig.customfields.UnameCustomField;


import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.FieldManager;

/**
 * @class This is a JUnit testcase for UnameCustomField class which is used to
 *        store and retrieve Username of Associate.
 * @author Varun Javalkar
 * @version 1.0
 * @see com.atlassian.jira.issue.customfields.*
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ComponentAccessor.class,CustomFieldValuePersister.class,FieldManager.class})
public class UnameCustomFieldTest {
	private static final String DEFAULT_TEST_STRING = "TEST_USERNAME";
	private static final String NULL_TEST_STRING = null;
	private static final String INVALID_USERNAME_TEST_STRING = "TEST";
	private static final String DEFAULT_STRING_OBJECT = "TEST_USERNAME";
	private final String objectOfStringTest = new String("TEST_USERNAME");
	private final String defaultUsernameTest = new String(
			"AB123456");

	UnameCustomField userCustomField;
	UnameCustomField userCustomFieldNull;

	@Mock
	CustomFieldValuePersister customFieldValuePersister;
	@Mock
	GenericConfigManager genericConfigManager;

	
	/**
	 * This test case will run once before any of the test methods in the class. Test method
	 * for
	 * {@link com.jira.esig.jira.customfields.UsernameCustomfield#UnameCustomField}
	 * .
	 * 
	 * @param CustomFieldValuePersister
	 *            genericConfigManager
	 */
	@Before
	public void testSetup() {
		userCustomField = new UnameCustomField(customFieldValuePersister,
				genericConfigManager);
		userCustomFieldNull = new UnameCustomField(null, null);
	}

	/**
	 * This test case checks that Db value from Object is a String. Test method
	 * for
	 * {@link com.jira.esig.jira.customfields.UsernameCustomfield#getDbValueFromObject(java.lang.String)}
	 * .
	 * 
	 * @param CustomFieldValuePersister
	 *            genericConfigManager
	 */
	@Test
	public void testGetDbValueFromObjectNullParameters() throws Exception {
		assertEquals("AB123456",
				userCustomFieldNull.getDbValueFromObject(defaultUsernameTest));
	}

	/**
	 * This test case is used to check that GetSingularObjectFromString throws
	 * FieldValidationException if length of Username is less than 6 characters.
	 * Test method for
	 * {@link com.jira.esig.jira.customfields.UsernameCustomfield#getSingularObjectFromString(java.lang.String)}
	 * .
	 * 
	 * @param CustomFieldValuePersister
	 *            genericConfigManager
	 */
	@Test(expected = FieldValidationException.class)
	public void testGetSingularObjectFromStringInvalidUsername() {
		userCustomField
				.getSingularObjectFromString(INVALID_USERNAME_TEST_STRING);
	}

	/**
	 * This test case checks that a String is converted to suitable object
	 * storing in Db. Test method for
	 * {@link com.jira.esig.jira.customfields.UsernameCustomfield#getDbValueFromObject(java.lang.String)}
	 * .
	 * 
	 * @param CustomFieldValuePersister
	 *            genericConfigManager
	 */
	@Test
	public void testGetDbValueFromObject() {
		assertEquals(objectOfStringTest,
				(userCustomField.getDbValueFromObject(DEFAULT_STRING_OBJECT)));
	}

	
	/**
	 * This test case checks that it accepts valid Username String and returns a
	 * valid Username String after validation Test method for
	 * {@link com.jira.esig.jira.customfields.UsernameCustomfield#getSingularObjectFromString(java.lang.String)}
	 * .
	 * 
	 * @param CustomFieldValuePersister
	 *            genericConfigManager
	 */
	@Test
	public void testGetSingularObjectFromString() {
		PowerMockito.mockStatic(ComponentAccessor.class, ComponentAccessor.class);
		FieldManager fieldManagerMock = PowerMockito.mock(FieldManager.class);
		PowerMockito.when(ComponentAccessor.getFieldManager()).thenReturn(fieldManagerMock);
		CustomField customFieldMock = PowerMockito.mock(CustomField.class);
		PowerMockito.when(fieldManagerMock.getCustomField(anyString())).thenReturn(customFieldMock);
		assertEquals(DEFAULT_TEST_STRING,
				(userCustomField
						.getSingularObjectFromString(DEFAULT_TEST_STRING)));
	}

	/**
	 * This test case checks that GetSingularObjectFromString returns
	 * FieldValidationException if the Username String is null. Test method for
	 * {@link com.jira.esig.jira.customfields.UsernameCustomfield#getSingularObjectFromString(java.lang.String)}
	 * .
	 * 
	 * @param CustomFieldValuePersister
	 *            genericConfigManager
	 */
	@Test(expected = FieldValidationException.class)
	public void testGetSingularObjectFromStringNullString() throws Exception {
		userCustomField.getSingularObjectFromString(NULL_TEST_STRING);
	}

	/**
	 * This test case checks that getStringFromSingularObject returns
	 * FieldValidationException if the Username String is null. Test method for
	 * {@link com.jira.esig.jira.customfields.UsernameCustomfield#getStringFromSingularObject(java.lang.String)}
	 * .
	 * 
	 * @param CustomFieldValuePersister
	 *            genericConfigManager
	 */
	@Test(expected = FieldValidationException.class)
	public void testGetStringFromSingularObjectNullString() throws Exception {
		userCustomField.getStringFromSingularObject(NULL_TEST_STRING);
	}

	/**
	 * This test case is used to check equality between objects based on the
	 * members of the object. Test method for
	 * {@link com.jira.esig.jira.customfields.UsernameCustomfield#getSingularObjectFromString(java.lang.String)}
	 * .
	 * 
	 * @param CustomFieldValuePersister
	 *            genericConfigManager
	 */
	@Test
	public void testGetSingularObjectFromStringEqualsObject() {
		// test null
		assertFalse(userCustomField.equals(null));
		// test self
		assertTrue(userCustomField.equals(userCustomField));
		// test two references to same object
		UnameCustomField userCustomFieldref = userCustomField;
		assertTrue(userCustomField.equals(userCustomFieldref));
	}
}