package cn.eoe.wiki.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import cn.eoe.wiki.R;
import cn.eoe.wiki.WikiApplication;
import cn.eoe.wiki.utils.L;
import cn.eoe.wiki.utils.WikiUtil;
import cn.eoe.wiki.view.SliderEntity;
import cn.eoe.wiki.view.SliderLayer;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * 应用程序的主界面
 * 
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data 2012-8-5
 * @version 1.0.0
 */
public class MainActivity extends ActivityGroup {

	private static WikiApplication 	mWikiApplication;
	private  	MainActivity 		mMainActivity;

	private LocalActivityManager 	mActivityManager;

	private SliderLayer 			mSliderLayers;
	private boolean 				mReadyExit;
	private Timer					mExitTimer;
	private ExitTask				mExitTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWikiApplication = WikiApplication.getApplication();
		mMainActivity = this;
		mWikiApplication.setMainActivity(mMainActivity);
		setContentView(R.layout.main);

		mActivityManager = getLocalActivityManager();
		mExitTimer = new Timer();

		mSliderLayers = (SliderLayer) findViewById(R.id.animation_layout);
		//umeng event
		MobclickAgent.onEvent(this, "home", "enter");
		int sceenWidth = WikiUtil.getSceenWidth(mMainActivity);
		ViewGroup layerOne = (ViewGroup) findViewById(R.id.animation_layout_one);
		layerOne.setPadding(0,0, WikiUtil.dip2px(mMainActivity, 20), 0);
		ViewGroup layerTwo = (ViewGroup) findViewById(R.id.animation_layout_two);
		layerTwo.setPadding(0, 0, WikiUtil.dip2px(mMainActivity, 15), 0);
		ViewGroup layerThree = (ViewGroup) findViewById(R.id.animation_layout_three);
		layerThree.setPadding(0, 0, 0, 0);
		mSliderLayers.addLayer(new SliderEntity(layerOne, 0, sceenWidth, 0));
		mSliderLayers.addLayer(new SliderEntity(layerTwo, 0, sceenWidth - WikiUtil.dip2px(mMainActivity, 23), 0));
		mSliderLayers.addLayer(new SliderEntity(layerThree, WikiUtil.dip2px(mMainActivity, -10), sceenWidth - WikiUtil.dip2px(mMainActivity, 20), 0));

		Intent intent = new Intent(this, MainCategoryActivity.class);
		showView(0, intent);
	}

	public void showView(final int index, Intent intent) {
		intent.putExtra(SliderActivity.KEY_SLIDER_INDEX, index);
		// 这里id是最关键的，不能重复。
		// 如果看过一个wiki想快速加载，我们只能通过读取数据库来实现。
		// 我们不想通过保存上一次的activity来作一个快速的显示，因为页面可能有一些的改动。
		// 而后那样我们这里的处理步骤也是会复杂一点的
		String id = String.valueOf(System.currentTimeMillis());
		View view = mActivityManager.startActivity(id, intent).getDecorView();
		ViewGroup currentView = mSliderLayers.getLayer(index);
		currentView.removeAllViews();
		currentView.addView(view);
		// if the index ==0 , no need to open .
		if (index == 0)
			return;
		view.post(new Runnable() {
			@Override
			public void run() {
				//为什么要在这里才进行打开这个动作 
				//如果直接在addview后执行此动作，会造成在动画的时候又在绘图
				//界面就会乱掉
				mSliderLayers.openSidebar(index);
			}
		});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(mSliderLayers.isAnimationing()) {
			//if the slider is moving , we need to stop all the touch event
			return true;
		}
		else {
			return super.dispatchTouchEvent(ev);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		L.e("MainActivity dispatchKeyEvent:"+event.getKeyCode());
		int keyCode = event.getKeyCode();
		int keyAction = event.getAction();
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			L.e("MainActivity　keyAction:"+keyAction);
			if( keyAction == KeyEvent.ACTION_DOWN) {

				int index = mSliderLayers.openingLayerIndex();
				if (index > 0) {
					mSliderLayers.closeSidebar(index);
				} else {
					if(mReadyExit==false) {
						mReadyExit = true;
						Toast.makeText(mMainActivity, R.string.tip_exit, Toast.LENGTH_SHORT).show();
						if(mExitTask!=null) {
							mExitTask.cancel();
						}
						mExitTask = new ExitTask();
						mExitTimer.schedule(mExitTask, 2000);  
                    }
					else {
						//发送一个广播，通知其它所有的页面，要结束该应用程序了。
						//baseActivity里面接收这个广播，并作相应的处理。
						//这也是为什么要求所有的activity都必需直接或者间隔继承于baseactivity的原因
						sendBroadcast(new Intent(BaseActivity.ACTION_EXIT));
						finish();
					}
				}
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	public SliderLayer getSliderLayer() {
		return mSliderLayers;
	}
	class ExitTask extends TimerTask {  
        
        @Override 
        public void run() {
            mReadyExit = false;
        }  
    }
}
