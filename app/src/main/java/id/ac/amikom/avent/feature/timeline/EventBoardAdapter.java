package id.ac.amikom.avent.feature.timeline;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.ac.amikom.avent.R;
import id.ac.amikom.avent.model.Event;
import id.ac.amikom.avent.utility.ImageUtil;

/**
 * Created by dhiyaulhaqza on 12/4/17.
 */

public class EventBoardAdapter extends RecyclerView.Adapter<EventBoardAdapter.ViewHolder> {

    public List<Event> eventList;
    private EventClickListener eventClickListener;

    public EventBoardAdapter(EventClickListener eventClickListener) {
        eventList = new ArrayList<>();
        this.eventClickListener = eventClickListener;
    }

    public void addEvent(Event event) {
        eventList.add(event);
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        eventList.clear();
    }

    @Override
    public EventBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventBoardAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mImgPoster;
        private TextView mTvTitle;
        private TextView mTvOrganizer;
        private TextView mTvDate;

        public ViewHolder(View itemView) {
            super(itemView);

            mImgPoster = itemView.findViewById(R.id.img_item_event_poster);
            mTvTitle = itemView.findViewById(R.id.tv_item_event_title);
            mTvOrganizer = itemView.findViewById(R.id.tv_item_event_organizer);
            mTvDate = itemView.findViewById(R.id.tv_item_event_date);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            Event event = eventList.get(position);
            if (event != null) {
                mTvTitle.setText(event.getTitle());
                mTvOrganizer.setText(event.getOrganizer());
                mTvDate.setText(event.getDate());
                ImageUtil.loadImageFromUrl(mImgPoster, event.getPosterUrl());
            }
        }

        @Override
        public void onClick(View v) {
            eventClickListener.onEventClick(eventList.get(getAdapterPosition()));
        }
    }

    public interface EventClickListener {
        void onEventClick(Event event);
    }
}
