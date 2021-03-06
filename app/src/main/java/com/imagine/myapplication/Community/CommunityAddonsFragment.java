package com.imagine.myapplication.Community;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.health.SystemHealthManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.imagine.myapplication.MainActivity;
import com.imagine.myapplication.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CommunityAddonsFragment extends Fragment {
    public HashMap<String,String> args;
    public Community community;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseAuth auth = FirebaseAuth.getInstance();
    public ArrayList<Addon> addons = new ArrayList<>();
    public RecyclerView recyclerView;
    public Community_Addons_Adapter adapter;
    public CommunityAddonsFragment fragment;



    public CommunityAddonsFragment(HashMap<String, String> args) {
        this.args = args;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.community_viewpager_addons,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String commID = args.get("commID");
        String  name = args.get("name");
        String description = args.get("description");
        String imageURL = args.get("imageURL");
        this.community = new Community(name,imageURL,commID,description);
        this.fragment = this;

        Button newAddOnButton = view.findViewById(R.id.new_addon_button);
        newAddOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment.getContext(),Community_Addon_New_Addon_Activity.class);
                Gson gson = new Gson();
                String commString = gson.toJson(community);
                intent.putExtra("comm",commString);
                fragment.getContext().startActivity(intent);
            }
        });

        setSmallBackgroundView(view);
        this.fetchTheAddons();
    }

    public void setSmallBackgroundView(View view) {
        ConstraintLayout backgroundView = view.findViewById(R.id.new_addon_background);
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(40);
        shape.setColor(ContextCompat.getColor(view.getContext(), R.color.md_white_1000));
        backgroundView.setBackground(shape);
        backgroundView.setClipToOutline(true);
        backgroundView.setAlpha(0.95f);
    }

    public void fetchTheAddons(){
//        Addon header = new Addon();
//        header.isHeader = true;
//        header.community = this.community;
//        addons.add(header);
        Configuration conf = MainActivity.configContext.getResources().getConfiguration();
        final Locale locale = conf.locale;
        Query ref;
        switch(locale.getLanguage()){
            case "de":
                ref = db.collection("Facts").document(this.community.topicID)
                        .collection("addOns").orderBy("popularity", Query.Direction.DESCENDING);
                break;
            case "en":
                ref = db.collection("Data").document("en")
                        .collection("topics").document(this.community.topicID).collection("addOns")
                        .orderBy("popularity", Query.Direction.DESCENDING);
                break;
            default:
                ref = db.collection("Data").document("en")
                        .collection("topics").document(this.community.topicID).collection("addOns")
                        .orderBy("popularity", Query.Direction.DESCENDING);
                break;
        }
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    System.out.println("!!!");
                    QuerySnapshot result = task.getResult();
                    List<DocumentSnapshot> docList = result.getDocuments();
                    for(DocumentSnapshot docSnap : docList){
                        Addon addon = new Addon();
                        Map<String,Object> data = docSnap.getData();
                        addon.OP = (docSnap.getString("OP") != null)
                                ? docSnap.getString("OP")
                                : "";
                        addon.description = (docSnap.getString("description") != null)
                                ? docSnap.getString("description")
                                : "";
                        addon.imageURL = (docSnap.getString("imageURL") != null)
                                ? docSnap.getString("imageURL")
                                : "";
                        addon.popularity = (docSnap.getLong("popularity") != null)
                                ? docSnap.getLong("popularity")
                                : 0;
                        addon.thanksCount = (docSnap.getLong("thanksCount") != null)
                                ? docSnap.getLong("thanksCount")
                                : 0;
                        addon.title = (docSnap.getString("title") != null)
                                ? docSnap.getString("title")
                                : "";
                        addon.headerDescription = (docSnap.getString("headerDescription") != null)
                                ? docSnap.getString("headerDescription")
                                : "";
                        addon.headerIntro = (docSnap.getString("headerIntro") != null)
                                ? docSnap.getString("headerIntro")
                                : "";
                        addon.moreInformationLink = (docSnap.getString("moreInformationLink") != null)
                                ? docSnap.getString("moreInformationLink")
                                : "";
                        addon.linkedFactID = (docSnap.getString("linkedFactID") != null)
                                ? docSnap.getString("linkedFactID")
                                : "";
                        addon.headerTitle = (docSnap.getString("headerTitle") != null)
                                ? docSnap.getString("headerTitle")
                                : "";
                        assert data != null;
                        addon.itemOrder = ( ArrayList<String>) data.get("itemOrder");
                        addon.community = community;
                        addon.addonID = docSnap.getId();
                        addons.add(addon);
                    }
                    fetchFinished();
                }else if(task.isCanceled()){
                    System.out.println("!");
                }
            }
        });
    }

    public void fetchFinished(){
        this.recyclerView = getView().findViewById(R.id.community_items_recyclerView);
        this.adapter = new Community_Addons_Adapter(addons,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}


