/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmusictagger.filenavigation;

import java.io.File;

/**
 * A custom File filter to be used with the javax.swing.JFileChooser
 * @author yurifw
 */
public class FileFilter extends javax.swing.filechooser.FileFilter {
    
    String validFormats[] = {"JPEG", "JPG"};
    private final char DOT_INDEX = '.';
    
    /**
     * The method which will determine if the file will be shown in the JFileChooser
     * @param f the file to be analyzed
     * @return true if the file is valid to be shown<br>false if the file should not be shown
     */
    private boolean isFileValid(File f){
        if (f.isDirectory())
            return true;
        for (String s: validFormats){
            if (extension(f).equalsIgnoreCase(s))
                return true;
        }
        return false;
    }
    
    public boolean accept (File f) {        
        return isFileValid(f);
    }
    
    public String getDescription () {
        String description = "Arquivos";
        for (String s: validFormats){
            description = description.concat(" ."+s);
        }
        return description;
    }
    
    /**
     * Returns the extension of the given file
     * @param f the file
     * @return the extendion of the file
     */
    public String extension (File f) {
        String fileName = f.getName();
        int indexFile = fileName.lastIndexOf(DOT_INDEX);
        if (indexFile >0 && indexFile<fileName.length()) {
            return fileName.substring(indexFile+1);
        } else {
            return "";
        }
        
    }
    
    
    
}
