package com.example.front_end.model;

import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;

public class ClientSock{
    private BufferedWriter os;
    private BufferedReader is;
    protected Socket clientSocket;
    private int ID_Server;

    public ClientSock(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        System.out.println("Kết nối thành công!");

        // Tạo luồng đầu ra tại client (Gửi dữ liệu tới server)
        os = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        // Luồng đầu vào tại Client (Nhận dữ liệu từ server).
        is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    public String recv() throws IOException {
        return is.readLine();
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
