package in.softment.playrangers.Model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class User {

    public String name;
    public String mobile;
    public String mail;
    public Date time;
    public String uid;
    public boolean isMobileVerified;
    public int matchesPlayed;
    public int won;
    public int earnedByReferral;
    public String pollId;
    public String selectedOption;
    public int depositAmount;
    public int winningAmount;
    public String freeFireUsername;

    static public User data;


    public String getFreeFireUsername() {
        return freeFireUsername;
    }

    public void setFreeFireUsername(String freeFireUsername) {
        this.freeFireUsername = freeFireUsername;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public User() {
        data = this;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isMobileVerified() {
        return isMobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        isMobileVerified = mobileVerified;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public int getEarnedByReferral() {
        return earnedByReferral;
    }

    public void setEarnedByReferral(int earnedByReferral) {
        this.earnedByReferral = earnedByReferral;
    }

    public int getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(int depositAmount) {
        this.depositAmount = depositAmount;
    }

    public int getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(int winningAmount) {
        this.winningAmount = winningAmount;
    }
}
