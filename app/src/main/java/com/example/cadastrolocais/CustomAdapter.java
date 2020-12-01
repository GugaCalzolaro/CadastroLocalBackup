package com.example.cadastrolocais;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {
    ListActivity listActivity;
    List<Model> modelList;
    Context context;

    public CustomAdapter(ListActivity listActivity, List<Model> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_layout, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(itemView);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            //Metodo de toque longo
            @Override
            public void onItemLongClick(View view, final int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(listActivity);

                String[] options = { "Editar", "Excluir" }; //Opções da caixa de Alerta
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (which == 0){
                            String id = modelList.get(position).getId();
                            String title = modelList.get(position).getTitle();
                            String description = modelList.get(position).getDescription();
                            String latitude = modelList.get(position).getLatitude();
                            String longitude = modelList.get(position).getLongitude();
                            String horario = modelList.get(position).getHorario();

                            Intent intent = new Intent(listActivity, MainActivity.class);

                            intent.putExtra("pId", id);
                            intent.putExtra("pTitle", title);
                            intent.putExtra("pDescription", description);
                            intent.putExtra("pLatitude", latitude);
                            intent.putExtra("pLongitude", longitude);
                            intent.putExtra("pHorario", horario);

                            listActivity.startActivity(intent);


                        }
                        if (which == 1){
                            listActivity.deleteData(i);
                        }
                    }
                }).create().show();
            }
        });

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mTitleTv.setText(modelList.get(i).getTitle());
        viewHolder.mDescriptionTv.setText(modelList.get(i).getDescription());
        viewHolder.mLatitudeTv.setText(modelList.get(i).getLatitude());
        viewHolder.mLongitudeTv.setText(modelList.get(i).getLongitude());
        viewHolder.mHorarioTv.setText(modelList.get(i).getHorario());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
