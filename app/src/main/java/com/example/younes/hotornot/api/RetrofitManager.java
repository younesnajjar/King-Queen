package com.example.younes.hotornot.api;

/**
 * Created by younes on 9/18/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.example.younes.hotornot.interfaces.LoyallEndpointInterface;
import com.example.younes.hotornot.models.NewRatingBody;
import com.example.younes.hotornot.models.Student;
import com.example.younes.hotornot.models.StudentExistence;
import com.example.younes.hotornot.utils.AppConstants;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetrofitManager {
    private static RetrofitManager mInstance;

    private static Context mContext;
    private static SharedPreferences preferences;


    private RetrofitManager() {
    }



    public static RetrofitManager getInstance(Context context) {
        RetrofitManager instance = mInstance != null ? mInstance : (mInstance = new RetrofitManager());
        if (context != null)
            mContext = context;
        return instance;
    }

//    private OkHttpClient getHttpClient() {
//        OkHttpClient okClient = new OkHttpClient.Builder()
//                .addInterceptor(
//                        new Interceptor() {
//                            @Override
//                            public Response intercept(Interceptor.Chain chain) throws IOException {
//                                Request original = chain.request();
//                                Request.Builder requestBuilder = original.newBuilder()
//                                        .method(original.method(), original.body());
//                                requestBuilder.addHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwibmFtZSI6Ik1hcmphbmUiLCJkZXNjcmlwdGlvbiI6IlN1cGVyIG1hcmtldCAiLCJpbWFnZV9wYXRoIjpudWxsfQ.tfmdhLVm7sWPMlPhMN7OQEd0NZvGldjX3z46BK-aQGM");
//                                Request request = requestBuilder.build();
//                                Response response = chain.proceed(request);
//                                ResponseBody body = response.body();
//                                return response;
//                            }
//                        })
//                .readTimeout(60, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .build();
//        return okClient;
//    }

    private Retrofit getRetrofit(String baseURL) {
        return new Retrofit.Builder()
//                .client(this.getHttpClient())
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private LoyallEndpointInterface getAPIService() {
        return getRetrofit(AppConstants.URL_ENDPOINT_PRIMARY).create(LoyallEndpointInterface.class);
    }


//    public Call<List<CardSection>> getMainTitles(Callback<List<CardSection>> callback) {
//        Call<List<CardSection>> call = getSecondaryAPIService().getMainTitles();
//        call.enqueue(callback);
//        return call;
//    }
    public Call<List<Student>> getAllStudents(int raterId,Callback<List<Student>> callback) {
        Call<List<Student>> call = getAPIService().getAllStudents(raterId);
        call.enqueue(callback);
        return call;
    }
    public Call<StudentExistence> verifyExistenceInDB(StudentExistence studentExistenceRequestBody, Callback<StudentExistence> callback) {
        Call<StudentExistence> call = getAPIService().verifyExistence(studentExistenceRequestBody);
        call.enqueue(callback);
        return call;
    }
    public Call<NewRatingBody> addRating(NewRatingBody ratingBody, Callback<NewRatingBody> callback) {
        Call<NewRatingBody> call = getAPIService().addRating(ratingBody);
        call.enqueue(callback);
        return call;
    }



    OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            Request.Builder builder = originalRequest.newBuilder();

            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }
    }).build();



}