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
    private boolean isMuted;
    private float volume;

    // Private constructor (for singleton pattern)
    private AudioManager() {
        // Initialize default values
        this.isMuted = false;
        this.volume = 0.4f; // Default volume is 40%
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

            // Check if file exists
            if (audioSrc == null) {
                System.out.println("Cannot find audio file: " + resourcePath);
                return;
            }

            // Create buffered input stream for better performance
            InputStream bufferedIn = new BufferedInputStream(audioSrc);

            // Create audio input stream
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);

            // Create player
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);

            // Set volume if control is supported
            boolean hasVolumeControl = backgroundMusic.isControlSupported(FloatControl.Type.MASTER_GAIN);
            if (hasVolumeControl) {
                volumeControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(volume);
            }

            // Loop playback forever
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

            // Start playback if not muted
            if (!isMuted) {
                backgroundMusic.start();
            }
        } catch (Exception e) {
            // Print error message if anything goes wrong
            System.out.println("Error playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sets volume (value between 0.0 and 1.0)
     * @param volume Volume value, 0.0 (mute) to 1.0 (maximum volume)
     */
    public void setVolume(float volume) {
        // Check if volume is in valid range
        if (volume < 0.0f) {
            System.out.println("Volume too low, setting to 0.0");
            volume = 0.0f;
        }

        if (volume > 1.0f) {
            System.out.println("Volume too high, setting to 1.0");
            volume = 1.0f;
        }

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
            float minGain = volumeControl.getMinimum();
            float maxGain = volumeControl.getMaximum();

            if (gain < minGain) {
                gain = minGain;
            }

            if (gain > maxGain) {
                gain = maxGain;
            }

            // Apply the volume
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

        // Apply mute setting if music is playing
        if (backgroundMusic != null) {
            if (backgroundMusic.isOpen()) {
                if (mute) {
                    // Stop playback if muted
                    backgroundMusic.stop();
                } else {
                    // Resume playback if unmuted
                    backgroundMusic.start();
                }
            }
        }
    }

    /**
     * Stops background music and releases resources
     */
    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            if (backgroundMusic.isOpen()) {
                backgroundMusic.stop();
                backgroundMusic.close();
            }
        }
    }
}