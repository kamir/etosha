package com.fblogin.dao;

public class FBUser {
	
	private String id;
	private String name;
	private String lastName; 
	private String firstName; 
	private String gender;
	private String email;
	private String birthday;
	private String website;
	private String hometown;
	private String location;
	private String bio;
	private String about;
	private String link;
	private String locale;
	private String relationshipStatus;
	private String interestedIn;
	private String profilePicLink;

	public FBUser(String id, String name, String lastName, String firstName,
			String gender, String email, String birthday, String website,
			String hometown, String location, String bio, String about,
			String link, String locale, String relationshipStatus,
			String interestedIn) {
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.firstName = firstName;
		this.gender = gender;
		this.email = email;
		this.birthday = birthday;
		this.website = website;
		this.hometown = hometown;
		this.location = location;
		this.bio = bio;
		this.about = about;
		this.link = link;
		this.locale = locale;
		this.relationshipStatus = relationshipStatus;
		this.interestedIn = interestedIn;
		this.profilePicLink = "https://graph.facebook.com/" + this.id + "/picture";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getRelationshipStatus() {
		return relationshipStatus;
	}

	public void setRelationshipStatus(String relationshipStatus) {
		this.relationshipStatus = relationshipStatus;
	}

	public String getInterestedIn() {
		return interestedIn;
	}

	public void setInterestedIn(String interestedIn) {
		this.interestedIn = interestedIn;
	}

	public String getProfilePicLink() {
		return profilePicLink;
	}

	public void setProfilePicLink(String profilePicLink) {
		this.profilePicLink = profilePicLink;
	}

	
}
