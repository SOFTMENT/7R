
package in.softment.playrangersadmin.Model.CricketMatchDetailsModel;

import java.io.Serializable;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class LocalteamDlData implements Serializable
{

    @SerializedName("score")
    @Expose
    private Object score;
    @SerializedName("overs")
    @Expose
    private Object overs;
    @SerializedName("wickets_out")
    @Expose
    private Object wicketsOut;
    private final static long serialVersionUID = -606707798921460609L;

    public Object getScore() {
        return score;
    }

    public void setScore(Object score) {
        this.score = score;
    }

    public Object getOvers() {
        return overs;
    }

    public void setOvers(Object overs) {
        this.overs = overs;
    }

    public Object getWicketsOut() {
        return wicketsOut;
    }

    public void setWicketsOut(Object wicketsOut) {
        this.wicketsOut = wicketsOut;
    }

}
