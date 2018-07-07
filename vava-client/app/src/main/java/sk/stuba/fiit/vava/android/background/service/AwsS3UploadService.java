package sk.stuba.fiit.vava.android.background.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Size;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sk.stuba.fiit.vava.android.aws.AwsS3Helper;
import sk.stuba.fiit.vava.android.aws.FileNameTransferListener;
import sk.stuba.fiit.vava.android.util.Utilities;

/**
 * Android service to upload images on AWS S3
 */
public class AwsS3UploadService extends IntentService {

    // Action unique names
    private static final String ACTION_UPLOAD_IMAGES = "sk.stuba.fiit.vava.android.action.UPLOAD_IMAGES";
    // Intent extra keys
    private static final String EXTRA_PARAM_UID = "sk.stuba.fiit.vava.android.extra.UID";
    private static final String EXTRA_PARAM_PATHS = "sk.stuba.fiit.vava.android.extra.PATHS";

    /**
     * Standard format constructor
     */
    public AwsS3UploadService() {
        super("AwsS3UploadService");
    }

    /**
     * Service factory method
     * @param context Context firing intent
     * @param uid User ID
     * @param paramPaths Paths of images to be uploaded
     */
    public static void startActionUploadImages(@NonNull Context context, @NonNull String uid,
                                               @NonNull @Size(min=1) ArrayList<String> paramPaths) {
        Intent intent = new Intent(context, AwsS3UploadService.class);
        intent.setAction(ACTION_UPLOAD_IMAGES);
        intent.putExtra(EXTRA_PARAM_UID, uid);
        // Note: we do need ArrayList NOT List due to intent extra type
        intent.putStringArrayListExtra(EXTRA_PARAM_PATHS, paramPaths);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Always handle service logic on worker thread
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_IMAGES.equals(action)) {
                final String uid = intent.getStringExtra(EXTRA_PARAM_UID);
                final ArrayList<String> paramPaths = intent.getStringArrayListExtra(EXTRA_PARAM_PATHS);
                if (uid != null && paramPaths != null && !paramPaths.isEmpty()) {
                    handleActionUploadImages(uid, paramPaths);
                }
            }
        }
    }

    /**
     * Initialize S3 image upload
     * @param paths Paths of images to be uploaded
     */
    private void handleActionUploadImages(@NonNull final String uid,
                                          @NonNull @Size(min=1) final List<String> paths) {
        // Get transfer utility
        TransferUtility transferUtility = AwsS3Helper.buildTransferUtility(getApplicationContext());
        // Separate upload for each image
        for (String path : paths) {
            // Image path must exist
            File file = new File(path);
            if (!file.exists()) {
                continue;
            }
            // Get image
            Bitmap image = Utilities.loadBitmapFromFile(this, Uri.fromFile((file)));
            // Resize image
            Bitmap resizedImage = Utilities.resizeBitmap(image, 480, 360);
            // Make current timestamp file name
            String thumbnailName = Utilities.buildJpgFileName(
                    Long.toString(System.currentTimeMillis()));
            // Prepare file
            File thumbnail = Utilities.saveBitmapToFile(resizedImage, thumbnailName);
            // Initialize upload
            TransferObserver observer = transferUtility.upload(
                    AwsS3Helper.getBucketName(),
                    thumbnailName,
                    thumbnail,
                    CannedAccessControlList.PublicRead // Allow to be readable by other services! // TODO protect
            );
            // Listen to file name until end of upload
            observer.setTransferListener(new FileNameTransferListener(thumbnailName, uid) {
                @Override
                public void onCompleted() {
                    // Save image url
                    String url = AwsS3Helper.buildS3ImageUrl(getFileName());
                    String uid = getUid();
                    Utilities.getTemporaryUrlProvider().save(url, uid);
                }
            });
        }
    }
}
