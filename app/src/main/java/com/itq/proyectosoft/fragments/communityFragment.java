package com.itq.proyectosoft.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.itq.proyectosoft.R;
import com.itq.proyectosoft.activities.postActivity;
import com.itq.proyectosoft.adapters.PostsAdapter;
import com.itq.proyectosoft.models.Posts;
import com.itq.proyectosoft.providers.PostProvider;

public class communityFragment extends Fragment {

    View mView;
    FloatingActionButton mFab;
    RecyclerView mRecyclerView_Comunity;
    PostProvider mPostProvider_posts_comunity;
    PostsAdapter mPostsAdapter_community;
    public communityFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_community, container, false);
        mFab = mView.findViewById(R.id.fab_new_post);
        mRecyclerView_Comunity = mView.findViewById(R.id.recycler_view_posts);
        mPostProvider_posts_comunity = new PostProvider();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView_Comunity.setLayoutManager(linearLayoutManager);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPost();
            }
        });
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider_posts_comunity.getAll();
        FirestoreRecyclerOptions<Posts> options =
                new FirestoreRecyclerOptions.Builder<Posts>()
                        .setQuery(query, Posts.class)
                        .build();
        mPostsAdapter_community = new PostsAdapter(options, getContext());
        mRecyclerView_Comunity.setAdapter(mPostsAdapter_community);
        mPostsAdapter_community.startListening();
    }

    public void onStop() {
        super.onStop();
        mPostsAdapter_community.stopListening();
    }

    private void goToPost() {
        try {
            Intent intent = new Intent(getContext(), postActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("postActivityError", "Error al abrir postActivity", e);
            Toast.makeText(getActivity(), "Error al abrir postActivity", Toast.LENGTH_SHORT).show();
        }
    }
}