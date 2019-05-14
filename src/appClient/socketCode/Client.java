import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket("localhost", 12345);
            PrintWriter pr = new PrintWriter(socket.getOutputStream());

            System.out.println("Server Connected");

            pr.println("Rizwan\nHasan");
            pr.flush();

            /*
            String msg = "";
            while (!msg.equals("exit")) {
                msg = new Scanner(System.in).nextLine();
                pr.println(msg);
                pr.flush();
            }
             */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
