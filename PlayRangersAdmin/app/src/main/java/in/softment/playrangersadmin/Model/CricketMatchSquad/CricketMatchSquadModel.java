
package in.softment.playrangersadmin.Model.CricketMatchSquad;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

@Generated("jsonschema2pojo")
public class CricketMatchSquadModel {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static Map<String, CricketMatchSquadModel>  cricketMatchSquadModelMap = new HashMap<>();



}
