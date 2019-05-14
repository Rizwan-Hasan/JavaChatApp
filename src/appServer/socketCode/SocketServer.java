package appServer.socketCode;

import appServer.Main;
import javafx.application.Platform;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SocketServer
{
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;
    public DataInputStream din;
    public DataOutputStream dout;
    public BufferedReader br;

    public void setPort (int port) {
        this.port = port;
    }

    public boolean isConntected () {
        boolean status;
        try {
            status = this.socket.isConnected();
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    public void closeConnection ()
    {
        try {
            socket.close();
            this.serverSocket = null;
        } catch (Exception e) {
            System.out.println("Closed");
        }
    }

    public void startServer ()
    {
        try {
            this.serverSocket = new ServerSocket(12345);
            this.socket = serverSocket.accept();
            System.out.println("Client Connected");

            this.din = new DataInputStream(socket.getInputStream());
            this.dout = new DataOutputStream(socket.getOutputStream());
            this.br = new BufferedReader(new InputStreamReader(System.in));

            this.startTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startTask ()
    {
        // Create a Runnable
        Runnable task = this::runTask;
        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Start the thread
        backgroundThread.start();
    }
    private void runTask ()
    {
        try {
            String str = "", str2 = "";
            while (!str.equals("stop")) {
                str = this.din.readUTF();
                //System.out.println("client says: " + str);
                final String tmp = str;
                Platform.runLater(() -> Main.controller.receiveMsgBox.appendText(tmp));
                Platform.runLater(() -> Main.controller.receiveMsgBox.appendText("\n-----\n"));
                str2 = this.br.readLine();
                this.dout.writeUTF(str2);
                this.dout.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
