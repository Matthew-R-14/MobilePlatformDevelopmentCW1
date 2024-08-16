//Matthew Robertson S2121577
package com.example.robertson_matthew_s2121577;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class DriverViewModel extends AndroidViewModel {
    private final DriverRepository driverRepository;
    private final MutableLiveData<List<DriverDetails>> driverDetails;

    public DriverViewModel(Application application) {
        super(application);
        driverRepository = new DriverRepository(application);
        driverDetails = driverRepository.getDriverDetails();
    }

    public LiveData<List<DriverDetails>> getDriverDetails() {
        return driverDetails;
    }
}
