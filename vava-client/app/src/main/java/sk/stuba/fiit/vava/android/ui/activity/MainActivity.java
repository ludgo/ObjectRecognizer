package sk.stuba.fiit.vava.android.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Picker;

import java.util.ArrayList;

import sk.stuba.fiit.vava.android.R;
import sk.stuba.fiit.vava.android.background.service.AwsS3UploadService;
import sk.stuba.fiit.vava.android.background.task.UploadUrlsTask;

/**
 * Entry point to application activity
 */
public class MainActivity extends MenuActivity implements Picker.PickListener {

    private static final int REQUEST_CODE_PERMISSIONS_READ_WRITE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS_READ_WRITE) {
            // Check requested permission for image chooser
            if (grantResults.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                pickImage();
            }
        }
    }

    // START Picker.PickListener
    @Override
    public void onPickedSuccessfully(final ArrayList<ImageEntry> images) {
        if (images == null || images.isEmpty()) return;
        // Transform picked images to custom model
        ArrayList<String> paths = new ArrayList<>();
        for (ImageEntry imageEntry : images) {
            paths.add(imageEntry.path);
        }
        // Pass image urls to AWS service for upload
        AwsS3UploadService.startActionUploadImages(this, getUid(), paths);
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, getString(R.string.error_cancelled), Toast.LENGTH_SHORT).show();
    }
    // END Picker.PickListener

    /**
     * Initialize image picker
     */
    private void pickImage() {
        // Show library's image chooser
        new Picker.Builder(this, this, R.style.MIP_theme)
                .build()
                .startActivity();
    }

    /**
     * User event: layout button click
     * <p>
     * Initialize adding new image flow
     * @param view View with set onClick event
     */
    public void dropImage(View view) {
        // Verify a user is signed in before interaction with functionality
        getSignedUser();
        // Check permission to manipulate images
        if (Build.VERSION.SDK_INT >= 23
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission for image chooser
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS_READ_WRITE);
        }
        else {
            pickImage();
        }

    }

    /**
     * User event: layout button click
     * <p>
     * Initialize uploading urls to server in background
     * @param view View with set onClick event
     */
    public void uploadUrls(View view) {
        String uid = getUid();
        UploadUrlsTask uploadUrlsTask = new UploadUrlsTask(uid);
        uploadUrlsTask.execute();
    }
}
