/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.gc4mir.basics;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joseph
 */
public class Category {
    private String id;
    private List<Recording> recordings;
    
    public Category(String id){
        this.id = id;
        this.recordings = new ArrayList<Recording>();
    }

    public int existsRecording(String path){
        int result = -1;
        for(int i = 0; i < recordings.size(); i++){
            if(recordings.get(i).getPath().equals(path)){
                result = i;
                break;
            }
        }
        return result;
    }
    
    public void addRecording(Recording r){
        String path = r.getPath();
        Boolean repeated = false;
        for(int i = 0; i < recordings.size(); i++){
            if(recordings.get(i).getPath().equals(path)){
                repeated = true;
                break;
            }
        }
        if(!repeated){
            recordings.add(r);
        }
        
    }
    
    public void resetRecordings(){
        this.recordings = new ArrayList<Recording>();
    }
    
    public void removeRecording(String path){
        for(int i = 0; i < recordings.size(); i++){
            if(recordings.get(i).getPath().equals(path)){
                recordings.remove(i);
                break;
            }
        }
    }
    
    public String getId() {
        return id;
    }

    public List<Recording> getRecordings() {
        return recordings;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    
}
