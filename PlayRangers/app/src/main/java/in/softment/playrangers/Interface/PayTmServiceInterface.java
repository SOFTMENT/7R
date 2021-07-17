package in.softment.playrangers.Interface;

import com.squareup.okhttp.RequestBody;

import in.softment.playrangers.Model.Token_Res;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface PayTmServiceInterface {


    @FormUrlEncoded
    @POST("/PayTM/init_Transaction.php")
    Call<Token_Res> generateTokenCall(
            @Field("MID" ) String mid,
            @Field("ORDER_ID") String order_id,
            @Field("AMOUNT") String amount
    );
}
