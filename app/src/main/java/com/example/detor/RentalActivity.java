package com.example.detor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class RentalActivity extends AppCompatActivity implements View.OnClickListener {

    private Block block;

    private Button rentButton, reserveButton, lockerButton;

    private TextView topText;

    private boolean isLockOpen;

    private Timer timer;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental);
        block = (Block) getIntent().getSerializableExtra("block");

        timer = new Timer(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        topText = findViewById(R.id.tv_available);
        int how_many = block.howManyAvailable();
        topText.setText(how_many + " " + topText.getText());

        rentButton = findViewById(R.id.rent_button);
        reserveButton = findViewById(R.id.reserve_button);
        lockerButton = findViewById(R.id.locker_button);

        rentButton.setOnClickListener(this);
        reserveButton.setOnClickListener(this);
        lockerButton.setOnClickListener(this);

        if (timer.isUpTimer()) {
            timer.continueCountUpTimer();
        } else if (timer.isDownTimer()) {
            timer.continueCountDownTimer();
        } else if (Timer.HAS_RENTED_BEFORE)
            reserveButton.setText("ALREADY RESERVED \nTODAY");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent switchActivityIntent = new Intent(this, MapActivity.class);
                if (timer.isDownTimer() || timer.isUpTimer())
                    timer.cancel();
                startActivity(switchActivityIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateTakenInDatabase(Locker locker){
        FirebaseDatabase.getInstance()
                .getReference().child("blocks").child(String.valueOf(block.getNum()))
                .child("lockers").child(String.valueOf(block.getLockers().indexOf(locker)))
                .child("isTaken").setValue(true);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rent_button) {
            if (timer.isUpTimer()){ // user already renting, wants to stop.
                if (isLockOpen){
                    Toast.makeText(this, "You need to unlock locker first", Toast.LENGTH_SHORT).show();
                } else {
                    showMyCustomDialog();
                    timer.stopUpTimer();
                    lockerButton.setBackground(getDrawable(R.drawable.close_lock));
                    lockerButton.setVisibility(View.INVISIBLE);
                }
            } else { // not renting yet, wants to rent.
                if (timer.isDownTimer()) // user is reserving.
                    timer.stopDownTimer();
                timer.startCountUpTimer();
                Locker locker = block.getLockers().get(block.getIndexOfFreeLockerAndUpdateBlock());
                updateTakenInDatabase(locker);
                lockerButton.setVisibility(View.VISIBLE);
            }
        }
        if (v.getId() == R.id.reserve_button) {
            if (!timer.isDownTimer()) { // user already reserving
                if (!timer.isUpTimer()) { // wants to rent
                    if (!Timer.HAS_RENTED_BEFORE){
                        timer.startCountDownTimer();
                        Locker locker = block.getLockers().get(block.getIndexOfFreeLockerAndUpdateBlock());
                        updateTakenInDatabase(locker);
                    }
                }
            }
        }
        if (v.getId() == R.id.locker_button) {
            if (isLockOpen){
                lockerButton.setBackground(getDrawable(R.drawable.close_lock));
            } else {
                lockerButton.setBackground(getDrawable(R.drawable.open_lock));
            }
            isLockOpen = !isLockOpen;
        }
    }

    @SuppressLint("SetTextI18n")
    public void showMyCustomDialog() {

        int time = Timer.getTime();
        int minutes = time / 60;
        int seconds = time - (minutes * 60);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //tell the Dialog to use the customalertdialog.xml as it's layout description
        dialog.setContentView(R.layout.rental_dialog);
        TextView titleText = dialog.findViewById(R.id.dialog_cost);
        titleText.setText(titleText.getText() + " 2$");
        TextView txt = dialog.findViewById(R.id.dialog_time);
        txt.setText(txt.getText() + " " + (minutes + " minutes and " + seconds + " seconds."));
        Button dialogButton = dialog.findViewById(R.id.dialog_button);
        dialogButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public Block getBlock() {
        return block;
    }

}