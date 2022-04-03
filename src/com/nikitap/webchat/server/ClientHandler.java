package com.nikitap.webchat.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            clientUsername = in.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
        } catch (IOException e) {
            closeEverything(socket, in, out);
        }
    }

    private void broadcastMessage(String message) {
        try {
            for (ClientHandler clientHandler : clientHandlers) {
                if (!clientHandler.clientUsername.equals(this.clientUsername)) {
                    clientHandler.out.write(message);
                    clientHandler.out.newLine();
                    clientHandler.out.flush();
                }
            }
        } catch (IOException e) {
            closeEverything(socket, in, out);
        }
    }

    private void closeEverything(Socket socket, BufferedReader in, BufferedWriter out) {
        removeClientHandler();
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER " + clientUsername + " has left the chat!");
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = in.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, in, out);
                break;
            }
        }
    }
}
