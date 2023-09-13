package com.rakibofc.imagemaptester.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;

import com.rakibofc.imagemaptester.R;
import com.rakibofc.imagemaptester.databinding.ActivityImageTesterBinding;
import com.rakibofc.imagemaptester.viewmodel.ImageTesterViewModel;

public class ImageTesterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityImageTesterBinding binding = ActivityImageTesterBinding.inflate(getLayoutInflater());
        ImageTesterViewModel viewModel = new ViewModelProvider(this).get(ImageTesterViewModel.class);

        setContentView(binding.getRoot());

        int pageNo = getIntent().getIntExtra("imagePageNo", -1);
        Uri imageUri = getIntent().getData();

        if (pageNo == -1) {
            onBackPressed();
        }

        binding.imageView.setImageURI(imageUri);
        binding.mToolbar.setTitle(String.format(getString(R.string.page_no_d), pageNo));
        viewModel.loadGlyphInfoList(pageNo);

        viewModel.getGlyphInfoListLiveData().observe(this, glyphList -> {

            binding.imageView.setHighlight(glyphList);
            binding.imageView.setOnRectClickListener((v, surahNumber, ayahNumber) -> {

                // Show bottom sheet dialog fragment
                showAyahInfoBottomSheet(getSupportFragmentManager(), pageNo, surahNumber, ayahNumber);
            });
        });

        binding.mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void showAyahInfoBottomSheet(FragmentManager fragmentManager, int pageNo, int surahNumber, int ayahNumber) {

        AyahInfoFragment fragment = AyahInfoFragment.newInstance(pageNo, surahNumber, ayahNumber);
        fragment.show(fragmentManager, AyahInfoFragment.TAG);
    }
}