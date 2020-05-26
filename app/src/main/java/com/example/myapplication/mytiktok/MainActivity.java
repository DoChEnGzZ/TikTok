package com.example.myapplication.mytiktok;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.mytiktok.gson_internet.RetrofitVideo;
import com.example.myapplication.mytiktok.gson_internet.VideoMessage;
import com.example.myapplication.mytiktok.viewpager2.ViewPagerActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;
import java.net.URL;



public class MainActivity extends AppCompatActivity {

    private static final String TAG="download test";
    private static final int FILE_EXIST=1;
    private static final int FILE_DOWNLOAD_FAILED=0;
    private static final int FILE_DOWNLOAD_SUCCESS=2;
    public float TimePiece;
    public LottieAnimationView lottie;
    public List<VideoMessage> messageList;
    public List<VideoMessage> textlist;
    public Intent intent;
    public Bundle bundle;
    public Toast toast;
    public DownLoadThread downLoadThread=new DownLoadThread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bundle=new Bundle();
        lottie=(LottieAnimationView) findViewById(R.id.progress);
        intent=new Intent(this,ViewPagerActivity.class);
        RetrofitVideo retrofitVideo=retrofit.create(RetrofitVideo.class);
        retrofitVideo.getvideo().enqueue(new Callback<List<VideoMessage>>() {
            @Override
            public void onResponse(Call<List<VideoMessage>> call, Response<List<VideoMessage>> response) {
                messageList=response.body();
//                Log.d(TAG,"get json success:" + messageList.toString());
                if (messageList != null) {
                    TimePiece=1f/(2*messageList.size());
                }
                bundle.putSerializable("key",(Serializable) messageList);
                intent.putExtras(bundle);
                try {
                    downLoadThread.start();
                    downLoadThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                toast=Toast.makeText(getApplicationContext(),"加载成功",Toast.LENGTH_LONG);
               toast.show();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<VideoMessage>> call, Throwable t) {
//                Log.d(TAG,"get json fail" + t.getMessage());
               toast=Toast.makeText(getApplicationContext(),"获取视频信息失败！！！"+t.getMessage(),Toast.LENGTH_LONG);
               toast.show();
            }
        });

    }

    public int downloadmp4(VideoMessage videoMessage)  {
        String Mp4Dirname= this.getExternalFilesDir(null)+"/mp4/";
        File mp4dir=new File(Mp4Dirname);
        if(!mp4dir.exists())
        {
            mp4dir.mkdir();
        }
        File mp4=new File(Mp4Dirname+videoMessage.getVideoName());
        if(mp4.exists())
        {
            mp4.delete();
            Log.d(TAG,mp4+"已经存在，删除旧文件");
        }
        byte[] bytes=new byte[1024];
        int len;
        try {
            URL url=new URL(videoMessage.getVideoUrl());
            InputStream inputStream=url.openStream();
            OutputStream outputStream=new FileOutputStream(mp4);
            while((len=inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
            }
            inputStream.close();
            outputStream.close();
            return FILE_DOWNLOAD_SUCCESS;
        }catch(MalformedURLException e){
            mp4 = null;
            Log.d(TAG,"URL对象创建失败"+e.getMessage());
            return FILE_DOWNLOAD_FAILED;
        }catch(FileNotFoundException e){
            mp4 = null;
            Log.d(TAG,"文件加载失败"+e.getMessage());
            return FILE_DOWNLOAD_FAILED;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"IO操作失败"+e.getMessage());
            return FILE_DOWNLOAD_FAILED;
        }
    }

    public int downloadpic(VideoMessage videoMessage)
    {
        String PicDirname=this.getExternalFilesDir(null)+"/pic/";
        File picdir=new File(PicDirname);
        if(!picdir.exists())
        {
            picdir.mkdir();
        }
        File pic=new File(PicDirname+videoMessage.getPicName());
        if(pic.exists())
        {
            pic.delete();
            Log.d(TAG,pic+"已经存在，删除旧文件");

        }
        byte[] bytes=new byte[1024];
        int len;
        try {
            URL url=new URL(videoMessage.getAvatorUrl());
            InputStream inputStream=url.openStream();
            OutputStream outputStream=new FileOutputStream(pic);
            while((len=inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
            }
            inputStream.close();
            outputStream.close();
            return FILE_DOWNLOAD_SUCCESS;
        }catch(MalformedURLException e){
            pic = null;
            Log.d(TAG,"URL对象创建失败"+e.getMessage());
            return FILE_DOWNLOAD_FAILED;
        }catch(FileNotFoundException e){
            pic = null;
            Log.d(TAG,"文件加载失败"+e.getMessage());
            return FILE_DOWNLOAD_FAILED;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"IO操作失败"+e.getMessage());
            return FILE_DOWNLOAD_FAILED;
        }
    }

    public class DownLoadThread extends Thread{
        @Override
        public void run() {
            super.run();
            float count_download=0;
            for(int i=0;i<messageList.size();i++)
            {
                switch (downloadmp4(messageList.get(i))){
                    case FILE_EXIST:
                        Log.d(TAG,"视频已经存在："+messageList.get(i).getVideoUrl());
                        break;
                    case FILE_DOWNLOAD_SUCCESS:
                        Log.d(TAG,"视频下载成功："+messageList.get(i).getVideoUrl());
                        count_download=count_download+1;
                        lottie.setProgress(0.5f);
                        break;
                    case FILE_DOWNLOAD_FAILED:
                        Log.d(TAG,"视频下载失败："+messageList.get(i).getVideoUrl());
                        break;
                }
                switch (downloadpic(messageList.get(i))){
                    case FILE_EXIST:
                        Log.d(TAG,"图片已经存在："+messageList.get(i).getAvatorUrl());
                        break;
                    case FILE_DOWNLOAD_SUCCESS:
                        Log.d(TAG,"图片下载成功："+messageList.get(i).getAvatorUrl());
                        count_download=count_download+1;
                        lottie.setProgress(count_download*TimePiece);
                        break;
                    case FILE_DOWNLOAD_FAILED:
                        Log.d(TAG,"图片下载失败："+messageList.get(i).getAvatorUrl());
                        break;
                }
            }
        }
    }


}
