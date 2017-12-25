package id.ac.amikom.avent.feature.timeline;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import id.ac.amikom.avent.R;
import id.ac.amikom.avent.adapter.EventBoardAdapter;
import id.ac.amikom.avent.feature.editor.EventEditorActivity;
import id.ac.amikom.avent.feature.detail.DetailActivity;
import id.ac.amikom.avent.model.Event;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventBoardFragment extends Fragment implements EventBoardAdapter.EventClickListener {

    private static final String TAG = EventBoardFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFabAddEvent;
    private ProgressBar mProgressBar;
    private EventBoardAdapter mEventAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;
    private ChildEventListener mChildEventListener;

    public EventBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_board, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDbReference = mFirebaseDatabase.getReference("event_board");

        setupView(view);
        setupViewListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventAdapter.clearAdapter();
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    mProgressBar.setVisibility(View.GONE);
                    Event event = dataSnapshot.getValue(Event.class);
                    mEventAdapter.addEvent(event);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };

            Query query = mDbReference.orderByChild("date");
            query.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        Log.d(TAG, "detachDatabaseReadListener: ");
        if (mChildEventListener != null) {
            mDbReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void setupView(View view) {
        mFabAddEvent = view.findViewById(R.id.fab_event_board_create);
        mRecyclerView = view.findViewById(R.id.rv_event_board);
        mProgressBar = view.findViewById(R.id.pb_event_board_loading);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mEventAdapter = new EventBoardAdapter(this);
        mRecyclerView.setAdapter(mEventAdapter);
    }

    private void setupViewListener() {
        mFabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EventEditorActivity.class));
            }
        });
    }

    @Override
    public void onEventClick(Event event) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }
}
