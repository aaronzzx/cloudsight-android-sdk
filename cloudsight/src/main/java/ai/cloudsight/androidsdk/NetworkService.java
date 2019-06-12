package ai.cloudsight.androidsdk;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private static final String CLOUDSIGHT_URL = "https://api.cloudsight.ai"; // Could this be some kind of 'configuration variable'?
    private static NetworkService mInstance;
    private Retrofit mRetrofit;

    private NetworkService(final String apiKey) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
                                      @Override
                                      public Response intercept(Interceptor.Chain chain) throws IOException {
                                          Request original = chain.request();

                                          Request request = original.newBuilder()
                                                  .addHeader("Authorization", "CloudSight " + apiKey)
                                                  .method(original.method(), original.body())
                                                  .build();

                                          return chain.proceed(request);
                                      }
                                  });

        OkHttpClient client = httpClient.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(CLOUDSIGHT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    private NetworkService(String consumerKey, String consumerSecret) {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(consumerKey, consumerSecret))
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(CLOUDSIGHT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }

    public static NetworkService getInstance(String apiKey) {
        if (mInstance == null) {
            mInstance = new NetworkService(apiKey);
        }
        return mInstance;
    }

    public static NetworkService getInstance(String consumerKey, String consumerSecret) {
        if (mInstance == null) {
            mInstance = new NetworkService(consumerKey, consumerSecret);
        }
        return mInstance;
    }

    public CloudSightApi getCloudSightApi() {
        return mRetrofit.create(CloudSightApi.class);
    }
}
