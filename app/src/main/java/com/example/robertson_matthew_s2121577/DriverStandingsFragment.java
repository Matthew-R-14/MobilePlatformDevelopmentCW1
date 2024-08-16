//Matthew Robertson S2121577
package com.example.robertson_matthew_s2121577;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DriverStandingsFragment extends Fragment {

    private ListView listViewDriverStandings;
    private DriverViewModel driverViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_standings, container, false);

        listViewDriverStandings = view.findViewById(R.id.listViewDriverStandings);
        ImageButton buttonBack = view.findViewById(R.id.buttonBack);

        driverViewModel = new ViewModelProvider(this).get(DriverViewModel.class);

        driverViewModel.getDriverDetails().observe(getViewLifecycleOwner(), driverList -> {
            DriverListAdapter adapter = new DriverListAdapter(getContext(), driverList);
            listViewDriverStandings.setAdapter(adapter);
        });

        buttonBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        displayLastUpdate(view);

        return view;
    }

    private void displayLastUpdate(View view) {
        TextView textViewLastUpdate = view.findViewById(R.id.textViewLastUpdate);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        textViewLastUpdate.setText("Last update: " + currentDateAndTime);
    }

    private class DriverListAdapter extends BaseAdapter {

        private Context context;
        private List<DriverDetails> driverList;
        private LayoutInflater inflater;
        private boolean[] isExpanded;

        public DriverListAdapter(Context context, List<DriverDetails> driverList) {
            this.context = context;
            this.driverList = driverList;
            this.inflater = LayoutInflater.from(context);
            this.isExpanded = new boolean[driverList.size()];
        }

        @Override
        public int getCount() {
            return driverList.size();
        }

        @Override
        public Object getItem(int position) {
            return driverList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_driver, parent, false);
            }

            DriverDetails driver = (DriverDetails) getItem(position);

            TextView textViewDriverPosition = convertView.findViewById(R.id.textViewDriverPosition);
            TextView textViewDriverName = convertView.findViewById(R.id.textViewDriverName);
            TextView textViewDriverPoints = convertView.findViewById(R.id.textViewDriverPoints);
            TextView textViewDriverWins = convertView.findViewById(R.id.textViewDriverWins);
            TextView textViewDriverDOB = convertView.findViewById(R.id.textViewDriverDOB);
            TextView textViewDriverNationality = convertView.findViewById(R.id.textViewDriverNationality);
            TextView textViewDriverConstructor = convertView.findViewById(R.id.textViewDriverConstructor);

            textViewDriverPosition.setText("Position: " + driver.getPosition());

            textViewDriverName.setText(driver.getGivenName() + " " + driver.getFamilyName());
            textViewDriverPoints.setText("Points: " + driver.getPoints());
            textViewDriverWins.setText("Wins: " + driver.getWins());

            textViewDriverDOB.setText("Date of Birth: " + driver.getDateOfBirth());
            textViewDriverNationality.setText("Nationality: " + driver.getNationality());
            textViewDriverConstructor.setText("Constructor: " + driver.getConstructorName());

            if (isExpanded[position]) {
                textViewDriverDOB.setVisibility(View.VISIBLE);
                textViewDriverNationality.setVisibility(View.VISIBLE);
                textViewDriverConstructor.setVisibility(View.VISIBLE);
            } else {
                textViewDriverDOB.setVisibility(View.GONE);
                textViewDriverNationality.setVisibility(View.GONE);
                textViewDriverConstructor.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(v -> {
                isExpanded[position] = !isExpanded[position];
                notifyDataSetChanged();
            });

            return convertView;
        }
    }
}
