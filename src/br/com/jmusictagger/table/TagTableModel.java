/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmusictagger.table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import br.com.jmusictagger.model.MP3;
import br.com.jmusictagger.model.Tag;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

/**
 *
 * @author yurifw
 */
public class TagTableModel extends AbstractTableModel {
    
    /**
     * The MP3 List that will be shown in the table
     */
    private List<MP3> songs = new ArrayList<>();

    /**
     * Returns the list of mp3 songs in the table
     *
     * @return the List of MP3 in the table
     */
    public List<MP3> getSongs() {
        return songs;
    }

    @Override
    public int getRowCount() {
        return songs.size();
    }

    public int getColumnCount() {
        return 7;
    }

    public String getColumnName(int col) {
        return Tag.values()[col].toString();
    }

    public Class getColumnClass(int col) {
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        MP3 m = songs.get(row);
        switch (Tag.values()[col]) {
            case ALBUM:
                return m.getAlbum();
            case ARTIST:
                return m.getArtist();
            case LYRICS:
                return m.getLyrics();
            case TRACK_NUMBER:
                return m.getTrack();
            case TITLE:
                return m.getTitle();
            case GENRE:
                return m.getGenre();
            case YEAR:
                return m.getReleaseDate();
            default:
                return null;
        }
    }

    public void setValueAt(Object value, int row, int col) {
        switch (Tag.values()[col]) {
            case ALBUM:
                songs.get(row).setAlbum(value.toString());
                break;
            case ARTIST:
                songs.get(row).setArtist(value.toString());
                break;
            case LYRICS:
                songs.get(row).setLyrics(value.toString());
                break;
            case TRACK_NUMBER:
                songs.get(row).setTrack(value.toString());
                break;
            case TITLE:
                songs.get(row).setTitle(value.toString());
                break;
            case GENRE:
                songs.get(row).setGenre(value.toString());
                break;
            case YEAR:
                songs.get(row).setReleaseDate(value.toString());
                break;
        }
        fireTableDataChanged();
    }

    /**
     * Changes all the values on the given column
     *
     * @param value The new value of the column
     * @param col The index of the column that will be changed
     */
    public void setValueAtColumn(Object value, int col) {
        for (int i = 0; i < songs.size(); i++) {
            setValueAt(value, i, col);
        }
    }

    /**
     * Sets all track numbers of the table according to the chosen pattern 1-
     * the tracks will be numbered according to the file name<br> 2- the tracks
     * will be numbered according to the sequence they appear in the table
     *
     * @param pattern the number representing the chosen pattern, must be 1 or 2
     * @throws IllegalArgumentException thrown when the pattern is neither 1 nor
     * 2
     */
    public void setNumberTracks(int pattern) throws IllegalArgumentException {
        if (pattern == 1) {
            for (int i = 0; i < songs.size(); i++) {
                //gets the title of the mp3
                String title = songs.get(i).getFileName();
                title = title.substring(title.lastIndexOf(System.getProperty("file.separator")) + 1, title.length() - 4);

                //gets the first two numbers of the file name, they will be the track number
                int num = Integer.parseInt(title.replaceAll("\\D", "").substring(0, 2));

                songs.get(i).setTrack(num + "");

            }
        } else if (pattern == 2) {
            for (int i = 0; i < songs.size(); i++) {
                songs.get(i).setTrack(i + 1 + "");
            }
        } else {
            throw new IllegalArgumentException("The given pattern was neither 1 nor 2");
        }
        fireTableDataChanged();
    }

    /**
     * Sets the front cover picture for a specific MP3
     *
     * @param file the picture to be set
     * @param row the table row in which the MP3 is represented
     * @throws IOException
     */
    public void setCover(File file, int row) throws IOException {
        songs.get(row).setCover(file);
    }

    /**
     * Sets the front cover picture for all MP3
     *
     * @param file the picture to be set
     * @throws IOException
     */
    public void setAllCovers(File file) throws IOException {
        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).setCover(file);
        }
    }

    /**
     * Returns an array containing all the album covers
     *
     * @return the ImageIcon[] of the covers, the description of the icons is
     * the track's title.
     */
    public ImageIcon[] getAllCovers() {
        ImageIcon[] capas = new ImageIcon[getRowCount()];
        int cont = 0;
        for (MP3 m : songs) {
            capas[cont] = new ImageIcon(m.getCoverAsImage());
            capas[cont].setDescription(m.getTitle());
            cont++;
        }
        return capas;
    }

    /**
     * Returns all the lyrics concatenated with their track titles
     *
     * @return the concatenated String
     */
    public String getAllLyrics() {
        StringBuilder lyrics = new StringBuilder();
        for (MP3 m : songs) {
            lyrics.append(m.getTitle()).append("\n");
            lyrics.append(m.getLyrics()).append("\n\n\n");
        }
        return lyrics.toString();
    }

    public boolean isCellEditable(int row, int col) {
        if (col == Tag.LYRICS.getIndex()) {
            return false;
        }
        return true;
    }

    /**
     * Saves all the changes in all the tags
     *
     * @throws IOException if the tag Title is not greater than or equal 1
     */
    public void saveAll() throws IOException, TagException, ReadOnlyFileException, ReadOnlyFileException, CannotReadException, CannotReadException, InvalidAudioFrameException, InvalidAudioFrameException, CannotWriteException {
        for (MP3 m : songs) {
            m.save();
        }
    }

    /**
     *
     * @param song
     */
    public void add(MP3 song) {
        songs.add(song);
        fireTableDataChanged();
    }

    /**
     * Adds a List of MP3 to the table
     *
     * @param songs the MP3 list
     */
    public void add(List<MP3> songs) {
        this.songs.addAll(songs);
        fireTableDataChanged();
    }

    /**
     * Removes the given MP3 from the table
     *
     * @param musica the MP3 to be removed
     */
    public void remove(MP3 musica) {
        songs.remove(musica);
        fireTableDataChanged();
    }

    /**
     * Removes a list of MP3 from the table
     *
     * @param musicas the list of the MP3 to be removed
     */
    public void remove(List<MP3> musicas) {
        this.songs.removeAll(musicas);
        fireTableDataChanged();
    }

    /**
     * Removes all the MP3 from the table
     */
    public void clear() {
        songs.clear();
        fireTableDataChanged();
    }

    /**
     * Sorts the table by the track number
     */
    public void orderByTrack() {
        Collections.sort(songs, new Comparator<MP3>() {
            @Override
            public int compare(MP3 o1, MP3 o2) {
                return new Integer(o1.getTrack()) - new Integer(o2.getTrack());
            }
        });
    }

    /**
     * Checks if all the lyrics are the same
     *
     * @return true - if all the lyrics are the same <br> false - if at least
     * one lyrics is different
     *
     */
    public boolean isLyricsEquivalent() {

        String amostra = songs.get(0).getLyrics();
        for (MP3 m : songs) {
            if (!m.getLyrics().equals(amostra)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void fireTableDataChanged() {
        orderByTrack();
        super.fireTableDataChanged();
    }
}
