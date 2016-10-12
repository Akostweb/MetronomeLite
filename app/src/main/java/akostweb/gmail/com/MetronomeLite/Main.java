package akostweb.gmail.com.MetronomeLite;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;



public class Main extends Activity {


    public TextView mTextValue;
    public boolean stopper = true;
    public boolean loaded;
    public Button btnPlay, btnStop;
    public SeekBar seekBar;
    public int nomer, vibor, time, times;

    private static final String TAG = "MyTest";

    public SoundPool sp;
    public AudioManager am;
    private static final int MAX_STREAMS = 1;
    private static final int streamType = AudioManager.STREAM_MUSIC;
    int soundShort, soundSmall;
    private float volume;

    String[] data = {"2/4", "3/4", "4/4", "5/4", "7/4", "3/8", "5/8", "6/8", "7/8", "9/8", "12/8"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);


        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        float currentVolumeIndex = (float) am.getStreamVolume(streamType);
        float maxVolumeIndex = (float) am.getStreamMaxVolume(streamType);
        this.volume = currentVolumeIndex / maxVolumeIndex;
        this.setVolumeControlStream(streamType);
        this.sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 1);
        soundShort = this.sp.load(this, R.raw.aaaa, 1);
        soundSmall = this.sp.load(this, R.raw.ddddddd, 1);

        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
                Log.i(TAG, "Загруженно");

            }
        });

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setEnabled(false);
                if (times == 0) {
                    times = Integer.parseInt(mTextValue.getText().toString()) + 1;
                } else {
                    times = Integer.parseInt(mTextValue.getText().toString());
                }

                final float leftVolumn = volume;
                final float rightVolumn = volume;
                vibor = 0;

                if (nomer == 0) {
                    vibor = 2;
                    time = 1;
                } else if (nomer == 1) {
                    vibor = 3;
                    time = 1;
                } else if (nomer == 2) {
                    vibor = 4;
                    time = 1;
                } else if (nomer == 3) {
                    vibor = 5;
                    time = 1;
                } else if (nomer == 4) {
                    vibor = 7;
                    time = 1;
                } else if (nomer == 5) {
                    vibor = 3;
                    time = 2;
                } else if (nomer == 6) {
                    vibor = 5;
                    time = 2;
                } else if (nomer == 7) {
                    vibor = 6;
                    time = 2;
                } else if (nomer == 8) {
                    vibor = 7;
                    time = 2;
                } else if (nomer == 9) {
                    vibor = 9;
                    time = 2;
                } else {
                    vibor = 12;
                    time = 2;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (times == 0) {
                            times = times + 1;
                        }
                        stopper = true;
                        do {
                            if(!stopper){

                            }else {
                                sp.play(soundShort, leftVolumn, rightVolumn, 1, 0, 1);
                                try {
                                    Thread.sleep(60000 / times / time);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (!stopper) {
                            } else {
                                for (int i = 1; i < vibor; i++) {
                                    sp.play(soundSmall, leftVolumn, rightVolumn, 1, 0, 1);
                                    try {
                                        Thread.sleep(60000 / times / time);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } while (stopper);
                    }
                }).start();
            }
        });

        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setEnabled(true);
                stopper = false;
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row, R.id.spinnerColor, data);
        spinner.setAdapter(adapter);


        mTextValue = (TextView) findViewById(R.id.textView2);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTextValue.setText(String.valueOf(seekBar.getProgress()));
                times = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mTextValue.setText(String.valueOf(seekBar.getProgress()));
            }
        });
        seekBar.setProgress(40);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                nomer = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

}




