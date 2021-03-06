package com.imagine.myapplication.Community;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.imagine.myapplication.R;
import com.imagine.myapplication.nav_fragments.New_Post_Fragment;

public class Community_New_Post extends AppCompatActivity {
    public Community community;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String commString = intent.getStringExtra("comm");
        Gson gson = new Gson();
        Community comm = gson.fromJson(commString,Community.class);
        this.community = comm;
        setContentView(R.layout.activtiy_community_new_post);
        FragmentManager manager = getSupportFragmentManager();
        New_Post_Fragment fragment = new New_Post_Fragment(this,this.community);
        manager.beginTransaction()
                .replace(R.id.community_new_post_frame, fragment)
                .commit();
    }
}
