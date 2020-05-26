package com.example.myapplication.mytiktok.viewpager2;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.mytiktok.R;
import com.example.myapplication.mytiktok.gson_internet.VideoMessage;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PagerViewHolder extends RecyclerView.ViewHolder {
    public LikeButton likeButton;
    public ImageView share;
    public ImageView comment;
    public TextView sharenum;
    public TextView commentnum;
    public TextView likenum;
    public CircleImageView avator;
    public TextView nickname;
    public TextView description;
    public VideoView videoView;
    public ImageView pause;
    public ImageView f1pic;
    public Uri VideoPath;
    private MediaMetadataRetriever retriever=new MediaMetadataRetriever();
    private Bitmap bitmap;

    private Uri AvatorPath;


    public PagerViewHolder(@NonNull View itemView) {
        super(itemView);
        likeButton = itemView.findViewById(R.id.likeBtn);
        share = itemView.findViewById(R.id.iv_share);
        comment = itemView.findViewById(R.id.iv_comment);
        sharenum = itemView.findViewById(R.id.tv_share);
        commentnum = itemView.findViewById(R.id.tv_comment);
        likenum = itemView.findViewById(R.id.tv_like);
        avator = itemView.findViewById(R.id.ci_avator);
        nickname = itemView.findViewById(R.id.tv_nickname);
        description = itemView.findViewById(R.id.tv_description);
        pause=itemView.findViewById(R.id.iv_pause);
        videoView=itemView.findViewById(R.id.vv);
        f1pic=itemView.findViewById(R.id.iv_1framepic);
    }

    public void Bind(final VideoMessage message, Context context, final GestureDetector mgesture) throws MalformedURLException {
        VideoPath=Uri.parse(message.getVideoUrl());
        likenum.setText(TransformLikenum(message.getLikeCount()));
        nickname.setText("@"+message.getNickName());
        description.setText(message.getDesCripTion());
        VideoPath=Uri.parse("file:///"+context.getExternalFilesDir(null)+"/mp4/"+message.getVideoName());
        AvatorPath=Uri.parse("file:///"+context.getExternalFilesDir(null)+"/pic/"+message.getPicName());
        avator.setImageURI(AvatorPath);
        videoView.setVideoURI(VideoPath);
        retriever.setDataSource(context.getExternalFilesDir(null)+"/mp4/"+message.getVideoName());
        bitmap=retriever.getFrameAtTime(0,MediaMetadataRetriever.OPTION_CLOSEST);
        Glide.with(videoView)
                .load(bitmap)
                .into(f1pic);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickAnimator(share);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickAnimator(comment);
            }
        });
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mgesture.onTouchEvent(event);
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        f1pic.setVisibility(View.INVISIBLE);
                        return true;
                    }
                });
            }
        });
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                int nums=message.getLikeCount();
                nums=nums+1;
                message.setLikeCount(nums);
                likenum.setText(TransformLikenum(nums));


            }

            @Override
            public void unLiked(LikeButton likeButton) {
                int nums=message.getLikeCount();
                nums=nums-1;
                message.setLikeCount(nums);
                likenum.setText(TransformLikenum(nums));
            }
        });;
    }

    private void ClickAnimator(ImageView imageView)
    {
        ObjectAnimator scalex=ObjectAnimator.ofFloat(imageView,"scaleX",0.8f,1.2f);
        ObjectAnimator scaley=ObjectAnimator.ofFloat(imageView,"scaleY",0.8f,1.2f);
        ObjectAnimator scalex2=ObjectAnimator.ofFloat(imageView,"scaleX",1.2f,1f);
        ObjectAnimator scaley2=ObjectAnimator.ofFloat(imageView,"scaleY",1.2f,1f);
        AnimatorSet animatorSet=new AnimatorSet();
        AnimatorSet animatorSet2=new AnimatorSet();
        animatorSet.playTogether(scalex,scaley);
        animatorSet2.playTogether(scalex2,scaley2);
        animatorSet.setDuration(200);
        animatorSet2.setDuration(200);
        animatorSet.start();
        animatorSet2.start();
    }

    private String TransformLikenum(int num)
    {
        int likenum=num;
        if(num/1000!=0)
        {
            likenum=likenum/1000;
            return likenum+"k";
        }
        else
            return String.valueOf(num);

    }




    @Override
    public String toString() {
        return "PagerViewHolder{" +
                "likeButton=" + likeButton +
                ", share=" + share +
                ", comment=" + comment +
                ", sharenum=" + sharenum +
                ", commentnum=" + commentnum +
                ", likenum=" + likenum +
                ", avator=" + avator +
                '}';
    }
}
