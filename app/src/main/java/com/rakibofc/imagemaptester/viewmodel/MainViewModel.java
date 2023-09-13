package com.rakibofc.imagemaptester.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.rakibofc.imagemaptester.helper.GlyphsDatabaseHelper;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public class MainViewModel extends AndroidViewModel {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void convertExcelToSqlite(Intent data) {

        Context context = getApplication().getApplicationContext();

        if (data != null) {

            try {

                // Get URI from data
                Uri selectedFileUri = data.getData();

                // Get file from user file explorer
                assert selectedFileUri != null;
                InputStream excelFile = context.getContentResolver().openInputStream(selectedFileUri);

                // Now, you can use a library like Apache POI to read the Excel data
                assert excelFile != null;
                Workbook workbook = new XSSFWorkbook(excelFile);

                // Initialize your database helper
                GlyphsDatabaseHelper databaseHelper = new GlyphsDatabaseHelper(context);
                Sheet sheet = workbook.getSheetAt(0);

                // Clear all existing data from the database
                databaseHelper.clearDatabase();

                // Assuming that the sheet has rows and the first row contains column names, so start with 1
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

                    Row row = sheet.getRow(rowIndex);

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
                }
                workbook.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
