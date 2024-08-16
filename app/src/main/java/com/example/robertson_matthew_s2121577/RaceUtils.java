//Matthew Robertson S2121577
package com.example.robertson_matthew_s2121577;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RaceUtils {
    public static Date parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String formatDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    public static String formatTime(String timeString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        try {
            Date date = inputFormat.parse(timeString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return timeString;
        }
    }
    public static List<RaceDetails> filterRaces(List<RaceDetails> races, boolean isPast) {
        List<RaceDetails> filteredRaces = new ArrayList<>();
        Date currentDate = new Date();

        for (RaceDetails race : races) {
            Date raceDate = parseDate(race.getDate());
            if (raceDate != null) {
                if (isPast && raceDate.before(currentDate)) {
                    filteredRaces.add(race);
                } else if (!isPast && !raceDate.before(currentDate)) {
                    filteredRaces.add(race);
                }
            }
        }

        return filteredRaces;
    }

    public static RaceDetails getClosestRace(List<RaceDetails> upcomingRaces) {
        Date currentDate = new Date();
        RaceDetails closestRace = null;
        long minDifference = Long.MAX_VALUE;

        for (RaceDetails race : upcomingRaces) {
            Date raceDate = parseDate(race.getDate());
            if (raceDate != null) {
                long difference = raceDate.getTime() - currentDate.getTime();
                if (difference >= 0 && difference < minDifference) {
                    minDifference = difference;
                    closestRace = race;
                }
            }
        }

        return closestRace;
    }
}
