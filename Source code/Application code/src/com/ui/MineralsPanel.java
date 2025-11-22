package com.ui;
import javax.swing.*;

import com.model.DataManager;
import com.model.Profile;

import java.awt.*;
import java.util.List;

public class MineralsPanel extends JPanel {
    private JComboBox<String> mineralsComboBox;
    private JLabel mineralsErrorLabel;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final List<Profile> profiles;
    private final ResultsPanel resultsPanel;

    public MineralsPanel(CardLayout cardLayout, JPanel mainPanel, List<Profile> profiles, ResultsPanel resultsPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.profiles = profiles;
        this.resultsPanel = resultsPanel;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = UIComponents.createTitleLabel("Minerals Selection");

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setMaximumSize(new Dimension(500, 150));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Initialize form fields
        initializeFormFields();

        // Add form fields to panel
        formPanel.add(UIComponents.createFormField("Select Mineral:", mineralsComboBox, mineralsErrorLabel));

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Back button
        JButton backButton = UIComponents.createStyledButton("Back");
        backButton.setBackground(new Color(189, 195, 199));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "results"));

        // Submit button
        JButton submitButton = UIComponents.createStyledButton("Submit");
        submitButton.addActionListener(e -> {
            if (validateForm()) {
                submitMineral();
            }
        });

        // Add buttons with spacing
        buttonsPanel.add(backButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        buttonsPanel.add(submitButton);

        // Add components to main panel
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(formPanel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(buttonsPanel);
    }

    private void initializeFormFields() {
        String[] minerals = {"Select a Mineral", "Sodium", "Calcium", "Potassium", "Magnesium", "Iron", "Zinc"};
        mineralsComboBox = new JComboBox<>(minerals);
        mineralsComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        mineralsErrorLabel = new JLabel("Please select a mineral");
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (mineralsComboBox.getSelectedIndex() == 0) {
            mineralsErrorLabel.setVisible(true);
            isValid = false;
        } else {
            mineralsErrorLabel.setVisible(false);
        }

        return isValid;
    }

    private void submitMineral() {
        String selectedMineral = mineralsComboBox.getSelectedItem().toString();
        if (selectedMineral.equals("Select a Mineral")) {
            return;
        }

        // Get the current profile from ResultsPanel
        Profile currentProfile = resultsPanel.getCurrentProfile();
        if (currentProfile == null) {
            JOptionPane.showMessageDialog(
                this,
                "No profile selected. Please select a profile first.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Update the profile with the selected mineral
        currentProfile.setSelectedMineral(selectedMineral);
        
        // Save all profiles to update the file
        DataManager.saveAllProfiles(profiles);
        
        // Show test completion panel
        showTestCompletionPanel(currentProfile);
    }

    private void showTestCompletionPanel(Profile profile) {
        // Create a new panel for test completion
        JPanel testCompletionPanel = new JPanel();
        testCompletionPanel.setLayout(new BoxLayout(testCompletionPanel, BoxLayout.Y_AXIS));
        testCompletionPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        testCompletionPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = UIComponents.createTitleLabel("Test Completed");

        // Profile details panel
        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(221, 221, 221)),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        detailsPanel.setMaximumSize(new Dimension(500, 200));
        detailsPanel.setBackground(new Color(249, 249, 249));

        // Add profile details
        detailsPanel.add(new JLabel("Name:"));
        detailsPanel.add(new JLabel(profile.getName()));
        detailsPanel.add(new JLabel("Age:"));
        detailsPanel.add(new JLabel(String.valueOf(profile.getAge())));
        detailsPanel.add(new JLabel("Gender:"));
        detailsPanel.add(new JLabel(profile.getGender()));
        detailsPanel.add(new JLabel("Selected Mineral:"));
        detailsPanel.add(new JLabel(profile.getSelectedMineral()));

        // Create minerals values panel
        JPanel mineralsPanel = createMineralsPanel(profile);

        // Success message
        JLabel successLabel = new JLabel("Test completed successfully!");
        successLabel.setFont(new Font("Arial", Font.BOLD, 16));
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        successLabel.setForeground(new Color(46, 204, 113)); // Green color

        // Button to return to main menu
        JButton finishButton = UIComponents.createStyledButton("Finish");
        finishButton.addActionListener(e -> {
            // Update the welcome panel before showing it
            cardLayout.show(mainPanel, "welcome");
        });

        // Add all components to the completion panel
        testCompletionPanel.add(titleLabel);
        testCompletionPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        testCompletionPanel.add(detailsPanel);
        testCompletionPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        testCompletionPanel.add(mineralsPanel);
        testCompletionPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        testCompletionPanel.add(successLabel);
        testCompletionPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        testCompletionPanel.add(finishButton);

        // Add the completion panel to the main panel and show it
        mainPanel.add(testCompletionPanel, "completion");
        cardLayout.show(mainPanel, "completion");
    }

    private JPanel createMineralsPanel(Profile profile) {
        JPanel mineralsPanel = new JPanel();
        mineralsPanel.setLayout(new BoxLayout(mineralsPanel, BoxLayout.Y_AXIS));
        mineralsPanel.setBackground(Color.WHITE);
        mineralsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mineralsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        mineralsPanel.setMaximumSize(new Dimension(500, 300));

        // Minerals Values Title
        JLabel mineralsTitle = new JLabel("Minerals Values");
        mineralsTitle.setFont(new Font("Arial", Font.BOLD, 24));
        mineralsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mineralsTitle.setForeground(new Color(44, 62, 80));

        // Create minerals table
        JPanel mineralsTable = new JPanel(new GridLayout(0, 3, 10, 10));
        mineralsTable.setBackground(Color.WHITE);
        mineralsTable.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(221, 221, 221)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        mineralsTable.setMaximumSize(new Dimension(400, 300));

        // Add headers
        JLabel mineralHeader = new JLabel("Mineral");
        mineralHeader.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel valueHeader = new JLabel("Value");
        valueHeader.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel unitHeader = new JLabel("Unit");
        unitHeader.setFont(new Font("Arial", Font.BOLD, 14));

        mineralsTable.add(mineralHeader);
        mineralsTable.add(valueHeader);
        mineralsTable.add(unitHeader);

        // Add all minerals with their values
        String[] minerals = {"Sodium", "Calcium", "Potassium", "Magnesium", "Iron", "Zinc"};
        String[] units = {"mg/dL", "mg/dL", "mEq/L", "mg/dL", "µg/dL", "µg/dL"};
        
        for (int i = 0; i < minerals.length; i++) {
            JLabel mineralLabel = new JLabel(minerals[i]);
            mineralLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JLabel valueLabel = new JLabel("0.0");
            valueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JLabel unitLabel = new JLabel(units[i]);
            unitLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            
            if (minerals[i].equals(profile.getSelectedMineral())) {
                valueLabel.setForeground(new Color(46, 204, 113)); // Green for selected mineral
                mineralLabel.setForeground(new Color(46, 204, 113));
            }
            
            mineralsTable.add(mineralLabel);
            mineralsTable.add(valueLabel);
            mineralsTable.add(unitLabel);
        }

        // Add components to minerals panel
        mineralsPanel.add(mineralsTitle);
        mineralsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mineralsPanel.add(mineralsTable);

        return mineralsPanel;
    }
}