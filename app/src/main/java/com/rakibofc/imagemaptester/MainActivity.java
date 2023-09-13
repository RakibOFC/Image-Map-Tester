package com.rakibofc.imagemaptester;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.rakibofc.imagemaptester.databinding.ActivityMainBinding;
import com.rakibofc.imagemaptester.helper.GlyphsDatabaseHelper;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
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
                    new Thread(() -> operation(data)).start();
                }
            }
    );

    private void operation(Intent data) {

        if (data != null) {

            try {

                // Get URI from data
                Uri selectedFileUri = data.getData();

                // Get file from user file explorer
                assert selectedFileUri != null;
                InputStream excelFile = getContentResolver().openInputStream(selectedFileUri);

                // Now, you can use a library like Apache POI to read the Excel data
                assert excelFile != null;
                Workbook workbook = new XSSFWorkbook(excelFile);

                // Initialize your database helper
                GlyphsDatabaseHelper databaseHelper = new GlyphsDatabaseHelper(this);
                Sheet sheet = workbook.getSheetAt(0);

                // Clear all existing data from the database
                databaseHelper.clearDatabase();

                for (Row row : sheet) {

                    try {
                        int glyphId = (int) row.getCell(0).getNumericCellValue();
                        int pageNumber = (int) row.getCell(1).getNumericCellValue();
                        int lineNumber = (int) row.getCell(2).getNumericCellValue();
                        int suraNumber = (int) row.getCell(3).getNumericCellValue();
                        int ayahNumber = (int) row.getCell(4).getNumericCellValue();
                        int position = (int) row.getCell(5).getNumericCellValue();
                        float minX = (float) row.getCell(6).getNumericCellValue();
                        float maxX = (float) row.getCell(7).getNumericCellValue();
                        float minY = (float) row.getCell(8).getNumericCellValue();
                        float maxY = (float) row.getCell(9).getNumericCellValue();

                        // Insert the cell values into your SQLite database
                        databaseHelper.insertGlyphData(glyphId, pageNumber, lineNumber, suraNumber, ayahNumber, position, minX, maxX, minY, maxY);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                workbook.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}