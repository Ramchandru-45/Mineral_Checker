package com.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import com.model.DataManager;
import com.model.Profile;
import com.service.JavaServer;

public class WelcomePanel extends JPanel {
    private JPanel profilesListPanel;
    private JButton addProfileButton;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final List<Profile> profiles;
    private  final JavaServer server;

    public WelcomePanel(CardLayout cardLayout, JPanel mainPanel, List<Profile> profiles,JavaServer server) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.profiles = profiles;
        this.server = server;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = UIComponents.createTitleLabel("Minerals Checker");

        // Profiles list panel
        profilesListPanel = new JPanel();
        profilesListPanel.setLayout(new BoxLayout(profilesListPanel, BoxLayout.Y_AXIS));
        profilesListPanel.setBackground(Color.WHITE);
        profilesListPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add Profile button
        addProfileButton = UIComponents.createStyledButton("Add Profile");
        addProfileButton.addActionListener(e -> cardLayout.show(mainPanel, "personalInfo"));


        // Add components to panel
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(profilesListPanel);
        add(Box.createRigidArea(new Dimension(0, 15)));

        // Update the profiles list
        updateProfilesList();

    }

    public void updateProfilesList() {
        profilesListPanel.removeAll();

        if (profiles.isEmpty()) {
            JLabel noProfilesLabel = new JLabel("No profiles found.");
            noProfilesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            noProfilesLabel.setFont(new Font("Arial", Font.ITALIC, 14));

            profilesListPanel.add(noProfilesLabel);
            profilesListPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            profilesListPanel.add(addProfileButton);

        } else {
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            listPanel.setBackground(Color.WHITE);

            JLabel profilesHeaderLabel = new JLabel("Select a profile:");
            profilesHeaderLabel.setFont(new Font("Arial", Font.BOLD, 16));
            profilesHeaderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            listPanel.add(profilesHeaderLabel);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            for (int i = 0; i < profiles.size(); i++) {
                Profile profile = profiles.get(i);
                final int profileIndex = i;

                JPanel profilePanel = createProfilePanel(profile, profileIndex);
                listPanel.add(profilePanel);
                listPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }

            JScrollPane scrollPane = new JScrollPane(listPanel);
            scrollPane.setPreferredSize(new Dimension(300, 200));
            scrollPane.setMaximumSize(new Dimension(300, 400));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

            profilesListPanel.add(scrollPane);
            profilesListPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            profilesListPanel.add(addProfileButton);
            profilesListPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        }

        profilesListPanel.revalidate();
        profilesListPanel.repaint();
    }

    private JPanel createProfilePanel(Profile profile, int profileIndex) {
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.X_AXIS));
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.setMaximumSize(new Dimension(300, 40));

        JButton profileButton = new JButton(profile.getName());
        profileButton.setMaximumSize(new Dimension(150, 40));
        profileButton.setPreferredSize(new Dimension(150, 40));
        profileButton.setFont(new Font("Arial", Font.PLAIN, 14));
        profileButton.setBackground(new Color(240, 240, 240));
        profileButton.setFocusPainted(false);

        profileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                profileButton.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                profileButton.setBackground(new Color(240, 240, 240));
            }
        });

        profileButton.addActionListener(e -> {
            // Handle profile selection
            mainPanel.add(new ResultsPanel(cardLayout, mainPanel, profile, profiles, server),"result");
            cardLayout.show(mainPanel, "result");
        });

        JButton deleteButton = UIComponents.createStyledButton("Delete");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setPreferredSize(new Dimension(80, 30));
        deleteButton.setMaximumSize(new Dimension(80, 30));

        deleteButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                deleteButton.setBackground(new Color(192, 57, 43));
            }

            public void mouseExited(MouseEvent e) {
                deleteButton.setBackground(new Color(231, 76, 60));
            }
        });

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete the profile for " + profile.getName() + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                profiles.remove(profileIndex);
                DataManager.saveAllProfiles(profiles);
                updateProfilesList();
                
            }
        });

        profilePanel.add(profileButton);
        profilePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        profilePanel.add(deleteButton);

        return profilePanel;
    }
}