package id.ac.amikom.avent.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import id.ac.amikom.avent.R;
import id.ac.amikom.avent.feature.bookmark.BookmarkActivity;
import id.ac.amikom.avent.feature.timeline.EventBoardFragment;
import id.ac.amikom.avent.feature.login.LoginActivity;
import id.ac.amikom.avent.feature.profile.ProfileActivity;
import id.ac.amikom.avent.model.User;
import id.ac.amikom.avent.user.UserData;
import id.ac.amikom.avent.user.UserPref;
import id.ac.amikom.avent.utility.ImageUtil;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, UserData.OnUserDataFetch {

    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Avent");

        UserData userData = new UserData(this);
        userData.fetchUserData();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new EventBoardFragment())
                .commit();


        View view = navigationView.getHeaderView(0);
        ImageView imgProfilePhoto = view.findViewById(R.id.img_header_profile_photo);
        TextView tvProfileName = view.findViewById(R.id.tv_header_profile_name);
        TextView tvProfileEmail = view.findViewById(R.id.tv_header_profile_email);

        Uri photoUri = getMainApp().getUser().getPhotoUrl();
        if (photoUri != null) {
            Log.d("TAG", "onCreate: url : " + photoUri.toString());
            ImageUtil.loadImageFromUrl(imgProfilePhoto, photoUri.toString());
        }
        tvProfileName.setText(getMainApp().getUser().getDisplayName());
        tvProfileEmail.setText(getMainApp().getUser().getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_timeline:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new EventBoardFragment())
                        .commit();
                break;
            case R.id.nav_bookmark:
                startActivity(new Intent(MainActivity.this, BookmarkActivity.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.nav_signout:
                FirebaseAuth.getInstance().signOut();
                UserPref userPref = new UserPref(MainActivity.this);
                userPref.cleanUpUserPref();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_timeline);
    }

    @Override
    public void onUserFetchSuccess(User user) {
        UserPref userPref = new UserPref(this);
        userPref.setUser(user);
        Log.d("TAG", "onUserFetchSuccess: " + user.getNoId());
    }

    @Override
    public void onUserFetchFailed() {

    }
}
