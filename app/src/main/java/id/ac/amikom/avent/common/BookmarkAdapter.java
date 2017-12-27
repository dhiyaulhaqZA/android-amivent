package id.ac.amikom.avent.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.amikom.avent.R;
import id.ac.amikom.avent.model.Bookmark;

/**
 * Created by dhiyaulhaqza on 12/27/17.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private List<Bookmark> bookmarkList;
    private Context context;

    public BookmarkAdapter() {
        bookmarkList = new ArrayList<>();
    }

    public void addBookmarks(List<Bookmark> bookmarks) {
        bookmarkList.clear();
        bookmarkList.addAll(bookmarks);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_bookmark_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_bookmark_title) TextView tvTitle;
        @BindView(R.id.tv_item_bookmark_description) TextView tvDescription;
        @BindView(R.id.tv_item_bookmark_date) TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position) {
            Bookmark bookmark = bookmarkList.get(position);
            if (bookmark != null) {
                tvTitle.setText(bookmark.getTitle());
                tvDescription.setText(bookmark.getDescription());
                tvDate.setText(bookmark.getDate());
            }
        }
    }
}
