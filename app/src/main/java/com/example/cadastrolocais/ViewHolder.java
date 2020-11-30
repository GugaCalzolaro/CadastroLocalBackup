package com.example.cadastrolocais;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView mTitleTv, mDescriptionTv, mLatitudeTv, mLongitudeTv, mHorarioTv;
    View mView;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //Clique simples
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });

        //Clique longo
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

        //Inicializa as views com o Model_Layout.xml
        mTitleTv = itemView.findViewById(R.id.rTitleTv);
        mDescriptionTv = itemView.findViewById(R.id.rDescriptionTv);
        mLatitudeTv = itemView.findViewById(R.id.rLatitudeTv);
        mLongitudeTv = itemView.findViewById(R.id.rLongitudeTv);
        mHorarioTv = itemView.findViewById(R.id.rDataTv);
    }

    private ViewHolder.ClickListener mClickListener;

    //Interface para o Click Listener
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }





}
