package ai.cloudsight.demoapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import ai.cloudsight.androidsdk.CloudSightClient;
import ai.cloudsight.androidsdk.CloudSightCallback;
import ai.cloudsight.androidsdk.CloudSightResponse;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 2;
    TextView statusTextView;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button galleryButton = (Button) findViewById(R.id.pick_gallery);
        statusTextView = (TextView) findViewById(R.id.recogntion_status);
        resultTextView = (TextView) findViewById(R.id.recogntion_result);

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.ACTION_GET_CONTENT, true);
                startActivityForResult(intent, PICK_FROM_GALLERY);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                uploadImageRequest(new File(getPath(selectedImage)));
            }
        }
    }

    private void uploadImageRequest(File file) {

        CloudSightClient client = new CloudSightClient().init("");
        //CloudSightClient client = new CloudSightClient().init("", "");

        client.setLocale("en-US");

        client.getImageInformation(file, new CloudSightCallback() {
            @Override
            public void imageUploaded(CloudSightResponse response) {
                statusTextView.setText("Status: " + response.getStatus());
                resultTextView.setText("Result: " + response.getName());
                Log.d("CloudSight ", "Image uploaded to server");
            }

            @Override
            public void imageRecognized(CloudSightResponse response) {
                statusTextView.setText("Status: " + response.getStatus());
                resultTextView.setText("Result: " + response.getName());
            }

            @Override
            public void imageRecognitionFailed(String reason) {
                statusTextView.setText("Status: " + reason);

            }

            @Override
            public void onFailure(Throwable throwable) {
                statusTextView.setText("Status: " + throwable.getLocalizedMessage());
            }
        });
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }
}
