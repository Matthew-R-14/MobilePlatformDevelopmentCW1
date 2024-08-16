//Matthew Robertson S2121577
package com.example.robertson_matthew_s2121577;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class RaceViewModel extends AndroidViewModel {
    private final RaceRepository raceRepository;
    private final MutableLiveData<List<RaceDetails>> raceDetails;

    public RaceViewModel(Application application) {
        super(application);
        raceRepository = new RaceRepository(application);
        raceDetails = raceRepository.getRaceDetails();
    }

    public LiveData<List<RaceDetails>> getRaceDetails() {
        return raceDetails;
    }
}
