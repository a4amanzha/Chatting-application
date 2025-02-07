Overview

This Java-based chat application enables secure, real-time messaging between a client and a server. It features a modern GUI and employs AES (Advanced Encryption Standard) for end-to-end encryption, ensuring message confidentiality and integrity.

Features

End-to-End Encryption (E2EE): Messages are encrypted using AES, ensuring only the sender and recipient can read them.

Graphical User Interface (GUI):

Dark-themed modern interface with JTextPane, JButton, and other Swing components.

Color-coded messages: Client messages in cyan, server messages in orange.

Real-time message display, auto-scrolling, and audio notifications for new messages.

Send and clear buttons with custom styling.

Socket Programming:

Uses Java's Socket and ServerSocket for network communication.

Multithreading ensures smooth, asynchronous message handling without UI freezing.

Custom Message Formatting:

Different fonts, sizes, and colors for enhanced readability.

Automatic alignment: Client messages on the right, server messages on the left.

Prerequisites

Java Development Kit (JDK) 8 or above.

An IDE like IntelliJ IDEA, Eclipse, or NetBeans (optional).

How to Run

1. Compile the Code

Open your terminal or IDE and compile both the server and client files:

javac ChatServerGUI.java
javac ChatClientGUI.java

2. Run the Server

Start the server first to listen for incoming connections:

java ChatServerGUI

3. Run the Client

Open another terminal window or run from your IDE:

java ChatClientGUI

The client will connect to the server automatically if it's running on localhost and port 12345. Adjust the IP and port if needed.

Encryption Details

Algorithm: AES (Advanced Encryption Standard)

Key Management: Both client and server use a shared secret key for encryption and decryption.

Confidentiality: Messages are encrypted before transmission and decrypted upon receipt.

File Structure

.
├── ChatServerGUI.java   # Server-side code with GUI and encryption
├── ChatClientGUI.java   # Client-side code with GUI and encryption
└── README.md            # Project documentation



License

This project is open-source and free to use under the MIT License.
