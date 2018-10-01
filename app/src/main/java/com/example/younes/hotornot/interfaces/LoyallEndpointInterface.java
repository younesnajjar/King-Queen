package com.example.younes.hotornot.interfaces;

import com.example.younes.hotornot.models.NewRatingBody;
import com.example.younes.hotornot.models.Student;
import com.example.younes.hotornot.models.StudentExistence;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by younes on 9/18/2018.
 */

public interface LoyallEndpointInterface {
    @GET("students/{rater_id}")
    Call<List<Student>> getAllStudents(@Path("rater_id") int raterId);
    @POST("students/verifyexistence")
    Call<StudentExistence> verifyExistence(@Body StudentExistence usernameBody);
    @POST("ratings")
    Call<NewRatingBody> addRating(@Body NewRatingBody newRatingBody);
}
