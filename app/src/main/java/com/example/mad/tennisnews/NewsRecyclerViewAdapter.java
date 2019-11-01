package com.example.mad.tennisnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {

    private List<TennisNews> mNews;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private TennisNews currentTennisNews ;
    private Context mContext ;

    /**
     * The part of the time string from the Guardian  that we use to seperate date from time
     */


    public NewsRecyclerViewAdapter(Context context , List<TennisNews> tennisNews){
        this.mInflater = LayoutInflater.from(context);
        this.mNews = tennisNews ;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Find the TextView with view ID title
        @BindView(R.id.title_name)
        TextView titleTextView;

        // Find the TextView with view ID title
        @BindView(R.id.section_name)
        TextView sectionTextView;

        // Find the TextView with view ID date
        @BindView(R.id.date)
        TextView dateView;

        // Find the TextView with view ID time
        @BindView(R.id.time)
        TextView timeView;

         ViewHolder(@NonNull View itemView) {
            super(itemView);
             ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (mClickListener != null)
                mClickListener.onItemClick(v, getAdapterPosition() , mNews );

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = mInflater.inflate(R.layout.tennis_news_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        // Find the news at the given position in the list of news
        currentTennisNews = mNews.get(i);

        viewHolder.titleTextView.setText(currentTennisNews.getTitle());




        viewHolder.sectionTextView.setText(currentTennisNews.getSectionName());

        // Get the whole date and time of news
        String date_time = currentTennisNews.getTime();

        String date ;
        String time;

         String TIME_SEPARATOR =mContext.getString(R.string.t) ;


        String [] parts = date_time.split(TIME_SEPARATOR);

        date = parts[0];
        time = parts[1].substring(0,5);

        // Display the date of the nes in that TextView
        viewHolder.dateView.setText(date);

        // Display the time of the news in that TextView
        viewHolder.timeView.setText(time);


    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }



    // convenience method for getting data at click position
    String getItem(int id) {
        return mNews.get(id).getTitle();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position , List<TennisNews> list);
    }

    // methode to update recycle view with new values


    public void swapList(List<TennisNews> newsList) {
        if (mNews == newsList) {
            return;
        }
        this.mNews = newsList;
        if (newsList != null) {
            this.notifyDataSetChanged();
        }
    }

}
