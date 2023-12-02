package com.example.mychatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.mychatroom.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    String receiverId, receiverRoom, senderRoom;
    DatabaseReference databaseReferenceSender, databaseReferenceReceiver;
    MsgAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        receiverId = getIntent().getStringExtra("id");

        senderRoom = FirebaseAuth.getInstance().getUid() + receiverId;
        receiverRoom = receiverId + FirebaseAuth.getInstance().getUid();

        msgAdapter = new MsgAdapter(this);
        binding.recycler.setAdapter(msgAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        databaseReferenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        databaseReferenceReceiver = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);

        databaseReferenceSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgAdapter.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MsgModel msgModel = dataSnapshot.getValue(MsgModel.class);
                    msgAdapter.add(msgModel);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.ivSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = binding.etMsg.getText().toString();
                if (msg.trim().length() > 0){
                    sendMsg(msg);
                }
            }
        });
    }

    private void sendMsg(String msg) {
        String msgId = UUID.randomUUID().toString();
        MsgModel msgModel = new MsgModel(msgId, FirebaseAuth.getInstance().getUid(), msg);

        msgAdapter.add(msgModel);

        databaseReferenceSender
                .child(msgId)
                .setValue(msgModel);
        databaseReferenceReceiver
                .child(msgId)
                .setValue(msgModel);
    }
}