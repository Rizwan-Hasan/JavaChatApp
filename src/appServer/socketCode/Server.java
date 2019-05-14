import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private BufferedReader br;

    Server ()
    {
        try
        {
            this.serverSocket = new ServerSocket(12345);
            this.socket = serverSocket.accept();
            System.out.println("Client Connected");

            this.din = new DataInputStream(socket.getInputStream());
            this.dout = new DataOutputStream(socket.getOutputStream());
            this.br = new BufferedReader(new InputStreamReader(System.in));

            this.startTask();

            for (int i = 0; i < 100; i++) {
                System.out.println(i);
                Thread.sleep(1000);
            }

        }
        catch (Exception e) {
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
                System.out.println("client says: " + str);
                str2 = this.br.readLine();
                this.dout.writeUTF(str2);
                this.dout.flush();
            }
            //this.din.close();
            //this.socket.close();
            //this.serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args)
    {
        Server server = new Server();
    }



}
