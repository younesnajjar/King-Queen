package com.example.younes.hotornot.ui.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


import com.example.younes.hotornot.R;

import com.example.younes.hotornot.api.RetrofitManager;
import com.example.younes.hotornot.models.Student;
import com.example.younes.hotornot.models.StudentExistence;
import com.example.younes.hotornot.ui.adapters.StudentCard;
import com.example.younes.hotornot.utils.SaveSharedPreference;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;


import org.json.JSONException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SwiperScreenActivity extends AppCompatActivity {
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    CallbackManager callbackManager;
    AccessToken accessToken;
    boolean isLoggedIn;
    View loginOverLayView;
    LinearLayout loginErrorView;

    private com.facebook.login.widget.LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiper_screen);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // Declaring View
        mSwipeView            = findViewById(R.id.swipe_view);
        mContext              = getApplicationContext();
        loginButton           = findViewById(R.id.login_button);
        loginOverLayView      = findViewById(R.id.login_over_lay);
        loginErrorView        = findViewById(R.id.login_error_view);

// ------> Facebook Stuff
        //Verify if the User Is logged in or not
        accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn && SaveSharedPreference.getLoggedStatus(this)){
            Log.e("logged in", "onCreate: logged in" );
            toggleLoginScreen(true);
        }
        else{
            toggleLoginScreen(false);
            Log.e("logged Out", "onCreate: logged Out" );
        }

        RetrofitManager.getInstance(this).getAllStudents(SaveSharedPreference.getStudentId(getApplicationContext()),new StudentsCallBack());
        // Permissions To Ask For
        loginButton.setReadPermissions("email", "public_profile");
        // Create A CallBack Manager
        callbackManager = CallbackManager.Factory.create();

        // SwipeViewSettings
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.student_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.student_swipe_out_msg_view));

        // Choice Buttons OnClickListeners
        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });



        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        Log.e("ok", "onSuccess: "+(loginResult.getAccessToken().getToken()) );

                        new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                loginResult.getAccessToken().getUserId(),
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        try {
                                            Log.e("loginfacebook after", "onCompleted: "+response.getJSONObject().get("name"));
                                            StudentExistence existenceBody = new StudentExistence();
                                            Log.e("test ", "onSuccess: "+loginResult.getAccessToken().getUserId());
                                            existenceBody.setUsername(response.getJSONObject().get("name").toString());
                                            RetrofitManager.getInstance(SwiperScreenActivity.this).verifyExistenceInDB(existenceBody,new existenceCallBack());

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        ).executeAsync();

    }

                    @Override
                    public void onCancel() {
                        Log.e("ok", "onCancel: canceled ");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("ok", "onError: canceled "+error);
                    }
                });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class StudentsCallBack implements Callback<List<Student>> {

        @Override
        public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
            for(Student student : response.body() )
                mSwipeView.addView(new StudentCard(SwiperScreenActivity.this, student, mSwipeView));
        }

        @Override
        public void onFailure(Call<List<Student>> call, Throwable t) {
            Log.e("error : ", "onFailure: "+ t.getMessage() );
        }
    }
    private void toggleLoginScreen(boolean loginState){
        if(loginState == true){
            loginButton.setVisibility(View.GONE);
            loginOverLayView.setVisibility(View.GONE);
        }
        else{
            loginButton.setVisibility(View.VISIBLE);
            loginOverLayView.setVisibility(View.VISIBLE);
        }
    }

    private class existenceCallBack implements Callback<StudentExistence> {
        @Override
        public void onResponse(Call<StudentExistence> call, Response<StudentExistence> response) {
            Log.e("Existence", "onResponse: id = " + response.body().getUserId() );
            if(response.body().getExistenceCount()){
                SaveSharedPreference.setLoggedIn(getApplicationContext(),true);
                SaveSharedPreference.setStudentId(getApplicationContext(),response.body().getUserId());
                toggleLoginScreen(true);
                loginErrorView.setVisibility(View.GONE);
            }
            else{
                loginErrorView.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onFailure(Call<StudentExistence> call, Throwable t) {

        }
    }
}

