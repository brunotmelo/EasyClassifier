/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gc4mir.controllers;

import java.util.ArrayList;
import org.gc4mir.basics.Recording;

/**
 *
 * @author Bruno
 */
public class BaseRecordings {
    private ArrayList<Recording> recordings;
    
    public BaseRecordings(){
        recordings = new ArrayList<>();
    }
    
    public void addRecording(Recording r){
        boolean repeated = false;
        //only inserts if recording does not exist
        for(int i = 0; i < recordings.size(); i++){
            if(recordings.get(i).getPath().compareTo(r.getPath()) == 0){
                repeated = true;
                break;
            }
        }
        if(!repeated){
            recordings.add(r);
        }
    }
    
    public void removeRecording(String path){
        for(int i = 0; i < getRecordingsSize(); i++){
                if(getRecording(i).getPath().equals(path)){
                    recordings.remove(i);
                    break;
                }
            }
    }
    
    public Recording getRecording(int i){
        return recordings.get(i);
    }
    
    public int getRecordingsSize(){
        return recordings.size();
    }
    
    public boolean isEmpty(){
        return recordings.isEmpty();
    }
    
    public void reset(){
        recordings = new ArrayList<>();
    }
}

