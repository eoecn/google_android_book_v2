package com.eoe.socketServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public Server(){
		new ServerThread().start();
	}
	class ServerThread extends Thread{
		public void run() {
			try {
				ServerSocket ss=new ServerSocket(8888); ////创建一个ServerSocket对象，并让这个ServerSocket在8888端口监听
				while(true){
					Socket socket=ss.accept(); //调用ServerSocket的accept()方法，接受客户端所发送的请求，如果客户端没有发送数据，那么该线程就停滞不继续
//					try {
//						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  //接收客户端信息
//						String readline = in.readLine();
//						System.out.println("readline:"+readline);
//						
//						PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
//						out.println("link server success");
//						
//						in.close();   //关闭流
//						out.close();//关闭流
//					    socket.close();//关闭打开的socket
//						
//					} catch (Exception e) {
//						// TODO: handle exception
//					}finally{
//					//	socket.close();//
//					}
					try {
						DataInputStream in=new DataInputStream(socket.getInputStream());//接收客户端信息
						String readline=in.readUTF();
						System.out.println(readline);
							
						DataOutputStream out=new DataOutputStream(socket.getOutputStream());  //向客户端发送消息
						out.writeUTF("link server success");
						out.flush();
						
						in.close();   //关闭流
						out.close();//关闭流
						socket.close();//关闭打开的socket
						
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	public static void main(String[] args) throws IOException {
		new Server();   //开启服务器
	}
	

}
