package cn.eoe.wiki.view;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.UMFeedbackService;

import cn.eoe.wiki.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class AboutDialog extends Dialog {
	
	private Context con;
	private Button btnCancel;
	private Button btnFeedback;

	public AboutDialog(Context context) {
		super(context,R.style.dialog);
		setContentView(R.layout.about_dialog);
		this.con = context;
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnFeedback = (Button) findViewById(R.id.btn_feedback);
		btnCancel.setOnClickListener(cancelListener);
		btnFeedback.setOnClickListener(feedbackListener);
	}
	
	private View.OnClickListener cancelListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			MobclickAgent.onEvent(con, "about", "btn_cancel");
			dismiss();
		}
	};
	private View.OnClickListener feedbackListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			MobclickAgent.onEvent(con, "about", "btn_feedback");
			dismiss();
			UMFeedbackService.openUmengFeedbackSDK(con);
		}
	};

}
