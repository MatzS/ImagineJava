package com.imagine.myapplication.Feed.viewholder_classes;

import android.content.Context;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.imagine.myapplication.PostActivitys.ThoughtPostActivity;
import com.imagine.myapplication.PostActivitys.TranslationPostActivity;
import com.imagine.myapplication.R;
import com.imagine.myapplication.user_classes.User;
import com.imagine.myapplication.user_classes.UserActivity;
import com.imagine.myapplication.UserCallback;
import com.imagine.myapplication.post_classes.TranslationPost;

public class TranslationViewHolder extends CustomViewHolder{
    private static final String TAG = "TranslationViewHolder";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public Context mContext;
    public User userObj;

    public TranslationViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
    }

    public void bind(final TranslationPost post){
        // calls init method and sets up the post specific views
        init(post);
        TextView title_textView = itemView.findViewById(R.id.title_textView);
        TextView createTime_textView = itemView.findViewById(R.id.createDate_textView);
        TextView name_textView = itemView.findViewById(R.id.name_textView);
        ImageView profilePicture_imageView = itemView.findViewById(
                R.id.profile_picture_imageView);

        title_textView.setText(post.title);
        createTime_textView.setText(post.createTime);

        if(post.originalPoster.equals("anonym")){
            name_textView.setText("Anonym");
            Glide.with(itemView).load(R.drawable.anonym_user).into(
                    profilePicture_imageView);
        }else{
            getUser(post.originalPoster, new UserCallback() {
                @Override
                public void onCallback(User user) {
                    userObj = user;
                    post.user = user;
                    setName(post);
                }
            });
        }
        setLinkedFact(post.linkedFactId);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String jsonObj = gson.toJson(post);
                Intent intent = new Intent(itemView.getContext(), TranslationPostActivity.class );
                intent.putExtra("post",jsonObj);
                itemView.getContext().startActivity(intent);
            }
        });
        ImageButton options = itemView.findViewById(R.id.feed_menu_button);
        if(post.originalPoster.equals(auth.getCurrentUser().getUid())){
            options.setVisibility(View.VISIBLE);
            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMenu();
                }
            });
        } else {
            options.setVisibility(View.INVISIBLE);
        }
    }

    public void setName(final TranslationPost post){
        // sets up the user views
        TextView username_textView = itemView.findViewById(R.id.name_textView);
        ImageView profilePicture_imageView = itemView.findViewById(
                R.id.profile_picture_imageView);
        username_textView.setText(post.user.name+ "  TranslationPost");
        if(post.user.imageURL == null || post.user.imageURL == ""){
            Glide.with(itemView).load(R.drawable.default_user).into(
                    profilePicture_imageView
            );
        }
        else{
            Glide.with(itemView).load(post.user.imageURL).into(
                    profilePicture_imageView
            );
            profilePicture_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    String userString = gson.toJson(userObj);
                    Intent intent = new Intent(mContext, UserActivity.class);
                    intent.putExtra("user",userString);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public void showMenu(){
        ImageButton options = itemView.findViewById(R.id.feed_menu_button);
        PopupMenu menu = new PopupMenu(itemView.getContext(),options);
        MenuInflater inflater = menu.getMenuInflater();
        inflater.inflate(R.menu.feed_post_menu, menu.getMenu());
        menu.show();
    }

    public String getType(){
        return "translation";
    }
}
