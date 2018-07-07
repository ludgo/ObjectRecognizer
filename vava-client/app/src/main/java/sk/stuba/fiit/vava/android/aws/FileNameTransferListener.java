package sk.stuba.fiit.vava.android.aws;

import android.support.annotation.NonNull;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;

import lombok.Getter;

/**
 * S3 transfer listener waiting for single image upload's finish
 * and persisting image file name meanwhile
 */
public abstract class FileNameTransferListener implements TransferListener {

    // Desired file name
    @Getter
    private String fileName;
    // User ID
    @Getter
    private String uid;

    /**
     * Constructor
     * @param fileName Name of file being uploaded
     * @param uid User ID
     */
    protected FileNameTransferListener(@NonNull String fileName, @NonNull String uid) {
        this.fileName = fileName;
        this.uid = uid;
    }

    /**
     * Do something after successful upload of file
     */
    protected abstract void onCompleted();

    @Override
    public void onStateChanged(int id, TransferState state) {
        if (TransferState.COMPLETED.equals(state)) {
            // Image has been uploaded successfully
            onCompleted();
        }
    }

    @Override
    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
        // Indicate upload progress here
    }

    @Override
    public void onError(int id, Exception e) {
        Log.e(FileNameTransferListener.class.getName(),
                "AWS S3 image upload failed for file: " + fileName, e);
    }
}
