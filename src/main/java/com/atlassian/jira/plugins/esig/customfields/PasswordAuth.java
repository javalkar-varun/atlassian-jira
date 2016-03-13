package com.atlassian.jira.plugins.esig.customfields;

import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ofbiz.core.entity.GenericEntityException;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.customfields.impl.GenericTextCFType;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.customfields.persistence.PersistenceFieldType;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.plugins.esig.ldap.connect.LDAPConnectionImpl;

import com.unboundid.ldap.sdk.LDAPException;

/**
 * @class A class to accept and validate password of Associate. This class
 *        extends {@link GenericTextCFType} since data is stored and displayed
 *        as a single {@link java.lang.String}
 * @author Varun Javalkar
 * @langversion java 1.6 and above
 * @version 0.1
 * @see com.atlassian.jira.issue.customfields.*
 */

public class PasswordAuth extends GenericTextCFType {
	private final String serverName = "GRDADNORTHAWHQ6.northamerica.atlassian.net";
	private final int serverPort = 636;
	private final String field = "customfield_10000";
	private static boolean isAuthenticate;
	List<Object> listForCustomFieldValuePersistor;
	public String resolveTimeStamp;
	private long issueId;

	/**
	 * Class constructor
	 * 
	 * @param customFieldValuePersister
	 *            This interface is used to save an issue's custom field value
	 *            to the database
	 * @param genericConfigManager
	 *            This interface is used to store Generic configuration values
	 *            for issue's custom field
	 */
	public PasswordAuth(CustomFieldValuePersister customFieldValuePersister,
			GenericConfigManager genericConfigManager) {
		super(customFieldValuePersister, genericConfigManager);
	}

	/**
	 * This method turns the Transport object (String, in our case) into text.
	 * 
	 * @param transport
	 *            object of type {@link java.lang.String}
	 * @return string representation of Singular Object
	 * @throws FieldValidationException
	 *             if object is null
	 */
	@Override
	public String getStringFromSingularObject(final String singularObject) {
		if (singularObject == null)
			throw new FieldValidationException("singularObject cannot be null.");
		else
			return singularObject.toString();
	}

	/**
	 * This method does validation for Password. Pattern matcher checks that
	 * password should: Be between 6 and 40 characters long. Contain at least
	 * one digit. Contain at least one lower case character. Contain at least
	 * one upper case character. Contain at least on special character from [ @
	 * # $ % ! . ].
	 * 
	 * @param stringPassword
	 *            of type {@link java.lang.String}
	 * @return string representation of Singular Object
	 * @throws FieldValidationException
	 *             is thrown if password is null or if password does not match
	 *             regular expression pattern.
	 */

	@Override
	public String getSingularObjectFromString(final String stringPassword)
			throws FieldValidationException {

		FieldManager fieldManager = ComponentAccessor.getFieldManager();
		
		CustomField myCustomField = fieldManager.getCustomField(field);

		try {

			listForCustomFieldValuePersistor = customFieldValuePersister.getValues(myCustomField, issueId,
							PersistenceFieldType.TYPE_LIMITED_TEXT);

			String stringUsername = (String) listForCustomFieldValuePersistor.get(0);

			if (stringPassword == null)
				throw new FieldValidationException("Password cannot be null.");

			if (stringPassword.length() < 6)
				throw new FieldValidationException(
						"Password cannot be less than 6 characters.");

			LDAPConnectionImpl ldapConnection = new LDAPConnectionImpl();
			ldapConnection.connect(serverName, serverPort);

			isAuthenticate = ldapConnection.authenticate(stringUsername,
					stringPassword);

			if (!isAuthenticate) {
				throw new FieldValidationException("Invalid Password!");
			}

			SimpleDateFormat timeStamp = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");
			Date now = new Date();
			String strDate = timeStamp.format(now);
			return strDate;

		} catch (LDAPException ex) {
			throw new FieldValidationException("Not a valid password.");
		} catch (GeneralSecurityException e) {
			throw new FieldValidationException("Not a valid password.");
		}
	}

	@Override
	public Map<String, Object> getVelocityParameters(Issue issue,
			CustomField field, FieldLayoutItem fieldLayoutItem) {
		issueId = issue.getId();
		Map<String, Object> params = super.getVelocityParameters(issue, field,
				fieldLayoutItem);
		params.put("issueid", issueId);
		return params;
	}

	/**
	 * This method will tell JIRA what kind of database column to store the data
	 * in. Here we use macro: {@link PersistenceFieldType.TYPE_LIMITED_TEXT}
	 * 
	 * @return Persistent field value of type TEXT.
	 */
	@Override
	protected PersistenceFieldType getDatabaseType() {
		return PersistenceFieldType.TYPE_LIMITED_TEXT;
	}

	/**
	 * This takes a value from the DB and converts it to our transport object
	 * (String, in our case).
	 * 
	 * @param databaseValue
	 *            of type {@link java.lang.Object}
	 * @return This method returns string representation of databaseValue
	 */
	@Override
	protected String getObjectFromDbValue(final Object databaseValue)
			throws FieldValidationException {
		return getSingularObjectFromString((String) databaseValue);
	}

	/**
	 * It takes a value as our transport object and converts it to an Object.
	 * 
	 * @param customFieldObject
	 *            of type {@link java.lang.String}
	 * @return This method returns string representation of databaseValue
	 */
	@Override
	protected Object getDbValueFromObject(final String customFieldObject) {
		return getStringFromSingularObject(customFieldObject);
	}
}