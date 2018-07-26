package com.ChatServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class MyServer {
    ArrayList al = new ArrayList();
    ArrayList users = new ArrayList();
    ServerSocket ss;
    Socket s;
    public static final int PORT = 5000;
    public static final String UPDATE_USERS = "updateuserslist:";
    public static final String LOGOUT_MESSAGE = "@@logoutme@@:";

    public MyServer() {
        try {
            this.ss = new ServerSocket(PORT);
            System.out.println("Server Started " + this.ss);

            while(true) {
                this.s = this.ss.accept();
                MyThread var1 = new MyThread(this.s, this.al, this.users);
                Thread var2 = new Thread(var1);
                var2.start();
            }
        } catch (Exception var3) {
            System.err.println("Server constructor" + var3);
        }
    }

    public static void main(String[] var0) {
        new MyServer();
    }
}
