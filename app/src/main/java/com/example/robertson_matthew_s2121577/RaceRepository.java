//Matthew Robertson S2121577
package com.example.robertson_matthew_s2121577;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

import androidx.lifecycle.MutableLiveData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RaceRepository {

    private final MutableLiveData<List<RaceDetails>> raceDetails;

    public RaceRepository(Application application) {
        raceDetails = new MutableLiveData<>();
        loadRaceDetails(application.getApplicationContext());
    }

    public MutableLiveData<List<RaceDetails>> getRaceDetails() {
        return raceDetails;
    }

    private void loadRaceDetails(Context context) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open("f1_race_schedule.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(inputStream, null);
            parser.nextTag();

            List<RaceDetails> races = readRaceSchedule(parser);
            raceDetails.postValue(races);

        } catch (IOException | XmlPullParserException e) {
            Log.e("RaceRepository", "Error loading race data", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<RaceDetails> readRaceSchedule(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<RaceDetails> raceSchedule = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, "http://ergast.com/mrd/1.5", "MRData");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            if (parser.getName().equals("RaceTable")) {
                raceSchedule = readRaceTable(parser);
            } else {
                skip(parser);
            }
        }
        return raceSchedule;
    }

    private List<RaceDetails> readRaceTable(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<RaceDetails> raceSchedule = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, "http://ergast.com/mrd/1.5", "RaceTable");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            if (parser.getName().equals("Race")) {
                raceSchedule.add(readRace(parser));
            } else {
                skip(parser);
            }
        }
        return raceSchedule;
    }

    private RaceDetails readRace(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, "http://ergast.com/mrd/1.5", "Race");

        String raceName = "";
        String date = "";
        String time = "";
        String circuitName = "";
        String round = parser.getAttributeValue(null, "round");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            String name = parser.getName();
            if (name.equals("RaceName")) {
                raceName = readText(parser);
            } else if (name.equals("Date")) {
                date = readText(parser);
            } else if (name.equals("Time")) {
                time = readText(parser);
            } else if (name.equals("Circuit")) {
                circuitName = readCircuitName(parser);
            } else {
                skip(parser);
            }
        }

        time = RaceUtils.formatTime(time);

        return new RaceDetails(round, raceName, date, time, circuitName);
    }

    private String readCircuitName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, "http://ergast.com/mrd/1.5", "Circuit");
        String circuitName = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            String name = parser.getName();
            if (name.equals("CircuitName")) {
                circuitName = readText(parser);
            } else {
                skip(parser);
            }
        }
        return circuitName;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
