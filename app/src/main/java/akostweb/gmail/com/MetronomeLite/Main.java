package akostweb.gmail.com.MetronomeLite;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;


public class Main extends Activity {

    private FirebaseAnalytics mFirebaseAnalytics;

    public static final int STEP = 50;
    public static final int CHANGE_PICTURE = 15;
    public static final int CHANGE_AXE = 41;

    public boolean change, changeaxe, spinnerChanged;
    public ImageView imageView;
    public TextView mTextValue;
    public boolean stopper = true;
    public Button btnPlay, btnStop;
    public SeekBar seekBar, seekBarChanger;
    public int nomer, vibor, time, times, stopTouch, tik;

    public SoundPlayer soundPlayer;


    String[] data = {"2/4", "3/4", "4/4", "5/4", "7/4", "3/8", "5/8", "6/8", "7/8", "9/8", "12/8"};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4750718940737434~5012194903");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        mTextValue = (TextView) findViewById(R.id.tvProgressSpin);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnStop = (Button) findViewById(R.id.btnStop);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row, R.id.spinnerColor, data);
        spinner.setAdapter(adapter);

        seekBarChanger = (SeekBar) findViewById(R.id.seekBarChanger);
        seekBarChanger.setProgress(0);

        imageView = (ImageView) findViewById(R.id.imageView);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                times = progress;
                mTextValue.setText(String.valueOf(seekBar.getProgress()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                stopTouch = seekBar.getProgress();

                if (stopTouch != 0) {
                    times = stopTouch;
                    mTextValue.setText(String.valueOf(times));
                } else {
                    times = stopTouch + 1;
                    mTextValue.setText(String.valueOf(times));
                }
            }
        });
        seekBar.setProgress(40);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                nomer = position;
                spinnerChanged = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void OnClick(View view) {
        soundPlayer = new SoundPlayer(this);
        switch (view.getId()) {
            case R.id.btnPlay:
                btnPlay.setBackgroundResource(R.drawable.woodenplaydisabled);
                btnStop.setBackgroundResource(R.drawable.woodenstnew);
                stopper = false;
                btnPlay.setEnabled(false);
                time = Takt(nomer);
                vibor = Vibor(nomer);

                final Handler handler = new Handler();
                final Runnable updateRunner = new Runnable() {
                    public void run() {
                        if (change) {
                            imageView.setBackgroundResource(R.drawable.hitred);
                        } else {
                            imageView.setBackgroundResource(R.drawable.hithit);
                        }
                    }
                };

                final Runnable updateAxe = new Runnable() {
                    @Override
                    public void run() {
                        if (changeaxe) {
                            seekBarChanger.setThumb(getResources().getDrawable(R.drawable.woodenaxelse));
                        } else {
                            seekBarChanger.setThumb(getResources().getDrawable(R.drawable.woodenaxezzz));
                        }


                    }
                };


                Thread thread = new Thread() {
                    public void run() {
                        tik = 0;
                        while (!stopper) {
                            if (spinnerChanged){
                                time = Takt(nomer);
                                vibor = Vibor(nomer);
                                spinnerChanged = false;
                            }

                            for (int j = 0; j <= STEP; j++) {

                                tik = tik + 1;
                                try {
                                    Thread.sleep(60000 / time / times / STEP);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                seekBarChanger.setProgress(tik);
                                if (stopper){
                                    j = STEP;
                                }
                                if (j == CHANGE_AXE) {
                                    changeaxe = true;
                                    handler.post(updateAxe);
                                }
                                if (j == CHANGE_PICTURE) {
                                    change = false;
                                    handler.post(updateRunner);
                                }
                                if (j == STEP) {
                                    change = true;
                                    handler.post(updateRunner);
                                    seekBarChanger.setProgress(0);
                                    tik = 0;

                                }

                            }
                            changeaxe = false;
                            handler.post(updateAxe);
                            soundPlayer.PlaySound(1);
                            if (spinnerChanged){
                                time = Takt(nomer);
                                vibor = Vibor(nomer);
                                spinnerChanged = false;
                            }


                            for (int i = 1; i < vibor; i++) {
                                if (spinnerChanged){
                                    time = Takt(nomer);
                                    vibor = Vibor(nomer);
                                    spinnerChanged = false;
                                }
                                if (stopper) {
                                    i = vibor;
                                } else {

                                    for (int j = 0; j <= STEP; j++) {

                                        tik = tik + 1;
                                        try {
                                            Thread.sleep(60000 / time / times / STEP);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        seekBarChanger.setProgress(tik);

                                        if (stopper){
                                            j = STEP;
                                        }


                                        if (j == CHANGE_AXE) {
                                            changeaxe = true;
                                            handler.post(updateAxe);
                                        }

                                        if (j == CHANGE_PICTURE) {
                                            change = false;
                                            handler.post(updateRunner);
                                        }

                                        if (j == STEP) {
                                            change = true;
                                            handler.post(updateRunner);
                                            seekBarChanger.setProgress(0);
                                            tik = 0;
                                        }

                                    }
                                }

                                soundPlayer.PlaySound(0);
                                changeaxe = false;
                                handler.post(updateAxe);
                                if (spinnerChanged){
                                    time = Takt(nomer);
                                    vibor = Vibor(nomer);
                                    spinnerChanged = false;
                                }
                            }
                        }
                    }
                };

                thread.start();
                break;

            case R.id.btnStop:
                btnStop.setBackgroundResource(R.drawable.woodenstopdisable);
                btnPlay.setBackgroundResource(R.drawable.woodenpl);
                btnPlay.setEnabled(true);
                stopper = true;
                soundPlayer.PlaySound(0);
                break;
        }

    }


    public int Takt(int nomer) {
        if (nomer <= 5) {
            time = 1;
        } else {
            time = 2;
        }
        return time;
    }

    public int Vibor(int nomer) {
        if (nomer <= 3) {
            vibor = nomer + 2;
        } else if (nomer == 4) {
            vibor = 7;
        } else if (nomer == 5) {
            vibor = 3;
        } else if (nomer >= 6 && nomer <= 8) {
            vibor = nomer - 1;
        } else if (nomer == 9) {
            vibor = nomer;
        } else {
            vibor = 12;
        }
        return vibor;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}




