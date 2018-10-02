package com.example.younes.hotornot.ui.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.example.younes.hotornot.R;

import com.example.younes.hotornot.api.RetrofitManager;
import com.example.younes.hotornot.models.Student;
import com.example.younes.hotornot.ui.adapters.StudentCard;
import com.example.younes.hotornot.utils.FaceBookLoginStatus;
import com.example.younes.hotornot.utils.SaveSharedPreference;
import com.facebook.login.LoginManager;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SwiperScreenActivity extends AppCompatActivity {
    private SwipePlaceHolderView mSwipeView;
    ImageButton logOutImageButton;
    ImageButton acceptedButton;
    ImageButton rejectedButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiper_screen);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        // Declaring View
        mSwipeView            = findViewById(R.id.swipe_view);
        logOutImageButton       = findViewById(R.id.log_out_imagebutton);
        acceptedButton = findViewById(R.id.acceptBtn);
        rejectedButton = findViewById(R.id.acceptBtn);



        //if the User Is logged in
        if(FaceBookLoginStatus.isFbConnected(this)){
            Log.e("logged in", "onCreate: logged in" );
        }
        else{
            Log.e("logged Out", "onCreate: logged Out" );
        }

        RetrofitManager.getInstance(this).getAllStudents(SaveSharedPreference.getStudentId(getApplicationContext()),new StudentsCallBack());

        // SwipeViewSettings
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.student_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.student_swipe_out_msg_view));

        // Choice ButtonsListeners
        acceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });
        rejectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });
        //Log Out
        logOutImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                goBackToLoginActivity();
            }
        });
    }

    private void goBackToLoginActivity() {
        Intent intentToLoginActivity = new Intent(getApplicationContext(),StartingActivity.class);
        startActivity(intentToLoginActivity);
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
}

