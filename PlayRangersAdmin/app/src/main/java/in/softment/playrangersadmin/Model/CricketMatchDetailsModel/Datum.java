
package in.softment.playrangersadmin.Model.CricketMatchDetailsModel;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Datum implements Serializable
{

    @SerializedName("resource")
    @Expose
    private String resource;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("league_id")
    @Expose
    private Integer leagueId;
    @SerializedName("season_id")
    @Expose
    private Integer seasonId;
    @SerializedName("stage_id")
    @Expose
    private Integer stageId;
    @SerializedName("round")
    @Expose
    private String round;
    @SerializedName("localteam_id")
    @Expose
    private Integer localteamId;
    @SerializedName("visitorteam_id")
    @Expose
    private Integer visitorteamId;
    @SerializedName("starting_at")
    @Expose
    private String startingAt;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("live")
    @Expose
    private Boolean live;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("last_period")
    @Expose
    private Object lastPeriod;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("venue_id")
    @Expose
    private Integer venueId;
    @SerializedName("toss_won_team_id")
    @Expose
    private Object tossWonTeamId;
    @SerializedName("winner_team_id")
    @Expose
    private Object winnerTeamId;
    @SerializedName("draw_noresult")
    @Expose
    private Object drawNoresult;
    @SerializedName("first_umpire_id")
    @Expose
    private Object firstUmpireId;
    @SerializedName("second_umpire_id")
    @Expose
    private Object secondUmpireId;
    @SerializedName("tv_umpire_id")
    @Expose
    private Object tvUmpireId;
    @SerializedName("referee_id")
    @Expose
    private Object refereeId;
    @SerializedName("man_of_match_id")
    @Expose
    private Object manOfMatchId;
    @SerializedName("man_of_series_id")
    @Expose
    private Object manOfSeriesId;
    @SerializedName("total_overs_played")
    @Expose
    private Object totalOversPlayed;
    @SerializedName("elected")
    @Expose
    private Object elected;
    @SerializedName("super_over")
    @Expose
    private Boolean superOver;
    @SerializedName("follow_on")
    @Expose
    private Boolean followOn;
    @SerializedName("localteam_dl_data")
    @Expose
    private LocalteamDlData localteamDlData;
    @SerializedName("visitorteam_dl_data")
    @Expose
    private VisitorteamDlData visitorteamDlData;
    @SerializedName("rpc_overs")
    @Expose
    private Object rpcOvers;
    @SerializedName("rpc_target")
    @Expose
    private Object rpcTarget;
    @SerializedName("weather_report")
    @Expose
    private List<Object> weatherReport = null;
    private final static long serialVersionUID = -4830845225077743827L;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    public Integer getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
    }

    public Integer getStageId() {
        return stageId;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public Integer getLocalteamId() {
        return localteamId;
    }

    public void setLocalteamId(Integer localteamId) {
        this.localteamId = localteamId;
    }

    public Integer getVisitorteamId() {
        return visitorteamId;
    }

    public void setVisitorteamId(Integer visitorteamId) {
        this.visitorteamId = visitorteamId;
    }

    public String getStartingAt() {
        return startingAt;
    }

    public void setStartingAt(String startingAt) {
        this.startingAt = startingAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getLive() {
        return live;
    }

    public void setLive(Boolean live) {
        this.live = live;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getLastPeriod() {
        return lastPeriod;
    }

    public void setLastPeriod(Object lastPeriod) {
        this.lastPeriod = lastPeriod;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }

    public Object getTossWonTeamId() {
        return tossWonTeamId;
    }

    public void setTossWonTeamId(Object tossWonTeamId) {
        this.tossWonTeamId = tossWonTeamId;
    }

    public Object getWinnerTeamId() {
        return winnerTeamId;
    }

    public void setWinnerTeamId(Object winnerTeamId) {
        this.winnerTeamId = winnerTeamId;
    }

    public Object getDrawNoresult() {
        return drawNoresult;
    }

    public void setDrawNoresult(Object drawNoresult) {
        this.drawNoresult = drawNoresult;
    }

    public Object getFirstUmpireId() {
        return firstUmpireId;
    }

    public void setFirstUmpireId(Object firstUmpireId) {
        this.firstUmpireId = firstUmpireId;
    }

    public Object getSecondUmpireId() {
        return secondUmpireId;
    }

    public void setSecondUmpireId(Object secondUmpireId) {
        this.secondUmpireId = secondUmpireId;
    }

    public Object getTvUmpireId() {
        return tvUmpireId;
    }

    public void setTvUmpireId(Object tvUmpireId) {
        this.tvUmpireId = tvUmpireId;
    }

    public Object getRefereeId() {
        return refereeId;
    }

    public void setRefereeId(Object refereeId) {
        this.refereeId = refereeId;
    }

    public Object getManOfMatchId() {
        return manOfMatchId;
    }

    public void setManOfMatchId(Object manOfMatchId) {
        this.manOfMatchId = manOfMatchId;
    }

    public Object getManOfSeriesId() {
        return manOfSeriesId;
    }

    public void setManOfSeriesId(Object manOfSeriesId) {
        this.manOfSeriesId = manOfSeriesId;
    }

    public Object getTotalOversPlayed() {
        return totalOversPlayed;
    }

    public void setTotalOversPlayed(Object totalOversPlayed) {
        this.totalOversPlayed = totalOversPlayed;
    }

    public Object getElected() {
        return elected;
    }

    public void setElected(Object elected) {
        this.elected = elected;
    }

    public Boolean getSuperOver() {
        return superOver;
    }

    public void setSuperOver(Boolean superOver) {
        this.superOver = superOver;
    }

    public Boolean getFollowOn() {
        return followOn;
    }

    public void setFollowOn(Boolean followOn) {
        this.followOn = followOn;
    }

    public LocalteamDlData getLocalteamDlData() {
        return localteamDlData;
    }

    public void setLocalteamDlData(LocalteamDlData localteamDlData) {
        this.localteamDlData = localteamDlData;
    }

    public VisitorteamDlData getVisitorteamDlData() {
        return visitorteamDlData;
    }

    public void setVisitorteamDlData(VisitorteamDlData visitorteamDlData) {
        this.visitorteamDlData = visitorteamDlData;
    }

    public Object getRpcOvers() {
        return rpcOvers;
    }

    public void setRpcOvers(Object rpcOvers) {
        this.rpcOvers = rpcOvers;
    }

    public Object getRpcTarget() {
        return rpcTarget;
    }

    public void setRpcTarget(Object rpcTarget) {
        this.rpcTarget = rpcTarget;
    }

    public List<Object> getWeatherReport() {
        return weatherReport;
    }

    public void setWeatherReport(List<Object> weatherReport) {
        this.weatherReport = weatherReport;
    }

}
