package com.rakibofc.imagemaptester.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.rakibofc.imagemaptester.R;
import com.rakibofc.imagemaptester.databinding.ActivityImageTesterBinding;
import com.rakibofc.imagemaptester.helper.GlyphsDatabaseHelper;
import com.rakibofc.imagemaptester.model.GlyphInfo;
import com.rakibofc.imagemaptester.model.ImageData;

import java.util.List;

public class ImageTesterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityImageTesterBinding binding = ActivityImageTesterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int pageNo = getIntent().getIntExtra("imagePageNo", 1);
        Uri imageUri = getIntent().getData();

        binding.imageView.setImageURI(imageUri);

        binding.mToolbar.setTitle(String.format(getString(R.string.page_no_d), pageNo));

        try (GlyphsDatabaseHelper dbHelper = new GlyphsDatabaseHelper(getApplicationContext())) {
            List<GlyphInfo> glyphList = dbHelper.getGlyphsByPageAndAyah(pageNo);

            binding.imageView.setHighlight(glyphList);

            binding.imageView.setOnRectClickListener((v, surahNumber, ayahNumber) -> {

                // Click event
                // showAyahInfoBottomSheet(fragmentManager, surahNumber, ayahNumber - 1);
            });
        }

        binding.mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}