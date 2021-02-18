package com.example.warriorsocial.ui.organizations;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String s1[], s2[];
    int images[]={R.drawable.acm,R.drawable.bio,R.drawable.chem,R.drawable.civil,R.drawable.elec,R.drawable.esd,R.drawable.mech,R.drawable.race,R.drawable.rob,R.drawable.swe};
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        recyclerView=findViewById(R.id.recyclerView);
        s1 = getResources().getStringArray(R.array.programming_languages);
        s2 = getResources().getStringArray(R.array.description);
        //imitialize new class and create new object
        MyAdapter myAdapter= new MyAdapter(this,s1, s2,images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
