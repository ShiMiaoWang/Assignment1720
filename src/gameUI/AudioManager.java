package gameUI;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Manages audio playback in the game
 * Uses singleton pattern (only one instance)
 */
public class AudioManager {
    // The single instance of AudioManager
    private static AudioManager instance;

    // Audio playback objects
    private Clip backgroundMusic;
    private FloatControl volumeControl;

    // Audio state
    private float volume;

    // Private constructor (for singleton pattern)
    private AudioManager() {
        // Initialize default value
        this.volume = 0.05f; // Default volume is 5%
    }

    /**
     * Gets the singleton instance of AudioManager
     */
    public static AudioManager getInstance() {
        // Create instance if it doesn't exist
        if (instance == null) {
            instance = new AudioManager();
        }

        // Return the instance
        return instance;
    }

    /**
     * Plays background music
     * @param resourcePath The path to the audio file
     */
    public void playBackgroundMusic(String resourcePath) {
        try {
            // Stop currently playing music
            if (backgroundMusic != null) {
                if (backgroundMusic.isOpen()) {
                    backgroundMusic.stop();
                    backgroundMusic.close();
                }
            }

            // Load audio file
            InputStream audioSrc = getClass().getResourceAsStream(resourcePath);

            // Create buffered input stream for better performance
            InputStream bufferedIn = new BufferedInputStream(audioSrc);

            // Create audio input stream
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);

            // Create player
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);

            // Set volume if control is supported
            if (backgroundMusic.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(volume);
            }

            // Loop playback forever
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

            // Start playback
            backgroundMusic.start();
        } catch (Exception e) {
            // Print error message if anything goes wrong
            System.out.println("Error playing background music: " + e.getMessage());
        }
    }

    /**
     * Sets volume (value between 0.0 and 1.0)
     * @param volume Volume value, 0.0 (mute) to 1.0 (maximum volume)
     */
    public void setVolume(float volume) {
        // Save the volume value
        this.volume = volume;

        // Apply volume if control is available
        if (volumeControl != null) {
            // Convert value between 0-1 to decibels
            float gain;

            if (volume == 0.0f) {
                // Special case for zero volume
                gain = volumeControl.getMinimum();
            } else {
                // Normal case - convert to decibels
                gain = (float) (20.0 * Math.log10(volume));
            }

            // Ensure within control range
            if (gain < volumeControl.getMinimum()) {
                gain = volumeControl.getMinimum();
            }
            if (gain > volumeControl.getMaximum()) {
                gain = volumeControl.getMaximum();
            }

            // Apply the volume
            volumeControl.setValue(gain);
        }
    }
}