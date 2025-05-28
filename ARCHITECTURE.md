# R2-D2 Morse Translator - Architecture Overview

This document provides a high-level overview of the architectural components and their interactions within the Morse Code Translator application. It aims to clarify the responsibilities of each module and how data flows through the system.

## Core Components

### 1. `MorseCodeTranslator.java`

- **Responsibility:** Acts as the brain of the translation process. It contains the static methods and mapping (dictionaries) necessary for converting English text to Morse code and vice-versa.
- **Functionality:** Handles character-by-character translation and manages word separation rules for Morse code.
- **Dependencies:** Purely logical; no external dependencies within the project's source code.
- **Interaction:** Primarily called by `MorseTranslatorGUI` to perform translation operations.

### 2. `MorseAudioPlayer.java`

- **Responsibility:** Manages the generation and playback of audio tones representing Morse code dots, dashes, and silence. It configures audio lines and handles timing for all Morse elements.
- **Functionality:** Uses Java's `javax.sound.sampled` API to synthesize sine waves at a specific frequency and play them through the system's audio output.
- **Dependencies:** `javax.sound.sampled` (part of the Java Standard Edition).
- **Interaction:** Instantiated and invoked by `MorseTranslatorGUI` when the user requests audio playback of a translated Morse sequence. Audio playback runs on a separate thread to ensure the GUI remains responsive.

### 3. `MorseTranslatorGUI.java`

- **Responsibility:** Provides the graphical user interface that users interact with. It's the central orchestration component that binds the translation logic and audio playback together for the user.
- **Functionality:**
  - Presents input and output text areas.
  - Manages "Encode," "Decode," "Play Morse," and "Clear" button actions.
  - Applies a custom "Galactic Console" theme for visual appeal.
  - Handles user input and displays translated output.
- **Dependencies:** `javax.swing`, `java.awt`. Directly uses `MorseCodeTranslator` for translation and `MorseAudioPlayer` for audio playback.
- **Interaction:** The main entry point (`main` method) for the application. All user interactions are processed here, which then trigger calls to the `MorseCodeTranslator` and `MorseAudioPlayer` as needed.

## Simplified Data and Control Flow

1.  **User Input:** Text (English or Morse) is typed into the `inputText` area in `MorseTranslatorGUI`.
2.  **Translation Request:** User clicks either the "ENCODE" or "DECODE" button.
3.  **Logic Delegation:** `MorseTranslatorGUI` takes the input text and passes it to the appropriate static method in `MorseCodeTranslator` (`lettersToMorseCode` or `morseCodeToLetters`).
4.  **Output Display:** The result returned by `MorseCodeTranslator` is then set as the text in `MorseTranslatorGUI`'s `outputText` area.
5.  **Audio Playback Request:** User clicks the "PLAY MORSE" button.
6.  **Audio Delegation:** `MorseTranslatorGUI` retrieves the Morse code from its `outputText` area and passes it to the `MorseAudioPlayer.playMorseCode()` method. This operation is typically run on a background thread to prevent the UI from freezing.
7.  **Audio Generation:** `MorseAudioPlayer` processes the Morse string, calling `playDit()`, `playDah()`, and `playSilence()` methods to generate the audio signals in sequence.
