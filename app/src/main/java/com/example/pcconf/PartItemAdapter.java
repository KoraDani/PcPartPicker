package com.example.pcconf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PartItemAdapter extends RecyclerView.Adapter<PartItemAdapter.ViewHolder> implements Filterable{

    private static final String LOG_TAG = PartItemAdapter.class.getName();
    private ArrayList partList;
    private ArrayList<Part> partListAll;
    private Context context;
    private int lastPosition = -1;

    private boolean visibel = false;

    public PartItemAdapter(Context context,ArrayList<Part> partList) {
        this.partList = partList;
        this.partListAll = partList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_part, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PartItemAdapter.ViewHolder holder, int position) {
        Part currentPart = (Part) partList.get(position);

        holder.bindTo(currentPart);

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }

    }

    @Override
    public int getItemCount() {
        return partList.size();
    }

    @Override
    public Filter getFilter() {
        return partFilert;
    }

    private Filter partFilert = new Filter() {
        ArrayList<Part> filteredList = new ArrayList<>();
        FilterResults filterResults = new FilterResults();
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            if(constraint == null || constraint.length() == 0){
                filterResults.count = partListAll.size();
                filterResults.values = partListAll;
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Part p : partListAll){
                    if(p.getBrand().toLowerCase().contains(filterPattern)){
                        filteredList.add(p);
                    }
                }
                filterResults.count = filteredList.size();
                filterResults.values = filteredList;
            }

            return filterResults;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            partList = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView partBrandTextView;
        private TextView partModelTextView;
        private TextView partSocketTextView;
        private TextView partInfoTextView;
        private TextView priceTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            partBrandTextView = itemView.findViewById(R.id.partBrandTextView);
            partModelTextView = itemView.findViewById(R.id.partModelTextView);
            partSocketTextView = itemView.findViewById(R.id.partSocketTextView);
            partInfoTextView = itemView.findViewById(R.id.partInfoTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);

        }


        public void bindTo(Part currentPart) {
            partBrandTextView.setText(currentPart.getBrand());
            partModelTextView.setText(currentPart.getModel());
            partSocketTextView.setText(currentPart.getSocket());
            partInfoTextView.setText(currentPart.getInfo());
            priceTextView.setText(String.valueOf(currentPart.getPrice()));

            itemView.findViewById(R.id.deleteButton).setOnClickListener(v -> {
                ((PcPartListActivity)context).deletePart(currentPart);
            });

            itemView.findViewById(R.id.updateButton).setOnClickListener(v -> {
                ((PcPartListActivity)context).updatePart(currentPart);
            });
        }
    }
}


