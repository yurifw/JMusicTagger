/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmusictagger.table;

import br.com.jmusictagger.internationalization.Message;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * TableCellRenderer to keep line breaks of the lyrics.
 * @author ywaki
 */
public class JTextAreaCellRenderer extends JTextArea implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setText(value.toString());
        this.setToolTipText(Message.get("lyrics_tooltip"));
        
        table.setRowHeight(40);
        
        if (isSelected) {
            super.setForeground(Color.WHITE);
            super.setBackground(Color.BLACK);
        } else {
            super.setForeground(table.getForeground());
            super.setBackground(table.getBackground());
        }
        return this;
    }
}