import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class ChatClientGUI {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private JFrame frame;
    private JTextPane textPane;
    private JTextField textField;
    private JButton sendButton, clearButton;
    private static final String SECRET_KEY = "1234567890123456"; // 16-byte key for AES

    public ChatClientGUI(String serverAddress, int port) {
        frame = new JFrame("Java Chat");
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setBackground(Color.DARK_GRAY);
        textPane.setForeground(Color.LIGHT_GRAY);
        textPane.setFont(new Font("SansSerif", Font.ITALIC, 14));

        textField = new JTextField(40);
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textField.setBackground(Color.WHITE);

        sendButton = new JButton("Send");
        sendButton.setFocusPainted(false);
        sendButton.setBackground(Color.GREEN);
        sendButton.setForeground(Color.WHITE);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        clearButton = new JButton("Clear");
        clearButton.setFocusPainted(false);
        clearButton.setBackground(Color.RED);
        clearButton.setForeground(Color.WHITE);
        clearButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        clearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        sendButton.addActionListener(e -> sendMessage());
        clearButton.addActionListener(e -> textField.setText(""));
        textField.addActionListener(e -> sendMessage());

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(sendButton);
        buttonPanel.add(clearButton);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(textField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        JLabel header = new JLabel("Welcome to Java Chat", JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setOpaque(true);
        header.setBackground(Color.BLACK);
        header.setForeground(Color.GREEN);
        header.setPreferredSize(new Dimension(100, 40));

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(inputPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(header, BorderLayout.NORTH);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        try {
            socket = new Socket(serverAddress, port);
            appendMessage("Connected to server.\n", Color.GREEN);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(new ServerMessageHandler()).start();
        } catch (IOException e) {
            appendMessage("Error: " + e.getMessage() + "\n", Color.RED);
        }
    }

    private void sendMessage() {
        String message = textField.getText();
        if (!message.isEmpty()) {
            try {
                String encryptedMessage = encrypt(message);
                out.println(encryptedMessage);
                appendMessage("You: " + message, Color.CYAN);
                textField.setText("");
            } catch (Exception e) {
                appendMessage("Encryption error: " + e.getMessage(), Color.RED);
            }
        }
    }

    private void appendMessage(String message, Color color) {
        try {
            StyledDocument doc = textPane.getStyledDocument();
            Style style = textPane.addStyle("Style", null);
            StyleConstants.setForeground(style, color);
            StyleConstants.setItalic(style, true);
            doc.insertString(doc.getLength(), message + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private String encrypt(String strToEncrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    }

    private String decrypt(String strToDecrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }

    private class ServerMessageHandler implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    try {
                        String decryptedMessage = decrypt(message);
                        appendMessage("Server: " + decryptedMessage, Color.ORANGE);
                        Toolkit.getDefaultToolkit().beep();
                    } catch (Exception e) {
                        appendMessage("Decryption error: " + e.getMessage(), Color.RED);
                    }
                }
            } catch (IOException e) {
                appendMessage("Server disconnected.\n", Color.RED);
            }
        }
    }

    public static void main(String[] args) {
        new ChatClientGUI("localhost", 12345);
    }
}