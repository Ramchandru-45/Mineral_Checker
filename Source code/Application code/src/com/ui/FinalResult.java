package com.ui;
import javax.swing.*;

import com.model.DataManager;
import com.model.Profile;

import java.awt.*;
import java.util.List;

public class FinalResult extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final Profile profile;
    private JLabel resultNameLabel;
    private JLabel resultGenderLabel;
    private JLabel resultAgeLabel;
    private JLabel resultHeightLabel;
    private JLabel resultWeightLabel;
    private JLabel bodyRatio;
    private float bmi;
    public Font labelFont = new Font("Arial", Font.PLAIN, 14);
   
    public FinalResult(CardLayout cardLayout, JPanel mainPanel, Profile profile) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.profile = profile;
        bmi=(profile.getWeight()/( profile.getHeight()/100*profile.getHeight()/100));
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = UIComponents.createTitleLabel("Result");

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2));
        formPanel.setBackground(Color.WHITE);
        formPanel.setMaximumSize(new Dimension(500, 150));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        initializeResultLabels();
        
        formPanel.add(resultNameLabel);
        formPanel.add(resultGenderLabel);
        formPanel.add(resultAgeLabel);
        formPanel.add(resultHeightLabel);
        formPanel.add(resultWeightLabel);
        formPanel.add(bodyRatio);
        
        //mineral panel
        JPanel mineralPanel = new JPanel();
        mineralPanel.setLayout(new BoxLayout(mineralPanel, BoxLayout.PAGE_AXIS));
        mineralPanel.setBackground(Color.WHITE);
        mineralPanel.setMaximumSize(new Dimension(500, 200));
        mineralPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        String[] mineral={"Potassium" , "Iron", "Calcium"};
        
        for(int i=0; i<profile.getMinerals().length-1;i++)
        {
        	JLabel sample = new JLabel(mineral[i]+" level is "+profile.getMinerals()[i]);
        	JLabel sample2 = new JLabel(getMessage(mineral[i], profile.getMinerals()[i]));
        	sample.setFont(labelFont);
        	sample2.setFont(labelFont);
        	mineralPanel.add(sample);
        	mineralPanel.add(sample2);
        	mineralPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        }
        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Back button
        JButton backButton = UIComponents.createStyledButton("Back");
        backButton.setBackground(new Color(189, 195, 199));
        backButton.addActionListener(e -> {
        cardLayout.show(mainPanel, "result");
        mainPanel.remove(this);
        mainPanel.revalidate();
        });


        // Add buttons with spacing
        buttonsPanel.add(backButton);
        
        
        // Add components to main panel
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(formPanel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(mineralPanel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(buttonsPanel);
    }

    
    private void initializeResultLabels() {
        resultNameLabel = new JLabel("Name: "+profile.getName());
        resultGenderLabel = new JLabel("Gender: "+profile.getGender());
        resultAgeLabel = new JLabel("Age: "+profile.getAge()+"");
        resultHeightLabel = new JLabel("Height (in cm): "+profile.getHeight()+"");
        resultWeightLabel =new JLabel("Weight (in kg): "+profile.getWeight()+"");
        bodyRatio =new JLabel("Body Mass Ratio: "+bmi);
        
        resultNameLabel.setFont(labelFont);
        resultGenderLabel.setFont(labelFont);
        resultAgeLabel.setFont(labelFont);
        resultHeightLabel.setFont(labelFont);
        resultWeightLabel.setFont(labelFont);
        bodyRatio.setFont(labelFont);
    }

    
    private String getMessage(String mineral, float value)
    {
    	switch(mineral)
    	{
    	case "Potassium":
    		if(value<13.4f)
    			return "Potassium is low \n will have muscle cramp and nerves problem";
    		else if(value>22.0f)
    			return "Potassium is high \nwill have high pressure (High BP)";
    		else
    			return "Potassium is normal \n Continue the regular intake and have balance diet";
    	case "Iron":
    		if(value<55.0f)
    			return "Iron is low in saliva \n high in blood hemoglobin will cause CO2 distruption";
    		else if(value>147.2f)
    			return "Iron is high in saliva\n low in blood high risk of anemia ";
    		else
    			return "Iron is normal";
    	case  "Calcium":
    		if(value<3.4f)
    			return "Calcium is low\n weak bones and teeth";
    		else if(value>5.4f)
    			return "Calcium is high \n good for bone and teeth";
    		else
    			return "Calcium is normal \n good for bone and teeth";
    	}
    	return "--";
    }
}