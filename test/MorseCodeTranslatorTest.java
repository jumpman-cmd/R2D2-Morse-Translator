import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MorseCodeTranslator class.
 * Ensures the lettersToMorseCode and morseCodeToLetters methods
 * function accurately under various conditions.
 */
public class MorseCodeTranslatorTest {

    // --- Tests for lettersToMorseCode method ---

    @Test
    void testLettersToMorseCode_BasicText() {
        // Test with a common sentence
        String input = "Hello World";
        String expected = ".... . .-.. .-.. --- / .-- --- .-. .-.. -..";
        assertEquals(expected, MorseCodeTranslator.lettersToMorseCode(input), "Basic text conversion failed");
    }

    @Test
    void testLettersToMorseCode_MixedCase() {
        // Test with mixed case input, ensuring it's handled by toUpperCase()
        String input = "RaNdOm TeXt";
        String expected = ".-. .- -. -.. --- -- / - . -..- -";
        assertEquals(expected, MorseCodeTranslator.lettersToMorseCode(input), "Mixed case conversion failed");
    }

    @Test
    void testLettersToMorseCode_NumbersAndPunctuation() {
        // Test with numbers and punctuation
        String input = "123!@.";
        String expected = ".---- ..--- ...-- -.-.-- .--.-. .-.-.-";
        assertEquals(expected, MorseCodeTranslator.lettersToMorseCode(input), "Numbers and punctuation conversion failed");
    }

    @Test
    void testLettersToMorseCode_EmptyString() {
        // Test with an empty string input
        String input = "";
        String expected = "";
        assertEquals(expected, MorseCodeTranslator.lettersToMorseCode(input), "Empty string conversion failed");
    }

    @Test
    void testLettersToMorseCode_WhitespaceOnly() {
        // Test with a string containing only spaces
        String input = "   "; // Multiple spaces should result in an empty Morse string
        String expected = "";
        assertEquals(expected, MorseCodeTranslator.lettersToMorseCode(input), "Whitespace-only string conversion failed");
    }

   @Test
    void testLettersToMorseCode_UnsupportedCharacters() {
        // Test with characters not in the map (e.g., accented letters, symbols not defined)
        // 'é' and '#' are not in the map and should be ignored.
        // The space between 'é' and '#' is also effectively ignored because no valid Morse
        // characters are produced around it that would trigger a word separator.
        // So, "Café #" should produce Morse for "CAF"
        String input = "Café #"; 
        String expected = "-.-. .- ..-."; // Morse for "CAF", ignoring 'é' and '#'
        assertEquals(expected, MorseCodeTranslator.lettersToMorseCode(input), "Unsupported characters handling failed");
    }

    // --- Tests for morseCodeToLetters method ---

    @Test
    void testMorseCodeToLetters_BasicMorse() {
        // Test with a common Morse code sequence
        String input = ".... . .-.. .-.. --- / .-- --- .-. .-.. -..";
        String expected = "HELLO WORLD";
        assertEquals(expected, MorseCodeTranslator.morseCodeToLetters(input), "Basic Morse to letters failed");
    }

    @Test
    void testMorseCodeToLetters_NumbersAndPunctuation() {
        // Test with Morse code for numbers and punctuation
        String input = ".---- ..--- ...-- -.-.-- .--.-. .-.-.-"; // 123!@.
        String expected = "123!@.";
        assertEquals(expected, MorseCodeTranslator.morseCodeToLetters(input), "Morse to numbers and punctuation failed");
    }

    @Test
    void testMorseCodeToLetters_EmptyString() {
        // Test with an empty Morse code string
        String input = "";
        String expected = "";
        assertEquals(expected, MorseCodeTranslator.morseCodeToLetters(input), "Empty Morse string decoding failed");
    }

    @Test
    void testMorseCodeToLetters_UnknownMorseSequence() {
        // Test with an unknown Morse code sequence, should convert to '?'
        String input = "...-.-.. / --. .-. . .- -"; // First sequence is unknown
        String expected = "?GREAT";
        assertEquals(expected, MorseCodeTranslator.morseCodeToLetters(input), "Unknown Morse sequence handling failed");
    }

    @Test
    void testMorseCodeToLetters_MultipleWordSeparators() {
        // Test with multiple word separators (should be treated as a single space)
        String input = ".... . .-.. .-.. --- / / .-- --- .-. .-.. -.."; // "Hello  World"
        String expected = "HELLO WORLD";
        assertEquals(expected, MorseCodeTranslator.morseCodeToLetters(input), "Multiple word separators handling failed");
    }

    @Test
    void testMorseCodeToLetters_TrailingLeadingSpaces() {
        // Test with leading/trailing spaces in the Morse input string
        String input = " .... . .-.. .-.. --- / .-- --- .-. .-.. -.. ";
        String expected = "HELLO WORLD";
        assertEquals(expected, MorseCodeTranslator.morseCodeToLetters(input), "Trailing/leading spaces in Morse failed");
    }

    @Test
    void testMorseCodeToLetters_JustWordSeparator() {
        // Test input that is just the word separator, should result in an empty string
        String input = " / ";
        String expected = "";
        assertEquals(expected, MorseCodeTranslator.morseCodeToLetters(input), "Just word separator decoding failed");
    }

    @Test
    void testMorseCodeToLetters_MultipleSpacesWithinWord() {
        // Test morse with extra spaces within a morse char sequence (e.g., ".. .." instead of "....")
        String input = "....  .  .-.. .-.. ---"; // "H E L L O" with extra spaces
        String expected = "HELLO";
        assertEquals(expected, MorseCodeTranslator.morseCodeToLetters(input), "Multiple spaces within Morse char failed");
    }
}