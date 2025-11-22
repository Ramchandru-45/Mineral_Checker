package com.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class JavaClient {

	InetAddress add;
	Socket client;
	DataInputStream in;
	DataOutputStream out;

	public JavaClient() throws InterruptedException {
		Random random = new Random();
		try {
			System.out.println("Client starting");
			client=new Socket(InetAddress.getLocalHost(), 80);
			System.out.println("Connected to server");
			System.out.println(client.getRemoteSocketAddress());
			in=new DataInputStream(client.getInputStream());
			out=new DataOutputStream(client.getOutputStream());
			while(true){
				
				while(in.available()>0){
					System.out.println("in available");
					int message=in.readInt();
					System.err.println(message);
					if(message==255){
						System.err.println("message to stop");
						client.close();
						break;
					}
					else if(message==1){
						int n= 100;
						while(n-->0){
							out.write('1');
							out.write(',');
							Thread.sleep(25);
						}
						System.out.println("sent bytes");
						out.writeInt(-1);
						
					}
				}
				if(!client.isConnected()) {
					break;
				}
				if(client.isClosed()){
					break;
				}
			}
		} catch (IOException e) {
			
			try {
				client.close();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
		}
		
		
	}
	public static void main(String[] args) throws InterruptedException {
		new JavaClient();
	}
}
