import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Random;

public class SoundManager {
    String[] soundEffects = {"explosion_01.wav", "explosion_02.wav","explosion_03.wav"};
    Random rnd = new Random();

    public void playBrickSound(){
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(soundEffects[rnd.nextInt(0,3)]));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
