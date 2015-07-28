/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gc4mir.core;

import ace.CommandLine;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gc4mir.util.ACECommunicator;

/**
 *
 * @author Bruno
 */
public class CommandThread extends Thread{
    
    private String [] args;
    private boolean train=false;
    private String model;
    
    public CommandThread(String[] args){
        this.args = args;
    }
    
    /**
     * Special CommandThread constructor to train a classifier.
     * 
     * @param args the arguments to be passed to ACE
     * @param model the name of the model to be created
     */
    public CommandThread(String[] args, String model){
        this.args = args;
        this.train = true;
        this.model = model;
        
    }
    
    public void run(){
        try{
            new CommandLine(args).processRequests();
            if(train){
                Core.getInstance().getBaseClassifiers().addClassifier(model);
                copyTaxonomy(model);
            }
        }catch(Exception ex){
            Logger.getLogger(ACECommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    private void copyTaxonomy(String modelname){
        File src = new File("Taxonomy.xml");
        File dest = new File("classifiers/Taxonomy-"+ modelname+".xml");
        
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try{
                is.close();
                os.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        
        
    }
    
}
