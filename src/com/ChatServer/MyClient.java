package com.ChatServer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class MyClient implements ActionListener {
    Socket s;
    DataInputStream dis;
    DataOutputStream dos;
    JButton sendButton;
    JButton logoutButton;
    JButton loginButton;
    JButton exitButton;
    JFrame chatWindow;
    JTextArea txtBroadcast;
    JTextArea txtMessage;
    JList usersList;

    public void displayGUI() {
        this.chatWindow = new JFrame();
        this.txtBroadcast = new JTextArea(5, 30);
        this.txtBroadcast.setEditable(false);
        this.txtMessage = new JTextArea(2, 20);
        this.usersList = new JList();
        this.sendButton = new JButton("Send");
        this.logoutButton = new JButton("Log out");
        this.loginButton = new JButton("Log in");
        this.exitButton = new JButton("Exit");
        JPanel var1 = new JPanel();
        var1.setLayout(new BorderLayout());
        var1.add(new JLabel("Broad Cast messages from all online users", 0), "North");
        var1.add(new JScrollPane(this.txtBroadcast), "Center");
        JPanel var2 = new JPanel();
        var2.setLayout(new FlowLayout());
        var2.add(new JScrollPane(this.txtMessage));
        var2.add(this.sendButton);
        JPanel var3 = new JPanel();
        var3.setLayout(new FlowLayout());
        var3.add(this.loginButton);
        var3.add(this.logoutButton);
        var3.add(this.exitButton);
        JPanel var4 = new JPanel();
        var4.setLayout(new GridLayout(2, 1));
        var4.add(var2);
        var4.add(var3);
        JPanel var5 = new JPanel();
        var5.setLayout(new BorderLayout());
        var5.add(new JLabel("Online Users", 0), "East");
        var5.add(new JScrollPane(this.usersList), "South");
        this.chatWindow.add(var5, "East");
        this.chatWindow.add(var1, "Center");
        this.chatWindow.add(var4, "South");
        this.chatWindow.pack();
        this.chatWindow.setTitle("Login for Chat");
        this.chatWindow.setDefaultCloseOperation(0);
        this.chatWindow.setVisible(true);
        this.sendButton.addActionListener(this);
        this.logoutButton.addActionListener(this);
        this.loginButton.addActionListener(this);
        this.exitButton.addActionListener(this);
        this.logoutButton.setEnabled(false);
        this.loginButton.setEnabled(true);
        this.txtMessage.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent var1) {
                MyClient.this.txtMessage.selectAll();
            }
        });
        this.chatWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent var1) {
                if (MyClient.this.s != null) {
                    JOptionPane.showMessageDialog(MyClient.this.chatWindow, "u r logged out right now. ", "Exit", 1);
                    MyClient.this.logoutSession();
                }

                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent var1) {
        JButton var2 = (JButton)var1.getSource();
        if (var2 == this.sendButton) {
            if (this.s == null) {
                JOptionPane.showMessageDialog(this.chatWindow, "u r not logged in. plz login first");
                return;
            }

            try {
                this.dos.writeUTF(this.txtMessage.getText());
                this.txtMessage.setText("");
            } catch (Exception var4) {
                this.txtBroadcast.append("\nsend button click :" + var4);
            }
        }

        if (var2 == this.loginButton) {
            String var3 = JOptionPane.showInputDialog(this.chatWindow, "Enter Your lovely nick name: ");
            if (var3 != null) {
                this.clientChat(var3);
            }
        }

        if (var2 == this.logoutButton && this.s != null) {
            this.logoutSession();
        }

        if (var2 == this.exitButton) {
            if (this.s != null) {
                JOptionPane.showMessageDialog(this.chatWindow, "u r logged out right now. ", "Exit", 1);
                this.logoutSession();
            }

            System.exit(0);
        }

    }

    public void logoutSession() {
        if (this.s != null) {
            try {
                this.dos.writeUTF("@@logoutme@@:");
                Thread.sleep(500L);
                this.s = null;
            } catch (Exception var2) {
                this.txtBroadcast.append("\n inside logoutSession Method" + var2);
            }

            this.logoutButton.setEnabled(false);
            this.loginButton.setEnabled(true);
            this.chatWindow.setTitle("Login for Chat");
        }
    }

    public void clientChat(String var1) {
        try {
            this.s = new Socket(InetAddress.getLocalHost(), 10);
            this.dis = new DataInputStream(this.s.getInputStream());
            this.dos = new DataOutputStream(this.s.getOutputStream());
            ClientThread var2 = new ClientThread(this.dis, this);
            Thread var3 = new Thread(var2);
            var3.start();
            this.dos.writeUTF(var1);
            this.chatWindow.setTitle(var1 + " Chat Window");
        } catch (Exception var4) {
            this.txtBroadcast.append("\nClient Constructor " + var4);
        }

        this.logoutButton.setEnabled(true);
        this.loginButton.setEnabled(false);
    }

    public MyClient() {
        this.displayGUI();
    }

    public static void main(String[] var0) {
        new MyClient();
    }
}
