package cn.eoe.wiki.db.entity;


/**
 * 用于表示wiki update的实体
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-11
 * @version 1.0.0
 */
public class UpdateEntity extends DataBaseEntity {
	private String uri;
	private long updateDate;
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public long getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(long updateDate) {
		this.updateDate = updateDate;
	}
	
	
}
