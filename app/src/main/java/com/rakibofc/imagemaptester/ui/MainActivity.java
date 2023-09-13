package com.rakibofc.imagemaptester.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.rakibofc.imagemaptester.R;
import com.rakibofc.imagemaptester.databinding.ActivityMainBinding;
import com.rakibofc.imagemaptester.helper.ExcelConversionTask;
import com.rakibofc.imagemaptester.helper.Loading;
import com.rakibofc.imagemaptester.helper.LoadingStatus;
import com.rakibofc.imagemaptester.model.ImageData;
import com.rakibofc.imagemaptester.viewmodel.MainViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements Loading {

    private MainViewModel viewModel;
    private ImageData imageData;
    private AlertDialog alertDialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setContentView(binding.getRoot());

        // Get file name from view model and set text in textview
        viewModel.getExcelFileNameLiveData().observe(this, binding.tvExcelFileName::setText);
        viewModel.getPngNameLiveData().observe(this, imageData -> {

            this.imageData = imageData;
            Uri imageUri = imageData.getImageUri();

            binding.tvPngFileName.setText(imageData.getImageFileName());
            binding.ivPreview.setImageURI(imageUri);
            binding.tvPageNoTitle.setText(String.format(getString(R.string.page_no_d), imageData.getPageNo()));

            binding.imagePreview.setVisibility(View.VISIBLE);
        });

        binding.btnChooseExcel.setOnClickListener(v -> chooseExcelFile());
        binding.btnChooseImage.setOnClickListener(v -> choosePngFile());

        binding.btnNext.setOnClickListener(v -> {

            if (imageData != null) {

                Intent intent = new Intent(getApplicationContext(), ImageTesterActivity.class);

                // Put the imageData as an extra in the Intent
                intent.putExtra("imagePageNo", imageData.getPageNo());
                intent.setData(imageData.getImageUri());

                startActivity(intent);
            } else
                Toast.makeText(this, R.string.invalid_image_file_text, Toast.LENGTH_SHORT).show();
        });
    }

    private void chooseExcelFile() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        filePickerLauncher.launch(intent);
    }

    private void choosePngFile() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/png");
        pngPickerLauncher.launch(intent);
    }

    @Override
    public void onLoad(LoadingStatus status) {

        if (status == LoadingStatus.START_LOADING) {
            showDialogLoading();

        } else if (status == LoadingStatus.END_LOADING) {
            dismissDialogLoading();
        }
    }

    public void showDialogLoading() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_loading, null);

        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);

        alertDialogLoading = alertDialogBuilder.create();
        alertDialogLoading.show();
    }

    public void dismissDialogLoading() {

        if (alertDialogLoading != null) {
            // After completing the registration process or any error handling, dismiss the loading dialog.
            alertDialogLoading.dismiss();
        }
    }

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),

            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    if (data != null) {

                        // Get URI from data
                        Uri selectedFileUri = data.getData();

                        if (selectedFileUri != null) {

                            // Show the loading dialog
                            showDialogLoading();

                            // Load file name
                            viewModel.loadExcelFileName(selectedFileUri);

                            // Create an Executor (e.g., ThreadPoolExecutor or Executors.newSingleThreadExecutor())
                            Executor executor = Executors.newSingleThreadExecutor();

                            // Create and submit the task
                            ExcelConversionTask conversionTask = new ExcelConversionTask(viewModel, selectedFileUri, this);
                            executor.execute(conversionTask);
                        }
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> pngPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),

            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    if (data != null) {

                        // Get URI from data
                        Uri selectedFileUri = data.getData();

                        if (selectedFileUri != null) {

                            // Load file name
                            viewModel.loadPngFileName(selectedFileUri);

                        }
                    }
                }
            }
    );
}