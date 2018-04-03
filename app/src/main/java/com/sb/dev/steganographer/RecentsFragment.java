package com.sb.dev.steganographer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecentsFragment extends Fragment
    {
        private RecyclerView mRecyclerView;
        private RecyclerView.LayoutManager mLayoutManager;
        private RecyclerView.Adapter mAdapter;
        private String[] data;
        public RecentsFragment()
            {

            }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
            {

                View view=inflater.inflate(R.layout.layout_recents, container, false);

                mRecyclerView=view.findViewById(R.id.recycler_cards);

                mLayoutManager=new LinearLayoutManager(this.getActivity());
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter=new RecentsAdapter(data);
                mRecyclerView.setAdapter(mAdapter);

                return view;
            }

    }
