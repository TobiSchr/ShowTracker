package tobis.showtracker;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalDate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TobiX on 16.09.2016.
 *
 * Read and Write to JSON
 */
class EpisodeJSON {
    private Context context;
    private static final Type LOCAL_DATE_TYPE = new TypeToken<LocalDate>() {
    }.getType();

    EpisodeJSON(Context context) {
        this.context = context;
    }

    private Gson createGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LOCAL_DATE_TYPE, new LocalDateConverter());
        return builder.create();
    }

    void writeToFile(List<Episode> episodes) throws IOException {
        //REMOVE LATER
        for (Episode e : episodes) {
            Log.i("writeE", e.toString());
        }

        String filename = "shows.save";

        String s = createGsonBuilder().toJson(episodes);
        //REMOVE LATER
        Log.i("writerJSON", s);
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*-READ-------------------------------------------------------------------------------------*/

    List<Episode> readFromFile() {
        List<Episode> retList = new ArrayList<>();

        try {
            InputStream inputStream = context.openFileInput("shows.save");

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                String json = sb.toString();
                Log.i("readerJSON", json);
                Type token = new TypeToken<List<Episode>>() {
                }.getType();

                retList = createGsonBuilder().fromJson(json, token);

                for (Episode e : retList) {
                    Log.i("readE", e.toString());
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return retList;
    }
}
