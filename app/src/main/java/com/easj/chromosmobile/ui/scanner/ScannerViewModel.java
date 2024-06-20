package com.easj.chromosmobile.ui.scanner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScannerViewModel extends ViewModel {
    private final MutableLiveData<String> scannedCode = new MutableLiveData<>();

    public void setScannedCode(String code) {
        scannedCode.setValue(code);
    }

    public LiveData<String> getScannedCode() {
        return scannedCode;
    }
}
