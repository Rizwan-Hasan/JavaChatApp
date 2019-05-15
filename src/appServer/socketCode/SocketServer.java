package appServer.socketCode;

import appServer.Main;
import javafx.application.Platform;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer
{
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private BufferedReader br;
    private Thread backgroundThread;

    public void setPort (int port) {
        this.port = port;
    }

    public void closeConnection ()
    {
        try {
            if (serverSocket.isBound()) {
                serverSocket.close();
                socket.close();
                this.backgroundThread.interrupt();
            }
        } catch (Exception ignored) {}
    }

    public void startServer ()
    {
        try {
            this.serverSocket = new ServerSocket(port);
            this.socket = serverSocket.accept();
            System.out.println("Client Connected");

            this.din = new DataInputStream(socket.getInputStream());
            this.dout = new DataOutputStream(socket.getOutputStream());
            this.br = new BufferedReader(new InputStreamReader(System.in));

            this.startTask();
        } catch (IOException ignored) {}
    }

    private void startTask ()
    {
        // Creating a Runnable
        Runnable task = () -> {
            try {
                String str = "", str2;
                while (!str.equals("stop")) {
                    str = this.din.readUTF();
                    final String tmp = str;
                    Platform.runLater(() -> Main.controller.receiveMsgBox.appendText("Client: " + tmp));
                    Platform.runLater(() -> Main.controller.receiveMsgBox.appendText("\n----------\n"));
                    str2 = this.br.readLine();
                    this.dout.writeUTF(str2);
                    this.dout.flush();
                }
            } catch (Exception ignored) {}
        };

        // Run the task in a background thread
        this.backgroundThread = new Thread(task);

        // Start the thread
        this.backgroundThread.start();
    }
}
