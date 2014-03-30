/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmusictagger.table;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.CellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import br.com.jmusictagger.model.Tag;

/**
 * Class used to represent the MP3 table, when something is pasted (Ctrl+V) 
 * when something is pasted on the Column 5 of this table, it gets the data from 
 * the clipboard and puts it in the CellRenderer without passing through the 
 * cell, therefore keeping the line breaks. 
 * It should also be used a TableCellRenderer with a JTextArea if you want to 
 * keep the line breaks
 *
 * @author yurifw
 * @see JTextAreaCellRenderer
 */
public class TableMP3 extends JTable {

    TagTableModel model;

    /**
     * Constructs the TableMP3 and adds the listener for the paste action
     *
     */
    public TableMP3() {
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doPaste();
            }
        };
        final KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
        this.registerKeyboardAction(listener, "Paste", stroke, JComponent.WHEN_FOCUSED);
        
    }

    /**
     * "Overrides" the custom paste action of the system.
     */
    private void doPaste() {
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable content = clipboard.getContents(this);
        if (content != null) {
            try {
                final String value = content.getTransferData(DataFlavor.stringFlavor).toString();

                final int col = this.getSelectedColumn();
                final int row = this.getSelectedRow();
                if (isCellEditable(row, col) || col==Tag.LYRICS.getIndex()) {
                    this.setValueAt(value, row, col);
                    if (this.getEditingRow() == row && this.getEditingColumn() == col) {
                        final CellEditor editor = this.getCellEditor();
                        editor.cancelCellEditing();
                        this.editCellAt(row, col);
                    }
                }
                this.repaint();
            } catch (UnsupportedFlavorException e) {
                // String have to be the standard flavor
                System.err.println("UNSUPPORTED FLAVOR EXCEPTION " + e.getLocalizedMessage());
            } catch (IOException e) {
                // The data is consumed?
                System.err.println("DATA CONSUMED EXCEPTION " + e.getLocalizedMessage());
            }
        }
    }
    
}
