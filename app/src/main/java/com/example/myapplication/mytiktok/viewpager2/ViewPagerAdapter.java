package com.example.myapplication.mytiktok.viewpager2;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.mytiktok.R;
import com.example.myapplication.mytiktok.gson_internet.VideoMessage;
import com.google.android.exoplayer.C;
import com.like.LikeButton;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import retrofit2.http.POST;

public class ViewPagerAdapter extends RecyclerView.Adapter<PagerViewHolder> {

    public int PagerNums;
    private int Postion;
    List<VideoMessage> messageList;
    List<PagerViewHolder> pagerViewHolderList=new ArrayList<>();
    private Context context;
    private GestureDetector gesture;

    public ViewPagerAdapter(Context context, List<VideoMessage> messageList, GestureDetector gesture) {
        PagerNums=messageList.size();
        this.messageList=messageList;
        this.context=context;
        this.gesture=gesture;
    }

    @NonNull
    @Override
    public PagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.pager_view,parent,false);
        return new PagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagerViewHolder holder, int position) {

        try {
            holder.Bind(messageList.get(position),context,gesture);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.Postion=position;
        pagerViewHolderList.add(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull PagerViewHolder holder) {
        if(Postion==0)
        {
            holder.pause.setVisibility(View.INVISIBLE);
            holder.f1pic.setVisibility(View.INVISIBLE);
            holder.videoView.setVisibility(View.VISIBLE);
            holder.videoView.start();
        }
        else{
            holder.f1pic.setVisibility(View.VISIBLE);
            holder.pause.setVisibility(View.INVISIBLE);
            holder.videoView.setVisibility(View.INVISIBLE);
        }

        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return PagerNums;
    }
}
