/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gc4mir.core;

import ace.CommandLine;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gc4mir.util.ACECommunicator;

/**
 *
 * @author Bruno
 */
public class CommandThread extends Thread{
    
    private String [] args;
    
    public CommandThread(String[] args){
        this.args = args;
    }
    
    public void run(){
        try{
            new CommandLine(args).processRequests();
        }catch(Exception ex){
            Logger.getLogger(ACECommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
}
