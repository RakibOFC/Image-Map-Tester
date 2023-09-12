package com.rakibofc.imagemaptester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import com.rakibofc.imagemaptester.helper.GlyphsDatabaseHelper;

import org.apache.poi.ss.usermodel.Cell;
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
        setContentView(R.layout.activity_main);

       /* try {

            // Initialize your database helper
            GlyphsDatabaseHelper databaseHelper = new GlyphsDatabaseHelper(this);

            // Clear all existing data from the database
            databaseHelper.clearDatabase();

            AssetManager assetManager = getAssets();
            InputStream excelFile = assetManager.open("glyphs.xlsx");

            // Now, you can use a library like Apache POI to read the Excel data
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);


            for (Row row : sheet) {
                *//* *
                for (Cell cell : row) {

                    String cellValue = cell.toString();
                    Log.e("cellValue", cellValue);
                }
                databaseHelper.*//*
                int glyphId = 1; // (int) row.getCell(0).getNumericCellValue();
                int pageNumber = 1; // (int) row.getCell(1).getNumericCellValue();
                int lineNumber = 1; // (int) row.getCell(2).getNumericCellValue();
                int suraNumber = 1; // (int) row.getCell(3).getNumericCellValue();
                int ayahNumber = 1; // (int) row.getCell(4).getNumericCellValue();
                int position = 1; // (double) row.getCell(5).getNumericCellValue();
                double minX = 0.0; // (double) row.getCell(6).getNumericCellValue();
                double maxX = 0.0; // (double) row.getCell(7).getNumericCellValue();
                double minY = 0.0; // (double) row.getCell(8).getNumericCellValue();
                double maxY = 0.0; // (double) row.getCell(9).getNumericCellValue();

                // Insert the cell values into your SQLite database
                databaseHelper.insertGlyphData(glyphId, pageNumber, lineNumber, suraNumber, ayahNumber, position, minX, maxX, minY, maxY);
            }
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}