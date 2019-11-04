package at.wifi.swdev.MyFestl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> implements Filterable {

    Context mContex;
    List<NewsItem> mData;
    List<NewsItem> mDataFiltered;

    public NewsAdapter(Context mContex, List<NewsItem> mData) {
        this.mContex = mContex;
        this.mData = mData;
        this.mDataFiltered = mData;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContex).inflate(R.layout.item_news, viewGroup, false);
        return new NewsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int position) {

        try {

            newsViewHolder.tv_title.setText(mDataFiltered.get(position).getTitle());
            newsViewHolder.tv_content.setText(mDataFiltered.get(position).getContent());
            newsViewHolder.tv_date.setText(mDataFiltered.get(position).getDate());
            newsViewHolder.img_user.setImageResource(mDataFiltered.get(position).getUserPhoto());
            newsViewHolder.tv_entfernungView.setText(mDataFiltered.get(position).getEntfernung());
        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return Math.min(mData.size(), 13);
        } else {
            return 0;
        }
        //    return mDataFiltered.size();


    }

    @Override
    public Filter getFilter() {


        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String Key = constraint.toString();
                if (Key.isEmpty()) {

                    mDataFiltered = mData;
                } else {
                    List<NewsItem> IstFiltered = new ArrayList<>();

                    for (NewsItem row : mData) {

                        if (row.getTitle().toLowerCase().contains(Key.toLowerCase())) { // Hier wird nach dem Titel gesucht
                            IstFiltered.add(row);
                        }

                        if (row.getContent().toLowerCase().contains(Key.toLowerCase())) { // hier wird nach dem Content gesucht
                            IstFiltered.add(row);
                        }

                        if (row.getDate().toLowerCase().contains(Key.toLowerCase())) { // hier wird nach dem Datum gesucht
                            IstFiltered.add(row);
                        }

                        if (row.getDate().toLowerCase().contains(Key.toLowerCase())) { // hier wird nach dem Datum gesucht
                            IstFiltered.add(row);
                        }
                    }
                    mDataFiltered = IstFiltered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDataFiltered = (List<NewsItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_content, tv_date;
        ImageView img_user;
        TextView kordinateb;
        TextView kordinatel;
        TextView tv_entfernungView;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_discription);
            tv_date = itemView.findViewById(R.id.tv_date);
            img_user = itemView.findViewById(R.id.img_user);
            kordinateb = itemView.findViewById(R.id.tv_kordinate1);
            kordinatel = itemView.findViewById(R.id.tv_koordinate2);
            tv_entfernungView = itemView.findViewById(R.id.tv_entfernung);
        }
    }
}
