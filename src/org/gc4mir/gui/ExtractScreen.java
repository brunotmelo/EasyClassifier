/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gc4mir.gui;

import jAudio.JAudioCommandLine;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.gc4mir.controllers.BaseRecordings;
import org.gc4mir.core.Core;

/**
 *
 * @author Bruno
 */
public class ExtractScreen extends javax.swing.JFrame {

    private DefaultTableModel tableModelRecordings;
    private JFileChooser load_recording_chooser = null;
    private int categoryIndex;
    private Core core;
    /**
     * Creates new form ExtractScreen
     */
    public ExtractScreen() {
        this.setTitle("Extract features");
        initComponents();
        setLocationRelativeTo(null);
        
        setLocationRelativeTo(null);
        
        this.core = Core.getInstance();
        this.tableModelRecordings = new DefaultTableModel();
        this.tableModelRecordings.addColumn("Name");
        this.tableModelRecordings.addColumn("Path");
        jTable1.setModel(this.tableModelRecordings);
    }

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("Extract");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Name", "Path"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jScrollPane2.setViewportView(jScrollPane1);

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        jButton3.setText("Remove");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                        .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(addButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addGap(96, 96, 96)
                .addComponent(jButton1)
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if (load_recording_chooser == null) {
			load_recording_chooser = new JFileChooser();
			load_recording_chooser.setCurrentDirectory(new File("."));
			load_recording_chooser
					.setFileFilter(new jAudioFeatureExtractor.jAudioTools.FileFilterAudio());
			load_recording_chooser
					.setFileSelectionMode(JFileChooser.FILES_ONLY);
			load_recording_chooser.setMultiSelectionEnabled(true);
		}

        // Read the user's choice of load or cancel
        int dialog_result = load_recording_chooser.showOpenDialog(null);

        // Add the files to the table and to recording_list
        if (dialog_result == JFileChooser.APPROVE_OPTION) // only do if OK
        // chosen
        {
                File[] load_files = load_recording_chooser.getSelectedFiles();
                try {
                    for(int i = 0; i < load_files.length; i++){
                        // Verify that the file exists
                        if(load_files[i].exists()){
                            core.addRecording(load_files[i].getName(), load_files[i].getPath());
                        }
                    }
                    refreshRecordingTable();
                } catch (Exception e1) {
                        e1.printStackTrace();
                }
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String path;
        
        int[] indexes = jTable1.getSelectedRows();
        for(int i = 0; i< indexes.length; i++){
            path = tableModelRecordings.getValueAt(indexes[i], 1).toString();
            core.getBaseRecordings().removeRecording(path);
        }
        
        refreshRecordingTable();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        BaseRecordings recordings = core.getBaseRecordings();
        
        ArrayList<String> argsArray = new ArrayList();
        argsArray.add("-s");
        argsArray.add("jaudio-settings.xml");
        argsArray.add("feature_values");
        for (int i=0; i<recordings.getRecordingsSize(); i++){
            argsArray.add(recordings.getRecording(i).getPath());
        }
        
        String[]args = argsArray.toArray(new String[(core.getRecordingsSize()+3)]);
        try{
            JAudioCommandLine.executeException(args);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error during extraction","Error",JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(this, "Extraction completed");
        recordings.reset();
        this.dispose();
        
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void refreshRecordingTable(){
        tableModelRecordings.setRowCount(0);
        for(int i = 0; i < core.getRecordingsSize(); i++){
            String name = core.getBaseRecordings().getRecording(i).getName();
            String path = core.getBaseRecordings().getRecording(i).getPath();
            tableModelRecordings.addRow(new Object[]{name, path});
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ExtractScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExtractScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExtractScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExtractScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExtractScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}