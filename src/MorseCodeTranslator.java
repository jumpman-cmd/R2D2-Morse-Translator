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
public class MorseCodeTranslator 
{

    // --- Morse Code Mappings ---
    // A HashMap to store the mapping from characters to their Morse code representations.
    private static final Map<Character, String> LETTERS_TO_MORSE = new HashMap<>();
    // A HashMap to store the mapping from Morse code representations back to characters.
    private static final Map<String, Character> MORSE_TO_LETTERS = new HashMap<>();

    // Static initializer block to populate the Morse code mappings when the class is loaded.
    static 
    {
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
        for (Map.Entry<Character, String> entry : LETTERS_TO_MORSE.entrySet()) 
        {
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
    public static String lettersToMorseCode(String text) 
    {
        // Handle empty or null input to prevent NullPointerExceptions.
        if (text == null || text.isEmpty())
        {
            return "";
        }

        StringBuilder morseCodeBuilder = new StringBuilder();
        // Convert the entire text to uppercase to match the HashMap keys.
        String upperCaseText = text.toUpperCase();

        // Iterate through each character of the input text.
        for (int i = 0; i < upperCaseText.length(); i++) 
        {
            char character = upperCaseText.charAt(i);

            // If the character is a space, append the word separator.
            if (character == ' ') 
            {
                morseCodeBuilder.append(" / ");
            } 
            
            else if (LETTERS_TO_MORSE.containsKey(character)) 
            {
                // If the character is in our mapping, append its Morse code representation.
                morseCodeBuilder.append(LETTERS_TO_MORSE.get(character)).append(" ");
            }
            // If the character is not found in the map (e.g., unsupported symbols), it is ignored.
        }

        // Trim any trailing space to ensure clean output.
        return morseCodeBuilder.toString().trim();
    }

    /**
     * Converts a given Morse code string back into plain text.
     * Morse code characters are separated by spaces, and words by " / ".
     * Unknown Morse sequences are represented by a '?'.
     *
     * @param code The Morse code string to convert.
     * @return The plain text representation of the input Morse code.
     */
    public static String morseCodeToLetters(String code) 
    {
        // Handle empty or null input.
        if (code == null || code.isEmpty()) 
        {
            return "";
        }

        StringBuilder plainTextBuilder = new StringBuilder();
        // Split the input Morse code string by the word separator " / ".
        String[] words = code.split(" / ");

        // Iterate through each "word" in the Morse code.
        for (int i = 0; i < words.length; i++) 
        {
            String word = words[i];
            // Split each word into individual Morse code characters (separated by single spaces).
            String[] morseChars = word.split(" ");

            // Iterate through each Morse character within the current word.
            for (String morseChar : morseChars) 
            {
                // Trim any whitespace from the Morse character to ensure accurate lookup.
                morseChar = morseChar.trim();
                if (morseChar.isEmpty()) 
                {
                    continue; // Skip empty strings that might result from multiple spaces
                }

                // If the Morse sequence is in our mapping, append the corresponding character.
                if (MORSE_TO_LETTERS.containsKey(morseChar)) 
                {
                    plainTextBuilder.append(MORSE_TO_LETTERS.get(morseChar));
                } 
                
                else 
                {
                    // If the Morse sequence is unknown, append a '?' to indicate an error.
                    plainTextBuilder.append("?");
                }
            }

            // Add a space between words in the plain text output, but not after the last word.
            if (i < words.length - 1) 
            {
                plainTextBuilder.append(" ");
            }
        }

        return plainTextBuilder.toString();
    }

    /**
     * Main method to provide a command-line interface (CLI) for the translator.
     * Users can choose to encode or decode messages.
     */
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in); // Create a Scanner object for user input.
        boolean running = true; // Control variable for the main loop.

        System.out.println("🌟 Welcome, Young Padawan, to R2-D2's Morse Code Translator! 🌟");
        System.out.println("---------------------------------------------------------------");

        // Main application loop.
        while (running) 
        {
            System.out.println("\nChoose an option:");
            System.out.println("1. Encode (Text to Morse)");
            System.out.println("2. Decode (Morse to Text)");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1, 2, or 3): ");

            String choice = scanner.nextLine(); // Read the user's choice.

            switch (choice) 
            {
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