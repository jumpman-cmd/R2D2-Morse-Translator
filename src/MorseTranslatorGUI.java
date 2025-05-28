import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MorseTranslatorGUI extends JFrame {

    // --- Custom Color Palette (Galactic Console Theme) ---
    private static final Color DARK_BACKGROUND = new Color(15, 15, 25);     // Very dark indigo-black
    private static final Color PANEL_BACKGROUND = new Color(35, 45, 55);    // Rich dark slate grey
    private static final Color TEXT_AREA_BACKGROUND = new Color(25, 30, 35); // Slightly lighter than background for text areas
    private static final Color TEXT_COLOR = new Color(220, 230, 240);       // Soft off-white for all text
    private static final Color BORDER_COLOR = new Color(80, 90, 100);       // Subtle cool light grey for borders

    private static final Color ENCODE_BUTTON_COLOR = new Color(0, 200, 100);  // Electric Green
    private static final Color DECODE_BUTTON_COLOR = new Color(0, 150, 200);  // Bright Cyan/Aqua
    private static final Color PLAY_BUTTON_COLOR = new Color(255, 120, 0);   // Striking Deep Orange
    private static final Color CLEAR_BUTTON_COLOR = new Color(200, 50, 50);  // Ruby Red

    // --- UI Components ---
    private JTextArea inputText;
    private JTextArea outputText;
    private JButton encodeButton;
    private JButton decodeButton;
    private JButton playButton;
    private JButton clearButton;

    // --- Audio Player Instance ---
    private MorseAudioPlayer audioPlayer;
    private static final int MORSE_UNIT_DURATION_MS = 60; 

    public MorseTranslatorGUI() {
    
        audioPlayer = new MorseAudioPlayer(MORSE_UNIT_DURATION_MS);

        // --- 1. Frame Setup ---
        setTitle("Yanga Mdede - R2-D2's Universal Translator Console");
        setSize(750, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window
        getContentPane().setBackground(DARK_BACKGROUND);

        // Use a GridBagLayout for flexible, weighted layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12); // Increased padding around components

        // --- 2. Title Label ---
        JLabel titleLabel = new JLabel("Yanga Mdede - R2-D2 Morse Code Translator");
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 30)); // Stronger, techy font
        titleLabel.setForeground(new Color(150, 255, 255)); // Bright Aqua for title
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // --- 3. Input Panel ---
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5)); // Small internal gap
        inputPanel.setBackground(PANEL_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2), // Outer border
            new EmptyBorder(15, 15, 15, 15)    // Inner padding
        ));

        JLabel inputLabel = new JLabel("INPUT CONSOLE:");
        inputLabel.setFont(new Font("Arial", Font.BOLD, 16));
        inputLabel.setForeground(TEXT_COLOR);
        inputPanel.add(inputLabel, BorderLayout.NORTH);

        inputText = new JTextArea(10, 50); // Increased rows for more input space
        setupTextArea(inputText, "Enter your galactic transmission here..."); // Custom setup method
        JScrollPane inputScrollPane = new JScrollPane(inputText);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH; // Fill horizontally and vertically
        gbc.weighty = 0.45; // Takes up slightly less vertical space for input
        add(inputPanel, gbc);

        // --- 4. Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Wider gaps
        buttonPanel.setBackground(DARK_BACKGROUND); // Match frame background

        encodeButton = new JButton("ENCODE");
        decodeButton = new JButton("DECODE");
        playButton = new JButton("PLAY MORSE");
        clearButton = new JButton("CLEAR");

        setupButton(encodeButton, ENCODE_BUTTON_COLOR);
        setupButton(decodeButton, DECODE_BUTTON_COLOR);
        setupButton(playButton, PLAY_BUTTON_COLOR);
        setupButton(clearButton, CLEAR_BUTTON_COLOR);

        buttonPanel.add(encodeButton);
        buttonPanel.add(decodeButton);
        buttonPanel.add(playButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Only fill horizontally
        gbc.weighty = 0; // Don't take vertical space
        add(buttonPanel, gbc);

        // --- 5. Output Panel ---
        JPanel outputPanel = new JPanel(new BorderLayout(5, 5)); // Small internal gap
        outputPanel.setBackground(PANEL_BACKGROUND);
        outputPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel outputLabel = new JLabel("OUTPUT LOG:");
        outputLabel.setFont(new Font("Arial", Font.BOLD, 16));
        outputLabel.setForeground(TEXT_COLOR);
        outputPanel.add(outputLabel, BorderLayout.NORTH);

        outputText = new JTextArea(10, 50);
        setupTextArea(outputText, "Translated signals will appear here...");
        outputText.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputText);
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.55; // Takes more vertical space for output
        add(outputPanel, gbc);

        // --- 6. Action Listeners ---
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputText.getText();
                // Avoid translating placeholder text if it's still present
                if (text.equals("Enter your galactic transmission here...")) {
                    text = "";
                }
                String morse = MorseCodeTranslator.lettersToMorseCode(text);
                outputText.setText(morse);
            }
        });

        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String morse = inputText.getText();
                // Avoid translating placeholder text if it's still present
                if (morse.equals("Enter your galactic transmission here...")) {
                    morse = "";
                }
                String text = MorseCodeTranslator.morseCodeToLetters(morse);
                outputText.setText(text);
            }
        });

        // IMPORTANT: Playback on a separate thread to prevent GUI freeze!
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String morseToPlay = outputText.getText(); // Get Morse from output area

                // Check for placeholder text or empty output before playing
                if (morseToPlay.isEmpty() || morseToPlay.equals("Translated signals will appear here...")) {
                    JOptionPane.showMessageDialog(MorseTranslatorGUI.this,
                                                  "Please translate some text to Morse code first!",
                                                  "No Morse Code to Play",
                                                  JOptionPane.INFORMATION_MESSAGE);
                    return; // Stop if no valid Morse is present
                }

                // Create and start a new thread for audio playback
                new Thread(() -> {
                    // Disable buttons on the Event Dispatch Thread (EDT) while playing
                    SwingUtilities.invokeLater(() -> {
                        encodeButton.setEnabled(false);
                        decodeButton.setEnabled(false);
                        playButton.setEnabled(false);
                        clearButton.setEnabled(false);
                    });

                    try {
                        audioPlayer.playMorseCode(morseToPlay); // Call the audio player method
                    } catch (Exception ex) {
                        // Show an error message on the EDT if something goes wrong during playback
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(MorseTranslatorGUI.this,
                                                          "Error during audio playback: " + ex.getMessage(),
                                                          "Audio Error",
                                                          JOptionPane.ERROR_MESSAGE);
                        });
                    } finally {
                        // Re-enable buttons on the EDT after playback is complete or an error occurs
                        SwingUtilities.invokeLater(() -> {
                            encodeButton.setEnabled(true);
                            decodeButton.setEnabled(true);
                            playButton.setEnabled(true);
                            clearButton.setEnabled(true);
                        });
                    }
                }).start(); // This starts the new thread
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputText.setText("Enter your galactic transmission here..."); // Reset input placeholder
                inputText.setForeground(TEXT_COLOR.darker().darker()); // Dim placeholder
                outputText.setText("Translated signals will appear here..."); // Reset output placeholder
                outputText.setForeground(TEXT_COLOR.darker().darker()); // Dim placeholder
            }
        });
    }

    /**
     * Helper method to apply common styling and placeholder behavior to JTextAreas.
     */
    private void setupTextArea(JTextArea textArea, String placeholder) {
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 15)); // Consolas for code/data feel
        textArea.setBackground(TEXT_AREA_BACKGROUND);
        textArea.setForeground(TEXT_COLOR);
        textArea.setCaretColor(TEXT_COLOR); // Color of the blinking cursor
        textArea.setBorder(new EmptyBorder(8, 8, 8, 8)); // Inner padding
        
        // Placeholder text logic
        textArea.setText(placeholder);
        textArea.setForeground(TEXT_COLOR.darker().darker()); // Start dimmed
        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(placeholder)) {
                    textArea.setText("");
                    textArea.setForeground(TEXT_COLOR);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setText(placeholder);
                    textArea.setForeground(TEXT_COLOR.darker().darker()); // Dim placeholder
                }
            }
        });
    }

    /**
     * Helper method to apply common styling and hover effect to JButtons.
     */
    private void setupButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK); // Black text on vibrant buttons for contrast
        button.setFont(new Font("Arial", Font.BOLD, 15)); // Slightly larger font
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bgColor.darker().darker(), 2), // Darker border
            new EmptyBorder(10, 20, 10, 20) // More padding
        ));
        // Simple hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.brighter());
                button.setForeground(Color.WHITE); // White text on hover
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
                button.setForeground(Color.BLACK); // Back to black text
            }
        });
    }


    public static void main(String[] args) {
        // Ensure GUI creation is done on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MorseTranslatorGUI().setVisible(true);
            }
        });
    }
}