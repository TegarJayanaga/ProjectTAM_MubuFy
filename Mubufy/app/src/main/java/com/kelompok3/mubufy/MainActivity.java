package com.kelompok3.mubufy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView play, prev, next, imageView;
    TextView songTitle;
    SeekBar mSeekBarTime, mSeekBarVol;
    static MediaPlayer mMediaPlayer;
    private Runnable runnable;
    private AudioManager mAudioManager;
    int currentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //  views

        play = findViewById(R.id.play);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        songTitle = findViewById(R.id.songTitle);
        imageView = findViewById(R.id.imageView);
        mSeekBarTime = findViewById(R.id.seekBarTime);
        mSeekBarVol = findViewById(R.id.seekBarVol);

        // Membuat ArrayList untuk menampung lagu

        final ArrayList<Integer> songs = new ArrayList<>();

        songs.add(0, R.raw.homura);
        songs.add(1, R.raw.eula);
        songs.add(2, R.raw.ayasa);
        songs.add(3, R.raw.higurashi);
        songs.add(4, R.raw.mio);


        // menginisialisasi mediaplayer

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));



        // seekbar untuk mengatur volume

        int maxV = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curV = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSeekBarVol.setMax(maxV);
        mSeekBarVol.setProgress(curV);

        mSeekBarVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //tombol diatas seekbar volume

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    play.setImageResource(R.drawable.play_btn);
                } else {
                    mMediaPlayer.start();
                    play.setImageResource(R.drawable.pause_btn);
                }

                songNames();

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer != null) {
                    play.setImageResource(R.drawable.pause_btn);
                }

                if (currentIndex < songs.size() - 1) {
                    currentIndex++;
                } else {
                    currentIndex = 0;
                }

                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }

                mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));

                mMediaPlayer.start();
                songNames();

            }
        });


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMediaPlayer != null) {
                    play.setImageResource(R.drawable.pause_btn);
                }

                if (currentIndex > 0) {
                    currentIndex--;
                } else {
                    currentIndex = songs.size() - 1;
                }
                assert mMediaPlayer != null;
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }

                mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                mMediaPlayer.start();
                songNames();

            }
        });

    }

    private void songNames() {
        if (currentIndex == 0) {
            songTitle.setText("Homura");
            imageView.setImageResource(R.drawable.homura);
        }
        if (currentIndex == 1) {
            songTitle.setText("Eula OST");
            imageView.setImageResource(R.drawable.eula);
        }
        if (currentIndex == 2) {
            songTitle.setText("Ayasa");
            imageView.setImageResource(R.drawable.ayasa);
        }
        if (currentIndex == 3) {
            songTitle.setText("Higurashi");
            imageView.setImageResource(R.drawable.higurashi);
        }
        if (currentIndex == 4) {
            songTitle.setText("Mio Honda");
            imageView.setImageResource(R.drawable.mio);
        }


        // seekbar untuk mengatur lagu
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                mMediaPlayer.start();
            }
        });

        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayer.seekTo(progress);
                    mSeekBarTime.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null) {
                    try {
                        if (mMediaPlayer.isPlaying()) {
                            Message message = new Message();
                            message.what = mMediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @SuppressLint("Handler Leak") Handler handler = new Handler () {
        @Override
        public void handleMessage  (Message msg) {
            mSeekBarTime.setProgress(msg.what);
        }
    };
}
