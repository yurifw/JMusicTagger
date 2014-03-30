/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmusictagger.internationalization;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author ywaki
 */
public class Message {
    private static ResourceBundle strings = ResourceBundle.getBundle("br.com.jmusictagger.internationalization.languages.language", Locale.getDefault());
     public static String get(String key){
         return strings.getString(key);
     }
}
