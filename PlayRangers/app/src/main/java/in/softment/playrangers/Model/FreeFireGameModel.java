package in.softment.playrangers.Model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FreeFireGameModel {

    public String gameID;
    public String title;
    public Date date;
    public int winPrize;
    public int perKill;
    public int entryFee;
    public String type;
    public String version;
    public String map;
    public int totalSpots;
    public int totalJoined;
    public String roomId;
    public String roomPassword;
    public String pdfLink;
    public boolean isExpired;
    static public FreeFireGameModel data;

    static public ArrayList<FreeFireGameModel> freeFireGameModels = new ArrayList<>();

    public ArrayList<String>  joinedUsers = new ArrayList<>();



    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }



    public ArrayList<String> getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(ArrayList<String> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    public FreeFireGameModel() {
        data = this;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getWinPrize() {
        return winPrize;
    }

    public void setWinPrize(int winPrize) {
        this.winPrize = winPrize;
    }

    public int getPerKill() {
        return perKill;
    }

    public void setPerKill(int perKill) {
        this.perKill = perKill;
    }

    public int getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(int entryFee) {
        this.entryFee = entryFee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(int totalSpots) {
        this.totalSpots = totalSpots;
    }

    public int getTotalJoined() {
        return totalJoined;
    }

    public void setTotalJoined(int totalJoined) {
        this.totalJoined = totalJoined;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }


}
