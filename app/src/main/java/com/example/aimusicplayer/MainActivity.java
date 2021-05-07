package com.example.aimusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout parentRelativeLayout;
    SpeechRecognizer speechRecognizer;
    Intent speechRecognizerIntent;
    String keeper = "",mode = "ON";
    ImageView pause_play,previous,next,imageView;
    Button voice_enable;
    TextView songnametxt;
    RelativeLayout lowerlayout;
    MediaPlayer  mymedia;
    ArrayList<File> mySongs;
    int position;
    String mSongName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkpermission();
        pause_play = (ImageView) findViewById(R.id.play_pause_btn);
        previous = (ImageView) findViewById(R.id.previous_btn);
        next = (ImageView) findViewById(R.id.next_btn);
        imageView = (ImageView) findViewById(R.id.logo);
        voice_enable=(Button) findViewById(R.id.voice_enable_btn);
        songnametxt = (TextView) findViewById(R.id.songName);
        lowerlayout = findViewById(R.id.lower);

        parentRelativeLayout = findViewById(R.id.parentRelativeLayout);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());



        voice_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals("ON"))
                {
                    mode="OFF";
                    voice_enable.setText("Voice Enable Mode - OFF");
                    lowerlayout.setVisibility(View.VISIBLE);
                }
                else {
                    mode="ON";
                    voice_enable.setText("Voice Enable Mode - ON");
                    lowerlayout.setVisibility(View.GONE);

                }
            }
        });

        validateReceivedAndStartPlaying();
        imageView.setBackgroundResource(R.drawable.logo);


        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matchesfifound = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                if(matchesfifound!=null)
                {
                    keeper = matchesfifound.get(0);
                    Toast.makeText(MainActivity.this, "Result = "+keeper, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        parentRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecognizerIntent);
                        keeper="";
                        break;
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        break;
                }
                return  false;
            }
        });
    }
    private void  checkpermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(!(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED))
            {
              Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,Uri.parse("package:" + getPackageName()));
              startActivity(intent);
              finish();
            }


        }
    }
private void validateReceivedAndStartPlaying(){
        if(mymedia != null)
        {
            mymedia.stop();
            mymedia.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mySongs=(ArrayList) bundle.getParcelableArrayList("song");
        mSongName = mySongs.get(position).getName();
        String SongName = i.getStringExtra("name");
        songnametxt.setText(SongName);
        songnametxt.setSelected(true);

        position = bundle.getInt("position",0);
        Uri uri= Uri.parse(mySongs.get(position).toString());
        mymedia = MediaPlayer.create(MainActivity.this,uri);
        mymedia.start();
}
}
