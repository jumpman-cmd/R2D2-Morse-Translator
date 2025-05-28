# R2-D2 Morse Translator - TODO List

This document outlines planned features, improvements, and known considerations for the R2-D2 Morse Code Translator.

## Planned Features

- Implement support for more complex character sets or symbols (e.g., punctuation not currently mapped).
- Add a visual "blinking light" indicator on the GUI during audio playback for a more authentic experience.
- Include options for users to customize the audio tone frequency and playback volume.
- Introduce a "Copy to Clipboard" button for the translated output text.

## Future Improvements

- Refactor the `MorseCodeTranslator` for enhanced extensibility, potentially allowing different Morse standards (e.g., American Morse code).
- Improve error handling and user feedback for invalid Morse code input, guiding the user on correct formatting.
- Optimize the GUI layout for responsiveness across various screen resolutions.
- Add unit tests for the `MorseTranslatorGUI` to ensure UI functionality.

## Known Considerations

- Currently, only standard English alphabet (A-Z) and numbers (0-9) are fully supported for encoding and decoding.
- Performance of audio playback may vary slightly depending on system resources.
