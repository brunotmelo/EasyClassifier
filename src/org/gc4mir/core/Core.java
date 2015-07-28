/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.gc4mir.core;

import ace.datatypes.Taxonomy;
import jAudioFeatureExtractor.Controller;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.gc4mir.basics.Recording;
import org.gc4mir.basics.Category;
import org.gc4mir.controllers.BaseCategories;
import org.gc4mir.controllers.BaseClassifiers;
import org.gc4mir.controllers.BaseRecordings;
import org.gc4mir.gui.ResultsScreen;
import weka.core.*;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;

/**
 *
 * @author Joseph
 */
public class Core {
    private BaseCategories baseCategories;
    private BaseRecordings baseRecordings;
    private BaseClassifiers baseClassifiers;
    private Taxonomy taxonomy;
    private static Core core;
    private boolean help;

    private static ResultsScreen results;
    
    private Core(){
        baseCategories = new BaseCategories();
        baseRecordings = new BaseRecordings();
        baseClassifiers = new BaseClassifiers();
        taxonomy = new Taxonomy();
    }
    
    public static Core getInstance(){
        if(core == null){
            core = new Core();
        }
        return core;
    }
    
    //the resultsScreen is saved in the core so that any window can reopen it.
    public void setResults(ResultsScreen results){
        this.results = results;
    }
    
    public ResultsScreen getResults(){
        return results;
    }
    
    
    public Category addCategory(String id){
        Category c = new Category(id);
        baseCategories.addCategory(c);
        taxonomy.addClass(id);
        refreshTaxonomy();
        
        return c;
    }
    
    
    public Controller createController(){
        return new Controller();
        
    }
    
    /**
     * renames the category with oldId name.
     * @param oldId the category old id
     * @param newId the category new id
     */
    public void editCategory(String oldId, String newId){
        baseCategories.editCategory(oldId, newId);
        taxonomy.deleteClass(oldId);
        taxonomy.addClass(newId);
        refreshTaxonomy();
    }
    
    /**
     * removes a category from baseCategories and from the taxonomy file.
     * @param id The id of the category to be deleted
     */
    public void removeCategory(String id){
        baseCategories.removeCategory(id);
        taxonomy.deleteClass(id);
        refreshTaxonomy();
    }
    /**
     * Adds an audio in a category
     * 
     * @param idCategory the category which the audio belongs
     * @param name the name of the audio to be added
     * @param path the path of the audio to be added
     */
    public void addRecording(String idCategory, String name, String path){
        if(baseCategories.getCategories().get(baseCategories.existsCategory(idCategory)).existsRecording(path) == -1){
            Recording r = new Recording(name, path);
            baseCategories.addRecording(idCategory, r);
        }
    }
    
    /**
     * Adds and uncategorized audio into baseRecordings.
     * @param name the name of the audio to be added
     * @param path the path of the audio to be added
     */
    public void addRecording(String name, String path){
        Recording r = new Recording(name,path);
        baseRecordings.addRecording(r);
    }
    
    public void removeRecording(String idCategory, String pathRecording){
        baseCategories.removeRecording(idCategory, pathRecording);
    }
    
    public boolean extractFeatures(){
        return baseCategories.extractFeatures();
    }

    public BaseCategories getBaseCategories() {
        return baseCategories;
    }
    
    public BaseClassifiers getBaseClassifiers(){
        return baseClassifiers;
    }
            
    
    public int getRecordingsSize(){
        return baseRecordings.getRecordingsSize();
        
    }
    
    public BaseRecordings getBaseRecordings(){
        return baseRecordings;
        
    }
    
    /**
     * Creates the files that are required to be sent to ace to build a classifier
     */
    public void createFiles(){
        try {
            String content = "";
            content = content + "<?xml version=\"1.0\"?>\n" +
                    "<!DOCTYPE classifications_file [\n" +
                    "   <!ELEMENT classifications_file (comments, data_set+)>\n" +
                    "   <!ELEMENT comments (#PCDATA)>\n" +
                    "   <!ELEMENT data_set (data_set_id, misc_info*, role?, classification)>\n" +
                    "   <!ELEMENT data_set_id (#PCDATA)>\n" +
                    "   <!ELEMENT misc_info (#PCDATA)>\n" +
                    "   <!ATTLIST misc_info info_type CDATA \"\">\n" +
                    "   <!ELEMENT role (#PCDATA)>\n" +
                    "   <!ELEMENT classification (section*, class*)>\n" +
                    "   <!ELEMENT section (start, stop, class+)>\n" +
                    "   <!ELEMENT class (#PCDATA)>\n" +
                    "   <!ELEMENT start (#PCDATA)>\n" +
                    "   <!ELEMENT stop (#PCDATA)>\n" +
                    "]>\n"
                    + "<classifications_file>\n" +
                    "\n" +
                    "   <comments></comments>\n"
                    + "\n";
            
            for(int i = 0; i < core.getBaseCategories().getCategories().size(); i++){
                for(int j = 0; j < core.getBaseCategories().getCategories().get(i).getRecordings().size(); j++){
                    String instance = "<data_set>\n" +
                                    "		<data_set_id>"
                                    + core.getBaseCategories().getCategories().get(i).getRecordings().get(j).getPath() +
                                    "</data_set_id>\n" +
                                    "		<role>training</role>\n" +
                                    "    	<classification><class>"
                                    + core.getBaseCategories().getCategories().get(i).getId() +
                                    "</class></classification>\n" +
                                    "   </data_set>\n";
                    content = content + instance;
                }
            }
            
            content = content + "\n</classifications_file>";
            
            FileWriter featuresNewFile;
            featuresNewFile = new FileWriter(new File("Instances.xml"));
            featuresNewFile.write(content);
            featuresNewFile.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    public void refreshTaxonomy(){
          
       // Get file to write to
       File save_file = new File("Taxonomy.xml");
       // Change the root name of the taxonomy
       Taxonomy.setRootName(taxonomy, save_file.getPath());

       // Save the file
       try
       {
            Taxonomy.saveTaxonomy(taxonomy, save_file, "");
       }
       catch (Exception e)
       {
            // e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
       }
     }
    
    public void saveClassifier(String model){
        //create a copy of the files used for this classifier in the classifiers folder
        
        
        
    }
    
    public void setHelp(Boolean enabled){
        this.help = enabled;
    }
    
    public Boolean getHelp(){
        return this.help;
    }
    
    public void openManual(){
        Desktop d=Desktop.getDesktop();

        try {
            // Browse a URL, say google.com
            d.browse(new URI("http://google.com"));
        } catch (URISyntaxException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex){
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //add class attribute to arff
    public void addArffClasses() throws IOException, Exception{
        //opens classless arff
        BufferedReader reader = new BufferedReader(
                              new FileReader("export/autoweka/clean.arff"));
        Instances data = new Instances(reader);
        reader.close();
        
        //add classes to file.
        
        //creates filter to add attribute
        Add filter;
        filter = new Add();
        filter.setAttributeIndex("last");
        
        //get category names to set in the header
        String nominalLabels = new String();
        for(int i=0;i<getBaseCategories().size(); i++){

            Category current = getBaseCategories().getCategories().get(i);
            nominalLabels += current.getId();
            if(i<getBaseCategories().size()-1){
                nominalLabels +=",";
            }
        }
        System.out.println("");
        filter.setNominalLabels(nominalLabels);
        filter.setAttributeName("Category");
        filter.setInputFormat(data);
        data = Filter.useFilter(data, filter);
        
        //add class values to each instance
        int classindex = data.numAttributes()-1;
        
        for(int i=0,n=0; i<getBaseCategories().size(); i++){
            Category current = getBaseCategories().getCategories().get(i);
            for(int k=0;k<current.getRecordings().size();k++){
                Instance inst = data.instance(n);
                //inst.setDataset(null);
                inst.setValue(classindex, current.getId());
                //inst.setDataset(data);
                n++;
            }
            
            //inst.insertAttributeAt();
        }
        
        
        //saves arff file
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File("export/autoweka/train.arff"));
        saver.writeBatch();
        
        System.out.println("File sucesfully saved, you can continue now");
        
    }
    
}
