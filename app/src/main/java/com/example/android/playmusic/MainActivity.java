package com.example.android.playmusic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.playmusic.SongAdapter;


public class MainActivity extends AppCompatActivity {
    //private MediaPlayer mMediaPlayer;

    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound=false;
   private static final int MY_PERMISSION_REQUEST=1;
     ArrayList<Song> SongList;
    ListView listview;
    SongAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
       {
           if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
           {
               ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
           }
           else
               {
                   ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
           }
       }
       else{
           doStuff();
       }
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setList(SongList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }


    public void doStuff()
    {
        listview = (ListView)findViewById(R.id.song_list);
        SongList = new ArrayList<Song>();
        getSongs();
        adapter = new SongAdapter(this,SongList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("hh","f");
                Song mysong= SongList.get(position);
                String songname= mysong.getSongName();
                Log.e("0","0");
                DetailFragment detailFragment= new DetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("songname",songname);
                Log.e("1","1");
                detailFragment.setArguments(bundle);
                FragmentManager manager= getSupportFragmentManager();
                Log.e("2","2");
                manager.beginTransaction().replace(R.id.detailfragment,detailFragment).commit();
                Log.e("3","3");
                songPicked(view,position);

            }
        });



    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicService=null;
        super.onDestroy();
    }
    public void getSongs() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);


        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns

            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //int albumColumn= musicCursor.getColumnIndex
                  //  (MediaStore.Audio.Albums.ALBUM_ART);
            //add songs to list
            do {

                int thisId = musicCursor.getInt(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);

               SongList.add(new Song(thisTitle,thisArtist,thisId));
            }
            while (musicCursor.moveToNext());
        }

    }


    public void onRequestPermissionResult(int requestCode,String[] permission,int[] grantResults)
    {
        switch(requestCode)
        {
            case MY_PERMISSION_REQUEST:
            {
                if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(this,"PERMISSION GRANTED",Toast.LENGTH_SHORT).show();
                        doStuff();
                    }
                    else
                    {
                        Toast.makeText(this," NO PERMISSION GRANTED",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    return;
                }
            }
        }
    }

    public void songPicked(View view,int position)
    {
        musicService.setSong(Integer.parseInt(view.getTag().toString()));
        musicService.playMethod();

    }


}
