/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmusictagger.viewer;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import br.com.jmusictagger.table.JTextAreaCellRenderer;
import br.com.jmusictagger.model.MP3;
import br.com.jmusictagger.table.TableMP3;
import br.com.jmusictagger.table.TagTableModel;
import br.com.jmusictagger.filenavigation.FileSystemTreeModel;
import br.com.jmusictagger.filenavigation.FileSystemTreeRenderer;
import br.com.jmusictagger.filenavigation.FileFilter;
import br.com.jmusictagger.internationalization.Message;
import java.awt.BorderLayout;
import javax.swing.DefaultComboBoxModel;
import br.com.jmusictagger.model.Tag;
import br.com.jmusictagger.model.TagPattern;

/**
 *
 * @author ywaki
 */
public class FrmMain extends JFrame {
    
    JTree directoryTree = new JTree();
    FileSystemTreeModel treeModel; //initialization on configureComponent
    JSplitPane verticalSplittedPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    TableMP3 table = new TableMP3();
    TagTableModel tagModel = new TagTableModel();
    JTextArea txtLyrics = new JTextArea();
    JTextField txtTagPattern = new JTextField();
    JButton btnRenameFileWithPattern = new JButton(Message.get("rename_tag_pattern"));
    JComboBox cboColumn = new JComboBox();
    JTextField txtValue = new JTextField();
    JButton btnChangeColumn = new JButton(Message.get("change"));
    JButton btnRenameFile = new JButton(Message.get("rename_file"));
    JButton btnRenameSequence = new JButton(Message.get("sequentially"));
    JButton btnChangeArtwork = new JButton(Message.get("change_cover"));
    JButton btnVisualizeArtwork = new JButton(Message.get("visualize_cover"));
    JButton btnSave = new JButton(Message.get("save"));

    public FrmMain() {
        this.setTitle(Message.get("app_title"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 600);
        this.add(verticalSplittedPane);

        configureComponents();
        createSplittedFrame();
        addListeners();
    }

    /**
     * Divides this frame in two separated frames. the upper half will contain
     * the JTree and the TabbedPane, the bottom half will contain the TableMP3
     */
    private void createSplittedFrame() {
        //adding the table in the bottom part
        JPanel tablePanel = new JPanel(new GridLayout(1, 1));
        JScrollPane scrollTable = new JScrollPane(table);
        tablePanel.add(scrollTable);
        verticalSplittedPane.setRightComponent(tablePanel);

        //adding the Directory Tree in the upper-left part
        JSplitPane horizontalSplittedPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JScrollPane scrollTree = new JScrollPane(directoryTree);
        scrollTree.setMinimumSize(new Dimension(200, 400));
        horizontalSplittedPane.setLeftComponent(scrollTree);

        //adding the tabbed pane in the upper-right part
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel editTagsPanel = new JPanel();
        BoxLayout editTagsLayout = new BoxLayout(editTagsPanel, BoxLayout.Y_AXIS);
        editTagsPanel.setLayout(editTagsLayout);

        //creating the "Edit tags" tab
        JPanel editManyTags = new JPanel(new GridLayout(1, 5));
        editManyTags.setBorder(new TitledBorder(Message.get("change_all_tags")));
        editManyTags.add(new JLabel(Message.get("change_all")));
        editManyTags.add(cboColumn);
        editManyTags.add(new JLabel(Message.get("to"), SwingConstants.CENTER));
        editManyTags.add(txtValue);
        editManyTags.add(btnChangeColumn);
        editManyTags.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
        editTagsPanel.add(editManyTags);

        editTagsPanel.add(Box.createHorizontalStrut(20));

        JPanel enumerateMusics = new JPanel(new GridLayout(1, 2));
        enumerateMusics.setBorder(new TitledBorder(Message.get("enumerate_songs")));
        enumerateMusics.add(btnRenameFileWithPattern);
        enumerateMusics.add(btnRenameSequence);
        enumerateMusics.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
        editTagsPanel.add(enumerateMusics);
        
        //creating the Rename Files panel
        JPanel renameFiles = new JPanel(new BorderLayout());
        renameFiles.add(txtTagPattern,BorderLayout.CENTER);
        JButton btnHelp = new JButton("?");
        btnHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(rootPane, Message.get("tag_pattern_help"),"Help",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        renameFiles.add(btnHelp,BorderLayout.EAST);
        renameFiles.add(btnRenameFileWithPattern,BorderLayout.SOUTH);
        renameFiles.setMaximumSize(new Dimension(Short.MAX_VALUE, 5*30));
        renameFiles.setBorder(new TitledBorder(Message.get("rename_file")));
        editTagsPanel.add(renameFiles);

        editTagsPanel.add(Box.createHorizontalStrut(20));

        JPanel editCover = new JPanel(new GridLayout(1, 2));
        editCover.setBorder(new TitledBorder(Message.get("cover")));
        editCover.add(btnChangeArtwork);
        editCover.add(btnVisualizeArtwork);
        editCover.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
        editTagsPanel.add(editCover);



        //creating the lyrics tab
        JScrollPane scrollLyrics = new JScrollPane(txtLyrics);


        //add the jpanels to the tabbed panel and set their title
        tabbedPane.add(editTagsPanel);
        tabbedPane.setTitleAt(0, Message.get("edit_tag"));
        tabbedPane.add(scrollLyrics);
        tabbedPane.setTitleAt(1, Message.get("lyrics"));

        //create the button save
        JPanel tabContainer = new JPanel();
        BoxLayout boxContainerLayout = new BoxLayout(tabContainer, BoxLayout.Y_AXIS);
        tabContainer.setLayout(boxContainerLayout);
        tabContainer.add(tabbedPane);
        JPanel panelBtnSalvar = new JPanel(new GridLayout(1, 1));
        panelBtnSalvar.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        panelBtnSalvar.add(btnSave);
        tabContainer.add(panelBtnSalvar);

        horizontalSplittedPane.setRightComponent(tabContainer);
        verticalSplittedPane.setLeftComponent(horizontalSplittedPane);
    }

    /**
     * configure the components so they will have the expected behavior. This
     * method does not change the position of any of the in the screen.
     */
    private void configureComponents() {
        txtLyrics.setEditable(false);
        cboColumn.setModel(new DefaultComboBoxModel(Tag.values()));

        //creating root directories list
        int size = File.listRoots().length + 1;
        File[] rootDirectories = new File[size];
        rootDirectories[0] = new File(System.getProperty("user.home"));
        int cont = 1;
        for (File f : File.listRoots()) {
            rootDirectories[cont] = f;
            cont++;
        }
        treeModel = new FileSystemTreeModel(rootDirectories);
        directoryTree.setModel(treeModel);
        directoryTree.setCellRenderer(new FileSystemTreeRenderer());
        directoryTree.setRootVisible(false);


        //configuring the JTable
        table.setModel(tagModel);
        table.setColumnSelectionAllowed(true);
        table.getColumnModel().getColumn(Tag.LYRICS.getIndex()).setCellRenderer(new JTextAreaCellRenderer());
    }

    private void addListeners() {

        directoryTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                tagModel.clear();
                for (TreePath t : directoryTree.getSelectionPaths()) {
                    File f = new File(t.getLastPathComponent().toString());
                    if (f.isFile()) {
                        //add the tags in the table:
                        MP3 musica = new MP3(f.getPath());
                        tagModel.add(musica);
                        txtLyrics.setText(tagModel.getAllLyrics());
                    }
                }
            }
        });

        cboColumn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cboColumn.getSelectedIndex() == 7) {
                    JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
                    fileChooser.setDialogTitle(Message.get("choose_image"));
                    fileChooser.setFileFilter(new FileFilter());
                    int opcao = fileChooser.showSaveDialog(null);
                    if (opcao == JFileChooser.APPROVE_OPTION) {
                        txtValue.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    }
                }
            }
        });

        txtValue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (cboColumn.getSelectedIndex() == 7) {
                        tagModel.setAllCovers(new File(txtValue.getText()));
                    } else {
                        tagModel.setValueAtColumn(txtValue.getText(), cboColumn.getSelectedIndex());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(rootPane, ex.toString());
                }
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tagModel.saveAll();
                    JOptionPane.showMessageDialog(rootPane, Message.get("change_successful"),Message.get("success"),JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(rootPane, ex.getMessage(),Message.get("error"),JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        btnChangeColumn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (cboColumn.getSelectedIndex() == 7) {
                        tagModel.setAllCovers(new File(txtValue.getText()));
                    } else {
                        tagModel.setValueAtColumn(txtValue.getText(), cboColumn.getSelectedIndex());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(rootPane, ex.toString());
                }
            }
        });

        btnRenameSequence.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tagModel.setNumberTracks(2);
            }
        });

        btnVisualizeArtwork.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImagePanel images = new ImagePanel(Message.get("cover_songs"), tagModel.getAllCovers());
                JFrame frame = new JFrame(Message.get("cover_songs"));
                frame.setLayout(new GridLayout(1, 1));
                frame.add(images);
                frame.setSize(new Dimension(400, 400));
                frame.setIconImage(new ImageIcon(getClass().getResource("/images/app-icon.png")).getImage());
                frame.setVisible(true);
            }
        });

        btnChangeArtwork.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (tagModel.getRowCount() > 1) {
                        JOptionPane.showMessageDialog(rootPane, Message.get("choose_ony_one"));
                    } else {
                        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
                        fileChooser.setDialogTitle(Message.get("choose_image"));
                        fileChooser.setFileFilter(new FileFilter());
                        int opcao = fileChooser.showSaveDialog(null);
                        if (opcao == 0) {
                            tagModel.setCover(new File(fileChooser.getSelectedFile().getAbsolutePath()), 0);
                        }
                        JOptionPane.showMessageDialog(null, Message.get("changes_made"));
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, e.toString());
                }
            }
        });
        
        btnRenameFileWithPattern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TagPattern pattern = new TagPattern(txtTagPattern.getText());
                pattern.renameFile(tagModel.getSongs().get(0));
                directoryTree.repaint();
            }
        });
        
    }
}