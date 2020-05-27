package com.example.myapplication.mytiktok.viewpager2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.myapplication.mytiktok.R;
import com.example.myapplication.mytiktok.gson_internet.VideoMessage;
import com.like.LikeButton;

import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {

    private static final String TAG="Retrofit test";
    public int currentpostion;
    public List<VideoMessage> messageList;
    public GestureDetector mgesture;
    public VideoView videoView;
    public boolean Videostate=true;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        Intent intent=this.getIntent();
        messageList=(List<VideoMessage>)intent.getSerializableExtra("key");
        Log.d(TAG,"get json success:" + messageList.toString());
        mgesture=new GestureDetector(this,new GestureListner());
        viewPagerAdapter=new ViewPagerAdapter(this,messageList,mgesture);
        viewPager2=findViewById(R.id.viewpager);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentpostion=viewPager2.getCurrentItem();
                videoView=viewPagerAdapter.pagerViewHolderList.get(currentpostion).videoView;
                videoView.setVisibility(View.VISIBLE);
                videoView.start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                currentpostion=viewPager2.getCurrentItem();
                videoView=viewPagerAdapter.pagerViewHolderList.get(currentpostion).videoView;
                switch (state) {
                    case ViewPager2.SCROLL_STATE_DRAGGING:
                        videoView.pause();
                        break;
                    case ViewPager2.SCROLL_STATE_IDLE:
                        break;
                    case ViewPager2.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
        mgesture.setOnDoubleTapListener(new GestureListner());
    }



    class GestureListner extends GestureDetector.SimpleOnGestureListener{


        public GestureListner() {
            super();
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            LikeButton likeButton=viewPagerAdapter.pagerViewHolderList.get(currentpostion).likeButton;
            if (likeButton.isLiked())
            {
                likeButton.setLiked(false);
                currentpostion=viewPager2.getCurrentItem();
                int nums=messageList.get(currentpostion).getLikeCount();
                nums=nums-1;
                messageList.get(currentpostion).setLikeCount(nums);
                viewPagerAdapter.pagerViewHolderList.get(currentpostion).likenum.setText(TransformLikenum(nums));
            }
            else
            {
                likeButton.setLiked(true);
                currentpostion=viewPager2.getCurrentItem();
                int nums=messageList.get(currentpostion).getLikeCount();
                nums=nums+1;
                messageList.get(currentpostion).setLikeCount(nums);
                viewPagerAdapter.pagerViewHolderList.get(currentpostion).likenum.setText(TransformLikenum(nums));
            }
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            ImageView pauseimage=viewPagerAdapter.pagerViewHolderList.get(currentpostion).pause;
            videoView=viewPagerAdapter.pagerViewHolderList.get(currentpostion).videoView;
            if(Videostate)
            {
                videoView.pause();
                pauseimage.setVisibility(View.VISIBLE);
                Videostate=!Videostate;
            }
            else
            {
                videoView.start();
                pauseimage.setVisibility(View.INVISIBLE);
                Videostate=!Videostate;
            }
            return super.onSingleTapConfirmed(e);
        }
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

}
