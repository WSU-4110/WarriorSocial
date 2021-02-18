package com.example.warriorsocial.ui.organizations;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;
//this is a java class for recyclreView Adapter
//extends a class that passes one parameter that will be the inner class
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    //new variables to hold values to pass inside the main activity
    String data1[], data2[];
    int images[];
    Context context;

    //constructor with four parameters
    public MyAdapter(Context ct, String s1[], String s2[], int img[]){
        //store the values from above inside the constructor
        context=ct;
        data1=s1;
        data2=s2;
        images = img;
    }
    //MyAdapter method
    @NonNull
    @Override
    //connected to the MyViewHolder class down below
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout inflater object
        LayoutInflater inflater= LayoutInflater.from(context);
        //inflate my_row xml file
        //store inflater inside view to return view
        View view = inflater.inflate(R.layout.my_row, parent, false);
        //Pass view to MyViewHolder class
        return new MyViewHolder(view);

    }
    //MyAdapter method
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //using holder to find myText1 to set the text dynamically
        holder.myText1.setText(data1[position]);
        holder.myText2.setText(data2[position]);
        holder.myImage.setImageResource(images[position]);
    }
    //MyAdapter method
    @Override
    public int getItemCount() {
    //pass the number of items we have in our arrays by using the dynamic method of getting the length
        return images.length;
    }

    //another inner class
    public class MyViewHolder extends RecyclerView.ViewHolder{
        //objects
        TextView myText1, myText2;
        ImageView myImage;
        //receive the view from above and communicates with onBindHolder
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //find the id
            myText1 = itemView.findViewById(R.id.myText1);
            myText2 = itemView.findViewById(R.id.myText2);
            myImage = itemView.findViewById(R.id.myImageView);


        }
    }
}
