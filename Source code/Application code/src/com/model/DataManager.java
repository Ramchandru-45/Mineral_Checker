package com.model;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

public class DataManager {
    private static final String DATA_FILE = "users.txt";

    public static void saveProfile(Profile profile) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE, true))) {
            String data = String.format("%s|%s|%s|%s|%s|%s",
                    profile.getName(), profile.getAge(), profile.getGender(), 
                    profile.getHeight(), profile.getWeight(),Arrays.toString(profile.getMinerals()));
            writer.println(data);
            System.out.println("Data saved to file: " + data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAllProfiles(List<Profile> profiles) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE, false))) {
            for (Profile profile : profiles) {
                String data = String.format("%s|%s|%s|%s|%s|%s",
                        profile.getName(), profile.getAge(), profile.getGender(), 
                                    profile.getHeight(), profile.getWeight(),Arrays.toString(profile.getMinerals()));
                writer.println(data);
            }
            System.out.println("All profiles saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadProfiles(List<Profile> profiles) {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("[|]+");
                if (data.length == 5) {
                    profiles.add(new Profile(data[0], Integer.parseInt(data[1]), data[2], Float.parseFloat(data[3]),
                     Float.parseFloat(data[4]), new float[3]));
                }
                else if(data.length == 6){
                    profiles.add(new Profile(data[0], Integer.parseInt(data[1]), data[2], Float.parseFloat(data[3]),
                    Float.parseFloat(data[4]), toFloatArray(data[5].substring(1,data[5].length()-1))));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static float[] toFloatArray(String data){
        String[] str=data.trim().split(",");
        float[] mineralList = new float[3];
        for(int i=0;i<mineralList.length;i++){
            mineralList[i]=Float.parseFloat(str[i].trim());
        }
        return mineralList;
    }

    public static String floatString(float[] data){
        StringBuffer str=new StringBuffer();
        for(float f:data){
            str.append(f);
        }
        return str.toString();
    }
}