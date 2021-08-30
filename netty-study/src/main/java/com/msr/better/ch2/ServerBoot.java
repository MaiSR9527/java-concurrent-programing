package com.msr.better.ch2;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-24 21:22:54
 */
public class ServerBoot {

    private static final int PORT = 8000;

    public static void main(String[] args) {
        Server server = new Server(PORT);
        server.start();
    }
}
