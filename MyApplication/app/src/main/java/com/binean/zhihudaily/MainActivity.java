package com.binean.zhihudaily;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.binean.zhihudaily.fragment.IndexFragment;
import com.binean.zhihudaily.fragment.PsyFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initBaseFragment();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            initBaseFragment();
        } else if (id == R.id.nav_l1) {
            initPsyFragment();
        } else if (id == R.id.nav_l2) {

        } else if (id == R.id.nav_l3) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initBaseFragment() {
        //TODO
        Fragment fragment = fm.findFragmentById(R.id.content_main);
//        Fragment replace = IndexFragment.createFragment(fragment);
//        FragmentTransaction ft = fm.beginTransaction();
//        if (fragment != replace) ft.remove(fragment);
//        ft.add(R.id.content_main, replace)
//                .commit();
        if (fragment == null) fragment = new IndexFragment();
        fm.beginTransaction()
                .add(R.id.content_main, fragment)
                .commit();
    }

    private void initPsyFragment() {
        Fragment fragment = fm.findFragmentById(R.id.content_main);
        Fragment replace = PsyFragment.createFragment(fragment);
        fm.beginTransaction()
                .replace(R.id.content_main, replace)
                .commit();
    }

//    private void initMovieFragment() {
//        Fragment fragment = fm.findFragmentById(R.id.content_main);
//        Fragment replace = IndexFragment.createFragment(fragment);
//        FragmentTransaction ft = fm.beginTransaction();
//        if (fragment != replace) ft.remove(fragment);
//        ft.add(R.id.content_main, replace)
//                .commit();
//    }
//
//    private void initNetSecurityFragment() {
//        Fragment fragment = fm.findFragmentById(R.id.content_main);
//        Fragment replace = IndexFragment.createFragment(fragment);
//        FragmentTransaction ft = fm.beginTransaction();
//        if (fragment != replace) ft.remove(fragment);
//        ft.add(R.id.content_main, replace)
//                .commit();
//    }
}