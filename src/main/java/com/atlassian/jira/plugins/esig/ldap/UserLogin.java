package com.atlassian.jira.plugins.esig.ldap;

/**
 * User Login model class which defines various getter and setter methods for
 * user name, distinguished name and canonical name.
 * 
 * @author SK030637
 */
public class UserLogin {

	private String userName = null;
	private String distinguishedName = null;
	private String canonicalName = null;

	public UserLogin(String userName, String distinguishedName,
			String canonicalName) {
		this.userName = userName;
		this.distinguishedName = distinguishedName;
		this.canonicalName = canonicalName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDistinguishedName() {
		return distinguishedName;
	}

	public void setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	public void setCanonicalName(String canonicalName) {
		this.canonicalName = canonicalName;
	}

}
