package com.rakibofc.imagemaptester.model;

import android.net.Uri;

public class ImageData {

    private final String imageFileName;
    private final Uri imageUri;
    private final int pageNo;

    public ImageData(String imageFileName, Uri imageUri, int pageNo) {
        this.imageFileName = imageFileName;
        this.imageUri = imageUri;
        this.pageNo = pageNo;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public int getPageNo() {
        return pageNo;
    }
}
