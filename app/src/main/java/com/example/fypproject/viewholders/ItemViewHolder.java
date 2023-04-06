package com.example.fypproject.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypproject.R;
import com.example.fypproject.interfaces.ItemClickListener;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ImageView itemIcon;
    public TextView textViewItemName;
    public Button buttonDelete;
    private ItemClickListener itemClickListener;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        itemIcon = itemView.findViewById(R.id.itemIcon);
        textViewItemName = itemView.findViewById(R.id.itemName);
        buttonDelete = itemView.findViewById(R.id.buttonDelete);
    }

    @Override
    public void onClick(View view) {
         itemClickListener.onClick(view, getLayoutPosition(), false); //get layout position
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
