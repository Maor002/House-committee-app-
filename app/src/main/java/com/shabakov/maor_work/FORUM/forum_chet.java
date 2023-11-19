package com.shabakov.maor_work.FORUM;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shabakov.maor_work.MODELS.UserDetail;
import com.shabakov.maor_work.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

public class forum_chet extends AppCompatActivity {
    private static final String TAG = "RecyclerViewAdapter";
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private ImageButton buttonSend;
    private boolean side = false;
    static UserDetail userDetail;
    private String uid = "null";
    static Vector<Message> messagesItems = new Vector<Message>();
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore fstore;
    private FirebaseUser fUser;
    private FirebaseAuth mFirebaseAuth;
    private String currentDate;
    private String key = "0";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_chet);
        listView = (ListView) findViewById(R.id.messages_view2);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.my_message, messagesItems);
        listView.setAdapter(chatArrayAdapter);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        currentDate = df.format(c.getTime());

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fstore = FirebaseFirestore.getInstance();
        buttonSend = findViewById(R.id.send2);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = fUser.getUid();
        get_id_Building();
        get_forum_list();
        chatText = (EditText) findViewById(R.id.editText_chat);
        //        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //                listView.removeViewAt(position);
        //            }
        //        });

        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();

                }
                return false;
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {

                new AlertDialog.Builder(forum_chet.this)
                        .setTitle("האם אתה בטוח? ")
                        .setMessage("אתה רוצה למחוק את הפריט?")
                        .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                chatArrayAdapter.remove(messagesItems.get(position));
                                messagesItems.get(position).delete_message(userDetail.getId_Building());
                                chatArrayAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("לא", null)
                        .show();

                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String message = chatText.getText().toString().trim();
                if (!message.isEmpty())
                    sendChatMessage();
                else
                    Toast.makeText(forum_chet.this, "אי אפשר לשלוח הודעה ריקה ", Toast.LENGTH_SHORT).show();
            }
        });

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                    sleep(100);
                    get_forum_list();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    private boolean sendChatMessage() {

        Message message1 = new Message(chatText.getText().toString().trim(), currentDate, userDetail.getFullName(), false, uid, key);
        chatArrayAdapter.add(message1);
        chatText.setText("");
        listView.setSelection(listView.getCount() - 1);
        side = !side;
        Map<String, Object> hashMap = new HashMap<>();
        //uid=hashMap
        hashMap.put("Date", message1.getDate());
        hashMap.put("belongsToCurrentUser", message1.isBelongsToCurrentUser());
        hashMap.put("message_content", message1.getMessage_content());
        hashMap.put("Name", message1.getName());
        hashMap.put("uid", message1.getUid());
        hashMap.put("key", message1.getKey());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Forum").child(userDetail.getId_Building()).push().setValue(hashMap);
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Forum").child(userDetail.getId_Building());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    key = ds.getKey();
                }
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals(key)) {
                        ref1.child(key).child("key").setValue(key);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return true;
    }

    public void get_id_Building() {
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userID;
                userID = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals(userID)) {
                        userDetail = ds.getValue(UserDetail.class);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void get_forum_list() {

        DatabaseReference ref2 = null;
        try {
            ref2 = FirebaseDatabase.getInstance().getReference("Forum").child(userDetail.getId_Building());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ref2 = FirebaseDatabase.getInstance().getReference("Forum").child(userDetail.getId_Building());
        ref2.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messagesItems.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Message message10 = new Message("1", "2", "3", false, "4", key);
                    message10 = ds.getValue(Message.class);
                    if (!uid.equals(message10.getUid()))
                        message10.setBelongsToCurrentUser(true);
                    messagesItems.add(message10);
                }
                chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.my_message, messagesItems);
                listView.setAdapter(chatArrayAdapter);
                chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        listView.setSelection(chatArrayAdapter.getCount() - 1);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

}