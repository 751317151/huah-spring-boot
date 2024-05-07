package com.huah.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author huah
 * @Date 2024-03-29 16:56
 */
public class BioServer {
    public static void main(String[] args) throws IOException {
        System.out.println(">>>>>>>...BIO服务端启动...>>>>>>>>");
        // 1.定义一个ServerSocket服务端对象，并为其绑定端口号
        ServerSocket server = new ServerSocket(8888);
        // 2.监听客户端Socket连接
        Socket socket = server.accept();
        // 3.从套接字中得到字节输入流并封装成输入流对象
        InputStream inputStream = socket.getInputStream();
        BufferedReader readBuffer =
                new BufferedReader(new InputStreamReader(inputStream));
        // 4.从Buffer中读取信息，如果读到信息则输出
        String msg;
        while ((msg = readBuffer.readLine()) != null) {
            System.out.println("收到信息：" + msg);
        }
        // 5.从套接字中获取字节输出流并封装成输出对象
        OutputStream outputStream = socket.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        // 6.通过输出对象往服务端传递信息
        printStream.println("Hi！我是竹子~");
        // 7.发送后清空输出流中的信息
        printStream.flush();
        // 8.使用完成后关闭流对象与套接字
        outputStream.close();
        inputStream.close();
        socket.close();
        inputStream.close();
        outputStream.close();
        socket.close();
        server.close();
    }
}