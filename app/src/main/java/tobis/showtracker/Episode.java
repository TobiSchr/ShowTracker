package tobis.showtracker;

import org.joda.time.LocalDate;

import java.util.Locale;

/**
 * Created by TobiX on 29.07.2016.
 * <p/>
 * Class with contains info of one Episode
 */
public class Episode {
    private String showName;
    private int seasonNumber;
    private int episodeNumber;
    private LocalDate date;

    /**
     * @param showName name of the show
     * @param seasonNumber  season number
     * @param episodeNumber episode number
     * @param date          airtime of the episode format "01.01.2017"
     */
    Episode(String showName, int seasonNumber, int episodeNumber, LocalDate date) {
        this.showName = showName;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.date = date;
    }

    public String getShowName() {
        return showName;
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

    public void setDate(int day, int month, int year) {
        this.date = new LocalDate(year, month, day);
    }

    /**
     * @return returns Data in format "01.01.2016"
     */
    public String getDateAsString() {
        return date.toString("dd.MM.yy");
        //TODO needs to be tested
    }

    /**
     * @return returns Season and Episode number in format "S01E001"
     */
    public String getSeasonEpisodeAsString() {
        //return "S" + String.format(Locale.GERMANY,"%02d", seasonNumber)
        //        + "-E" + String.format("%03d", episodeNumber);
        return "S" + seasonNumber + "-E" + episodeNumber;
    }
}