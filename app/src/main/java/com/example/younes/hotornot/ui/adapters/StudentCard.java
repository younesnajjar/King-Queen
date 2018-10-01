package com.example.younes.hotornot.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.younes.hotornot.R;
import com.example.younes.hotornot.api.RetrofitManager;
import com.example.younes.hotornot.models.NewRatingBody;
import com.example.younes.hotornot.models.Student;
import com.example.younes.hotornot.utils.SaveSharedPreference;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by younes on 9/16/2018.
 */

@Layout(R.layout.student_card_view)
public class StudentCard {

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    private Student mstudent;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public StudentCard(Context context, Student student, SwipePlaceHolderView swipeView) {
        mContext = context;
        mstudent = student;
        mSwipeView = swipeView;
    }
    private void addRating(String rate) {
        NewRatingBody ratingBody = new NewRatingBody();
        ratingBody.setRaterId(SaveSharedPreference.getStudentId(mContext));
        ratingBody.setRatedId(mstudent.getId());
//        RetrofitManager.getInstance(mContext).addRating(ratingBody,new RatingCallBack());
        switch (rate){
            case "Liked":
                ratingBody.setRate(1);
                break;
            case "Disliked":
                ratingBody.setRate(0);
                break;
        }


        RetrofitManager.getInstance(mContext).addRating(ratingBody,new RatingCallBack());
    }

    @Resolve
    private void onResolved(){
        Glide.with(mContext).load(createImagePathFromId(mstudent.getFbId())).into(profileImageView);
//        Picasso.get()
//                .load( createImagePathFromId(mstudent.getFbId()) )
//                .error( R.drawable.x_dislike )
//                .placeholder( R.drawable.progress_animation )
//                .into( profileImageView );
        nameAgeTxt.setText(mstudent.getFullName());
        locationNameTxt.setText("Morocco");
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        addRating("Disliked");
       // mSwipeView.addView(this);
    }
    private String createImagePathFromId(String id){
        return "http://graph.facebook.com/"+id+"/picture?type=large&width=400&hieght=400";
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");

        addRating("Liked");
    }



    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }

    private class RatingCallBack implements Callback<NewRatingBody> {
        @Override
        public void onResponse(Call<NewRatingBody> call, Response<NewRatingBody> response) {

        }

        @Override
        public void onFailure(Call<NewRatingBody> call, Throwable t) {

        }
    }
}
