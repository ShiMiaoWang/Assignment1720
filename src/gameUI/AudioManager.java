package gameUI;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Manages audio playback in the game
 */
public class AudioManager {
    private static AudioManager instance;
    private Clip backgroundMusic;
    private FloatControl volumeControl;
    private boolean isMuted = false;
    private float volume = 0.4f; // Default volume is 40%

    private AudioManager() {
        // Private constructor using singleton pattern
    }

    /**
     * Gets the singleton instance of AudioManager
     */
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    /**
     * Plays background music
     * @param resourcePath The resource path
     */
    public void playBackgroundMusic(String resourcePath) {
        try {
            // Stop currently playing music
            if (backgroundMusic != null && backgroundMusic.isOpen()) {
                backgroundMusic.stop();
                backgroundMusic.close();
            }

            // Load audio file
            InputStream audioSrc = getClass().getResourceAsStream(resourcePath);
            if (audioSrc == null) {
                System.err.println("Cannot find audio file: " + resourcePath);
                return;
            }

            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);

            // Create player
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);

            // Set volume
            if (backgroundMusic.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(volume);
            }

            // Loop playback
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

            // Start playback
            if (!isMuted) {
                backgroundMusic.start();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sets volume (value between 0.0 and 1.0)
     * @param volume Volume value, 0.0 (mute) to 1.0 (maximum volume)
     */
    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f) {
            throw new IllegalArgumentException("Volume must be between 0.0 and 1.0");
        }

        this.volume = volume;

        if (volumeControl != null) {
            // Convert value between 0-1 to dB value
            float gain = (volume == 0.0f) ? volumeControl.getMinimum() :
                    (float) (20.0 * Math.log10(volume));

            // Ensure within control range
            gain = Math.max(volumeControl.getMinimum(), Math.min(volumeControl.getMaximum(), gain));

            volumeControl.setValue(gain);
        }
    }

    /**
     * Gets current volume
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Mute or unmute
     * @param mute Whether to mute
     */
    public void setMuted(boolean mute) {
        this.isMuted = mute;

        if (backgroundMusic != null && backgroundMusic.isOpen()) {
            if (mute) {
                backgroundMusic.stop();
            } else {
                backgroundMusic.start();
            }
        }
    }

    /**
     * Stops background music
     */
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isOpen()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }
}