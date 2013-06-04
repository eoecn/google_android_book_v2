package cn.eoe.wiki.db.entity;
/**
 * database 的基本实体。
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-11
 * @version 1.0.0
 */
public class DataBaseEntity {
	private long addDate;
	private long modifyDate;
	private long   id;
	public long getAddDate() {
		return addDate;
	}
	public void setAddDate(long addDate) {
		this.addDate = addDate;
	}
	public long getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(long modifyDate) {
		this.modifyDate = modifyDate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
}
