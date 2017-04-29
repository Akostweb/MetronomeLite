package akostweb.gmail.com.MetronomeLite;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by Администратор on 07.02.2017.
 */

public class SoundPlayer  extends Activity{

    private SoundPool soundPool;
    private int soundID1;
    private int soundID2;

    SoundPlayer(Context myContext){
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        soundID1 = soundPool.load(myContext, R.raw.second, 1);
        soundID2 = soundPool.load(myContext, R.raw.first, 2);

    }

    public void PlaySound(int sound){
        if (sound == 1){
            soundPool.play(soundID1, 0.9f, 0.9f, 1, 0, 0);
        } else {
            soundPool.play(soundID2, 0.9f, 0.9f, 1, 0, 0);
        }




    }

}
