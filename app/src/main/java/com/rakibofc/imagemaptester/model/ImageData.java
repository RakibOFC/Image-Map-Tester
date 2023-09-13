package com.rakibofc.imagemaptester.model;

import android.net.Uri;

public class ImageData {

    private final String imageFileName;
    private final Uri imageUri;
    private final String imageTitle;

    public ImageData(String imageFileName, Uri imageUri, String imageTitle) {
        this.imageFileName = imageFileName;
        this.imageUri = imageUri;
        this.imageTitle = imageTitle;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getImageTitle() {
        return imageTitle;
    }
}
