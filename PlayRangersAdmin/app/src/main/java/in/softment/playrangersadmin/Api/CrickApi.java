package in.softment.playrangersadmin.Api;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CrickApi{
    private OkHttpClient client = new OkHttpClient();
    private final String baseURL = "https://cricket.sportmonks.com/api/v2.0/";
    private final String api_token = "nvvBMYyOJ4ahwRrI9390oIe0wh2E1LukChl3PJKvzEXZ1ADCNgol2B1orlxF";


    public Call getUpcomingMatches() {
        Request request = new Request.Builder()
                .url(baseURL+"fixtures?filter[starts_between]=2021-05-02,2021-06-03&api_token="+api_token)
                .method("GET", null)
                .build();
        return client.newCall(request);
    }


    public Call getSquadByTeamAndSeasonId(String teamId, String seasonId) {
        Request request = new Request.Builder()
                .url(baseURL+"teams/"+teamId+"/squad/"+seasonId+"?&api_token="+api_token)
                .method("GET", null)
                .build();

        return client.newCall(request);
    }



}



