package com.imagine.myapplication.Community;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.imagine.myapplication.R;
import com.imagine.myapplication.nav_fragments.Communities_Fragment;

public class Community_ViewHolder extends RecyclerView.ViewHolder {

    public Context mContext;
    public Communities_Fragment fragment;
    public Community comm;

    public Community_ViewHolder(@NonNull View itemView, Communities_Fragment fragment) {
        super(itemView);
        this.mContext = itemView.getContext();
        this.fragment = fragment;
    }

    public void bind(final Community comm){
        // sets up the communityviews and the onClickListener
        TextView title_tv = itemView.findViewById(R.id.comm_title);
        TextView description_tv = itemView.findViewById(R.id.comm_description);
        ImageView imageView = itemView.findViewById(R.id.comm_picture);
        ConstraintLayout contentView = itemView.findViewById(R.id.community_content_view);
        contentView.setClipToOutline(true);
        final String name = comm.name;
        final String description = comm.description;
        final String imageURL = comm.imageURL;
        final String commID = comm.topicID;
        final String displayOption = comm.displayOption;
        this.comm = new Community(name,imageURL,commID,description);
        this.comm.displayOption = displayOption;
        title_tv.setText(name);
        description_tv.setText(description);
        if(imageURL != null) {
            Glide.with(itemView).load(imageURL).into(imageView);
        } else {
            Glide.with(itemView).load(R.drawable.fact_stamp);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.addRecent(comm);
                Intent intent = new Intent(itemView.getContext(),Community_ViewPager_Activity.class);
                intent.putExtra("name", name);
                intent.putExtra("description",description);
                intent.putExtra("imageURL", imageURL);
                intent.putExtra("commID", commID);
                intent.putExtra("displayOption",displayOption);
                mContext.startActivity(intent);
            }
        });
    }

}
