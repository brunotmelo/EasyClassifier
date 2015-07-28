/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.gc4mir.controllers;

import java.util.ArrayList;
import java.util.List;
import org.gc4mir.basics.Recording;
import org.gc4mir.basics.Category;

/**
 *
 * @author Joseph
 */
public class BaseCategories {
    private List<Category> categories;
    
    public BaseCategories(){
        categories = new ArrayList<>();
    }
    
    public void reset(){
        categories = new ArrayList<>();
    }
    
    public void addCategory(Category c){
        boolean inserted = false;
        for(int i = 0; i < categories.size(); i++){
            if(categories.get(i).getId().compareTo(c.getId()) > 0){
                categories.add(i, c);
                inserted = true;
                break;
            }
        }
        if(!inserted){
            categories.add(c);
        }
    }
    
    public void editCategory(String oldId, String newId){
        for(int i = 0; i < categories.size(); i++){
            if(categories.get(i).getId().equals(oldId)){
                categories.get(i).setId(newId);
                break;
            }
        }
    }
    
    public void removeCategory(String id){
        for(int i = 0; i < categories.size(); i++){
            if(categories.get(i).getId().equals(id)){
                categories.remove(i);
                break;
            }
        }
    }
    
    public void addRecording(String idCategory, Recording r){
        Category s = null;
        for(int i = 0; i < categories.size(); i++){
            if(categories.get(i).getId().equals(idCategory)){
                s = categories.get(i);
                break;
            }
        }
        if(s != null){
            s.getRecordings().add(r);
        }
    }
    
    public void removeRecording(String idCategory, String pathRecording){
        Category s = null;
        for(int i = 0; i < categories.size(); i++){
            if(categories.get(i).getId().equals(idCategory)){
                s = categories.get(i);
                break;
            }
        }
        if(s != null){
            for(int i = 0; i < s.getRecordings().size(); i++){
                if(s.getRecordings().get(i).getPath().equals(pathRecording)){
                    s.getRecordings().remove(i);
                    break;
                }
            }
        }
    }
    
    public Category getCategory(String id){
        Category s = null;
        for(int i = 0; i < categories.size(); i++){
            if(categories.get(i).getId().equals(id)){
                s = categories.get(i);
                break;
            }
        }
        return s;
    }
    
    public boolean extractFeatures(){
        boolean success = false;
        return success;
    }

    public List<Category> getCategories() {
        return categories;
    }
    
    public int size(){
        return categories.size();
    }
    
    //Return -1 if the speaker is not registred or 'i' (his index) for speaker is registred
    public int existsCategory(String id){
        int result = -1;
        for(int i = 0; i < categories.size(); i++){
            if(categories.get(i).getId().equals(id)){
                result = i;
                break;
            }
        }
        return result;
    }
    
    public boolean hasRecordings(){
        boolean notEmpty = false;
        
        for(Category current:categories){

            if(!current.getRecordings().isEmpty()){
                notEmpty = true;
                break;
            }
        }
        
        return notEmpty;
    }
    
}
