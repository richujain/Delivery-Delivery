package com.example.deliverydeliverycourier;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapterForHistory extends RecyclerView.Adapter<RecyclerAdapterForHistory.ViewHolder> {
    private static final String TAG = "RecyclerView";
    private Context context;
    private ArrayList<ModelClass> modelClassArrayList;

    public RecyclerAdapterForHistory(Context context, ArrayList<ModelClass> modelClassArrayList) {
        this.context = context;
        this.modelClassArrayList = modelClassArrayList;
    }

    @NonNull
    @Override
    public RecyclerAdapterForHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterForHistory.ViewHolder holder, final int position) {
        holder.statusOfOrder.setText(modelClassArrayList.get(position).getStatus());
        holder.offeredAmount.setText(modelClassArrayList.get(position).getOfferedAmount());
        holder.orderDate.setText(modelClassArrayList.get(position).getDate());

        Glide.with(context).load(modelClassArrayList.get(position).getImageurl())
                .into(holder.vehicleTypeCircularImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get key somehow
                String key = modelClassArrayList.get(position).getKey();
                //context.startActivity(new Intent(context,ViewOrderDetails.class));
                Intent intent = new Intent(context,ViewOrderDetails.class);
                intent.putExtra("key",key);
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "Position is "+position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return modelClassArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        //widgets
        ImageView vehicleTypeCircularImage;
        TextView statusOfOrder, orderDate, offeredAmount;

        public ViewHolder(View itemView){
            super(itemView);
            vehicleTypeCircularImage = itemView.findViewById(R.id.vehicleTypeCircularImage);
            statusOfOrder = itemView.findViewById(R.id.statusOfOrder);
            orderDate = itemView.findViewById(R.id.orderDate);
            offeredAmount = itemView.findViewById(R.id.offeredAmount);
        }
    }
}
