package com.rakibofc.imagemaptester.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.rakibofc.imagemaptester.helper.GlyphsDatabaseHelper;
import com.rakibofc.imagemaptester.model.GlyphInfo;

import java.util.List;

public class ImageTesterViewModel extends AndroidViewModel {

    private final MutableLiveData<List<GlyphInfo>> glyphInfoListLiveData;

    public ImageTesterViewModel(@NonNull Application application) {
        super(application);
        glyphInfoListLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<GlyphInfo>> getGlyphInfoListLiveData() {
        return glyphInfoListLiveData;
    }

    public void loadGlyphInfoList(int pageNo) {
        glyphInfoListLiveData.setValue(getGlyphInfoList(pageNo));
    }

    private List<GlyphInfo> getGlyphInfoList(int pageNo) {
        Context context = getApplication().getApplicationContext();

        try (GlyphsDatabaseHelper dbHelper = new GlyphsDatabaseHelper(context)) {
            return dbHelper.getGlyphsByPageAndAyah(pageNo);
        }
    }
}
