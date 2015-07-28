/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gc4mir.core;

import jAudio.JAudioCommandLine;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.gc4mir.basics.Category;
import org.gc4mir.basics.Recording;
import org.gc4mir.controllers.BaseRecordings;

/**
 *
 * @author Bruno
 */
public class JaudioThread extends Thread{
    
    private JFrame mainScreen;
    private Core core;
    //there are two extraction methods, 
    //one uses categories(classes that labels files). other uses only recordings.
    //which call to make is determined by categorized.
    private boolean categorized;
    //if this option is enabled, the output will be in the autoweka format.
    private boolean autoweka;
    
    /**
     * 
     * @param mainScreen the screen that called this thread
     * @param categorized if true, will extract audios from categories
     */
    public JaudioThread(JFrame mainScreen,boolean categorized){
        this.mainScreen = mainScreen;
        core = Core.getInstance();
        this.categorized = categorized;
        this.autoweka = false;
        
    }
    /**
     * Initializes a thread that will extract audio features
     * 
     * @param mainScreen the screen that created this thread
     * @param categorized boolean indicating whether the input audio files have categories
     * @param autoweka boolean indicating if the output will be exported to auto-WEKA
     */
    public JaudioThread(JFrame mainScreen,boolean categorized,boolean autoweka){
        this.mainScreen = mainScreen;
        core = Core.getInstance();
        this.categorized = categorized;
        this.autoweka = autoweka;
        
    }

    
    public void run(){
        if(!categorized){
            extractFeatures();
        }else{
            extractFeaturesCategories();
        }
  
    }
    
    public void extractFeatures(){
        BaseRecordings recordings = core.getBaseRecordings();
        
        //this line loads extraction settings
        File jsettings = new File("jaudio-settings.xml");
        String jspath = jsettings.getAbsolutePath();
        
        //creates the arguments that will be sent to jAudio
        ArrayList<String> argsArray = new ArrayList();
        argsArray.add("-s");
        argsArray.add(jspath);
        argsArray.add("feature_values");
        for (int i=0; i<recordings.getRecordingsSize(); i++){
            argsArray.add(recordings.getRecording(i).getPath());
        }
        
        String[]args = argsArray.toArray(new String[(argsArray.size())]);

        try{
            JAudioCommandLine.execute(args);
        }catch(Exception e){
           //JOptionPane.showMessageDialog(mainScreen, "Error during extraction","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public void extractFeaturesCategories(){
        ArrayList<String> argsArray = new ArrayList();
        
        File jsettings;
        if(!autoweka){
            jsettings = new File("jaudio-settings.xml");
        }else{
            jsettings = new File("jaudio-settings-arff.xml");
        }
        
        String jspath = jsettings.getAbsolutePath();
        
        argsArray.add("-s");
        argsArray.add(jspath);
        //if autoweka export mode is enabled, output will be saved in the export folder
        //
        if(!autoweka){
           argsArray.add("feature_values");  
        }else{
           argsArray.add("export/autoweka/clean");
        }
        
        
        ArrayList <Category> categories = (ArrayList <Category>) core.getBaseCategories().getCategories();
        ArrayList <Recording> recordings;
        for(int i = 0;i<categories.size(); i++ ){
            recordings = (ArrayList<Recording>) categories.get(i).getRecordings();
            for(int k=0; k<recordings.size();k++){
                boolean repeated = false;
                //only inserts if recording does not exist
                /*for(int y = 0; y < argsArray.size(); y++){
                    if(recordings.get(i).getPath().compareTo(recordings.get(k).getPath()) == 0){
                        repeated = true;
                        break;
                    }
                }
                if(!repeated){
                    argsArray.add(recordings.get(k).getPath());
                }*/
                argsArray.add(recordings.get(k).getPath());
            }
            
        }
        String[]args = argsArray.toArray(new String[(argsArray.size())]);
        try{
            JAudioCommandLine.execute(args);
            
            if(autoweka){
                core.addArffClasses();
            }
            
        }catch(IOException e){
            System.out.println("Error while adding classes");
            e.printStackTrace();
        }catch(Exception e){
            System.out.println("jAudio extraction error");
            e.printStackTrace();
        }
                       
         
    }
    
}
