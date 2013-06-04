package cn.eoe.wiki.activity;

import android.content.Intent;
import android.os.Bundle;
import cn.eoe.wiki.R;
import cn.eoe.wiki.utils.FileUtil;
import cn.eoe.wiki.utils.ThreadPoolUtil;

/**启动的欢迎界面。
 * 
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @data  2012-8-2
 * @version 1.0.0
 */
public class SplashActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		initComponent();
		initData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	void initComponent() {
		
	}

	void initData() {
		ThreadPoolUtil.execute(new Runnable() {
			@Override
			public void run() {
				FileUtil.initExternalDir(true);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent = new Intent(mContext, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
