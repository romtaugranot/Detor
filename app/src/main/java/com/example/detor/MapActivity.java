package com.example.detor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private DatabaseReference database;

    private MapFragment map;

    public Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        database = FirebaseDatabase.getInstance().getReference();

        addMapAndBlocksToMap();

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView mNavigationView = findViewById(R.id.navigation_view);

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }

        timer = new Timer(this);
        if (timer.isUpTimer()) {
            timer.continueCountUpTimer();
        } else if (timer.isDownTimer()) {
            timer.continueCountDownTimer();
        }
    }

    private void addMapAndBlocksToMap() {

        database.child("blocks").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                List<Block> blocks = new ArrayList<>();
                DataSnapshot snapshot = task.getResult();
                for (DataSnapshot blockData : snapshot.getChildren()){
                    int block_num = Integer.parseInt(Objects.requireNonNull(blockData.getKey()));
                    String str = blockData.child("latLng").getValue(String.class);
                    String[] coor = str.split(", ");
                    LatLng pos = new LatLng(Double.parseDouble(coor[0]), Double.parseDouble(coor[1]));

                    Block block = new Block(block_num, pos);

                    boolean isFull = true;
                    for (DataSnapshot lockerData : blockData.child("lockers").getChildren()){
                        boolean isTaken = Boolean.TRUE.equals(lockerData.child("isTaken").getValue(Boolean.class));
                        if (!isTaken) isFull = false;
                        int locker_num = Integer.parseInt(Objects.requireNonNull(lockerData.getKey()));

                        Locker locker = new Locker(isTaken, locker_num);
                        block.addLocker(locker);
                    }

                    block.setFull(isFull);

                    blocks.add(block);
                }

                map = new MapFragment(this, blocks);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, map).commit();

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            switchActivity(AccountActivity.class);
        }
        if (id == R.id.nav_payment) {
            switchActivity(PaymentActivity.class);
        }
        if (id == R.id.nav_settings) {
            switchActivity(SettingsActivity.class);
        }

        return true;
    }

    public void switchActivity(Class<?> cls){
        Intent switchActivityIntent = new Intent(this, cls);
        startActivity(switchActivityIntent);
    }
}