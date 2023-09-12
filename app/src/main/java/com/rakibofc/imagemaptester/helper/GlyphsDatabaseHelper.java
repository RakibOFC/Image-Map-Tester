package com.rakibofc.imagemaptester.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GlyphsDatabaseHelper extends SQLiteOpenHelper {
    private static final String GLYPHS_DB_NAME = "glyphs.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "image_glyphs";
    private static final String COL_GLYPH_ID = "glyph_id";
    private static final String COL_PAGE_NUMBER = "page_number";
    private static final String COL_LINE_NUMBER = "line_number";
    private static final String COL_SURA_NUMBER = "sura_number";
    private static final String COL_AYAH_NUMBER = "ayah_number";
    private static final String COL_POSITION = "position";
    private static final String COL_MIN_X = "min_x";
    private static final String COL_MAX_X = "max_x";
    private static final String COL_MIN_Y = "min_y";
    private static final String COL_MAX_Y = "max_y";

    public GlyphsDatabaseHelper(Context context) {
        super(context, GLYPHS_DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the database table
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COL_GLYPH_ID + " INTEGER, " +
                COL_PAGE_NUMBER + " INTEGER, " +
                COL_LINE_NUMBER + " INTEGER, " +
                COL_SURA_NUMBER + " INTEGER, " +
                COL_AYAH_NUMBER + " INTEGER, " +
                COL_POSITION + " INTEGER, " +
                COL_MIN_X + " REAL, " +
                COL_MAX_X + " REAL, " +
                COL_MIN_Y + " REAL, " +
                COL_MAX_Y + " REAL)";
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades here if needed
    }

    public void insertGlyphData(int glyphId, int pageNumber, int lineNumber, int suraNumber, int ayahNumber,
                                int position, double minX, double maxX, double minY, double maxY) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_GLYPH_ID, glyphId);
        values.put(COL_PAGE_NUMBER, pageNumber);
        values.put(COL_LINE_NUMBER, lineNumber);
        values.put(COL_SURA_NUMBER, suraNumber);
        values.put(COL_AYAH_NUMBER, ayahNumber);
        values.put(COL_POSITION, position);
        values.put(COL_MIN_X, minX);
        values.put(COL_MAX_X, maxX);
        values.put(COL_MIN_Y, minY);
        values.put(COL_MAX_Y, maxY);

        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();

        if (newRowId != -1) {
            // Insertion was successful
            // You can add any additional logic or error handling here if needed
        } else {
            // Insertion failed
            // Handle the error appropriately
        }
    }

    public void clearDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
