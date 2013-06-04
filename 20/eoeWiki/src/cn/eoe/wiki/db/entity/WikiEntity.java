package cn.eoe.wiki.db.entity;

import java.io.File;
import java.util.UUID;

import android.text.TextUtils;
import cn.eoe.wiki.Constants;
import cn.eoe.wiki.utils.FileUtil;
import cn.eoe.wiki.utils.WikiUtil;

/**
 * 用于表示wiki的实体
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-11
 * @version 1.0.0
 */
public class WikiEntity extends DataBaseEntity {
	private String pageId;
	private String path;
	private String uri;
	private String displayTitle;
	private int version;
	
	private String content;
	
	private File   wikiFile;
	
	/**
	 * is the wiki file is exsit
	 * @return true->exsit, otherwise,false;
	 */
	public  boolean isWikiFileExist()
	{
		if(TextUtils.isEmpty(path))
		{
			return false;
		}
		if(wikiFile==null)
		{
			wikiFile = new File(path);
		}
		return wikiFile.exists();
	}
	/**
	 * get the wiki content
	 * @return
	 */
	public String getWikiFileContent()
	{
		if(isWikiFileExist())
		{
			content=FileUtil.getFileContent(wikiFile);
			return content;
		}
		return null;
	}
	/**
	 * 保存json到文件中去。<br>
	 * 如果保存成功，则会将保存的文件路径赋值给实体的path属性
	 * @param content
	 * @return
	 */
	public boolean saveWikiFile(String content)
	{
		if(!FileUtil.isExternalStorageEnable())
			return false;
		String path = Constants.CACHE_DIR+File.separator+UUID.randomUUID().toString();
		boolean save = FileUtil.saveFile(content, path);
		if(save)
		{//save to path
			this.path = path;
		}
		return save;
	}
	
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getDisplayTitle() {
		return displayTitle;
	}
	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}
	
}
