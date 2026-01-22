package ro.pub.cs.systems.eim.practicaltest02v9.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ro.pub.cs.systems.eim.practicaltest02v9.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02v9.general.Utilities;

public class CommunicationThread extends Thread {
    private ServerThread serverThread = null;
    private Socket socket = null;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[comm thread] run: Socket is null");
            return;
        }

        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            String word = bufferedReader.readLine();
            int number = Integer.parseInt(bufferedReader.readLine());

            String query = Constants.WEB_SERVICE_ADDRESS +
                    ":" + word;
            String pageSourceCode = "";

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(query)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                pageSourceCode = response.body().string();
            }

            if (pageSourceCode == null) {
                Log.e(Constants.TAG, "[comm thread]: error getting the info from the webservice");
                return;
            }

            Log.i(Constants.TAG, "[comm thread]: got the response from webservice");

            JSONObject content = new JSONObject(pageSourceCode);
            if(content.has("cod") && content.getString("cod").equals("404")){
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] '" + word + "' not found!");
                printWriter.println("'" + word + "' not found!");
                printWriter.flush();
                return;
            }

            JSONArray wordsArray = content.getJSONArray(Constants.ALL);

            String result = "";

            Log.i(Constants.TAG, "[conn thread]: number " + number);

            for (int i = 0; i < wordsArray.length(); i++) {
                if (wordsArray.getString(i).length() > number) {
                    result += wordsArray.getString(i) + " ";
                }
            }

            result += "\n";

            printWriter.println(result);
            printWriter.flush();

            Log.i(Constants.TAG, "[comm thread]: sent response to client");

        } catch (IOException e) {
            Log.e(Constants.TAG, "[comm thread ]: io exception " + e.getMessage());
        } catch (JSONException e) {
            Log.e(Constants.TAG, "[comm thread]: json exception " + e.getMessage());
        }
    }
}
