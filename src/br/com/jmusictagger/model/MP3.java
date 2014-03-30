/*
 * MP3_Beaney - The MP3 Tag Editor is a free ID3-Tag Editor
 * Copyright (C) 2012 Bernhard Teismann, André Seipelt, Steffen Gruschka, Imanuel Rohlfing
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package br.com.jmusictagger.model;

import br.com.jmusictagger.internationalization.Message;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

/**
 * Manages the ID3 Tags of MP3 Files.
 * <p/>
 * @author Steffen Gruschka
 * @author Bernhard Teismann
 * @author André Seipelt
 * @author Imanuel Rohlfing
 * @version 1.0
 */
public class MP3 {

    private String artist;
    private String title;
    private String album;
    private String releaseDate;
    private String genre;
    private String track;
    private String lyrics;
    private MP3File mp3;
    private File artwork;

    /**
     * Initializes de MP3 instance, setting all the tags of the instance. this
     * constructor will try to initialize each tag with the tag found in the MP3
     * file, if no tag is found, it will create a default tag which is an empty
     * String, except for the artwork. if no artwork is found, the default
     * artwork will be the application logo
     *
     * @param path the path to the physical MP3 file
     */
    public MP3(String path) {
        try {
            this.mp3 = (MP3File) AudioFileIO.read(new File(path));
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex) {
            ex.printStackTrace();
        }
        try {
            artist = mp3.getID3v2Tag().getFirst(FieldKey.ARTIST);
        } catch (Exception e) {
            artist = "";
        }
        try {
            genre = mp3.getID3v2Tag().getFirst(FieldKey.GENRE);
        } catch (Exception e) {
            genre = "";
        }
        try {
            setTrack(mp3.getID3v2Tag().getFirst(FieldKey.TRACK));
        } catch (Exception e) {
            setTrack("0");
        }
        try {
            releaseDate = mp3.getID3v2Tag().getFirst(FieldKey.YEAR);
        } catch (Exception e) {
            releaseDate = "";
        }
        try {
            album = mp3.getID3v2Tag().getFirst(FieldKey.ALBUM);
        } catch (Exception e) {
            album = "";
        }
        try {
            title = mp3.getID3v2Tag().getFirst(FieldKey.TITLE);
        } catch (Exception e) {
            title = "";
        }
        try {
            lyrics = mp3.getID3v2Tag().getFirst(FieldKey.LYRICS);
        } catch (Exception e) {
            lyrics = "";
        }
        try {
            artwork = File.createTempFile("cover", "jpg");
            artwork.deleteOnExit();
            ImageIO.write((BufferedImage) mp3.getID3v2Tag().getFirstArtwork().getImage(), "jpg", artwork);
        } catch (Exception e) {
            try {
                ImageIO.write(ImageIO.read(getClass().getResource("/br/com/jmusictagger/images/app-icon-background.png")), "png", artwork);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }



    }

    /**
     * Getter for lyrics.
     * <p/>
     * @return song's lyrics as string
     */
    public String getLyrics() {
        return lyrics;
    }

    /**
     * Setter for lyrics.
     * <p/>
     * @return song's lyrics string
     */
    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    /**
     * Getter for album.
     * <p/>
     * @return album's name as string
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Setter for album.
     * <p/>
     * @param album album's name as string
     */
    public void setAlbum(String album) {
        this.album = album;
    }

    /**
     * Getter for album.
     * <p/>
     * @return artist's name as string
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Setter for album.
     * <p/>
     * @param artist artist's name as string
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * Getter for genre.
     * <p/>
     * @return genre's name as string
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Setter for genre.
     * <p/>
     * @param genre genre's name as string
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Getter for path.
     * <p/>
     * @return path as string
     */
    public File getFile() {
        return mp3.getFile();
    }

    /**
     * Getter for filename.
     * <p/>
     * @return name of path
     */
    public String getFileName() {
        return mp3.getFile().getName();
    }

    /**
     * Getter for release.
     * <p/>
     * @return release's name as string
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Setter for release.
     * <p/>
     * @param release release's name as string
     */
    public void setReleaseDate(String release) {
        this.releaseDate = release;
    }

    /**
     * Getter for title.
     * <p/>
     * @return title as string
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title.
     * <p/>
     * @param title title's name as string
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for track.
     * <p/>
     * @return track's name as string
     */
    public String getTrack() {
        return track;
    }

    /**
     * Setter for track.
     * <p/>
     * @param track track's name as string
     */
    public void setTrack(String track) {
        if (track.length()<2) track = "0"+track;
        this.track = track;
    }

    /**
     * Creates an Artwork Object from the cover's path, which can be saved in
     * the ID3 Tag. Calls the method getImage() to get a BufferedImage.
     * <p/>
     * @return cover as BufferedImage if available, null if not
     */
    public BufferedImage getCoverAsImage() {
        if (artwork == null) {
            return null;
        } else {
            try {
                return (BufferedImage) ArtworkFactory.createArtworkFromFile(artwork).getImage();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * Getter for cover.
     * <p/>
     * @return artwork's name as string
     */
    public File getCover() {
        return artwork;
    }

    /**
     * Creates a new BufferedImage from the passed file and reduces its size to
     * COVER_SIZE. Said BufferedImage is then saved in the MP3's folder and is
     * passed to the MP3 Object as its cover.
     * <p/>
     * @param cover file in which the cover image is contained
     * @throws MP3Exception
     * @see MP3Exception
     */
    public void setCover(File cover) throws FileNotFoundException {

        BufferedImage image = null;
        try {
            image = (BufferedImage) ImageIO.read(cover);
            String dir = mp3.getFile().getAbsolutePath().substring(0, mp3.getFile().getAbsolutePath().lastIndexOf("."));
            File tempFile = File.createTempFile(this.getFileName(), "png");
            tempFile.deleteOnExit();
            ImageIO.write(image, "png", tempFile);
            this.artwork = tempFile;
        } catch (Exception e) {
            throw new FileNotFoundException(Message.get("cover_not_read"));
        }
    }

    /**
     * Creates a BufferedImage from the passed Artwork Object and reduces its
     * size to COVER_SIZE. Said BufferedImage is then saved in the MP3's folder
     * and is passed to the MP3 Object as its cover.
     * <p/>
     * @param cover Artwork object in which the cover image is conatined.
     * @throws MP3Exception
     * @see MP3Exception
     */
    public void setCover(Artwork cover) throws FileNotFoundException {
        BufferedImage image = null;
        try {
            image = (BufferedImage) cover.getImage();
            File tempFile = File.createTempFile(this.getFileName(), "png");
            tempFile.deleteOnExit();
            artwork = tempFile;

        } catch (Exception e) {
            throw new FileNotFoundException(Message.get("cover_not_read"));
        }
    }

    /**
     * Gets the given Tag
     *
     * @param t the Tag you want to get the value
     * @return the value of the given tag
     */
    public Object getTag(br.com.jmusictagger.model.Tag t) {
        switch (t) {
            case ALBUM:
                return getAlbum();
            case ARTIST:
                return getArtist();
            case ARTWORK:
                return getCoverAsImage();
            case GENRE:
                return getGenre();
            case LYRICS:
                return getLyrics();
            case TITLE:
                return getTitle();
            case TRACK_NUMBER:
                return getTrack();
            case YEAR:
                return getReleaseDate();
        }
        return "";
    }

    public void save() throws IOException, TagException, ReadOnlyFileException, CannotReadException, CannotReadException, InvalidAudioFrameException, CannotWriteException {
        MP3 mp3 = this;

        MP3File mp3File = (MP3File) AudioFileIO.read(mp3.getFile());
        mp3File.setTag(new ID3v23Tag());
        Tag v24 = mp3File.getTag();

        v24.setField(ArtworkFactory.createArtworkFromFile(mp3.getCover()));
        v24.setField(FieldKey.ARTIST, mp3.getArtist());
        v24.setField(FieldKey.TITLE, mp3.getTitle());
        v24.setField(FieldKey.ALBUM, mp3.getAlbum());
        v24.setField(FieldKey.GENRE, mp3.getGenre());
        v24.setField(FieldKey.YEAR, mp3.getReleaseDate());
        v24.setField(FieldKey.LYRICS, mp3.getLyrics());

        try {
            v24.setField(FieldKey.TRACK, mp3.getTrack());
        } catch (Throwable t) {
            v24.setField(FieldKey.TRACK, "-1");
        }

        mp3File.commit();
    }
}