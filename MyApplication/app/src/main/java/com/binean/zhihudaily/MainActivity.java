package com.binean.zhihudaily;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.binean.zhihudaily.fragment.BaseFragment;
import com.binean.zhihudaily.fragment.IndexFragment;
import com.binean.zhihudaily.fragment.ThemeFragment;
import com.binean.zhihudaily.network.NetUtils;
import com.binean.zhihudaily.presenter.BasePresenter;
import com.binean.zhihudaily.presenter.IndexPresenter;
import com.binean.zhihudaily.presenter.StoriesContract;
import com.binean.zhihudaily.presenter.ThemePresenter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager mFragManager;
    private BasePresenter mPresenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragManager = getSupportFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        IndexFragment indexFragment = new IndexFragment();
        mFragManager.beginTransaction()
                .replace(R.id.content_main, indexFragment)
                .commit();

        mPresenter = new IndexPresenter(indexFragment);
    }

    @Override public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Under Building...", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
//            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        BaseFragment replace;
        if (id == R.id.nav_index){
            replace = new IndexFragment();
            mPresenter = new IndexPresenter((IndexFragment)replace);
        } else {
            int number = NetUtils.THEME_MAP.get(id);
            replace = ThemeFragment.createThemeFragment(number);
            mPresenter = new ThemePresenter((ThemeFragment)replace);
        }
        mFragManager.beginTransaction()
                .replace(R.id.content_main, replace)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
