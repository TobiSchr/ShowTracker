package tobis.showtracker;

import org.joda.time.LocalDate;

/**
 * Created by TobiX on 29.07.2016.
 *
 * Class with contains info of one Episode
 */
public class Episode{
    private String showShortCut;
    private int seasonNumber;
    private int episodeNumber;
    private LocalDate date;

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
        this.date = new LocalDate(year, month, day);
    }

    /**
     * @return returns Data in format "01.01.2016"
     */
    public String getDateAsString(){
        return date.toString("dd.MM.yy");
        //TODO needs to be tested
    }

    /**
     * @return returns Season and Episode number in format "S01E001"
     */
    public String getSeasonEpisodeAsString(){
        StringBuilder sb = new StringBuilder();
        sb.append("S");
        sb.append(String.format("%02d", seasonNumber));
        sb.append("E");
        sb.append(String.format("%03d", episodeNumber));
        return sb.toString();
    }
}