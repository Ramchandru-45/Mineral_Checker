package com.model;


import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;




public class Process extends JPanel {

    JLabel text;

    public Process(){
        System.out.println("please wait...");
        this.setSize(new Dimension(200, 100));
        text = new JLabel("  Please wait testing in process...");
        //this.setBackground(Color.white);
        this.add(text);
        this.setVisible(true);
        System.out.println("completed");

    }

    
    public static void main(String[] args) {
        new Process();
    }
}
