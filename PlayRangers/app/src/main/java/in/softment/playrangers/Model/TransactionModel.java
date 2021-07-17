package in.softment.playrangers.Model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class TransactionModel {
    public String uid;
    public String title;
    public Date date;
    public int amount;
    public String type;
    public String reason;
    public String orderId;
    public boolean isSuccessful;
    public String tId;

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    static public TransactionModel data;

    static public ArrayList<TransactionModel> transactionModels = new ArrayList<>();
    static public ArrayList<TransactionModel> transactionModelsDebit = new ArrayList<>();
    static public ArrayList<TransactionModel> transactionModelsCredit = new ArrayList<>();

    public TransactionModel() {
       data = this;

    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }
}
