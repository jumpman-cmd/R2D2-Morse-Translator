# R2-D2 Morse Code Translator

A command-line and GUI application designed to translate human language to Morse code and vice-versa, specifically crafted for R2-D2's galactic communications needs.

## About the Project

This project aims to provide a reliable and easy-to-use tool for converting text into Morse code signals and decoding Morse back into human-readable text. It was developed with a focus on clear functionality and an intuitive user interface, resembling a console from a galaxy far, far away.

## Features

- **Text to Morse Translation:** Convert any English text into its corresponding Morse code representation.
- **Morse to Text Translation:** Decode Morse code sequences back into human-readable English text.
- **Intuitive GUI:** A custom-themed graphical user interface for easy interaction, designed with a "Galactic Console" aesthetic.
- **Morse Audio Playback:** Listen to the translated Morse code with adjustable speed, bringing the communication to life!

---

## How to Run

To get the R2-D2 Morse Code Translator up and running on your local machine:

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/jumpman-cmd/R2D2-Morse-Translator.git
    cd R2D2-Morse-Translator
    ```

2.  **Compile the Java files:**
    Ensure you have a Java Development Kit (JDK) installed.
    Navigate to the project root directory in your terminal and run:

    ```bash
    javac -d out src/*.java
    ```

    _This will compile all Java source files and place the compiled `.class` files into the `out` directory._

3.  **Run the GUI application:**
    ```bash
    java -cp out MorseTranslatorGUI
    ```
    _The R2-D2 Morse Translator GUI window should now appear._

---

## How to Contribute

We welcome contributions to the R2-D2 Morse Code Translator! If you have ideas for improvements, new features, or bug fixes, please follow these guidelines:

1.  **Fork the repository.**
2.  **Create a new branch** for your feature or bug fix (e.g., `feature/new-feature` or `bugfix/fix-audio`).
3.  **Make your changes.**
4.  **Commit your changes** with clear and concise commit messages.
5.  **Push your branch** to your forked repository.
6.  **Open a Pull Request** to the `development` branch of this repository.

---
