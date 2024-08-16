//Matthew Robertson S2121577
package com.example.robertson_matthew_s2121577;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RaceScheduleFragment extends Fragment {

    private LinearLayout layoutPreviousRaces;
    private LinearLayout layoutUpcomingRaces;
    private RaceViewModel raceViewModel;
    private RaceDetails nextRace;
    private boolean isPreviousRacesVisible = false;
    private boolean isUpcomingRacesVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_race_schedule, container, false);

        Button buttonPreviousRaces = view.findViewById(R.id.buttonPreviousRaces);
        Button buttonUpcomingRaces = view.findViewById(R.id.buttonUpcomingRaces);
        layoutPreviousRaces = view.findViewById(R.id.layoutPreviousRaces);
        layoutUpcomingRaces = view.findViewById(R.id.layoutUpcomingRaces);
        ImageButton buttonBack = view.findViewById(R.id.buttonBack);

        raceViewModel = new ViewModelProvider(this).get(RaceViewModel.class);

        buttonPreviousRaces.setOnClickListener(v -> {
            if (!isPreviousRacesVisible) {
                loadPreviousRaces();
                isPreviousRacesVisible = true;
            } else {
                layoutPreviousRaces.removeAllViews();
                isPreviousRacesVisible = false;
            }
        });

        buttonUpcomingRaces.setOnClickListener(v -> {
            if (!isUpcomingRacesVisible) {
                loadUpcomingRaces();
                isUpcomingRacesVisible = true;
            } else {
                layoutUpcomingRaces.removeAllViews();
                isUpcomingRacesVisible = false;
            }
        });

        buttonBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        displayLastUpdate(view);

        return view;
    }

    private void loadPreviousRaces() {
        raceViewModel.getRaceDetails().observe(getViewLifecycleOwner(), raceList -> {
            List<RaceDetails> previousRaces = RaceUtils.filterRaces(raceList, true);
            layoutPreviousRaces.removeAllViews();
            for (RaceDetails race : previousRaces) {
                View raceItemView = getRaceItemView(getContext(), race, layoutPreviousRaces);
                layoutPreviousRaces.addView(raceItemView);
            }
        });
    }

    private void loadUpcomingRaces() {
        raceViewModel.getRaceDetails().observe(getViewLifecycleOwner(), raceList -> {
            List<RaceDetails> upcomingRaces = RaceUtils.filterRaces(raceList, false);
            nextRace = RaceUtils.getClosestRace(upcomingRaces);
            layoutUpcomingRaces.removeAllViews();
            for (RaceDetails race : upcomingRaces) {
                View raceItemView = getRaceItemView(getContext(), race, layoutUpcomingRaces);

                if (race.equals(nextRace)) {
                    raceItemView.setBackgroundColor(getResources().getColor(R.color.highlight));
                }

                layoutUpcomingRaces.addView(raceItemView);
            }
        });
    }

    private View getRaceItemView(Context context, RaceDetails raceDetails, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.list_item_race, parent, false);

        TextView textViewRaceName = convertView.findViewById(R.id.textViewRaceName);
        TextView textViewRaceDate = convertView.findViewById(R.id.textViewRaceDate);
        TextView textViewRaceTime = convertView.findViewById(R.id.textViewRaceTime);
        TextView textViewCircuitName = convertView.findViewById(R.id.textViewCircuitName);
        TextView textViewRound = convertView.findViewById(R.id.textViewRound);

        textViewRaceName.setText(raceDetails.getRaceName());
        textViewRaceDate.setText(RaceUtils.formatDate(raceDetails.getDate()));
        textViewRaceTime.setText(RaceUtils.formatTime(raceDetails.getTime()));
        textViewCircuitName.setText(raceDetails.getCircuitName());
        textViewRound.setText("Round: " + raceDetails.getRound());

        return convertView;
    }

    private void displayLastUpdate(View view) {
        TextView textViewLastUpdate = view.findViewById(R.id.textViewLastUpdate);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        textViewLastUpdate.setText("Last update: " + currentDateAndTime);
    }
}
