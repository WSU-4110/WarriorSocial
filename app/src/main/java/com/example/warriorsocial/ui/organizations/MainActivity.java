package com.example.warriorsocial.ui.organizations;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;

//student organizations

public class MainActivity extends AppCompatActivity {
    //RecyclerView object
    RecyclerView recyclerView;
    String s1[], s2[];
    int images[]={R.drawable.acm,R.drawable.bio,R.drawable.chem,R.drawable.civil,R.drawable.elec,R.drawable.esd,R.drawable.mech,R.drawable.race,R.drawable.rob,R.drawable.swe};
    @Override
    //onCreate method
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orgs);//was activity_main1
        //finding the id for the recyclerView
        recyclerView=findViewById(R.id.recyclerView);
        //store the string array inside the string variable s1
        s1 = getResources().getStringArray(R.array.student_organizations);
        //store string array for descriptions
        s2 = getResources().getStringArray(R.array.description);
        //initialize new class and create new object and pass four parameters from adapter to main activity
        MyAdapter myAdapter= new MyAdapter(this,s1, s2,images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
