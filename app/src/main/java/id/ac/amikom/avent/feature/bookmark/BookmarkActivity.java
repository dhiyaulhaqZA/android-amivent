package id.ac.amikom.avent.feature.bookmark;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.amikom.avent.R;
import id.ac.amikom.avent.common.BookmarkAdapter;
import id.ac.amikom.avent.model.Bookmark;

public class BookmarkActivity extends AppCompatActivity {

    @BindView(R.id.rv_bookmark) RecyclerView rvBookmark;

    private BookmarkAdapter bookmarkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        ButterKnife.bind(this);
        setTitle("Bookmark");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setupRecyclerView();
        showBookmarkDummyData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupRecyclerView() {
        rvBookmark.setHasFixedSize(true);
        rvBookmark.setLayoutManager(new LinearLayoutManager(this));
        bookmarkAdapter = new BookmarkAdapter();
        rvBookmark.setAdapter(bookmarkAdapter);
    }

    private void showBookmarkDummyData() {
        List<Bookmark> bookmarks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            bookmarks.add(new Bookmark("React native", "Membuat aplikasi react native", "7 Dec"));
        }

        bookmarkAdapter.addBookmarks(bookmarks);
    }


}
