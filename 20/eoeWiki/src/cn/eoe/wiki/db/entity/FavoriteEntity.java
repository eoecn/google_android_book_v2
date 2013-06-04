package cn.eoe.wiki.db.entity;
/**
 * 用来表示favorite的实体
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-11
 * @version 1.0.0
 */
public class FavoriteEntity extends DataBaseEntity {
	private String		title;
	private String		revid;
	private String		url;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getRevid() {
		return revid;
	}
	public void setRevid(String revid) {
		this.revid = revid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
