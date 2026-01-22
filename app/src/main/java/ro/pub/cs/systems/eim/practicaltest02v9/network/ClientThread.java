package ro.pub.cs.systems.eim.practicaltest02v9.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02v9.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02v9.general.Utilities;

public class ClientThread extends Thread{
    private String address;
    private int port;
    private String word;
    private int number;

    private Socket socket;

    public ClientThread(String address, int port, String word, int number) {
        this.address = address;
        this.port = port;
        this.word = word;
        this.number = number;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            
            PrintWriter printWriter = Utilities.getWriter(socket);

            printWriter.println(word);
            printWriter.flush();

            printWriter.println(number);
            printWriter.flush();

            BufferedReader bufferedReader = Utilities.getReader(socket);
            String response;

            response = bufferedReader.readLine();

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.e(Constants.TAG, "run: Exception has occurred " + e.getMessage());
                }
            }
        }
    }
}
