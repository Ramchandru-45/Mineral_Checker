package com.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;



public class Notification implements Runnable{

	static JWindow window;
	JProgressBar pr;
	public Notification() {
		new Thread(this).start();
	}
	
	public static JWindow getWindow() {
		return window;
	}
	void showProcess() {
		window = new JWindow();
		window.setAlwaysOnTop(true);
		window.setSize(400, 20);
		window.setLocationRelativeTo(null);
		pr=new JProgressBar();
		pr.setSize(window.getWidth(), 10);
		Box box = new Box( 0);
		box.add(new JLabel("please wait..."));
		box.add(pr);
		window.add(box);
		window.show();
	}
	
	public void run()
	{
		showProcess();
		
		while(pr.getValue()!=100) {
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			window.paint(window.getGraphics());
		}
	}
}
