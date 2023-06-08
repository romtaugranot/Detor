package com.example.detor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        database = FirebaseDatabase.getInstance().getReference();

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

                Fragment fragment = new MapFragment(this, blocks);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();

            }
        });

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
        getSupportActionBar().setTitle("DETOR");

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }
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

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng((double) (location.getLatitude() * 1E6),
                    (double) (location.getLongitude() * 1E6));

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void switchActivity(Class<?> cls){
        Intent switchActivityIntent = new Intent(this, cls);
        startActivity(switchActivityIntent);
    }
}