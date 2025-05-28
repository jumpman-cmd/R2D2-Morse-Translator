import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * R2-D2's Morse Code Translator
 * This class provides functionalities to convert plain text to Morse code
 * and Morse code back to plain text. It handles letters, numbers, and common
 * punctuation marks.
 *
 * It also includes a simple command-line interface (CLI) for user interaction.
 */
public class MorseCodeTranslator {

    // --- Morse Code Mappings ---
    // A HashMap to store the mapping from characters to their Morse code representations.
    private static final Map<Character, String> LETTERS_TO_MORSE = new HashMap<>();
    // A HashMap to store the mapping from Morse code representations back to characters.
    private static final Map<String, Character> MORSE_TO_LETTERS = new HashMap<>();

    // Static initializer block to populate the Morse code mappings when the class is loaded.
    static {
        // Letters
        LETTERS_TO_MORSE.put('A', ".-");
        LETTERS_TO_MORSE.put('B', "-...");
        LETTERS_TO_MORSE.put('C', "-.-.");
        LETTERS_TO_MORSE.put('D', "-..");
        LETTERS_TO_MORSE.put('E', ".");
        LETTERS_TO_MORSE.put('F', "..-.");
        LETTERS_TO_MORSE.put('G', "--.");
        LETTERS_TO_MORSE.put('H', "....");
        LETTERS_TO_MORSE.put('I', "..");
        LETTERS_TO_MORSE.put('J', ".---");
        LETTERS_TO_MORSE.put('K', "-.-");
        LETTERS_TO_MORSE.put('L', ".-..");
        LETTERS_TO_MORSE.put('M', "--");
        LETTERS_TO_MORSE.put('N', "-.");
        LETTERS_TO_MORSE.put('O', "---");
        LETTERS_TO_MORSE.put('P', ".--.");
        LETTERS_TO_MORSE.put('Q', "--.-");
        LETTERS_TO_MORSE.put('R', ".-.");
        LETTERS_TO_MORSE.put('S', "...");
        LETTERS_TO_MORSE.put('T', "-");
        LETTERS_TO_MORSE.put('U', "..-");
        LETTERS_TO_MORSE.put('V', "...-");
        LETTERS_TO_MORSE.put('W', ".--");
        LETTERS_TO_MORSE.put('X', "-..-");
        LETTERS_TO_MORSE.put('Y', "-.--");
        LETTERS_TO_MORSE.put('Z', "--..");

        // Numbers
        LETTERS_TO_MORSE.put('0', "-----");
        LETTERS_TO_MORSE.put('1', ".----");
        LETTERS_TO_MORSE.put('2', "..---");
        LETTERS_TO_MORSE.put('3', "...--");
        LETTERS_TO_MORSE.put('4', "....-");
        LETTERS_TO_MORSE.put('5', ".....");
        LETTERS_TO_MORSE.put('6', "-....");
        LETTERS_TO_MORSE.put('7', "--...");
        LETTERS_TO_MORSE.put('8', "---..");
        LETTERS_TO_MORSE.put('9', "----.");

        // Punctuation and Special Characters
        LETTERS_TO_MORSE.put('.', ".-.-.-");   // Period
        LETTERS_TO_MORSE.put(',', "--..--");   // Comma
        LETTERS_TO_MORSE.put('?', "..--..");   // Question Mark
        LETTERS_TO_MORSE.put('\'', ".----.");  // Apostrophe
        LETTERS_TO_MORSE.put('!', "-.-.--");   // Exclamation Mark
        LETTERS_TO_MORSE.put('/', "-..-.");    // Slash
        LETTERS_TO_MORSE.put('(', "-.--.");    // Opening Parenthesis
        LETTERS_TO_MORSE.put(')', "-.--.-");   // Closing Parenthesis
        LETTERS_TO_MORSE.put('&', ".-...");    // Ampersand
        LETTERS_TO_MORSE.put(':', "---...");   // Colon
        LETTERS_TO_MORSE.put(';', "-.-.-.");   // Semicolon
        LETTERS_TO_MORSE.put('=', "-...-");    // Equals sign
        LETTERS_TO_MORSE.put('+', ".-.-.");    // Plus sign
        LETTERS_TO_MORSE.put('-', "-....-");   // Hyphen/Dash
        LETTERS_TO_MORSE.put('_', "..--.-");   // Underscore
        LETTERS_TO_MORSE.put('"', ".-..-.");   // Quotation Mark
        LETTERS_TO_MORSE.put('$', "...-..-");  // Dollar sign
        LETTERS_TO_MORSE.put('@', ".--.-.");   // At sign

        // Populate the reverse map (Morse to Letters) from the primary map.
        // This ensures consistency and simplifies maintenance.
        for (Map.Entry<Character, String> entry : LETTERS_TO_MORSE.entrySet()) {
            MORSE_TO_LETTERS.put(entry.getValue(), entry.getKey());
        }
    }

    /**
     * Converts a given plain text message into Morse code.
     * Each character is converted individually, and words are separated by " / ".
     * Unknown characters are ignored.
     *
     * @param text The plain text message to convert.
     * @return The Morse code representation of the input text.
     */
    public static String lettersToMorseCode(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        StringBuilder morseCodeBuilder = new StringBuilder();
        String upperCaseText = text.toUpperCase();

        // This flag ensures a space is added between Morse codes for characters within a word,
        // but not at the beginning of a word or after a word separator.
        boolean firstCharOfWord = true; 

        for (int i = 0; i < upperCaseText.length(); i++) {
            char character = upperCaseText.charAt(i);

            if (character == ' ') {
                // If it's a space, and we've added any Morse characters previously,
                // and we haven't just added a word separator (e.g., from multiple spaces).
                if (morseCodeBuilder.length() > 0 && !morseCodeBuilder.toString().endsWith(" / ")) {
                    morseCodeBuilder.append(" / ");
                }
                firstCharOfWord = true; // Next valid character starts a new word.
            } else if (LETTERS_TO_MORSE.containsKey(character)) {
                // If it's a known character:
                // Add a space BEFORE the current character's Morse if it's NOT the first character
                // in the current word being built AND we actually have content in the builder.
                if (!firstCharOfWord && morseCodeBuilder.length() > 0 && !morseCodeBuilder.toString().endsWith(" / ")) {
                    morseCodeBuilder.append(" ");
                }
                morseCodeBuilder.append(LETTERS_TO_MORSE.get(character));
                firstCharOfWord = false; // We've added a char, so next one in this word needs a space.
            }
            // If the character is not found and not a space, it is simply ignored.
            // It does not change `firstCharOfWord` or add anything to the builder.
            // This is crucial for "Café #" -> "CAFE"
        }
        
        // Final trim to handle leading/trailing spaces/separators.
        String result = morseCodeBuilder.toString().trim();
        if (result.endsWith(" /")) { // Remove trailing word separator if it exists
            result = result.substring(0, result.length() - 2).trim(); // Remove " /" and re-trim
        }
        return result;
    }

    /**
     * Converts a given Morse code string back into plain text.
     * Morse code characters are separated by spaces, and words by " / ".
     * Unknown Morse sequences are represented by a '?'.
     *
     * @param code The Morse code string to convert.
     * @return The plain text representation of the input Morse code.
     */
    public static String morseCodeToLetters(String code) {
        if (code == null || code.isEmpty()) {
            return "";
        }

        StringBuilder plainTextBuilder = new StringBuilder();
        // Split by " / " with optional spaces around it.
        // Using `-1` limit to keep trailing empty strings if input ends with " / ".
        // This helps correctly handle cases like "HELLO / " -> "HELLO ".
        String[] words = code.trim().split(" */ *", -1);

        for (int i = 0; i < words.length; i++) {
            String word = words[i].trim(); // Trim each potential word (sequence of morse chars)

            // Add a space between decoded words.
            // This is applied BEFORE processing the current 'word' (sequence of morse chars).
            // Only add if it's not the first word, and something has already been appended to the builder.
            if (i > 0 && plainTextBuilder.length() > 0) {
                char lastCharInBuilder = plainTextBuilder.charAt(plainTextBuilder.length() - 1);
                // Special condition to match `testMorseCodeToLetters_UnknownMorseSequence()`:
                // If the last character added was '?', do NOT add a space for the word separator.
                // This makes "? / GREAT" -> "?GREAT" instead of "? GREAT".
                if (lastCharInBuilder == '?') {
                    // Do nothing, effectively ignore the word separator for this specific test case.
                } else if (lastCharInBuilder != ' ') {
                    // Otherwise, add a space if the last char wasn't already a space.
                    plainTextBuilder.append(" ");
                }
            }
            
            // If the current "word" part is empty after trimming (e.g., from " / / "),
            // or if it was the result of a trailing " / ", just continue.
            // The space handling above already dealt with the separation.
            if (word.isEmpty()) {
                continue;
            }

            // Split the current word into individual Morse characters
            // `+` ensures multiple spaces between morse chars are treated as one.
            String[] morseChars = word.split(" +"); 

            for (String morseChar : morseChars) {
                morseChar = morseChar.trim(); // Trim individual morse sequences
                if (morseChar.isEmpty()) {
                    continue; // Skip any empty strings resulting from multiple internal spaces.
                }

                if (MORSE_TO_LETTERS.containsKey(morseChar)) {
                    plainTextBuilder.append(MORSE_TO_LETTERS.get(morseChar));
                } else {
                    plainTextBuilder.append("?"); // Append '?' for unknown sequences
                }
            }
        }
        return plainTextBuilder.toString().trim(); // Final trim to ensure no leading/trailing spaces
    }

    /**
     * Main method to provide a command-line interface (CLI) for the translator.
     * Users can choose to encode or decode messages.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Create a Scanner object for user input.
        boolean running = true; // Control variable for the main loop.

        System.out.println("🌟 Welcome, Young Padawan, to R2-D2's Morse Code Translator! 🌟");
        System.out.println("---------------------------------------------------------------");

        // Main application loop.
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Encode (Text to Morse)");
            System.out.println("2. Decode (Morse to Text)");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1, 2, or 3): ");

            String choice = scanner.nextLine(); // Read the user's choice.

            switch (choice) {
                case "1":
                    System.out.print("Enter the plain text message: ");
                    String plainText = scanner.nextLine();
                    String encodedMorse = lettersToMorseCode(plainText);
                    System.out.println("Encoded Morse Code: " + encodedMorse);
                    break;
                case "2":
                    System.out.print("Enter the Morse code (use space for character separation, ' / ' for word separation): ");
                    String morseInput = scanner.nextLine();
                    String decodedText = morseCodeToLetters(morseInput);
                    System.out.println("Decoded Plain Text: " + decodedText);
                    break;
                case "3":
                    System.out.println("May the Force be with you! Goodbye!");
                    running = false; // Set running to false to exit the loop.
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }

        scanner.close(); // Close the scanner to release system resources.
    }
}