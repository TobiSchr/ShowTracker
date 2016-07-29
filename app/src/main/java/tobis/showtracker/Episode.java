package tobis.showtracker;

import org.joda.time.LocalDate;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TobiX on 29.07.2016.
 */
public class Episode{
    private String showShortCut;
    private int seasonNumber;
    private int episodeNumber;
    private LocalDate date;

    //private final Format dateFormat = new SimpleDateFormat("dd.MM.yy");
    private final String dateFormat = "dd.MM.yy";
    /**
     *
     * @param showShortCut shortcut of the show name
     * @param seasonNumber season number
     * @param episodeNumber episode number
     * @param date airtime of the episode format "01.01.2017"
     */
    Episode(String showShortCut, int seasonNumber, int episodeNumber, LocalDate date) {
        this.showShortCut = showShortCut;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.date = date;
    }
    public String getShowShortCut() {
        return showShortCut;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDate(int day, int month, int year){
        LocalDate date = new LocalDate(year, month, day);
        this.date = date;
    }

    /**
     * @return returns Data in format "01.01.2016"
     */
    public String getDateAsString(){
        return date.toString(dateFormat);
        //TODO needs to be tested
    }

    /**
     * @return returns Season and Episode number in format "S01E001"
     */
    public String getSeasonEpisodeAsString(){
        //TODO
        return "yolo";
    }
}