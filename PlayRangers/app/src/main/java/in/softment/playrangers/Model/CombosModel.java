package in.softment.playrangers.Model;

import java.util.ArrayList;
import java.util.Map;

public class CombosModel {
    public String comboId;
    public int matchId;
    public ArrayList<Map<String,String>> playersInfos;
    public int totalPrizePool;
    public String title;
    public int totalSpots;
    public int eachComboPlayers;
    public int totalInvestors;
    public ArrayList<String> joinedPlayers;

    public int getTotalInvestors() {
        return totalInvestors;
    }

    public void setTotalInvestors(int totalInvestors) {
        this.totalInvestors = totalInvestors;
    }

    public int getEachComboPlayers() {
        return eachComboPlayers;
    }

    public ArrayList<Map<String, String>> getPlayersInfos() {
        return playersInfos;
    }

    public void setPlayersInfos(ArrayList<Map<String, String>> playersInfos) {
        this.playersInfos = playersInfos;
    }

    public void setEachComboPlayers(int eachComboPlayers) {
        this.eachComboPlayers = eachComboPlayers;
    }


    public String getComboId() {
        return comboId;
    }

    public void setComboId(String comboId) {
        this.comboId = comboId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }


    public int getTotalPrizePool() {
        return totalPrizePool;
    }

    public void setTotalPrizePool(int totalPrizePool) {
        this.totalPrizePool = totalPrizePool;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(int totalSpots) {
        this.totalSpots = totalSpots;
    }
}
