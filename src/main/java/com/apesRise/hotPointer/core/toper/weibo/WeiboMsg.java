package com.apesRise.hotPointer.core.toper.weibo;

import com.apesRise.hotPointer.core.toper.MaxTopInterface;

public class WeiboMsg implements MaxTopInterface<Double>{
	private String idstr;
	private String mid;
	private String text;
	private String source;
	private WeiboUser user;
	private int reposts_count;
	private int comments_count;
	private int attitudes_count;
	private WeiboRetweeted retweeted_status;

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

	public WeiboRetweeted getRetweeted_status() {
		return retweeted_status;
	}

	public void setRetweeted_status(WeiboRetweeted retweeted_status) {
		this.retweeted_status = retweeted_status;
	}

	@Override
	public Double getTime() {
		//userS = (followers_count / (friends_count+1)) +1 => [1,+∞]
		//msg = ((attitudes_count+1)*2 +(comments_count+1)*1.5+reposts_count)  / 3
		//reuserS = (followers_count / (friends_count+1)) +1 => [1,+∞]
		//remsg = ((attitudes_count+1)*2 +(comments_count+1)*1.5+reposts_count)  / 3
		//results = (userS * msg)+(reuserS+remsg)
		
		double userScore = (getUser().getFollowers_count() / (getUser().getFriends_count()+1)) +1;
		double msgScore = ((getAttitudes_count()+1)*2 +(getComments_count()+1)*1.5+getReposts_count())/3;
		
		if(getRetweeted_status()==null){
			return userScore*msgScore;
		}else{
			return userScore*msgScore+getRetweeted_status().getTime();
		}
	}

	@Override
	public String getID() {
		if(getRetweeted_status()==null){
			return getIdstr();
		}else{
			return getIdstr()+getRetweeted_status().getIdstr();
		}
	}
}
