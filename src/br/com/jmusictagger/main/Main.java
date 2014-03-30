/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmusictagger.main;

import com.pagosoft.plaf.PgsLookAndFeel;
import java.awt.Toolkit;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import br.com.jmusictagger.viewer.FrmMain;

/**
 *
 * @author ywaki
 */
public class Main {

    /**
     * Starter class for the JMuiscTagger. All configurations that will affect
     * it globally should be made here.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new PgsLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        if (!Locale.getDefault().equals(new Locale("us", "EN")) && !Locale.getDefault().equals(new Locale("pt", "BR"))){
            Locale.setDefault(new Locale("us", "EN"));
        }

        ToolTipManager.sharedInstance().setInitialDelay(0);

        FrmMain frm = new FrmMain();
        frm.setIconImage(new ImageIcon(Main.class.getResource("/br/com/jmusictagger/images/app-icon.png")).getImage());
        frm.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frm.setVisible(true);
    }
}
