package com.example.di.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.di.lab1.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Messages. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MessageDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MessageListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    protected static final String ACTIVITY_NAME = "MessageListActivity";
    private ListView listViewMessages;
    private EditText inputMessage;
    private Button btnSend;
    ArrayList<String> messages;
    private ChatAdapter messageAdapter;
    private SQLiteDatabase db ;  //a SQLiteDatabase object
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());



        //View recyclerView = findViewById(R.id.message_list);
        //assert recyclerView != null;
        //setupRecyclerView((RecyclerView) recyclerView);

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


        if (findViewById(R.id.message_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MessageDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        MessageDetailFragment fragment = new MessageDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.message_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MessageDetailActivity.class);
                        intent.putExtra(MessageDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
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
            LayoutInflater inflater = MessageListActivity.this.getLayoutInflater();//lab7 change chatAdapter.tihs to ...
            //LayoutInflater inflater = (LayoutInflater) ChatWindowActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View result = null;
            Log.i("Position::", Integer.toString(position));
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            else
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            TextView message = (TextView)result.findViewById(R.id.message_text);
            //message.setText( getItem(position)  ); // get the string at position

            //Lab7
            final String messageText = getItem(position);
            message.setText(messageText);

            //Lab7
            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MessageDetailFragment.ARG_ITEM_ID,messageText);
                        MessageDetailFragment fragment = new MessageDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.message_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MessageDetailActivity.class);
                        intent.putExtra(MessageDetailFragment.ARG_ITEM_ID,messageText);

                        context.startActivity(intent);
                    }
                }
            });


            return result;

        }
    }
}
