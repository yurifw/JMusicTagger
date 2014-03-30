/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmusictagger.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class to make operations that need a tag pattern, such as obtaining tags from file names.
 * A pattern is a String that includes a marking, for example, if you want to retrieve the
 * track number and title from a file named "01 - poison.mp3" you should 
 * specify the tag pattern @code{ "\<track number> - <track title>.mp3"}
 * @author yurifw
 */
public class TagPattern {
    private String pattern;

    public TagPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    
    /**
     * Retrieves tags from a file name.
     * @param fileName name of the file
     * @return A HashMap, where the key is the Tag and the String is its value
     */
    public HashMap<Tag, String> retrieveTags(String fileName){
        HashMap<Tag,String> tag = new HashMap<>();
        //<track number> - <title>
        String[] dividers = pattern.split("<[^>]*>");
        
        List<String> usedTags = new ArrayList<>();
        for (Tag t : Tag.values()){
            if (pattern.contains(t.getPattern())) {
                usedTags.add(t.getPattern());
            }
        }
        
        return tag;
    }
    
    /**
     * Rename a file according to its tags and the current tag pattern.
     */
    public void renameFile(MP3 file){
        String newName = getPattern();
        for (Tag t:Tag.values()){
            if (t==Tag.ARTWORK) continue;
            newName = newName.replace(t.getPattern(), (String)file.getTag(t));
        }
        newName = newName+".mp3";
        String fullName = file.getFile().getParent()+System.getProperty("file.separator")+newName;
        file.getFile().renameTo(new File(fullName));
    }
}
