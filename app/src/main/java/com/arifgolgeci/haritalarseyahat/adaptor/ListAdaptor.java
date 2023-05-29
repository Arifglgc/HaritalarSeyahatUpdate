package com.arifgolgeci.haritalarseyahat.adaptor;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arifgolgeci.haritalarseyahat.MainActivity;
import com.arifgolgeci.haritalarseyahat.R;
import com.arifgolgeci.haritalarseyahat.databinding.RecyclerRowListBinding;

import com.arifgolgeci.haritalarseyahat.model.ListPlace;

import java.util.List;

public class ListAdaptor extends RecyclerView.Adapter<ListAdaptor.ListHolder> {

    List<ListPlace> listPlace;
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    private OnItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public ListAdaptor(List<ListPlace> listPlace) {
        this.listPlace = listPlace;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerRowListBinding recyclerRowBinding = RecyclerRowListBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);

        return
                new ListAdaptor.ListHolder(recyclerRowBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder listHolder, int i) {
        listHolder.recyclerRowBinding.recyclerViewListTextView.setText(listPlace.get(i).name);

        Animation animation = AnimationUtils.loadAnimation(listHolder.itemView.getContext(), R.anim.slide_in_bottom);
        listHolder.itemView.startAnimation(animation);

        listHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(listHolder.itemView.getContext(),
                        MainActivity.class);
                //intent.putExtra("info","old");
                intent.putExtra("listId", listPlace.get(listHolder.getAdapterPosition()).uid);

                intent.putExtra("list",listPlace.get(listHolder.getAdapterPosition())); // supheli
                listHolder.itemView.getContext().startActivity(intent);
            }
        });

        listHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onItemLongClick(listHolder.getAdapterPosition());
                    return true;
                }
                return false;
            }
        });
    }






    @Override
    public int getItemCount() {
        return listPlace.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        RecyclerRowListBinding recyclerRowBinding;

        public ListHolder(RecyclerRowListBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding = recyclerRowBinding;
        }


    }
}
