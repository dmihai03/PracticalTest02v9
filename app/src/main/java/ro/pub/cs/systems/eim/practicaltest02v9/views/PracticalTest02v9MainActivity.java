package ro.pub.cs.systems.eim.practicaltest02v9.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import ro.pub.cs.systems.eim.practicaltest02v9.R;
import ro.pub.cs.systems.eim.practicaltest02v9.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02v9.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02v9.network.ServerThread;

public class PracticalTest02v9MainActivity extends AppCompatActivity {

    private ServerThread serverThread;
    private ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practical_test02v9_main);

        serverThread = new ServerThread(Constants.serverPort);
        serverThread.start();

        EditText clientPortEditText = (EditText) findViewById(R.id.client_port_edit_text);
        EditText numberEditText = (EditText) findViewById(R.id.number_edit_text);
        EditText wordEditText = (EditText) findViewById(R.id.word_edit_text);
        TextView resultTextView = (TextView) findViewById(R.id.response_text_view);

        Button getResultsButton = (Button) findViewById(R.id.get_results_button);

        getResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverAddressString = Constants.serverAddress;
                String clientPort = clientPortEditText.getText().toString();
                String word = wordEditText.getText().toString();
                String number = numberEditText.getText().toString();

                clientThread = new ClientThread(serverAddressString, Integer.parseInt(clientPort),
                        word, Integer.parseInt(number), resultTextView);
                clientThread.start();
            }
        });
    }
}
