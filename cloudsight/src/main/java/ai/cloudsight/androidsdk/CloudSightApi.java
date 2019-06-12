package ai.cloudsight.androidsdk;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface CloudSightApi {

    @Headers("User-Agent: Android") // This we'll want to use the device-specific user-agent. This is super important, especially for ad providers.
    @Multipart
    @POST("/v1/images")
    Call<CloudSightResponse> recognitionByImageFile(
            @Part("locale") RequestBody locale, // Again, optional. :-)
            @Part("image\"; filename=\"myfile.jpg\" ") RequestBody file);

    @Multipart
    @POST("/v1/images")
    Call<CloudSightResponse> recognitionByRemoteImageUrl(
            @Part("locale")
                    RequestBody locale, // Again, optional. :-)
            @Part("remote_image_url")
                    RequestBody imageUrl);

    @GET("/v1/images/{token}")
    Call<CloudSightResponse> getInfoByToken(
            @Path("token") String token);
}
