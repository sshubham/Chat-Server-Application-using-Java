package com.ChatServer;

import java.io.DataInputStream;
import java.util.StringTokenizer;
import java.util.Vector;

class ClientThread implements Runnable {
    DataInputStream dis;
    MyClient client;

    ClientThread(DataInputStream var1, MyClient var2) {
        this.dis = var1;
        this.client = var2;
    }

    public void run() {
        String var1 ;

        while(true) {
            while(true) {
                try {
                    var1 = this.dis.readUTF();
                    if (var1.startsWith("updateuserslist:")) {
                        this.updateUsersList(var1);
                    } else {
                        if (var1.equals("@@logoutme@@:")) {
                            return;
                        }

                        this.client.txtBroadcast.append("\n" + var1);
                    }

                    int var2 = this.client.txtBroadcast.getLineStartOffset(this.client.txtBroadcast.getLineCount() - 1);
                    this.client.txtBroadcast.setCaretPosition(var2);
                } catch (Exception var3) {
                    this.client.txtBroadcast.append("\ncom.ChatServer.ClientThread run : " + var3);
                }
            }
        }
    }

    public void updateUsersList(String var1) {
        Vector var2 = new Vector();
        var1 = var1.replace("[", "");
        var1 = var1.replace("]", "");
        var1 = var1.replace("updateuserslist:", "");
        StringTokenizer var3 = new StringTokenizer(var1, ",");

        while(var3.hasMoreTokens()) {
            String var4 = var3.nextToken();
            var2.add(var4);
        }

        this.client.usersList.setListData(var2);
    }
}
