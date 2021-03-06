package com.imagine.myapplication.CommunityPicker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.gif.GifFrameResourceDecoder;
import com.google.firebase.auth.FirebaseAuth;
import com.imagine.myapplication.Community.Communities_Helper;
import com.imagine.myapplication.Community.Community;
import com.imagine.myapplication.CommunityCallback;
import com.imagine.myapplication.R;

import java.util.ArrayList;

public class OwnCommsFragment extends Fragment {
    public CommunityPickActivity activity;
    public RecyclerView recyclerView;
    public ArrayList<Community> ownComms;
    public FirebaseAuth auth = FirebaseAuth.getInstance();

    public OwnCommsFragment(CommunityPickActivity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_communities,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.recyclerView = getView().findViewById(R.id.communities_recyclerview);
        Communities_Helper helper = new Communities_Helper(getContext());
        if(auth.getCurrentUser() != null){
            helper.getOwnCommunities(new CommunityCallback() {
                @Override
                public void onCallback(ArrayList<Community> commList) {
                    ownComms = commList;
                    initRecyclerView();
                }
            },auth.getCurrentUser().getUid());
        }else {
            Toast.makeText(getContext(),getResources().getString(R.string.own_comms_fragment_login),Toast.LENGTH_LONG)
                .show();
        }

    }

    public void initRecyclerView(){
        final CommunityPickerAdapter adapter = new CommunityPickerAdapter(this.ownComms,getContext(),activity);
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(adapter.getItemViewType(position)){
                    case R.layout.community_own_comms:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}
