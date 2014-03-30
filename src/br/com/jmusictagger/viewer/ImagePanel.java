/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmusictagger.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 *
 * A JPanel to show an image gallery.
 * 
 * @author yurifw
 */
public class ImagePanel extends JPanel {

    private int index;
    private ImageIcon[] images;
    private JLabel title;
    private JLabel currentImage;
    private JLabel paging;
    private JLabel imageName;
    private JButton btnFirst;
    private JButton btnPrevious;
    private JButton btnNext;
    private JButton btnLast;

    /**
     * initializes the variables and create the UI.
     */
    private ImagePanel(ImageIcon[] images) {
        index = 0;
        this.images = images;
        this.title = new JLabel();
        paging = new JLabel("", SwingConstants.RIGHT);
        imageName = new JLabel("",SwingConstants.CENTER);
        btnFirst = new JButton("|<");
        btnPrevious = new JButton("<");
        btnNext = new JButton(">");
        btnLast = new JButton(">|");
        currentImage = new JLabel(images[index],SwingConstants.CENTER);
        updatePaging();

        BoxLayout containerLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(containerLayout);

        JPanel header = new JPanel(new GridLayout(1, 2));
        header.add(this.title);
        header.add(this.paging);
        header.setMaximumSize(new Dimension(Short.MAX_VALUE, 27));
        this.add(header);

        JPanel centerPanel = new JPanel(new GridLayout(1, 1));
        centerPanel.add(new JScrollPane(currentImage));
        this.add(centerPanel);


        JPanel bottom = new JPanel(new BorderLayout(2, 2));

        JPanel previousPanel = new JPanel();
        BoxLayout previousLayout = new BoxLayout(previousPanel, BoxLayout.LINE_AXIS);
        previousPanel.setLayout(previousLayout);
        previousPanel.add(btnFirst);
        previousPanel.add(btnPrevious);
        bottom.add(previousPanel, BorderLayout.LINE_START);

        bottom.add(imageName, BorderLayout.CENTER);


        JPanel nextPanel = new JPanel();
        BoxLayout nextLayout = new BoxLayout(nextPanel, BoxLayout.X_AXIS);
        nextPanel.setLayout(nextLayout);
        nextPanel.add(btnNext);
        nextPanel.add(btnLast);
        bottom.setMaximumSize(new Dimension(Short.MAX_VALUE, 27));
        bottom.add(nextPanel, BorderLayout.LINE_END);
        this.add(bottom);
        
        addListeners();
    }

    /**
     * Creates new ImagePanel
     *
     * @param title The title of the gallery
     * @param images an array of the images to be shown
     */
    public ImagePanel(String title, ImageIcon[] images) {
        this(images);
        updatePaging();
        this.title.setText(title);
    }

    /**
     * Creates a new Form showing the given image
     *
     * @param imagem the image to be shown
     */
    public ImagePanel(ImageIcon imagem) {
        this("Image Gallery", new ImageIcon[]{imagem});
    }

    /**
     * Creates a new Form showing the image of the given path (the path must be
     * relative to the class, not absolute)
     *
     * @param path the path of the image
     */
    public ImagePanel(String path) {
        this(new ImageIcon(path));
    }

    /**
     * Creates a new Form showing the image of the given File
     *
     * @param file the file containing the image
     */
    public ImagePanel(File file) {
        this(file.getAbsolutePath());
    }

    /**
     * updates the panel paging
     */
    private void updatePaging() {
        paging.setText((index + 1) + "/" + images.length);
        imageName.setText(images[index].getDescription());
    }
    
    /**
     * Increments the variable index, do not increment it manually,
     * use this method since it guarantees that it will not go out of the images bounds
     */
    private void incrementIndex(){
        if ((index+1)<images.length){
            index++;
        }
    }
    
     /**
     * Decrements the variable index, do not increment it manually,
     * use this method since it guarantees that it will not go out of the images bounds
     */
    private void decrementIndex(){
        if ((index-1)>=0){
            index--;
        }
    }
    
    /**
     * Shows the next image
     */
    public void nextImage(){
        incrementIndex();        
        goToImage(index);
    }
    
    /**
     * Shows the previous image
     */
    public void previousImage(){
        decrementIndex();
        goToImage(index);
    }
    
    /**
     * Shows the last Image
     */
    public void lastImage(){
        index=images.length-1;
        goToImage(index);
    }
    
    /**
     * Shows the first Image
     */
    public void firstImage(){
        index=0;
        goToImage(index);
    }
    
    /**
     * Shows the selected image
     * @param i the index of the image to be shown (starting with 0)
     */
    public void goToImage(int i) {        
        currentImage.setIcon(images[i]);
        updatePaging();
        imageName.setText(images[i].getDescription());
        index=i;
    }

    /**
     * sets the gallery title visible or invisible
     * @param b true for visible and false to hide it
     */
    public void showGalleryTitle(boolean b){
        title.setVisible(b);
    }
    /**
     * returns the visibility of the gallery title
     * @return true for showing and false to not showing it
     */
    public boolean isGalleryTitleShowing(){
        return title.isVisible();
    }
    /**
     * sets the paging visible or invisible
     * @param b true for visible and false to hide it
     */
    public void showPaging(boolean b){
        paging.setVisible(b);
    }
    /**
     * returns the visibility of the paging.
     * @return true for showing and false to not showing it
     */
    public boolean isPagingShowing(){
        return paging.isVisible();
    }
    
    /**
     * sets the image name visible or invisible
     * @param b true for visible and false to hide it
     */
    public void showImageName(boolean b){
        imageName.setVisible(b);
    }
    
    /**
     * returns the visibility of the image name.
     * @return true for showing and false to not showing it
     */
    public boolean isImageName(){
        return imageName.isVisible();
    }
    
    
    private void addListeners(){
        btnFirst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstImage();
            }
        });
        
        btnPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousImage();
            }
        });
        
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextImage();
            }
        });
        
        btnLast.addActionListener(new ActionListener() { @Override
            public void actionPerformed(ActionEvent e) {
                lastImage();
            }
        });
    }
}