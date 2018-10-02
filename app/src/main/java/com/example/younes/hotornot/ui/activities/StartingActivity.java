package com.example.younes.hotornot.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.younes.hotornot.R;
import com.example.younes.hotornot.api.RetrofitManager;
import com.example.younes.hotornot.models.StudentExistence;
import com.example.younes.hotornot.utils.FaceBookLoginStatus;
import com.example.younes.hotornot.utils.SaveSharedPreference;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartingActivity extends AppCompatActivity {
    CallbackManager callbackManager;

    @BindView(R.id.login_button) LoginButton loginButton; // Facebook Login Button Declaration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        ButterKnife.bind(this);

        // Facebook Button Requested Permissions Settings
        loginButton.setReadPermissions("email", "public_profile"); // Setting the Requested FaceBook Permissions

        // If the Student is already Connected
        if(FaceBookLoginStatus.isFbConnected(this)){
            startSwipingActivity();
        }
        else{
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager, new FaceBookCallBack());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startSwipingActivity() {
        Intent intent = new Intent(getApplicationContext(), SwiperScreenActivity.class);
        startActivity(intent);
    }



    private class ExistenceCallBack implements Callback<StudentExistence> {
        @Override
        public void onResponse(Call<StudentExistence> call, Response<StudentExistence> response) {

            // If the user exists in our database
            if(response.body().getUserExistence()){
                SaveSharedPreference.setLoggedIn(getApplicationContext(),true);
                SaveSharedPreference.setStudentId(getApplicationContext(),response.body().getUserId());
                Log.e("Existence", "onResponse: id = " + response.body().getUserId() );
                startSwipingActivity();
            }
            else{
                // Show Error Screen
            }
        }
        @Override
        public void onFailure(Call<StudentExistence> call, Throwable t) {
            // Show Internet Error
        }
    }

    private class FaceBookCallBack implements FacebookCallback<LoginResult> {
        @Override
        public void onSuccess(final LoginResult loginResult) { // Until now a facebook user is connected to our application
            Log.e("ok", "onSuccess: "+(loginResult.getAccessToken().getToken()) );
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    loginResult.getAccessToken().getUserId(),
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        String studentName; // Will Contains the CallBack Student Name
                        public void onCompleted(GraphResponse response) {
                            try {
                                studentName = response.getJSONObject().get("name").toString();
                                // Verify if the connected user exists in Our DataBase
                                // (Exists In the facebook group (members are extracted by data scrapping))
                                RetrofitManager.getInstance(getApplicationContext())
                                        .verifyExistenceInDB(new StudentExistence(studentName),new ExistenceCallBack());
                                Log.e("Facebook API : ", "onCompleted: -> Connected User : "+response.getJSONObject().get("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).executeAsync();

        }

        @Override
        public void onCancel() {
            //Show Error
            Log.e("ok", "onCancel: canceled ");
        }

        @Override
        public void onError(FacebookException error) {
            //Show Error
            Log.e("ok", "onError: canceled "+error);
        }

    }
}
