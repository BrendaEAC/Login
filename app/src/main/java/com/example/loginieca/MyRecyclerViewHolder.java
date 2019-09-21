package com.example.loginieca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import modelosdatos.Model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MyRecyclerViewHolder extends RecyclerView.Adapter<MyRecyclerViewHolder.ViewHolder> {


    //estructura de datos para linear
    //los elementos graficos
    private  RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;
    private ArrayList<Model> modelList;

    //crear un constructor para inicializar la lista de
    // modelos con los datos que manda firebase y poder usarlos

    public interface RecyclerViewOnItemClickListener{
        void onClick(View v, int position);
    }

    public MyRecyclerViewHolder(ArrayList<Model> modelList, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.modelList = modelList;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup , int viewType) {


        View view= LayoutInflater.from(viewGroup
                .getContext()).inflate(R.layout.model_item_db,
                viewGroup, false);

        return new ViewHolder((view));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Model model= modelList.get(i);


        viewHolder.lblId.setText(model.getId());
        viewHolder.lblGroup.setText(model.getGroup());
        viewHolder.lblActivity.setText(model.getActivity());
        viewHolder.lblLecture.setText(model.getLecture());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView lblId,lblActivity,lblGroup,lblLecture;
        public View view;


        //aqui vamos a inicializar los componentes graficos del xml



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            this.lblId=view.findViewById(R.id.lblIdModelItem);
            this.lblGroup=view.findViewById(R.id.lblGrupoModelItem);
            this.lblActivity=view.findViewById(R.id.lblActividadModelItem);
            this.lblLecture=view.findViewById(R.id.lblMateriaModelItem);

        }

        @Override
        public void onClick(View view) {
            recyclerViewOnItemClickListener.onClick(view, getAdapterPosition());
        }
    }
}
