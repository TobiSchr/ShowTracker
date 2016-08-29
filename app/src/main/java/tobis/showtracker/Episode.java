package tobis.showtracker;

import org.joda.time.LocalDate;

/**
 * Created by TobiX on 29.07.2016.
 * Class with contains info of one Episode
 */
class Episode {
    private String showName;
    private int seasonNumber;
    private int episodeNumber;
    private LocalDate date;
    private boolean watchedStatus;

    /**
     * @param showName      name of the show
     * @param seasonNumber  season number
     * @param episodeNumber episode number
     * @param date          airtime of the episode format "01.01.2017"
     * @param watchedStatus status of the episode seen = true, unseen = false
     */
    Episode(String showName, int seasonNumber, int episodeNumber, LocalDate date, boolean watchedStatus) {
        this.showName = showName;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.date = date;
        this.watchedStatus = watchedStatus;
    }

    String getShowName() {
        return showName;
    }

    int getSeasonNumber() {
        return seasonNumber;
    }

    int getEpisodeNumber() {
        return episodeNumber;
    }

    LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDate(int day, int month, int year) {
        this.date = new LocalDate(year, month, day);
    }

    public boolean isWatchedStatus() {
        return watchedStatus;
    }

    public void setWatchedStatus(boolean watchedStatus) {
        this.watchedStatus = watchedStatus;
    }

    /**
     * @return returns Data in format "01.01.2016"
     */
    String getDateAsString() {
        String dayOfWeek;
        switch (date.getDayOfWeek()) {
            case 1:
                dayOfWeek = "(Mo)";
                break;
            case 2:
                dayOfWeek = "(Di)";
                break;
            case 3:
                dayOfWeek = "(Mi)";
                break;
            case 4:
                dayOfWeek = "(Do)";
                break;
            case 5:
                dayOfWeek = "(Fr)";
                break;
            case 6:
                dayOfWeek = "(Sa)";
                break;
            case 7:
                dayOfWeek = "(So)";
                break;
            default:
                dayOfWeek = "error";
                break;
        }
        return dayOfWeek + "\t" + date.toString("dd.MM.yy");
    }

    /**
     * @return returns Season and Episode number in format "S01E001"
     */
    String getSeasonEpisodeAsString() {
        //return "S" + String.format(Locale.GERMANY,"%02d", seasonNumber)
        //        + "-E" + String.format("%03d", episodeNumber);
        return "S" + seasonNumber + "-E" + episodeNumber;
    }
}