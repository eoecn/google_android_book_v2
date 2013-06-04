package cn.eoe.wiki.view;

import android.view.ViewGroup;

public class SliderEntity
{
	protected ViewGroup view;
	protected int width;
	protected int closeX;
	protected int openX;
	protected int y;
	
	public SliderEntity(ViewGroup view,int openX,int closeX,int y)
	{
		if(view==null)
			throw new NullPointerException("invalid view");
		this.view = view;
		this.closeX = closeX;
		this.openX = openX;
		this.y = y;
	}
	
	public ViewGroup getView() {
		return view;
	}
	public void setView(ViewGroup view) {

		if(view==null)
			throw new NullPointerException("invalid view");
		this.view = view;
	}
	public int getWidth() {
		if(width<=0)
		{
			width = view.getMeasuredWidth();
		}
		return width;
	}
	public int getCloseX() {
		return closeX;
	}
	public int getOpenX() {
		return openX;
	}
	public int getY() {
		return y;
	}
	
}
