package com.imagine.myapplication.Feed.viewholder_classes;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.imagine.myapplication.PostActivitys.YouTubePostActivity;
import com.imagine.myapplication.R;
import com.imagine.myapplication.User;
import com.imagine.myapplication.UserCallback;
import com.imagine.myapplication.post_classes.YouTubePost;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeViewHolder extends CustomViewHolder {
    public Context mContext;

    public YouTubeViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
    }

    public void bind(final YouTubePost post){
        init(post);
        TextView title_textView = itemView.findViewById(R.id.title_textView);
        TextView createTime_textView = itemView.findViewById(R.id.createDate_textView);
        TextView name_textView = itemView.findViewById(R.id.name_textView);
        ImageView profilePicture_imageView = itemView.findViewById(
                R.id.profile_picture_imageView);
        YouTubePlayerView youTubePlayerView = itemView.findViewById(R.id.youtube_player);
        youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
            @Override
            public void onYouTubePlayer(YouTubePlayer youTubePlayer) {
                String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
                Pattern compilesPattern = Pattern.compile(pattern);
                Matcher matcher = compilesPattern.matcher(post.link);
                if(matcher.find()){
                    String result = matcher.group();
                    youTubePlayer.cueVideo(result,0);
                }
            }
        });
        String dateString =dateToString(post.createTime);
        title_textView.setText(post.title);
        createTime_textView.setText(dateString);


        if(post.originalPoster == "anonym"){
            name_textView.setText("Anonym");
            Glide.with(itemView).load(R.drawable.default_user).into(
                    profilePicture_imageView);
        }else{
            getUser(post.originalPoster, new UserCallback() {
                @Override
                public void onCallback(User user) {
                    post.user = user;
                    setName(post);
                }
            });
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), YouTubePostActivity.class);
                itemView.getContext().startActivity(intent);
            }
        });
    }

    public void setName(YouTubePost post){
        TextView username_textView = itemView.findViewById(R.id.name_textView);
        ImageView profilePicture_imageView = itemView.findViewById(
                R.id.profile_picture_imageView);
        username_textView.setText(post.user.name+ "  YouTubePost");
        if(post.user.imageURL == null || post.user.imageURL == ""){
            Glide.with(itemView).load(R.drawable.default_user).into(
                    profilePicture_imageView
            );
        }
        else{
            Glide.with(itemView).load(post.user.imageURL).into(
                    profilePicture_imageView
            );
        }
    }

    public String getType(){
        return "youTube";
    }
}
