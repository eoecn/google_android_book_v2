package com.eoe.socketClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class socketClient extends Activity {
	private Button button;
	private TextView text;
	private EditText edit;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		button = (Button) findViewById(R.id.button);
		edit = (EditText) findViewById(R.id.edit);
		text = (TextView) findViewById(R.id.text);

		button.setOnClickListener(new View.OnClickListener() {
			private Socket socket = null;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String sendMsg = edit.getText().toString() + "\r\n";

				try {
					socket = new Socket("192.168.0.37", 8888); // 创建Socket，其中ip地址为我的PC机器的地址，手机通过wifi上网和服务器在一个网段

					// PrintWriter out = new PrintWriter(new BufferedWriter(new
					// OutputStreamWriter(socket.getOutputStream())),true);
					// out.println(sendMsg);
					//					
					// BufferedReader in = new BufferedReader(new
					// InputStreamReader(socket.getInputStream()));
					// String readMsg = in.readLine();
					// if(readMsg !=null){
					// text.setText(readMsg);
					// }else{
					// text.setText("错误");
					// }
					//					
					// out.close();
					// in.close();
					// socket.close();

					DataOutputStream out = new DataOutputStream(socket
							.getOutputStream()); // 向服务器发送消息
					out.writeUTF(sendMsg);
					out.flush();

					DataInputStream in = new DataInputStream(socket
							.getInputStream()); // 接收来自服务器的消息
					String readMsg = in.readUTF();
					if (readMsg != null) {
						text.setText(readMsg);
					}
					out.close();
					in.close();
					socket.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}