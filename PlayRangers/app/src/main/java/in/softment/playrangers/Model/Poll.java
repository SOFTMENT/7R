package in.softment.playrangers.Model;

import com.google.firebase.Timestamp;

import java.util.Map;

public class Poll {

    public int a;
    public int b;
    public Map<String, String> options;
    public String question;
    public String pollId;
    public Timestamp date;

    static public Poll data;
    public Poll() {
        data = this;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public static Poll getData() {
        return data;
    }

    public static void setData(Poll data) {
        Poll.data = data;
    }
}
