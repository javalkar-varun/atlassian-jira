package com.atlassian.jira.plugins.esig.customfields;

import java.util.ArrayList;
import java.util.Map;

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

/**
 * @class A class to store and retrieve Username of Associate. This class
 *        extends unknown macro: {@link GenericTextCFType} since data is stored
 *        and displayed as a single {@link java.lang.String}
 * @author Varun Javalkar
 * @langversion java 1.6 and above
 * @version 0.1
 * @see com.atlassian.jira.issue.customfields.*
 */

public class UnameCustomField extends GenericTextCFType {

	private static final String customFieldId = "customfield_10000";
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
	public UnameCustomField(
			CustomFieldValuePersister customFieldValuePersister,
			GenericConfigManager genericConfigManager) {
		super(customFieldValuePersister, genericConfigManager);
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
	 * This method turns the Transport object (String, in our case) into text.
	 * 
	 * @param transport
	 *            object of type {@link java.lang.String}
	 * @return string representation of Singular Object
	 * @throws FieldValidationException
	 *             if object is null
	 */
	@Override
	public String getStringFromSingularObject(final String singularObject)
			throws FieldValidationException {
		if (singularObject == null)
			throw new FieldValidationException("singularObject cannot be null.");
		else
			return singularObject;
	}

	/**
	 * This method does validation for Username and updates the values of custom
	 * field persistently.
	 * 
	 * @param stringUsername
	 *            of type {@link java.lang.String}
	 * @return string representation of Singular Object
	 * @throws FieldValidationException
	 *             is thrown if username is null or if length of username is
	 *             less than 6 characters.
	 */
	@Override
	public String getSingularObjectFromString(final String stringUsername)
			throws FieldValidationException {
		ArrayList<String> arrayListToGetPassword = new ArrayList<String>();
		arrayListToGetPassword.add(stringUsername);

		if (stringUsername == null)
			throw new FieldValidationException("Username cannot be null.");
		
		try {
			if (stringUsername.length() < 6) {
				throw new FieldValidationException(
						"Username cannot be less than 6 characters.");
			}

			FieldManager fieldManager = ComponentAccessor.getFieldManager();
			CustomField myCustomField = fieldManager
					.getCustomField(customFieldId);
			customFieldValuePersister.updateValues(myCustomField, issueId,
					PersistenceFieldType.TYPE_LIMITED_TEXT,
					arrayListToGetPassword);
			return stringUsername;
		} catch (NumberFormatException ex) {
			throw new FieldValidationException("Not a valid Username.");
		}
	}

	/**
	 * This method gets the Issue Id of current Issue context.
	 * 
	 * @param Issue
	 *            of type {@link com.atlassian.jira.issue.Issue} Gets current
	 *            context of Issue.
	 * @param CustomField
	 *            of type {@link com.atlassian.jira.issue.fields.CustomField}
	 * @param FieldLayoutItem
	 *            of type
	 *            {@link com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem}
	 * @return Map representation of velocity parameters
	 * 
	 */
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
	 * This takes a value from the DB and converts it to our transport object
	 * (String, in our case).
	 * 
	 * @param databaseValue
	 *            of type {@link java.lang.Object}
	 * @return This method returns string representation of databaseValue
	 */
	@Override
	protected String getObjectFromDbValue(@NotNull final Object databaseValue)
			throws FieldValidationException {
		return getSingularObjectFromString((String) databaseValue);
	}

	/**
	 * It takes a value as our transport object and converts it to an Object
	 * suitable for storing in the DB.
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