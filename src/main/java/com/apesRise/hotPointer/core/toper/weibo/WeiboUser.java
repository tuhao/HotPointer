package com.apesRise.hotPointer.core.toper.weibo;

public class WeiboUser {
	private String id;
	private String name;
	private String profile_image_url;
	private long followers_count;
	private long friends_count;
	private long statuses_count;
	private long favourites_count;
	private long bi_followers_count;
	private int province;
	private int city;
	private String location;

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

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public long getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(long followers_count) {
		this.followers_count = followers_count;
	}

	public long getFriends_count() {
		return friends_count;
	}

	public void setFriends_count(long friends_count) {
		this.friends_count = friends_count;
	}

	public long getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(long statuses_count) {
		this.statuses_count = statuses_count;
	}

	public long getFavourites_count() {
		return favourites_count;
	}

	public void setFavourites_count(long favourites_count) {
		this.favourites_count = favourites_count;
	}

	public long getBi_followers_count() {
		return bi_followers_count;
	}

	public void setBi_followers_count(long bi_followers_count) {
		this.bi_followers_count = bi_followers_count;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
