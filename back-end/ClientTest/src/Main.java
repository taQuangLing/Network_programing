import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try
        {
            Socket socket = new Socket("127.0.0.1", 5003);
            OutputStream output = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(output);
//            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            BufferedReader inFromServer = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);
            String str2;
            while(true){
                String str = scanner.nextLine();
                dataOutputStream.writeBytes(str);
                dataOutputStream.flush();
                if (str.equals("quit")){
                    break;
                }
                str2 = inFromServer.readLine();
                System.out.println("server say: " + str2);
            }
            dataOutputStream.close();
            inFromServer.close();
            socket.close();
        }
        catch(Exception e){
            System.out.println("ERROR_SYSTEM!!!");}
    }

}