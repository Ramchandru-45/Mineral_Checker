package com.ui;

import javax.swing.*;


import com.model.DataManager;
import com.model.Profile;
import com.service.JavaServer;

import java.awt.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;


public class ResultsPanel extends JPanel {
    private final String DATA_FILE ;
    private JLabel resultNameLabel;
    private JLabel resultGenderLabel;
    private JLabel resultAgeLabel;
    private JLabel resultHeightLabel;
    private JLabel resultWeightLabel;
    private List<Profile> profiles;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final Profile profile;
    private final JavaServer server;
    private static Notification notify;
    StringBuilder str;

    
    public ResultsPanel(CardLayout cardLayout, JPanel mainPanel, Profile profile, List<Profile> profiles, JavaServer server) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.profile = profile;
        this.profiles = profiles;
        this.server = server;
        this.DATA_FILE="SalivaSamples/"+profile.getName()+".txt";

        File fhf= new File("SalivaSamples");
        if(!fhf.exists()) {
        	fhf.mkdir();
        }
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = UIComponents.createTitleLabel("Profile Details");

        // Results panel
        JPanel resultsContentPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        resultsContentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(221, 221, 221)),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        resultsContentPanel.setMaximumSize(new Dimension(500, 150));
        resultsContentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsContentPanel.setBackground(new Color(249, 249, 249));

        // Initialize result labels
        initializeResultLabels();

        // Add components to results panel
        resultsContentPanel.add(new JLabel("Name:"));
        resultsContentPanel.add(resultNameLabel);
        resultsContentPanel.add(new JLabel("Gender:"));
        resultsContentPanel.add(resultGenderLabel);
        resultsContentPanel.add(new JLabel("Age:"));
        resultsContentPanel.add(resultAgeLabel);
        resultsContentPanel.add(new JLabel("Height (in cm):"));
        resultsContentPanel.add(resultHeightLabel);
        resultsContentPanel.add(new JLabel("Weight (in kg):"));
        resultsContentPanel.add(resultWeightLabel);

        //create saliva test button
        JPanel salivaTest = createSalivaTest();

        // Create minerals values panel
        JPanel mineralsPanel = createMineralsPanel();

        // Button panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Back to profiles button
        JButton backButton = UIComponents.createStyledButton("Back to Profiles");
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "welcome");
            mainPanel.remove(this);
            mainPanel.revalidate();
        });

        // Edit Profile button
        JButton editButton = UIComponents.createStyledButton("Generat Result");
        editButton.addActionListener(e -> Generate());

        // Add buttons to panel with spacing
        buttonsPanel.add(backButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        buttonsPanel.add(editButton);

        // Add all components to main panel
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(resultsContentPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(salivaTest);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(mineralsPanel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(buttonsPanel);
    }

    private void initializeResultLabels() {
        resultNameLabel = new JLabel(profile.getName());
        resultGenderLabel = new JLabel(profile.getGender());
        resultAgeLabel = new JLabel(profile.getAge()+"");
        resultHeightLabel = new JLabel(profile.getHeight()+"");
        resultWeightLabel =new JLabel(profile.getWeight()+"");

        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        resultNameLabel.setFont(labelFont);
        resultGenderLabel.setFont(labelFont);
        resultAgeLabel.setFont(labelFont);
        resultHeightLabel.setFont(labelFont);
        resultWeightLabel.setFont(labelFont);
    }

    private JPanel createSalivaTest(){
        JPanel salivaTest =new JPanel();
        salivaTest.setLayout(new BoxLayout(salivaTest, BoxLayout.X_AXIS));
        salivaTest.setBackground(Color.WHITE);
        salivaTest.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton testSaliva=new JButton("Add Sample");
        testSaliva.addActionListener(e->{
            if (!server.isConnected()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Device not connected. Please connect the device and try again.",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE
                );
            } else {
                try {
                    addSalivaSample(server.getHandler(), profile);
                    testSaliva.setBackground(Color.GREEN);
                    JOptionPane.showMessageDialog(
                        this,
                        "Sample collected successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error collecting sample: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    testSaliva.setBackground(Color.RED);
                }
            }
        });
        
        if(searchSalivaSample()){
            testSaliva.setBackground(Color.GREEN);
        }
        else{
            testSaliva.setBackground(Color.RED);
        }
        salivaTest.add(new JLabel("Saliva Sample"));
        salivaTest.add(add(Box.createRigidArea(new Dimension(10, 0))));
        salivaTest.add(testSaliva);
       

        return salivaTest;
    }

    private boolean searchSalivaSample(){
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return false;
        }
        if(file.length()<1){
            return false;
        }
        System.err.println(file.length());
        str=new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while((line=reader.readLine())!=null){
                this.str.append(line.trim()+"\n");
            }
            System.out.println(str);
        }catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }

    private JPanel createMineralsPanel() {
        JPanel mineralsPanel = new JPanel();
        mineralsPanel.setLayout(new BoxLayout(mineralsPanel, BoxLayout.Y_AXIS));
        mineralsPanel.setBackground(Color.WHITE);
        mineralsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mineralsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        mineralsPanel.setMaximumSize(new Dimension(600, 300));

        // Minerals Values Title
        JLabel mineralsTitle = new JLabel("Minerals");
        mineralsTitle.setFont(new Font("Arial", Font.BOLD, 24));
        mineralsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mineralsTitle.setForeground(new Color(44, 62, 80));

        // Create minerals table
        JPanel mineralsTable = new JPanel(new GridLayout(0, 4, 0, 0));
        mineralsTable.setBackground(Color.WHITE);
        mineralsTable.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(221, 221, 221)),
            BorderFactory.createEmptyBorder(15, 15, 10, 15)
        ));
        mineralsTable.setMaximumSize(new Dimension(400, 100));

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
        mineralsTable.add(new JLabel());

        // Add all minerals with their values
        String[] minerals = {"Potassium", "Iron",  "Calcium"};
        String[] units = {"mEq/L", "µg/dL", "mg/dL"};
        
        for (int i = 0; i < minerals.length-1; i++) {
            JLabel mineralLabel = new JLabel(minerals[i]);
            mineralLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            
            
            JLabel valueLabel = new JLabel(profile.getMinerals()[i]+"");
            valueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            valueLabel.setName(i+"");
            
            JLabel unitLabel = new JLabel(units[i]);
            unitLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JButton testButton =new JButton("test");
            testButton.setFont(new Font("Arial",Font.PLAIN, 14));

            testButton.addActionListener(e->{
                if (!server.isConnected()) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Device not connected. Please connect the device and try again.",
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                } 
                else if(str==null){
                	JOptionPane.showMessageDialog(
                            this,
                            "No Raw Sample Data available, First run Add Samples",
                            "Procedyral Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                }
                else {
                    try {
                    	
                        float newData = runTest(server.getHandler(), mineralLabel.getText(), profile);
                        profile.getMinerals()[Integer.parseInt(valueLabel.getName())] = newData;
                        DataManager.saveAllProfiles(profiles);
                        valueLabel.setText(newData + "");
                        JOptionPane.showMessageDialog(
                            this,
                            "Test completed successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Error running test: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            });
            
            mineralsTable.add(mineralLabel);
            mineralsTable.add(valueLabel);
            mineralsTable.add(unitLabel);
            mineralsTable.add(testButton);
        }

        // Add components to minerals panel
        mineralsPanel.add(mineralsTitle);
        mineralsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mineralsPanel.add(mineralsTable);

        return mineralsPanel;
    }

    private void Generate() {
    	mainPanel.add(new FinalResult(cardLayout, mainPanel, getCurrentProfile()), "generate");
    	cardLayout.show(mainPanel, "generate");
    	
    }

    private float runTest(Socket clientHandler,String mineral, Profile profile) {

        try{
        	notify= new Notification();
        	
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            DataOutputStream out=new DataOutputStream(clientHandler.getOutputStream());
            DataInputStream in= new DataInputStream(clientHandler.getInputStream());
            out.writeInt(1);
            out.flush();
            System.out.println("sent 1");
            StringBuilder testSample=new StringBuilder();
            int i=0;
            while (true) {
            	
                while(in.available()>0){
                    int message = in.read();
        
                    if(message==255){
                        System.out.println("received");
                        System.out.println(testSample);
                        if(notify.window!=null)notify.window.dispose();
                        notify = null;
                        return evaluate(testSample, mineral);
                    }
                    else if(message==44){
                        testSample.append("\n");
                        notify.pr.setValue(i++);
                    }
                    else{
                        testSample.append((char)message);
                    }
                }
            }
        }
        catch(IOException e){
        	if(notify.window!=null)notify.window.dispose();
        	notify = null;
            System.err.println("error in runtest");
        }

        return 0;
    }

    public void addSalivaSample(Socket clientHandler, Profile profile){
        try{
        	notify = new Notification();
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            DataOutputStream out=new DataOutputStream(clientHandler.getOutputStream());
            DataInputStream in= new DataInputStream(clientHandler.getInputStream());
            out.writeInt(1);
            out.flush();
            System.out.println("sent 1");
            str=new StringBuilder();
            int i=0;
            while (true) {
                while(in.available()>0){
                    int message = in.read();
        
                    if(message==255){
                        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE, false))) {
                           writer.println(String.format("%s", str.toString()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.print("received");
                        if(notify.window!=null)notify.window.dispose();
                        notify = null;
                        return;
                    }
                    else if(message==44){
                        str.append("\n");
                        notify.pr.setValue(i++);
                    }
                    else{
                        str.append((char)message);
                    }
                }
            }
        }
        catch(IOException e){
            System.err.println("error in SalivaTest");
            if(notify.window!=null)notify.window.dispose();
            notify = null;
        }
    }

    public float evaluate(StringBuilder testSample, String mineral) {
        // Split the test sample into lines
        String[] salivas= str.toString().split("[\n]+");
        String[] lines = testSample.toString().split("[\n]+");
        float result = 0.0f;
        float value=0.0f;
        // Process each line of the test sample
        System.out.println(salivas.length);
        System.out.println(lines.length);
        float avg = 0.0f;
        float avg2= 0.0f;
        for(String s:lines)
        {
        	avg+=Float.parseFloat(s);
        }
        avg = ((avg*3.3f)/4095.0f)*5f;
        
        for(String s:salivas)
        {
        	avg2+=Float.parseFloat(s);
        }
        avg2= ((avg2*3.3f)/4095.0f)*5f;
        avg/=100;
        avg2/=100;
        System.out.println(avg2);
        System.out.println(avg);
        
        value = avg2-avg;
        value= avg2-avg;
        System.out.println("Test value :"+value);
        if(value >=-0.1f && value<=0.1f)
        	return result;
        	
            try {
                // Map the mineral index to the corresponding mineral value
                switch(mineral) {
                    case "Potassium": {// Potassium
                    	int valence = 1;
                    	float mass= 39.09f;
                        result = 17.7f +((integrate(valence, value )*valence)/mass); // mEq/L
                        System.out.println("result  "+result);
                        break;
                    }
                    case "Calcium": {// Calcium
                        int valency=2;
                        int atom=20;
                        result = (12.79f-value)*profile.getWeight(); // mg/dL
                        System.out.println("result  "+result);
                        break;
                    }
                    case "Sodium": {// Sodium
                        result = value ; // mg/dL
                        break;
                    }
                    case "Magnesium": {// Magnesium
                        result = value ; // mg/dL
                        break;
                    }
                    case "Iron": {// Iron
                    	int valence=2;
                    	float mass = 55.84f;
                    	
                        result = 100+ (value*mass); // µg/dL
                        System.out.println("result  "+result);
                        break;
                    }
                    case "Zinc": {// Zinc
                        result = value ; // µg/dL
                        break;
                    }
                    default:
                        System.err.println("Unknown mineral: " + mineral);
                        return 0.0f;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        
        // Round to 2 decimal places
        return result;
    }

    public Profile getCurrentProfile() {
        return profile;
    }
    
    private float integrate( int b, float value) {
    	if(b==1)
    		return value;
    	return (float)Math.pow(2,b)*value + integrate(b-1, value);
    }
    
}