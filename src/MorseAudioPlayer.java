import javax.sound.sampled.*;
import javax.swing.JOptionPane; // Used for showing errors if audio line is unavailable

public class MorseAudioPlayer {

    // --- Audio Configuration ---
    private static final float SAMPLE_RATE = 44100; // Standard audio sample rate (samples per second)
    private static final int BITS_PER_SAMPLE = 16;  // 16-bit audio
    private static final int CHANNELS = 1;          // Mono audio
    private static final boolean SIGNED = true;     // Signed PCM audio
    private static final boolean BIG_ENDIAN = false; // Little-endian (standard for Java's AudioSystem default)

    private static final int FREQUENCY_HZ = 700;    // The frequency of the Morse tone (700 Hz is a common choice)

    // --- Morse Timing Units ---
    // All Morse timings are relative to a 'unit' duration.
    // - Dot (Dit) duration: 1 unit
    // - Dash (Dah) duration: 3 units
    // - Inter-element gap (between dot/dash within a character): 1 unit
    // - Inter-character gap (between characters within a word): 3 units
    // - Inter-word gap (between words): 7 units

    private final int unitDurationMs; // The base unit duration in milliseconds (controls playback speed)

    /**
     * Constructor for the MorseAudioPlayer.
     * @param unitDurationMs The duration of one Morse 'unit' in milliseconds.
     * A smaller value means faster Morse code.
     */
    public MorseAudioPlayer(int unitDurationMs) {
        this.unitDurationMs = unitDurationMs;
    }

    /**
     * Generates and plays a sine wave tone for a given duration.
     * @param durationMs The duration of the tone in milliseconds.
     */
    private void playTone(int durationMs) {
        if (durationMs <= 0) return; // Don't play tones of zero or negative duration

        int numSamples = (int) (durationMs * SAMPLE_RATE / 1000);
        byte[] buffer = new byte[numSamples * (BITS_PER_SAMPLE / 8)]; // Each sample is 2 bytes for 16-bit audio

        // Generate sine wave samples
        for (int i = 0; i < numSamples; i++) {
            double angle = 2.0 * Math.PI * FREQUENCY_HZ * i / SAMPLE_RATE;
            // Scale to 16-bit signed short range, use 0.7 amplitude to avoid clipping
            short sample = (short) (Short.MAX_VALUE * Math.sin(angle) * 0.7); 
            
            // Convert short to 2 bytes (little-endian)
            buffer[i * 2] = (byte) (sample & 0xFF);         // Lower byte
            buffer[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF); // Upper byte
        }

        try {
            // Define the audio format
            AudioFormat format = new AudioFormat(SAMPLE_RATE, BITS_PER_SAMPLE, CHANNELS, SIGNED, BIG_ENDIAN);
            // Get a data line for playback
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            
            line.open(format); // Open the line with the specified format
            line.start();      // Start playback
            line.write(buffer, 0, buffer.length); // Write the audio data
            line.drain();      // Wait for all data to be played
            line.stop();       // Stop the line
            line.close();      // Close the line to release resources
        } catch (LineUnavailableException e) {
            // Handle cases where audio line is not available (e.g., no sound card, in use)
            System.err.println("Audio line unavailable: " + e.getMessage());
            // In a GUI application, it's better to show a dialog
            JOptionPane.showMessageDialog(null, 
                "Error: Audio playback is not available. Please check your sound device.", 
                "Audio Playback Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal argument for audio: " + e.getMessage());
        }
    }

    /**
     * Plays silence (pauses execution) for a given duration.
     * @param durationMs The duration of the silence in milliseconds.
     */
    private void playSilence(int durationMs) {
        if (durationMs <= 0) return; // Don't sleep for non-positive durations
        try {
            Thread.sleep(durationMs); // Pause the current thread
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            System.err.println("Morse audio playback interrupted.");
        }
    }

    // --- Morse Symbol Playback Methods ---

    /** Plays a Morse dot (Dit) followed by inter-element silence. */
    private void playDit() {
        playTone(unitDurationMs);       // Tone for 1 unit
        playSilence(unitDurationMs);    // Silence for 1 unit (inter-element gap)
    }

    /** Plays a Morse dash (Dah) followed by inter-element silence. */
    private void playDah() {
        playTone(unitDurationMs * 3);   // Tone for 3 units
        playSilence(unitDurationMs);    // Silence for 1 unit (inter-element gap)
    }

    /** Plays the additional silence required for an inter-character gap (total 3 units). */
    private void playInterCharacterSpace() {
        // We already played 1 unit of silence after the last tone, so add 2 more.
        playSilence(unitDurationMs * 2); 
    }

    /** Plays the additional silence required for an inter-word gap (total 7 units). */
    private void playInterWordSpace() {
        // We already played 1 unit of silence after the last tone, so add 6 more.
        playSilence(unitDurationMs * 6);
    }

    /**
     * Plays the given Morse code string as audio.
     * It parses the string assuming standard Morse character separation (space)
     * and word separation (" / "). Each Morse symbol (dot or dash) is played
     * with appropriate timing. Pauses for inter-element, inter-character,
     * and inter-word gaps are included.
     *
     * @param morseCode The Morse code string to play (e.g., ".... . .-.. .-.. --- / .-- --- .-. .-.. -..").
     */
    public void playMorseCode(String morseCode) {
        if (morseCode == null || morseCode.isEmpty()) {
            return;
        }

        // Clean up leading/trailing spaces and normalize multiple word separators
        String cleanedMorse = morseCode.trim().replaceAll(" +", " ").replaceAll(" */ *", " / ").trim();
        if (cleanedMorse.isEmpty()) { // Check if it's empty after cleaning
            return;
        }

        // Split the entire Morse string into words using " / " as the delimiter
        String[] words = cleanedMorse.split(" / ");

        for (int i = 0; i < words.length; i++) {
            String word = words[i]; 

            if (!word.isEmpty()) { // Only process non-empty word segments
                // Split each word into individual Morse characters using a single space as delimiter
                String[] chars = word.split(" "); 
                for (int j = 0; j < chars.length; j++) {
                    String morseChar = chars[j];
                    if (!morseChar.isEmpty()) { // Only process non-empty Morse character sequences
                        // Play each dot or dash within the character
                        for (char symbol : morseChar.toCharArray()) {
                            if (symbol == '.') {
                                playDit();
                            } else if (symbol == '-') {
                                playDah();
                            }
                            // Any other symbol encountered is ignored for playback (e.g., '?')
                        }
                    }
                    // Add inter-character space if not the last character in the current word
                    if (j < chars.length - 1) { 
                        playInterCharacterSpace(); 
                    }
                }
            }

            // Add inter-word space if not the last word in the overall message
            if (i < words.length - 1) { 
                playInterWordSpace(); 
            }
        }
    }

    // Optional: Main method for testing MorseAudioPlayer independently
    public static void main(String[] args) {
        MorseAudioPlayer player = new MorseAudioPlayer(60); // 60ms unit duration (WPM ~ 20)
        System.out.println("Playing SOS...");
        player.playMorseCode("... --- ..."); 
        
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        System.out.println("Playing HELLO WORLD...");
        player.playMorseCode(".... . .-.. .-.. --- / .-- --- .-. .-.. -..");
        
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        System.out.println("Playing custom message with punctuation...");
        player.playMorseCode("-.-- --- ..- / .- .-. . / .--. .-. --- .-.-.-"); // YOU ARE PRO.
        System.out.println("Done.");
    }
}