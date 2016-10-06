package com.example.di.lab1;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    private ListView listViewMessages;
    private EditText inputMessage;
    private Button btnSend;
    ArrayList<String> messages;
    private ChatAdapter messageAdapter;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    // private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        listViewMessages = (ListView) findViewById(R.id.listView);
        inputMessage = (EditText) findViewById(R.id.textInput);
        btnSend = (Button) findViewById(R.id.bnt_Send);
        messages = new ArrayList<>();

        messageAdapter = new ChatAdapter(this);//in this case, "this" is the ChatWindow,which is-A Context object
        listViewMessages.setAdapter((ListAdapter) messageAdapter);
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                messages.add(inputMessage.getText().toString());
                messageAdapter.notifyDataSetChanged();//this restarts the process of getCount()/getView()
                inputMessage.setText("");
            }
        });
    }

    class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            Log.i("Size::", Integer.toString(messages.size()));
            return messages.size();
        }

        public String getItem(int position) {
            return messages.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            //LayoutInflater inflater = (LayoutInflater) ChatWindowActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View result = null;
            //Log.i("Position::", Integer.toString(position));
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            else
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText( getItem(position)  ); // get the string at position
            return result;

        }
    }


}


