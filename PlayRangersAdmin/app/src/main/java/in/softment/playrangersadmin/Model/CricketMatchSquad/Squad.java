
package in.softment.playrangersadmin.Model.CricketMatchSquad;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Squad {

    @SerializedName("resource")
    @Expose
    private String resource;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("dateofbirth")
    @Expose
    private String dateofbirth;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("battingstyle")
    @Expose
    private String battingstyle;
    @SerializedName("bowlingstyle")
    @Expose
    private Object bowlingstyle;
    @SerializedName("position")
    @Expose
    private Position position;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("squad")
    @Expose
    private Squad__1 squad;

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

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBattingstyle() {
        return battingstyle;
    }

    public void setBattingstyle(String battingstyle) {
        this.battingstyle = battingstyle;
    }

    public Object getBowlingstyle() {
        return bowlingstyle;
    }

    public void setBowlingstyle(Object bowlingstyle) {
        this.bowlingstyle = bowlingstyle;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Squad__1 getSquad() {
        return squad;
    }

    public void setSquad(Squad__1 squad) {
        this.squad = squad;
    }

}
