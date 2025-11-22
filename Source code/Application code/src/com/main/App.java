package com.main;
import javax.swing.*;

import com.model.DataManager;
import com.model.Profile;
import com.service.JavaServer;
import com.ui.Notification;
import com.ui.PersonalInfoPanel;
import com.ui.WelcomePanel;
import com.ui.ResultsPanel;


import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final List<Profile> profiles;
    

    public App() {
        setTitle("Minerals Checker");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);
        profiles = new ArrayList<>();

        // Load existing profiles
        DataManager.loadProfiles(profiles);
        JavaServer server = new JavaServer();
        
        // Create panels
        WelcomePanel welcomePanel = new WelcomePanel(cardLayout, mainPanel, profiles, server);
        PersonalInfoPanel personalInfoPanel = new PersonalInfoPanel(cardLayout, mainPanel, welcomePanel, profiles);

        // Add panels to main panel
        mainPanel.add(welcomePanel, "welcome");
        mainPanel.add(personalInfoPanel, "personalInfo");

        // Add main panel to frame
        add(mainPanel);

        // Show first card
        cardLayout.show(mainPanel, "welcome");

        // Close server listener
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                server.stop();
            }

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {
            	
            	
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            	
            }

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
    }

    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and show application
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
        });
    }
}