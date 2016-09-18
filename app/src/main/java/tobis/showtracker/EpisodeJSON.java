package tobis.showtracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import org.joda.time.LocalDate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by TobiX on 16.09.2016.
 * <p>
 * Read and Write to JSON
 */
public class EpisodeJSON {
    Context context;

    EpisodeJSON(Context context) {
        this.context = context;
    }

    public void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("shows.save", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void writeJsonStream(List<Episode> episodes) throws IOException {
        OutputStream out;
        out = context.openFileOutput("shows.save", Context.MODE_PRIVATE);
        //public void writeJsonStream(OutputStream out, List<Episode> episodes) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writeEpisodesArray(writer, episodes);
        Log.i("writer_before_close", writer.toString());
        writer.close();
        //Log.i("writer_after_close",writer.toString());
    }

    private void writeEpisodesArray(JsonWriter writer, List<Episode> episodes) throws IOException {
        writer.beginArray();
        for (Episode episode : episodes) {
            writeEpisode(writer, episode);
        }
        writer.endArray();
    }

    private void writeEpisode(JsonWriter writer, Episode episode) throws IOException {
        writer.beginObject();
        writer.name("showName").value(episode.getShowName());
        writer.name("seasonNumber").value(episode.getSeasonNumber());
        writer.name("episodeNumber").value(episode.getEpisodeNumber());
        writer.name("date").value(episode.getDate().toString("dd-MM-yyyy"));
        writer.name("watchedStatus").value(episode.isWatchedStatus());
        writer.endObject();
    }

    /*-READ-------------------------------------------------------------------------------------*/

    public List<Episode> readFromFile() {

        //String ret = "";
        List<Episode> retList = null;

        try {
            InputStream inputStream = context.openFileInput("shows.save");

            if (inputStream != null) {
                /*
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                */
                retList = readJsonStream(inputStream);
                //inputStream.close();
                //ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return retList;

        //return ret;
    }

    private List<Episode> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readEpisodesArray(reader);
        } finally {
            reader.close();
        }
    }

    private List<Episode> readEpisodesArray(JsonReader reader) throws IOException {
        List<Episode> episodes = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            episodes.add(readEpisode(reader));
        }
        reader.endArray();
        return episodes;
    }

    private Episode readEpisode(JsonReader reader) throws IOException {
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
