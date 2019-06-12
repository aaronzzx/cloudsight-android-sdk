package ai.cloudsight.androidsdk;

import androidx.annotation.NonNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CloudSightClient {
    private static final CloudSightClient ourInstance = new CloudSightClient();

    private static CloudSightClient getInstance() {
        return ourInstance;
    }

    // `locale` is optional - is there a way to make this an optional setting?
    private String locale = "en-US";
    public void setLocale(String locale){
        this.locale = locale;
    }

    public CloudSightClient init(@NonNull String apiKey) {
        CloudSightClient.getInstance().cloudSightApi = NetworkService.getInstance(apiKey)
                .getCloudSightApi();
        return CloudSightClient.getInstance();
    }

    public CloudSightClient init(@NonNull String consumerKey, @NonNull String consumerSecret) {
        CloudSightClient.getInstance().cloudSightApi = NetworkService.getInstance(consumerKey, consumerSecret)
                .getCloudSightApi();
        return CloudSightClient.getInstance();
    }

    private CloudSightApi cloudSightApi;

    public void getImageInformation(File imageFile, final CloudSightCallback callback) {

        RequestBody localeRequest = RequestBody.create(MediaType.parse("text/plain"), locale);
        RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);

        cloudSightApi.recognitionByImageFile(localeRequest, image)
                .enqueue(new Callback<CloudSightResponse>() {
                    @Override
                    public void onResponse(Call<CloudSightResponse> call, Response<CloudSightResponse> response) {
                        if (response.isSuccessful()) {
                            callback.imageUploaded(response.body());
                            assert response.body() != null;
                            checkResponse(response.body(), callback);
                        } else {
                            callback.imageRecognitionFailed(response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<CloudSightResponse> call, Throwable t) {
                        callback.onFailure(t);
                    }
                });
    }


    public void getImageInformation(String remoteImageUrl, final CloudSightCallback callback) {

        RequestBody localeRequest = RequestBody.create(MediaType.parse("text/plain"), locale);
        RequestBody image = RequestBody.create(MediaType.parse("text/plain"), remoteImageUrl);

        cloudSightApi.recognitionByRemoteImageUrl(localeRequest, image)
                .enqueue(new Callback<CloudSightResponse>() {
                    @Override
                    public void onResponse(Call<CloudSightResponse> call, Response<CloudSightResponse> response) {
                        callback.imageUploaded(response.body());
                        assert response.body() != null;
                        checkResponse(response.body(), callback);
                    }

                    @Override
                    public void onFailure(Call<CloudSightResponse> call, Throwable t) {
                        callback.onFailure(t);
                    }
                });


    }

    private void checkResponse(CloudSightResponse response, final CloudSightCallback callback) {
        try {
            switch (RecognitionStatus.valueOf(response.getStatus().toLowerCase())) {
                case COMPLETED:
                    callback.imageRecognized(response);
                    break;
                case SKIPPED:
                    callback.imageRecognitionFailed(response.getReason());
                    break;
                case TIMEOUT:
                case NOT_FOUND:
                    callback.imageRecognitionFailed(response.getStatus());
                    break;
                case NOT_COMPLETED:
                    getInformationByToken(response.getToken(), callback);
            }
        } catch (IllegalArgumentException e) {
            callback.imageRecognitionFailed("Unhandled response status");
        }
    }

    private void getInformationByToken(String token, final CloudSightCallback callback) {
        cloudSightApi.getInfoByToken(token)
                .enqueue(new Callback<CloudSightResponse>() {
                    @Override
                    public void onResponse(Call<CloudSightResponse> call, Response<CloudSightResponse> response) {
                        assert response.body() != null;
                        checkResponse(response.body(), callback);
                    }

                    @Override
                    public void onFailure(Call<CloudSightResponse> call, Throwable t) {
                        callback.onFailure(t);
                    }
                });
    }

}
