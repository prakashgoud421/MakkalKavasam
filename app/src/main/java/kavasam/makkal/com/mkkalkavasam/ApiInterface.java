package kavasam.makkal.com.mkkalkavasam;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("/api/login/login.php")
    Call<Loginresponse> verifyLogincredentials(
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password);
    @POST("/api/login/login.php")
    Call<signInResponse> signInUser(
            @Query("fullname") String name,
            @Query("username") String username,
            @Query("email") String email,
            @Query("mobile") String mobile,
            @Query("password") String password,
            @Query("address") String address,
            @Query("city") String city,
            @Query("state") String state);
}
