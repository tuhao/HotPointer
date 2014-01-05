package com.apesRise.hotPointer.core.toper.weibo;

public class WeiboRetweeted {
	private String idstr;
	private String mid;
	private String text;
	private String source;
	private WeiboUser user;
	private int reposts_count;
	private int comments_count;
	private int attitudes_count;
	private String bmiddle_pic;

	public String getBmiddle_pic() {
		return bmiddle_pic;
	}

	public void setBmiddle_pic(String bmiddle_pic) {
		this.bmiddle_pic = bmiddle_pic;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public WeiboUser getUser() {
		return user;
	}

	public void setUser(WeiboUser user) {
		this.user = user;
	}

	public int getReposts_count() {
		return reposts_count;
	}

	public void setReposts_count(int reposts_count) {
		this.reposts_count = reposts_count;
	}

	public int getComments_count() {
		return comments_count;
	}

	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}

	public int getAttitudes_count() {
		return attitudes_count;
	}

	public void setAttitudes_count(int attitudes_count) {
		this.attitudes_count = attitudes_count;
	}


	public Double getTime() {
 
		double userScore = (getUser().getFollowers_count() / (getUser().getFriends_count()+1)) +1;
		double msgScore = ((getAttitudes_count()+1)*2 +(getComments_count()+1)*1.5+getReposts_count())/3;
		
		return userScore*msgScore;
	}
	
}
