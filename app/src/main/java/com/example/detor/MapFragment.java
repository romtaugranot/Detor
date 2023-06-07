package com.example.detor;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private Context context;

    private List<LatLng> lockersPos;

    public MapFragment(Context context){
        this.context = context;
        this.lockersPos = new ArrayList<>();
        lockersPos.add(new LatLng(32.073959, 34.781893));
        lockersPos.add(new LatLng(32.083959, 34.781893));
        lockersPos.add(new LatLng(32.073959, 34.791893));
    }

    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.MY_MAP);
        supportMapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng telAviv = new LatLng(32.073959, 34.781893);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(telAviv, 15));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMaxZoomPreference(16);
        googleMap.setMinZoomPreference(14.5F);
        for (LatLng latlng : lockersPos) {
            googleMap.addMarker(new MarkerOptions().position(latlng)
                    .title("All lockers are full!" )
                        .icon(BitmapFromVector(context.getApplicationContext(), R.drawable.lock)));
        }
        googleMap.setOnMapClickListener(this);
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