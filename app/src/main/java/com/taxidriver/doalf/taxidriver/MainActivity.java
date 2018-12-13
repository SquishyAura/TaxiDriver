package com.taxidriver.doalf.taxidriver;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    List<EntityRequest> storedPendingRequestList;
    List<EntityRequest> storedAcceptedRequestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        loadFragment(new NavigationMap(), "map");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        storedAcceptedRequestList = new ArrayList<>();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            String tag = "";
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new NavigationMap();
                    tag = "map";
                    break;
                case R.id.navigation_dashboard:
                    fragment = new NavigationPending();
                    tag = "pending";
                    break;
                case R.id.navigation_notifications:
                    fragment = new NavigationAccepted();
                    tag = "accepted";
                    break;
            }
            return loadFragment(fragment, tag);
        }
    };

    private boolean loadFragment(Fragment fragment, String tag) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .commit();
            return true;
        }
        return false;
    }

    public void btnAcceptOnClick(View view) {
        NavigationPending pending = (NavigationPending) getSupportFragmentManager().findFragmentByTag("pending");
        EntityRequest request = pending.pendingRequestList.get(Integer.parseInt(String.valueOf(view.getTag())));
        storedAcceptedRequestList.add(storedAcceptedRequestList.size(), request);
        pending.removeCustomer(Integer.parseInt(String.valueOf(view.getTag())));
    }

    public void btnDeclineOnClick(View view) {
        NavigationPending pending = (NavigationPending) getSupportFragmentManager().findFragmentByTag("pending");
        pending.removeCustomer(Integer.parseInt(String.valueOf(view.getTag())));
    }
}
