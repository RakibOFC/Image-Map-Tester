package com.rakibofc.imagemaptester.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.rakibofc.imagemaptester.databinding.ActivityMainBinding;
import com.rakibofc.imagemaptester.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setContentView(binding.getRoot());

        // Get file name from view model and set text in textview
        viewModel.getExcelFileNameLiveData().observe(this, binding.tvExcelFileName::setText);
        viewModel.getPngNameLiveData().observe(this, imageData -> {

            binding.tvPngFileName.setText(imageData.getImageFileName());
            binding.ivPreview.setImageURI(imageData.getImageUri());
            binding.tvPageNoTitle.setText(imageData.getImageTitle());

            binding.imagePreview.setVisibility(View.VISIBLE);
        });

        binding.btnChooseExcel.setOnClickListener(v -> chooseExcelFile());
        binding.btnChooseImage.setOnClickListener(v -> choosePngFile());
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

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),

            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    if (data != null) {

                        // Get URI from data
                        Uri selectedFileUri = data.getData();

                        if (selectedFileUri != null) {

                            // Load file name
                            viewModel.loadExcelFileName(selectedFileUri);

                            // Run thread to avoid UI block and convert data
                            new Thread(() -> viewModel.convertExcelToSqlite(selectedFileUri)).start();
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