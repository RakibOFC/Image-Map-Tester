package com.rakibofc.imagemaptester.helper;

import android.net.Uri;

import com.rakibofc.imagemaptester.ui.MainActivity;
import com.rakibofc.imagemaptester.viewmodel.MainViewModel;

public class ExcelConversionTask implements Runnable {
    private final MainViewModel viewModel;
    private final Uri selectedFileUri;
    private final MainActivity activity;

    public ExcelConversionTask(MainViewModel viewModel, Uri selectedFileUri, MainActivity activity) {
        this.viewModel = viewModel;
        this.selectedFileUri = selectedFileUri;
        this.activity = activity;
    }

    @Override
    public void run() {
        // Perform the background task
        viewModel.convertExcelToSqlite(selectedFileUri);

        // Dismiss loading dialog and handle UI updates here
        activity.runOnUiThread(activity::dismissDialogLoading);
    }
}
