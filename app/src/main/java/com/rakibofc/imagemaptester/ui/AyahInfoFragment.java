package com.rakibofc.imagemaptester.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rakibofc.imagemaptester.R;
import com.rakibofc.imagemaptester.databinding.FragmentAyahInfoBinding;

public class AyahInfoFragment extends BottomSheetDialogFragment {
    public static final String TAG = "AyahInfoFragment";
    private static final String ARG_PARAM1 = "pageNo";
    private static final String ARG_PARAM2 = "surahNumber";
    private static final String ARG_PARAM3 = "ayahNumber";

    private int pageNo;
    private int surahNumber;
    private int ayahNumber;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pageNo Parameter 1.
     * @param surahNumber Parameter 2.
     * @param ayahNumber Parameter 3.
     * @return A new instance of fragment AyahInfoFragment.
     */

    public static AyahInfoFragment newInstance(int pageNo, int surahNumber, int ayahNumber) {

        AyahInfoFragment fragment = new AyahInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, pageNo);
        args.putInt(ARG_PARAM2, surahNumber);
        args.putInt(ARG_PARAM3, ayahNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            pageNo = getArguments().getInt(ARG_PARAM1);
            surahNumber = getArguments().getInt(ARG_PARAM2);
            ayahNumber = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentAyahInfoBinding binding = FragmentAyahInfoBinding.inflate(inflater, container, false);

        binding.tvPageNo.setText(String.format(getString(R.string.page_no_d), pageNo));
        binding.tvSurahNo.setText(String.format(getString(R.string.surah_no_d), surahNumber));
        binding.tvAyatNo.setText(String.format(getString(R.string.ayat_no_d), ayahNumber));

        binding.btnOk.setOnClickListener(v -> dismiss());

        return binding.getRoot();
    }
}