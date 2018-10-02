package com.example.younes.hotornot.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SwiperScreenActivity extends AppCompatActivity {

    @BindView(R.id.swipe_view) SwipePlaceHolderView mSwipeView;

    @OnClick(R.id.acceptBtn) public void swipeRight(){mSwipeView.doSwipe(true);}
    @OnClick(R.id.rejectBtn) public void swipeLeft() {mSwipeView.doSwipe(false);}
    @OnClick(R.id.log_out_imagebutton)
    public void logOut(){
        LoginManager.getInstance().logOut();
        goBackToLoginActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiper_screen);
        ButterKnife.bind(this);

        //if the User Is logged in
        if(!FaceBookLoginStatus.isFbConnected(this)){
            Log.e("logged Out", "onCreate: logged Out" );
            goBackToLoginActivity();
        }
        else{
            Log.e("logged in", "onCreate: logged in" );

            // SwipeViewSettings
            mSwipeView.getBuilder()
                    .setDisplayViewCount(3)
                    .setSwipeDecor(new SwipeDecor()
                            .setPaddingTop(20)
                            .setRelativeScale(0.01f)
                            .setSwipeInMsgLayoutId(R.layout.student_swipe_in_msg_view)
                            .setSwipeOutMsgLayoutId(R.layout.student_swipe_out_msg_view));

            RetrofitManager.getInstance(this).getAllStudents(SaveSharedPreference.getStudentId(this), new Callback<List<Student>>() {
                @Override
                public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                    for(Student student : response.body() )
                        mSwipeView.addView(new StudentCard(SwiperScreenActivity.this, student));
                }
                @Override
                public void onFailure(Call<List<Student>> call, Throwable t) {
                    Log.e("error : ", "onFailure: "+ t.getMessage() );
                }
            });
        }
    }
    private void goBackToLoginActivity() {
        Intent intentToLoginActivity = new Intent(getApplicationContext(),StartingActivity.class);
        startActivity(intentToLoginActivity);
    }
    @Override
    public void onBackPressed() { // On Back Button Pressed
        // Do Nothing
    }
}

