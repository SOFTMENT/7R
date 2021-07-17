package in.softment.playrangers.Model;

import java.util.ArrayList;

public class CricketMatchDetailsModel {

    public int tournamentId;
    public int matchId;
    public int teamIdHome;
    public int teamIdAway;
    public String teamNameHome;
    public String teamNameAway;
    public String startTime;
    public String teamImageHome;
    public String teamImageAway;

    public String getTeamImageHome() {
        return teamImageHome;
    }

    public void setTeamImageHome(String teamImageHome) {
        this.teamImageHome = teamImageHome;
    }

    public String getTeamImageAway() {
        return teamImageAway;
    }

    public void setTeamImageAway(String teamImageAway) {
        this.teamImageAway = teamImageAway;
    }

    public static CricketMatchDetailsModel data;
    public static ArrayList<CricketMatchDetailsModel> cricketMatchDetailsModels = new ArrayList<>();


    public CricketMatchDetailsModel(){
        data = this;
    }

    public static CricketMatchDetailsModel getData() {
        return data;
    }

    public static void setData(CricketMatchDetailsModel data) {
        CricketMatchDetailsModel.data = data;
    }

    public static ArrayList<CricketMatchDetailsModel> getCricketMatchDetailsModels() {
        return cricketMatchDetailsModels;
    }

    public static void setCricketMatchDetailsModels(ArrayList<CricketMatchDetailsModel> cricketMatchDetailsModels) {
        CricketMatchDetailsModel.cricketMatchDetailsModels = cricketMatchDetailsModels;
    }

    public int getMatchId() {
        return matchId;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getTeamIdHome() {
        return teamIdHome;
    }

    public void setTeamIdHome(int teamIdHome) {
        this.teamIdHome = teamIdHome;
    }

    public int getTeamIdAway() {
        return teamIdAway;
    }

    public void setTeamIdAway(int teamIdAway) {
        this.teamIdAway = teamIdAway;
    }

    public String getTeamNameHome() {
        return teamNameHome;
    }

    public void setTeamNameHome(String teamNameHome) {
        this.teamNameHome = teamNameHome;
    }

    public String getTeamNameAway() {
        return teamNameAway;
    }

    public void setTeamNameAway(String teamNameAway) {
        this.teamNameAway = teamNameAway;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


}
