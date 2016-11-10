package com.example.di.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    //below Lab5 added
    //private ChatDatabaseHelper dhHelper;
    private SQLiteDatabase db = null;  //a SQLiteDatabase object
    protected static final String ACTIVITY_NAME = "ChatWindow";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //Lab7  //setContentView(R.layout.activity_chat_window);

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

                //Lab5 ADDED
                ContentValues values = new ContentValues();
                values.put(ChatDatabaseHelper.KEY_MESSAGE, inputMessage.getText().toString());
                db.insert(ChatDatabaseHelper.TABLE_NAME,null,values);//teacher added

                messageAdapter.notifyDataSetChanged();//this restarts the process of getCount()/getView()
                inputMessage.setText("");

                 //Cursor results1 = db.query(false, MyDatabaseHelper.TABLENAME, new String[] {"Price"}, "? < ?", new String[]{"Price","7","ignore","ignore"}, null,null,null,null);

            }
        });


        //below LAB5 added
        ChatDatabaseHelper dhHelper = new ChatDatabaseHelper(this); //creates a temporary ChatDatabaseHelper object
        //dhHelper.getReadableDatabase(); //open it as read-only
        db = dhHelper.getWritableDatabase(); //open it for both read and write

        Cursor cursor = db.query(ChatDatabaseHelper.TABLE_NAME, new String[]
                {ChatDatabaseHelper.KEY_ID,ChatDatabaseHelper.KEY_MESSAGE},null,null,null,null,null);

        Log.i(ACTIVITY_NAME, "Cursor’s  column count = " + cursor.getColumnCount() );

        //int colIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);

        for(int i = 0; i < cursor.getColumnCount(); i++){
           Log.i(ACTIVITY_NAME, "Cursor's column Name: " + cursor.getColumnName(i));
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString( cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ) );
            messages.add(cursor.getString(1));
            cursor.moveToNext();

        }
        // make sure to close the cursor
        cursor.close();

        // END Lab5 ADDED
    }

    //Lab5 ADDED
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }//end added

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


