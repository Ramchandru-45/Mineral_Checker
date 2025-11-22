package com.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class JavaServer implements Runnable {
	private ServerSocket server;
	private Socket clientHandler;
	private DataInputStream in;
	private DataOutputStream out;
	private volatile boolean isRunning;
	private volatile boolean isConnected;

	// constructor
	public JavaServer() {
		isRunning = true;
		isConnected = false;
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		try {
			System.out.println("Server Starting on port 80");
			server = new ServerSocket(80);
			//server.setSoTimeout(1000); // Set timeout to 1 second for accept()
			
			while (isRunning) {
				try {
					System.out.println("Waiting for device connection...");
					clientHandler = server.accept();
					isConnected = true;
					System.out.println("Device Connected: " + clientHandler.getRemoteSocketAddress());

					in = new DataInputStream(clientHandler.getInputStream());
					out = new DataOutputStream(clientHandler.getOutputStream());
					while(isConnected);
						
					
				} catch (SocketTimeoutException e) {
					// Timeout occurred, continue loop
					continue;
				} catch (IOException e) {
					if (isRunning) {
						System.err.println("Connection error: " + e.getMessage());
						disconnect();
					}
				}
				
				isConnected = false;
			}
		} catch (IOException e) {
			System.err.println("Server error: " + e.getMessage());
		}
	}

	public void disconnect() {
		try {
			isConnected = false;
			if (clientHandler != null) {
				out.writeInt(-1);
				clientHandler.close();
			}
		} catch (IOException e) {
			System.err.println("Error closing connection: " + e.getMessage());
		}
	}

	public void stop() {
		isRunning = false;
		disconnect();
		try {
			if (server != null) {
				server.close();
			}
		} catch (IOException e) {
			System.err.println("Error stopping server: " + e.getMessage());
		}
	}

	public boolean isConnected() {
		return isConnected && clientHandler != null && clientHandler.isConnected();
	}

	public ServerSocket getServer() {
		return server;
	}

	public Socket getHandler() {
		return isConnected ? clientHandler : null;
	}
}
