package appClient.socketCode;

import appClient.Controller;
import appClient.Main;
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketClient {
    private final String msgSeparator = "--------------------------";
    private int port;
    private String server;
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private Thread msgUpdaterThread;

    public void setPort(int port) {
        this.port = port;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void closeConnection() {
        try {
            if (socket.isBound()) {
                socket.close();
                this.msgUpdaterThread.interrupt();
            }
        } catch (Exception ignored) {
        }
    }

    public void connectServer() {
        try {
            this.socket = new Socket(this.server, this.port);
            Platform.runLater(() -> Main.controller.statusLabel.setText("Connecting"));
            if (this.socket.isBound()) {
                System.out.println("Server connected");
                Platform.runLater(() -> {
                    Main.controller.chatStatusLabel.setText("Server connected");
                    Main.controller.statusLabel.setText("Connected");
                    Main.controller.connectServerBtn.setText("Disconnect");
                });

                this.din = new DataInputStream(socket.getInputStream());
                this.dout = new DataOutputStream(socket.getOutputStream());

                this.msgUpdater();
            } else {
                Platform.runLater(() -> {
                    Main.controller.statusLabel.setText("Connect");
                    Main.controller.chatStatusLabel.setText("Can not connect to the server");
                });
            }
        } catch (Exception ignored) {
        }
    }

    private void msgUpdater() {
        // Creating a Runnable
        Runnable task = () -> {
            try {
                String msg = "";
                while (!msg.equals("stop")) {
                    msg = this.din.readUTF();
                    System.out.println(msg);
                    final String tmp = msg.trim();
                    Platform.runLater(() -> {
                        Main.controller.receiveMsgBox.appendText(this.msgSeparator);
                        Main.controller.receiveMsgBox.appendText("\n" + "Server: " + tmp + "\n");
                        Main.controller.chatStatusLabel.setText("Server replied");
                    });
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
            if(!msg.equals(new Controller().ExitCode))
                Platform.runLater(() -> {
                    Main.controller.receiveMsgBox.appendText(this.msgSeparator);
                    Main.controller.receiveMsgBox.appendText("\n" + "You: " + tmp + "\n");
                });
            Platform.runLater(() -> Main.controller.msgBox.setText(null));
        } catch (IOException ignored) {
            return;
        }
        if(!msg.equals(new Controller().ExitCode))
            Platform.runLater(() -> Main.controller.chatStatusLabel.setText("Message sent"));
    }

}
