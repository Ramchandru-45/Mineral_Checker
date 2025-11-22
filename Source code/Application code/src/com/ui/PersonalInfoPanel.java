package com.ui;
import javax.swing.*;

import com.model.DataManager;
import com.model.Profile;

import java.awt.*;
import java.util.List;

public class PersonalInfoPanel extends JPanel {
    private JTextField nameField;
    private JTextField heightField;
    private JTextField weightField;
    private JComboBox<String> genderComboBox;
    private JSpinner ageSpinner;
    private JLabel nameErrorLabel;
    private JLabel genderErrorLabel;
    private JLabel ageErrorLabel;
    private JLabel heightErrorLabel;
    private JLabel weightErrorLabel;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final WelcomePanel welcomePanel;
    private final List<Profile> profiles;

    public PersonalInfoPanel(CardLayout cardLayout, JPanel mainPanel, WelcomePanel welcomePanel, List<Profile> profiles) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.profiles = profiles;
        this.welcomePanel  = welcomePanel;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = UIComponents.createTitleLabel("Personal Information");

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setMaximumSize(new Dimension(500, 400));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Initialize form fields
        initializeFormFields();

        // Add form fields to panel
        formPanel.add(UIComponents.createFormField("Name:", nameField, nameErrorLabel));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(UIComponents.createFormField("Gender:", genderComboBox, genderErrorLabel));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(UIComponents.createFormField("Age:", ageSpinner, ageErrorLabel));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(UIComponents.createFormField("Height (in cm)", heightField, heightErrorLabel));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(UIComponents.createFormField("Weight (in kg)", weightField, weightErrorLabel));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Back button
        JButton backButton = UIComponents.createStyledButton("Back");
        backButton.setBackground(new Color(189, 195, 199));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "welcome"));

        // Save button
        JButton saveButton = UIComponents.createStyledButton("Save");
        saveButton.addActionListener(e -> {
            if (validateForm()) {
                saveProfile();
            }
        });

        // Add buttons with spacing
        buttonsPanel.add(backButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        buttonsPanel.add(saveButton);

        // Add components to main panel
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(formPanel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(buttonsPanel);
    }

    private void initializeFormFields() {
        // Name field
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameErrorLabel = new JLabel("Please enter your name");

        // Gender field
        String[] genders = {"Select Gender", "Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        genderErrorLabel = new JLabel("Please select your gender");

        // Age field
        SpinnerNumberModel ageModel = new SpinnerNumberModel(20, 1, 100, 1);
        ageSpinner = new JSpinner(ageModel);
        ageSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        JComponent editor = ageSpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor)editor).getTextField().setHorizontalAlignment(JTextField.LEFT);
        }
        ageErrorLabel = new JLabel("Please enter a valid age (1-80)");

        // Height field
        heightField = new JTextField(20);
        heightField.setFont(new Font("Arial", Font.PLAIN, 14));
        heightErrorLabel = new JLabel("Please enter your height");

        // Weight field
        weightField = new JTextField(20);
        weightField.setFont(new Font("Arial", Font.PLAIN, 14));
        weightErrorLabel = new JLabel("Please enter your weight");
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Validate name
        if (nameField.getText().trim().isEmpty()) {
            nameErrorLabel.setVisible(true);
            isValid = false;
        } else {
            nameErrorLabel.setVisible(false);
        }

        // Validate gender
        if (genderComboBox.getSelectedIndex() == 0) {
            genderErrorLabel.setVisible(true);
            isValid = false;
        } else {
            genderErrorLabel.setVisible(false);
        }

        // Validate height
        if (heightField.getText().trim().isEmpty()) {
            heightErrorLabel.setVisible(true);
            isValid = false;
        } else {
            heightErrorLabel.setVisible(false);
        }

        // Validate weight
        if (weightField.getText().trim().isEmpty()) {
            weightErrorLabel.setVisible(true);
            isValid = false;
        } else {
            weightErrorLabel.setVisible(false);
        }

        return isValid;
    }

    private void saveProfile() {
        String name = nameField.getText().trim();
        String age = ageSpinner.getValue().toString();
        String gender = genderComboBox.getSelectedItem().toString();
        String height = heightField.getText().trim();
        String weight = weightField.getText().trim();
        // Create new profile
        Profile newProfile = new Profile(name, Integer.parseInt(age), gender,
                 Float.parseFloat(height), Float.parseFloat(weight), new float[3]);
        profiles.add(newProfile);
        
        // Save to file
        DataManager.saveProfile(newProfile);
        
        // Show success message
        JOptionPane.showMessageDialog(
            this,
            "Profile saved successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
        resetForm();

        // Return to welcome screen
        cardLayout.show(mainPanel, "welcome");
        welcomePanel.updateProfilesList();
    }

    public void resetForm() {
        nameField.setText("");
        genderComboBox.setSelectedIndex(0);
        ageSpinner.setValue(20);
        heightField.setText("");
        weightField.setText("");
        nameErrorLabel.setVisible(false);
        genderErrorLabel.setVisible(false);
        ageErrorLabel.setVisible(false);
        heightErrorLabel.setVisible(false);
        weightErrorLabel.setVisible(false);
    }
}