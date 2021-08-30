package com.msr.better.ch2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-24 21:22:54
 */
@SuppressWarnings("All")
public class Server {
    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("服务端启动成功，端口：" + port);
        } catch (IOException e) {
            System.out.println("发生异常启动失败！");
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(() -> doStart()).start();
    }

    private void doStart() {
        while (true) {
            try {
                Socket client = serverSocket.accept();
                new ClientHandler(client).start();
            } catch (IOException e) {
                System.out.println("服务端接收客户端连接时异常！");
                e.printStackTrace();
            }
        }
    }

}
