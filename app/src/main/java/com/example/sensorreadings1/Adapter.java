package com.example.sensorreadings1;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    int fullSize;
    List<Integer> images=new ArrayList<>();
    List<String> names;
    List<Integer> values;
    HashMap<String, String> map;
    Context ctx;

    public Adapter(Context ctx, List<String> names, List<Integer> values){
          this.names=names;
            Log.v("namesadap", String.valueOf(this.names));
          this.ctx=ctx;
          this.values=values;
          putImages();
    }
    public Adapter(Context ctx, HashMap<String, String> map){
        this.map=map;
        this.ctx=ctx;
        putImages();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(ctx);
        View view=inflater.inflate(R.layout.custom_grid_layout,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(names.size()==1){
            fullSize=370;
        }else if(names.size()==2)
            fullSize= 280;
        else  fullSize= 180;
        int left=dpToPx(10);
        int right=dpToPx(10);
        int top=dpToPx(10);
        int bottom=dpToPx(10);
        RecyclerView.LayoutParams rp= new RecyclerView.LayoutParams(dpToPx(fullSize),dpToPx(fullSize));
        holder.cardView.setMinimumHeight(150);
        holder.cardView.setMinimumWidth(150);
        rp.setMargins(left,top,right,bottom);
        holder.cardView.setLayoutParams(rp);

        holder.name.setText(String.format("%s %s %%", names.get(position), values.get(position)));
        holder.imageView.setAdjustViewBounds(true);
        holder.imageView.setImageResource(extractImages(position,(values.get(position))));
        if(values.get(position)<21){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#CD5C5C"));
        }

    }

    @Override
    public int getItemCount() {
        return names.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        ImageView imageView;
        TextView name;
        TextView names1;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //progressBar=itemView.findViewById(R.id.progressBar);
            name=itemView.findViewById(R.id.textView);
            imageView=(ImageView) itemView.findViewById(R.id.ic_tank);
            cardView=itemView.findViewById(R.id.cardView1);
        }
    }

    public int dpToPx(int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
    public int calcSize(int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
    public void putImages() {
        images.add(R.drawable.ic_tank0);
        images.add(R.drawable.ic_tank1);
        images.add(R.drawable.ic_tank2);
        images.add(R.drawable.ic_tank3);
        images.add(R.drawable.ic_tank4);
        images.add(R.drawable.ic_tank5);
        images.add(R.drawable.ic_tank6);
        images.add(R.drawable.ic_tank7);
        images.add(R.drawable.ic_tank8);
    }

    public int extractImages(int position, int values){
        if(values>=0 && values<=10){
            return images.get(0);
        }else if(values>10 && values<20){
            return images.get(1);
        }else if(values>=20 && values<30){
            return images.get(1);
        }else if(values>=30 && values<40){
            return images.get(2);
        }else if(values>=40 && values<50){
            return images.get(3);
        }else if(values>=50 && values<60){
            return images.get(4);
        }else if(values>=60 && values<70){
            return images.get(4);
        }else if(values>=70 && values<80){
            return images.get(5);
        }else if(values>=80 && values<90){
            return images.get(6);
        }else if(values>=90)
            return images.get(8);
        else return images.get(0);

    }

}
