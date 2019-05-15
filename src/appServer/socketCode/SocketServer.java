package appServer.socketCode;

import appServer.Main;
import javafx.application.Platform;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private final String msgSeparator = "\n----------\n";
    private Thread msgUpdaterThread;

    public void setPort(int port) {
        this.port = port;
    }

    public void closeConnection() {
        try {
            if (serverSocket.isBound()) {
                serverSocket.close();
                socket.close();
                this.msgUpdaterThread.interrupt();
            }
        } catch (Exception ignored) {
        }
    }

    public void startServer() {
        try {
            this.serverSocket = new ServerSocket(port);
            this.socket = serverSocket.accept();
            System.out.println("Client Connected");

            this.din = new DataInputStream(socket.getInputStream());
            this.dout = new DataOutputStream(socket.getOutputStream());

            this.msgUpdater();
        } catch (IOException ignored) {
        }
    }

    private void msgUpdater() {
        // Creating a Runnable
        Runnable task = () -> {
            try {
                String msg = "";
                while (!msg.equals("stop")) {
                    msg = this.din.readUTF();
                    final String tmp = msg;
                    Platform.runLater(() -> Main.controller.receiveMsgBox.appendText("Client: " + tmp));
                    Platform.runLater(() -> Main.controller.receiveMsgBox.appendText(this.msgSeparator));
                    Platform.runLater(() -> Main.controller.chatStatusLabel.setText("Client replied"));
                }
            } catch (Exception ignored) {
            }
        };

        // Run the task in a background thread
        this.msgUpdaterThread = new Thread(task);

        // Start the thread
        this.msgUpdaterThread.start();
    }

    public void sendMsg(String msg) {
        try {
            final String tmp = msg.trim();
            this.dout.writeUTF(tmp);
            this.dout.flush();
            Platform.runLater(() -> Main.controller.receiveMsgBox.appendText("Server: " + tmp));
            Platform.runLater(() -> Main.controller.receiveMsgBox.appendText(this.msgSeparator));
            Platform.runLater(() -> Main.controller.msgBox.setText(null));
        } catch (IOException ignored) {
            return;
        }
        Platform.runLater(() -> Main.controller.chatStatusLabel.setText("Message sent"));
    }

}
