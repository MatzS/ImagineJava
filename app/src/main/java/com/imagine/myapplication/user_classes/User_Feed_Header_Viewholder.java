package com.imagine.myapplication.user_classes;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.imagine.myapplication.Feed.viewholder_classes.CustomViewHolder;
import com.imagine.myapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Feed_Header_Viewholder extends CustomViewHolder {

    public Context mContext;
    public UserActivity activity;
    public final int GALLERY = 1;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public User_Feed_Header_Viewholder(@NonNull View itemView, UserActivity activity) {
        super(itemView);
        this.mContext = itemView.getContext();
        this.activity = activity;
    }

    public void bind(User user) {
        CircleImageView profileImageView = itemView.findViewById(R.id.userHeaderProfileImageView);
        TextView nameLabel = itemView.findViewById(R.id.userHeaderNameTextView);
        TextView statusTextLabel = itemView.findViewById(R.id.userHeaderStatusTextView);
        CircleImageView changeProfilePicture = itemView.findViewById(R.id.changeProfilePicture);
        CircleImageView changeProfilePictureBackground = itemView.findViewById(R.id.changeProfilePictureBackground);

        FirebaseUser currentUser = auth.getCurrentUser();
        activity.setHeader(this);

        if (currentUser != null) {
            if (currentUser.getUid().equals(user.userID)) {
                changeProfilePicture.setVisibility(View.VISIBLE);
                changeProfilePictureBackground.setVisibility(View.VISIBLE);
                changeProfilePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO Change Profile Picture
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activity.startActivityForResult(intent,GALLERY);
                    }
                });
            }
        }

        nameLabel.setText(user.name);

        if (user.imageURL != "") {
            Glide.with(itemView).load(user.imageURL).into(profileImageView);
        }
        if (user.statusQuote != "") {
            statusTextLabel.setText(user.statusQuote);
        } //else { setDefaultQuote and edit it the way you want if you are THE user

    }

    public void reloadPicture(){
        FirebaseUser user = auth.getCurrentUser();
        String imageURL = user.getPhotoUrl().toString();
        CircleImageView profileImageView = itemView.findViewById(R.id.userHeaderProfileImageView);
        if (imageURL != "") {
            Glide.with(itemView).load(imageURL).into(profileImageView);
        }
    }
}
