package com.example.front_end.controller;

import java.io.*;
import java.net.Socket;

public class ClientSock {
    private BufferedWriter os;
    private BufferedReader is;
    private Socket clientSocket;
    private int ID_Server;

    public void connectServer() throws IOException {
        clientSocket = new Socket("127.0.0.1", 5000);
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
}
