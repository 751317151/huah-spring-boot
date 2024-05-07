package com.huah.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @Author huah
 * @Date 2024-03-29 16:56
 */
public class BioClient {
    public static void main(String[] args) throws IOException {
        System.out.println(">>>>>>>...BIO客户端启动...>>>>>>>>");
        // 1.创建Socket并根据IP地址与端口连接服务端
        Socket socket = new Socket("127.0.0.1", 8888);
        // 2.从Socket对象中获取一个字节输出流并封装成输出对象
        OutputStream outputStream = socket.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        // 3.通过输出对象往服务端传递信息
        printStream.println("Hello！我是熊猫~");
        // 4.通过下述方法告诉服务端已经完成发送，接下来只接收消息
        socket.shutdownOutput();
        // 5.从套接字中获取字节输入流并封装成输入对象
        InputStream inputStream = socket.getInputStream();
        BufferedReader readBuffer =
                new BufferedReader(new InputStreamReader(inputStream));
        // 6.通过输入对象从Buffer读取信息
        String msg;
        while ((msg = readBuffer.readLine()) != null) {
            System.out.println("收到信息：" + msg);
        }
        // 7.发送后清空输出流中的信息
        printStream.flush();
        // 8.使用完成后关闭流对象与套接字
        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
