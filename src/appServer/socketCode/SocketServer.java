package appServer.socketCode;

import appServer.Main;
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    private final String msgSeparator = "--------------------------";
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private Thread msgUpdaterThread;
    private String ExitCode = "@_EXIT_@";

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

            System.out.println("Client connected");
            Platform.runLater(() -> Main.controller.chatStatusLabel.setText("Client connected"));

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
                while (true) {
                    msg = this.din.readUTF();
                    System.out.println(msg);
                    final String tmp = msg.trim();
                    if (!tmp.equals(this.ExitCode)) {
                        Platform.runLater(() -> {
                            Main.controller.receiveMsgBox.appendText(this.msgSeparator);
                            Main.controller.receiveMsgBox.appendText("\n" + "Client: " + tmp + "\n");
                            Main.controller.chatStatusLabel.setText("Client replied");
                        });
                    } else {
                        Platform.runLater(() -> Main.controller.startServerBtn.fire());
                    }
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
            Platform.runLater(() -> Main.controller.receiveMsgBox.appendText(this.msgSeparator));
            Platform.runLater(() -> Main.controller.receiveMsgBox.appendText("\n" + "You: " + tmp + "\n"));
            Platform.runLater(() -> Main.controller.msgBox.setText(null));
        } catch (IOException ignored) {
            return;
        }
        Platform.runLater(() -> Main.controller.chatStatusLabel.setText("Message sent"));
    }

}
