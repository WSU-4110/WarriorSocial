package com.example.warriorsocial.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

// This is linked to the RecyclerView on the Home Fragment (list below calendar where events appear)
public class RecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {
    /*
        // Getting firebase dependency
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // collectionreference now references CalendarEvents collection in firebase
        protected Task<QuerySnapshot> calendarEvents = db.collection("CalendarEvents")
                .whereEqualTo("title", "[Birthday]")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Tag", document.getId() + " => " + document.getData());
                            }
                        }
                        else {
                            Log.d("Tag", "Error getting documents: ", task.getException()) ;
                        }
                    }
                });

     */

        // Represents the year, month, and dayOfMonth
        private int year;
        private int month;
        private int day;

        // For demo purposes in Sprint 1
        String eventArr[] = {"Software Engineering Club Meeting", "Alpha Gamma Delta Meeting", "American Red Cross Club",
        "Armenian Folk Dance Club", "Bagels for Homeless Club", "Black Student Union", "Country Music Club",
        "Detroit vs. Addiction", "Formula One Club", "LGBT+ People in Medicine"};

        // Constructor
        public RecyclerAdapter(int year, int month, int dayOfMonth) {
            this.year = year;
            this.month = month;
            this.day = dayOfMonth;
            //test statement
            System.out.println("Recieved!: " + year + " " + month + " " + dayOfMonth);
        }

        @Override
        // Position in the RecyclerView
        public int getItemViewType(final int position) {
            System.out.println("Inside getItemViewType() got position: " + position);
            return R.layout.home_item_view;
        }

        // Inflate the Layout (instantiates the layout into the view)
        @NonNull
        @Override
        public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            System.out.println("Inside onCreateViewHolder() | ViewGroup: " + parent.toString() + " | viewType: " + viewType);
            // viewType defines which view we're using
            // parent is where the new view will be added
            // attachToRoot is false because RecyclerViewer will handle when to attach
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            // Return the view
            return new HomeRecyclerViewHolder(view);
        }

        // Set text in RecyclerView. Iterates as many times as items in getItemCount()
        @Override
        public void onBindViewHolder(@NonNull HomeRecyclerViewHolder holder, int position) {
                System.out.println("Inside onBindViewHolder| Holder: " + holder.toString() + "| At Pos: " + position);
                holder.getView().setText(eventArr[position]);
                //holder.getView().setText(calendarEvents.getResult().getQuery().toString());
        }

        // Number of entries, currently linked to array of student clubs for Sprint 1
       @Override
        public int getItemCount() {
                System.out.println("Inside getItemCount(): returning: " + eventArr.length);
                return eventArr.length;
           /*if(calendarEvents.isSuccessful()) {
               System.out.println("Returning not 0: " + calendarEvents.getResult().size());
               return calendarEvents.getResult().size();
           }
           System.out.println("Returning a 0");
           return 0;*/
        }
}
