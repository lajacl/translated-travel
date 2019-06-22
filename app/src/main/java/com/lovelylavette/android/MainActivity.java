package com.lovelylavette.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.nav_view)
    BottomNavigationView navView;
    @BindView(R.id.message)
    TextView messageText;

    FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.menu_dashboard:
                    messageText.setText(R.string.title_dashboard);
                    return true;
                case R.id.menu_search:
                    messageText.setText(R.string.title_search);
                    ft.replace(R.id.frag_container, new SearchFragment());
                    ft.commit();
                    return true;
                case R.id.menu_trips:
                    messageText.setText(R.string.title_trips);
                    return true;
                case R.id.menu_profile:
                    messageText.setText(R.string.title_profile);
                    return true;
            }
            return false;
        }
    };

    private void updateNav() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.frag_container);
        if(fragment == null) {
            navView.setSelectedItemId(R.id.menu_dashboard);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();
        updateNav();
    }

}
