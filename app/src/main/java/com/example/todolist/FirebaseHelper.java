package com.example.todolist;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    // Write a message to the database
    static FirebaseDatabase database;
    static DatabaseReference myRef;

    public DatabaseReference init(String path) {
        if (database != null) { return myRef; }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(path);
        return myRef;
    }
}
