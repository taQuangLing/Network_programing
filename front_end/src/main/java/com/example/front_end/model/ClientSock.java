package com.example.front_end.model;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ClientSock{
    private BufferedWriter os;
    private BufferedReader is;
    private DataInputStream in;
    private DataOutputStream out;
    protected Socket clientSocket;
    private int ID_Server;

    public ClientSock(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        System.out.println("Kết nối thành công!");

        // Tạo luồng đầu ra tại client (Gửi dữ liệu tới server)
        os = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        // Luồng đầu vào tại Client (Nhận dữ liệu từ server).
        is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream(clientSocket.getInputStream());

    }
    public String recv() throws IOException {
        return is.readLine();
    }
    public byte[] recvV2() throws IOException {
        byte[] buffer = new byte[1024];
        int length = in.read(buffer);
        return Arrays.copyOf(buffer, length);
    }
    public void sendV2(byte[] data, int lenght) throws IOException {
        out.write(data, 0, lenght);
        out.flush();
    }
    public void send(String message) throws IOException {
        os.write(message);
        os.flush();
    }
    public void closeSocket(){
        try {
            os.close();
            is.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
