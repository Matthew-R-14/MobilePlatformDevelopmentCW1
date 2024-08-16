//Matthew Robertson S2121577
package com.example.robertson_matthew_s2121577;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainFragment extends Fragment {

    private TextView textViewLastUpdate;
    private DriverViewModel driverViewModel;
    private RaceViewModel raceViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        textViewLastUpdate = view.findViewById(R.id.textViewLastUpdate);
        Button buttonDriverStandings = view.findViewById(R.id.buttonDriverStandings);
        Button buttonRaceSchedule = view.findViewById(R.id.buttonRaceSchedule);

        driverViewModel = new ViewModelProvider(this).get(DriverViewModel.class);
        raceViewModel = new ViewModelProvider(this).get(RaceViewModel.class);

        buttonDriverStandings.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new DriverStandingsFragment())
                    .addToBackStack(null)
                    .commit();
        });

        buttonRaceSchedule.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new RaceScheduleFragment())
                    .addToBackStack(null)
                    .commit();
        });

        displayLastUpdate();
        return view;
    }

    private void displayLastUpdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        textViewLastUpdate.setText("Last update: " + currentDateAndTime);
    }
}
