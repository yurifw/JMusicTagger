/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmusictagger.model;

import br.com.jmusictagger.internationalization.Message;

/**
 * ID3 Tag enumeration. contains all tags that can be edited through this 
 * application and methods to help it.
 * @author yurifw
 */
public enum Tag {
    
    TRACK_NUMBER(0),TITLE(1),ALBUM(2),ARTIST(3),YEAR(4),LYRICS(5),GENRE(6),ARTWORK(7);
    
    String[] stringRepresentation = {Message.get("track_number"),Message.get("track_title"),Message.get("album"),Message.get("artist"),Message.get("year"),Message.get("lyrics"),Message.get("genre"),Message.get("cover")};
    String[] pattern = {"<number>","<title>","<album>","<artist>","<year>","<lyrics>","<genre>","<artwork>"};
    
    int index;

    private Tag(int index) {
        this.index = index;
    }
    
    public int getIndex(){
        return index;
    }

    @Override
    public String toString() {
        return stringRepresentation[index];
    }
    
    public String getPattern(){
        return pattern[index];
    }
    
    
}
