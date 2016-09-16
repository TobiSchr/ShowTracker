package tobis.showtracker;

import android.util.JsonReader;
import android.util.JsonWriter;

import org.joda.time.LocalDate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TobiX on 16.09.2016.
 * <p>
 * Read and Write to JSON
 */
public class EpisodeJSON {
    public void writeJsonStream(OutputStream out, List<Episode> episodes) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writeEpisodesArray(writer, episodes);
        writer.close();
    }

    public void writeEpisodesArray(JsonWriter writer, List<Episode> episodes) throws IOException {
        writer.beginArray();
        for (Episode episode : episodes) {
            writeEpisode(writer, episode);
        }
        writer.endArray();
    }

    public void writeEpisode(JsonWriter writer, Episode episode) throws IOException {
        writer.beginObject();
        writer.name("showName").value(episode.getShowName());
        writer.name("seasonNumber").value(episode.getSeasonNumber());
        writer.name("episodeNumber").value(episode.getEpisodeNumber());
        writer.name("date").value(episode.getDate().toString("dd-MM-yyyy"));
        writer.name("watchedStatus").value(episode.isWatchedStatus());
        writer.endObject();
    }

    /*------------------------------------------------------------------------------------------*/

    public List<Episode> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readEpisodesArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<Episode> readEpisodesArray(JsonReader reader) throws IOException {
        List<Episode> episodes = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            episodes.add(readEpisode(reader));
        }
        reader.endArray();
        return episodes;
    }

    public Episode readEpisode(JsonReader reader) throws IOException {
        String showName = "";
        int seasonNumber = -1;
        int episodeNumber = -1;
        LocalDate date = null;
        boolean watchedStatus = false;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "showName":
                    showName = reader.nextString();
                    break;
                case "seasonNumber":
                    seasonNumber = reader.nextInt();
                    break;
                case "episodeNumber":
                    episodeNumber = reader.nextInt();
                    break;
                case "date":
                    String[] parts = reader.nextString().split("-");
                    int day = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int year = Integer.parseInt(parts[2]);
                    date = new LocalDate(day, month, year);
                    break;
                case "watchedStatus":
                    watchedStatus = reader.nextBoolean();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Episode(showName, seasonNumber, episodeNumber, date, watchedStatus);
    }
}
