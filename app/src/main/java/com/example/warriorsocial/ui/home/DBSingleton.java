package com.example.warriorsocial.ui.home;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

// Chloe Matthews
// gk6153
// Homework 5, Using Singleton Design Pattern
public class DBSingleton {

    // Self instance of DBSingleton class
    private static DBSingleton databaseConnection;
    // Database reference
    DatabaseReference databaseRef;

    // Constructor connects it to Firebase
    private DBSingleton(){
    databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized DBSingleton getInstance() {
        // Checks if first time instantiating
        if (databaseConnection == null){
            databaseConnection = new DBSingleton();
        }
        // Returns single instance
        return databaseConnection;
    }

    // Method getQuery returns the post reference based on year/month/day selected by user
    public Query getQuery(DBSingleton dbSingleton, int year, int month, int dayOfMonth) {
        // All posts
        return databaseRef.child("CalendarEvents/" + year + "/" + month + "/" + dayOfMonth);
    }
}
