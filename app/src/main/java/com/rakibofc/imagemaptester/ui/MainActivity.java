package com.rakibofc.imagemaptester.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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

        binding.btnChooseExcel.setOnClickListener(v -> chooseExcelFile());
        binding.btnChooseImage.setOnClickListener(v -> {});
    }

    private void chooseExcelFile() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        filePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),

            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    // Run thread to avoid UI block
                    new Thread(() -> viewModel.convertExcelToSqlite(data)).start();
                }
            }
    );
}