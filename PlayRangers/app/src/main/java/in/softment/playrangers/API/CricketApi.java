package in.softment.playrangers.API;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class CricketApi{
    private OkHttpClient client = new OkHttpClient();
    private final String baseURL = "https://dev132-cricket-live-scores-v1.p.rapidapi.com/";
    private final String apiKey = "a846994b49msh6069b2fee5883aap1312a8jsnacab5b115b65";
    private final String apiHost = "dev132-cricket-live-scores-v1.p.rapidapi.com";

    public Call getAllIpl2021Matches(String seriesID) {
        Request request = new Request.Builder()
                .url(baseURL+"?seriesid="+seriesID)
                .get()
                .addHeader("x-rapidapi-key", apiKey)
                .addHeader("x-rapidapi-host", apiHost)
                .build();
       return client.newCall(request);
    }

    public Call getCricketScore(String seriesID, String matchId) {
        Request request = new Request.Builder()
                .url(baseURL+"matchdetail.php?seriesid="+seriesID+"&matchid="+matchId)
                .get()
                .addHeader("x-rapidapi-key", apiKey)
                .addHeader("x-rapidapi-host", apiHost)
                .build();
        return client.newCall(request);
    }
}
