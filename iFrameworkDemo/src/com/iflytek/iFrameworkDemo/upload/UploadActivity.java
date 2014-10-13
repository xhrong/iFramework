package com.iflytek.iFrameworkDemo.upload;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.iflytek.iFramework.uploader.AbstractUploadServiceReceiver;
import com.iflytek.iFramework.uploader.ContentType;
import com.iflytek.iFramework.uploader.UploadRequest;
import com.iflytek.iFramework.uploader.UploadService;
import com.iflytek.iFrameworkDemo.R;

import java.io.File;
import java.net.URL;
import java.util.UUID;

/**
 * Activity that demonstrates how to use Android Upload Service.
 * 
 * @author Alex Gotev
 * 
 */
public class UploadActivity extends Activity {

    private static final String TAG = "AndroidUploadServiceDemo";

    private ProgressBar progressBar;
    private Button uploadButton;
    private EditText serverUrl;
    private EditText fileToUpload;
    private EditText parameterName;

    private final AbstractUploadServiceReceiver uploadReceiver = new AbstractUploadServiceReceiver() {

        @Override
        public void onProgress(String uploadId, int progress) {
            progressBar.setProgress(progress);

            Log.i(TAG, "The progress of the upload with ID " + uploadId + " is: " + progress);
        }

        @Override
        public void onError(String uploadId, Exception exception) {
            progressBar.setProgress(0);

            String message = "Error in upload with ID: " + uploadId + ". " + exception.getLocalizedMessage();
            Log.e(TAG, message, exception);
        }

        @Override
        public void onCompleted(String uploadId, int serverResponseCode, String serverResponseMessage) {
            progressBar.setProgress(0);

            String message = "Upload with ID " + uploadId + " is completed: " + serverResponseCode + ", "
                    + serverResponseMessage;
            Log.i(TAG, message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        progressBar = (ProgressBar) findViewById(R.id.uploadProgress);
        serverUrl = (EditText) findViewById(R.id.serverURL);
        fileToUpload = (EditText) findViewById(R.id.fileToUpload);
        parameterName = (EditText) findViewById(R.id.parameterName);
        uploadButton = (Button) findViewById(R.id.uploadButton);

        uploadButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onUploadButtonClick();
            }
        });

        progressBar.setMax(100);
        progressBar.setProgress(0);


        serverUrl.setText("http://192.168.63.110/upload.php");
        fileToUpload.setText(Environment.getExternalStorageDirectory()+"/a.jpg");
        parameterName.setText("n");

        // De-comment this line to enable self-signed SSL certificates in HTTPS connections
        // WARNING: Do not use in production environment. Recommended for development only
        // AllCertificatesAndHostsTruster.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }

    private boolean userInputIsValid(final String serverUrlString, final String fileToUploadPath,
                                     final String paramNameString) {
        if (serverUrlString.length() == 0) {
            Toast.makeText(this, getString(R.string.provide_valid_server_url), Toast.LENGTH_LONG).show();
            return false;
        }

        try {
            new URL(serverUrlString.toString());
        } catch (Exception exc) {
            Toast.makeText(this, getString(R.string.provide_valid_server_url), Toast.LENGTH_LONG).show();
            return false;
        }

        if (fileToUploadPath.length() == 0) {
            Toast.makeText(this, getString(R.string.provide_file_to_upload), Toast.LENGTH_LONG).show();
            return false;
        }

        if (!new File(fileToUploadPath).exists()) {
            Toast.makeText(this, getString(R.string.file_does_not_exist), Toast.LENGTH_LONG).show();
            return false;
        }

        if (paramNameString.length() == 0) {
            Toast.makeText(this, getString(R.string.provide_param_name), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void onUploadButtonClick() {

        final String serverUrlString = serverUrl.getText().toString();
        final String fileToUploadPath = fileToUpload.getText().toString();
        final String paramNameString = parameterName.getText().toString();

        if (!userInputIsValid(serverUrlString, fileToUploadPath, paramNameString))
            return;

        final UploadRequest request = new UploadRequest(this, UUID.randomUUID().toString(), serverUrlString);

        request.addFileToUpload(fileToUploadPath, paramNameString, "test", ContentType.APPLICATION_OCTET_STREAM);

        request.setNotificationConfig(R.drawable.ic_launcher, getString(R.string.app_name),
                                      getString(R.string.uploading), getString(R.string.upload_success),
                                      getString(R.string.upload_error), false);

        try {
            UploadService.startUpload(request);
        } catch (Exception exc) {
            Toast.makeText(this, "Malformed upload request. " + exc.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
