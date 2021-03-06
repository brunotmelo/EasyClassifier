/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gc4mir.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 *
 * @author Bruno
 */



public class BaseClassifiers {
    
    private ArrayList<String> classifiers;
    private File classifierFile;
    
    
    
    public BaseClassifiers(){
        classifierFile = new File("classifiers/list");
        classifiers = new ArrayList<>();
        loadClassifiers();
        
    }
    
    public String getName(int i){
        return classifiers.get(i);
    }
    
    public int getSize(){
        return classifiers.size();
    }
    
    public void addClassifier(String model){
        classifiers.add(model);
        writeFile();
        
    }
    

    
    public void writeFile(){
        try{
            
            FileWriter fw = new FileWriter(classifierFile.getAbsoluteFile());
        
            BufferedWriter bw = new BufferedWriter(fw);
        
            for (String classifier : classifiers) {
                bw.write(classifier);
                bw.newLine();
            }

            bw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void loadClassifiers(){
        
        BufferedReader br;
        try{
            try{
                br = new BufferedReader(new FileReader(classifierFile));
            }catch(FileNotFoundException e){
                classifierFile.createNewFile();
                br = new BufferedReader(new FileReader(classifierFile));
            }
            
            String line = null;
            classifiers.clear();
            
            while ((line = br.readLine()) != null) {
            
                classifiers.add(line);
                
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public int isRepeated(String model){
        int repeated = -1;
            //check if repeated:
            for(int i=0; i< classifiers.size();i++){
                if(classifiers.get(i).equals(model)){
                    repeated = i;
                    break;
                }
            }
            
            return repeated;
    }
    
    //this class does not work yet
    public void replaceClassifier(int index,String model){
        classifiers.set(index,model);
        writeFile();
    }
    
    /*public void renameClassifier(int index,String newName){
        String name = classifiers.get(index);
        
        File file = new File("classifiers\\"+name+".model");
        
        
    }*/
    
    public void removeClassifier(int index){
        String name = classifiers.get(index);
        
        try{
            //deletes .model file
        File file = new File("classifiers/"+name+".model");
 
    		if(file.delete()){
    			System.out.println(file.getName() + " was deleted");
    		}else{
    			System.out.println("File to be deleted not found");
    		}
        
        //deletes taxonomy file
        file = new File("classifiers/Taxonomy-"+name+".xml");
        
        if(file.delete()){
            System.out.println(file.getName() + " was deleted");
        }else{
            System.out.println("File to be deleted not found");
        }
        //removes the classifier from buffer
        classifiers.remove(index);
        //removes the classifier from list
        writeFile();
        }catch(Exception e){
            System.out.println("There was an error while trying to remove the classifier");
            e.printStackTrace();
        }
            
    }
    
}
