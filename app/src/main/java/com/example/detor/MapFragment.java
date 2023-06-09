package com.example.detor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.SearchView;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private MapActivity activity;

    private List<Block> blockOfLockersPos;


    public MapFragment(MapActivity activity, List<Block> blockOfLockersPos){
        this.activity = activity;
        this.blockOfLockersPos = blockOfLockersPos;
    }

    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.MY_MAP);
        supportMapFragment.getMapAsync(this);

        searchQueryHandle(view);

        return view;
    }

    private void searchQueryHandle(View view) {
        SearchView mapSearchView = view.findViewById(R.id.mapSearchView);
        mapSearchView.setIconified(false);
        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s){
                String location = mapSearchView.getQuery().toString();
                List<android.location.Address> addressList;

                if(location != null){
                    Geocoder geocoder = new Geocoder(activity);
                    try{
                        addressList = geocoder.getFromLocationName(location, 1);
                        android.location.Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                        Toast.makeText(activity, getString(R.string.no_address), Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s){
                return false;
            }

        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng telAviv = new LatLng(32.073959, 34.781893);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(telAviv, 15));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMaxZoomPreference(16);
        googleMap.setMinZoomPreference(14.5F);
        for (Block block : blockOfLockersPos) {
            String title = block.isFull() ? getString(R.string.all_lockers_are_full): getString(R.string.rent);
            googleMap.addMarker(new MarkerOptions().position(block.getPos())
                    .title(title)
                        .icon(BitmapFromVector(activity.getApplicationContext(), R.drawable.lock)));
        }
        googleMap.setOnInfoWindowClickListener(marker1 -> {
            if (marker1.getTitle().equals(getString(R.string.rent))) {
                Intent switchActivityIntent = new Intent(activity, RentalActivity.class);
                Block block = getBlockByPos(blockOfLockersPos, marker1.getPosition());
                switchActivityIntent.putExtra("block", block);
                if (activity.timer.isDownTimer() || activity.timer.isUpTimer())
                    activity.timer.cancel();
                startActivity(switchActivityIntent);
            }
        });
        googleMap.setOnMapClickListener(this);
    }

    private Block getBlockByPos(List<Block> blockOfLockersPos, LatLng position) {
        for (Block block : blockOfLockersPos){
            if (block.getPos().equals(position))
                return block;
        }
        return null;
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(
                context, vectorResId);

        // below line is use to set bounds to our vector
        // drawable.
        vectorDrawable.setBounds(
                0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our
        // bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }


}