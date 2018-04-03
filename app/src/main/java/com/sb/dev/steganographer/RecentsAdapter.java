package com.sb.dev.steganographer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.MyViewHolder>
    {
        private String[] mStrings;

        public static class MyViewHolder extends RecyclerView.ViewHolder
            {
                public TextView text;
                public ImageView image,overflow;
                public MyViewHolder(View view)
                    {
                        super(view);
                        image=(ImageView)view.findViewById(R.id.card_text);
                        text=(TextView)view.findViewById(R.id.card_text);
                        overflow=(ImageButton)view.findViewById(R.id.card_overflow);

                    }
            }

        public RecentsAdapter(String [] data)
            {
                mStrings=data;
            }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                TextView textView=(TextView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recents_cards,parent,false);
                MyViewHolder viewHolder=new MyViewHolder(textView);

                return viewHolder;
            }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
            {

            }

        @Override
        public int getItemCount()
            {

                return mStrings.length;
            }
    }
