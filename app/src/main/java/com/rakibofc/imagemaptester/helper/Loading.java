package com.rakibofc.imagemaptester.helper;

public interface Loading {
    /**
     * @param status An integer value indicating the status of the loading operation.
     *               The value 0 is typically used to represent the start of loading, and 1 is
     *               used to represent the end of loading.
     */
    void onLoad(LoadingStatus status);
}
