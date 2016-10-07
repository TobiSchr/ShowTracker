package tobis.showtracker;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TobiX on 07.10.2016.
 * bla
 */

class WatchListFunctions {
    /**
     * Converts an array of needed parameters as String to an Episode and adds it to watchList
     * ["showName","seasonNum","episodeNumbers","dd.MM.yyyy","interval"]
     * i.e.  ["Game Of Thrones","5","10","02.04.2016","7"]
     */
    List<Episode> getEpisodesfromSeasonString(String[] seasonArray) {
        if (seasonArray.length != 5) {
            //Log.e("seasonArray length", String.valueOf(seasonArray.length));
            return null;
        }
        List<Episode> listOfSeason = new ArrayList<>();

        String showName = seasonArray[0];
        int seasonNum = Integer.parseInt(seasonArray[1]);
        int episodeNumbers = Integer.parseInt(seasonArray[2]);
        //dd.MM.yy
        String[] parts = seasonArray[3].split("\\.");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]) + 2000; //+2000 because its saved as "dd.MM.yy"
        LocalDate startDate = new LocalDate(year, month, day);
        int interval = Integer.parseInt(seasonArray[4]);

        Episode e;
        for (int i = 1; i <= episodeNumbers; i++) {
            e = new Episode(showName, seasonNum, i, startDate, false);
            //Log.i("EPISODE_ADDED", e.toString());
            listOfSeason.add(e);
            startDate = startDate.plusDays(interval);
        }
        return listOfSeason;
    }

    /**
     * should be followed up by mAdapter.notifyDataSetChanged();
     */
    List<Episode> getReleasedEpisodeList(List<Episode> watchList) {
        LocalDate today = new LocalDate();
        List<Episode> releasedEpisodeList = new ArrayList<>();
        for (Episode watchListEpisode : watchList) {
            LocalDate epDate = watchListEpisode.getDate();
            //true of epDate is before or equals today
            if (epDate.compareTo(today) <= 0) {
                releasedEpisodeList.add(watchListEpisode);
            }
        }
        return releasedEpisodeList;
    }

}
