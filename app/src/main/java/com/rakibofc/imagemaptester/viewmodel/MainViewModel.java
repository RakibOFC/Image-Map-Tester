package com.rakibofc.imagemaptester.viewmodel;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.rakibofc.imagemaptester.helper.GlyphsDatabaseHelper;
import com.rakibofc.imagemaptester.model.ImageData;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainViewModel extends AndroidViewModel {

    private final MutableLiveData<String> excelFileNameLiveData;
    private final MutableLiveData<ImageData> pngNameLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        excelFileNameLiveData = new MutableLiveData<>();
        pngNameLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<String> getExcelFileNameLiveData() {
        return excelFileNameLiveData;
    }

    public MutableLiveData<ImageData> getPngNameLiveData() {
        return pngNameLiveData;
    }

    public void loadExcelFileName(Uri fileUri) {
        excelFileNameLiveData.setValue(getFileNameFromUri(fileUri));
    }

    public void loadPngFileName(Uri fileUri) {
        pngNameLiveData.setValue(loadImageData(fileUri));
    }

    private ImageData loadImageData(Uri fileUri) {

        String fileName = getFileNameFromUri(fileUri);
        return new ImageData(fileName, fileUri, extractPageNumber(fileName));
    }

    public void convertExcelToSqlite(Uri selectedFileUri) {

        Context context = getApplication().getApplicationContext();

        try {

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

                try {
                    int glyphId = (int) row.getCell(0).getNumericCellValue();
                    int suraNumber = (int) row.getCell(1).getNumericCellValue();
                    int pageNumber = (int) row.getCell(2).getNumericCellValue();
                    int ayahNumber = (int) row.getCell(3).getNumericCellValue();
                    int lineNumber = (int) row.getCell(4).getNumericCellValue();
                    int position = (int) row.getCell(5).getNumericCellValue();
                    float minX = (float) row.getCell(6).getNumericCellValue();
                    float minY = (float) row.getCell(7).getNumericCellValue();
                    float maxX = (float) row.getCell(8).getNumericCellValue();
                    float maxY = (float) row.getCell(9).getNumericCellValue();

                    // Insert the cell values into your SQLite database
                    databaseHelper.insertGlyphData(glyphId, suraNumber, pageNumber, ayahNumber, lineNumber, position, minX, minY, maxX, maxY);

                } catch (IllegalStateException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileNameFromUri(Uri uri) {

        Context context = getApplication().getApplicationContext();

        String result = null;

        if (Objects.equals(uri.getScheme(), "content")) {

            try (Cursor cursor = context.getContentResolver().query(uri,
                    null, null, null, null)) {

                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            // If the DISPLAY_NAME column is not available, you can fall back to
            // using the last path segment of the URI, but this may not always give
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private int extractPageNumber(String filename) {

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(filename);

        if (matcher.find()) {
            String numberStr = matcher.group();
            return Integer.parseInt(numberStr);
        } else {
            return -1;
        }
    }
}
