package com.example.detor;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Timer {

    private static final long DAY = 60 * 60 * 24 * 1000L;

    private static final int FIFTEEN_MINUTES = 10;

    private static int DOWN_TIME = FIFTEEN_MINUTES;

    private static int UP_TIME = 0;

    private CountDownTimer timer;

    private Button rentButton, reserveButton;

    private TextView topText;

    public static boolean HAS_RENTED_BEFORE;

    private AppCompatActivity activity;

    private int numOfFreeLocker;

    public Timer(AppCompatActivity activity){
        this.activity = activity;

        if (activity instanceof RentalActivity){
            rentButton = activity.findViewById(R.id.rent_button);
            reserveButton = activity.findViewById(R.id.reserve_button);
            topText = activity.findViewById(R.id.tv_available);
            Block block = ((RentalActivity) activity).getBlock();
            numOfFreeLocker = block.getIndexOfFreeLockerAndUpdateBlock() + 1;
        }
    }

    public void startCountDownTimer(){
        if (!HAS_RENTED_BEFORE && DOWN_TIME == FIFTEEN_MINUTES) { // user hasn't used reserve yet.
            this.timer = new CountDownTimer(FIFTEEN_MINUTES * 1000, 1000) {
                @SuppressLint("SetTextI18n")
                public void onTick(long millis) {
                    if (activity instanceof RentalActivity){
                        topText.setText("LOCKER "
                                + numOfFreeLocker
                                + " IS AVAILABLE FOR YOU!");
                    }
                    changeText(false);
                }

                public void onFinish() {
                    timer.cancel();
                    DOWN_TIME = FIFTEEN_MINUTES;
                    startCountUpTimer();
                }
            };
            timer.start();
        }
    }

    public void startCountUpTimer(){
        HAS_RENTED_BEFORE = true;
        if (activity instanceof RentalActivity) {
            rentButton.setText(activity.getString(R.string.stop_renting));
            rentButton.setBackground(activity.getDrawable(R.drawable.stop_renting_button_design));

        }
        this.timer = new CountUpTimer(DAY * 1000){
            @SuppressLint("SetTextI18n")
            public void onTick(int second){
                if (activity instanceof RentalActivity){
                    topText.setText("LOCKER "
                            + numOfFreeLocker
                            + " IS AVAILABLE FOR YOU!");
                }
                changeText(true);
            }
            public  void onFinish(){
                reserveButton.setText("FINISH!");
            }
        };
        timer.start();
    }

    public void continueCountUpTimer() {
        if (activity instanceof RentalActivity) {
            rentButton.setText(activity.getString(R.string.stop_renting));
            rentButton.setBackground(activity.getDrawable(R.drawable.stop_renting_button_design));
        }
        long remainingTime = DAY - UP_TIME;
        this.timer = new CountUpTimer(remainingTime * 1000){
            @SuppressLint("SetTextI18n")
            public void onTick(int second){
                if (activity instanceof RentalActivity){
                    topText.setText("LOCKER "
                            + numOfFreeLocker
                            + " IS AVAILABLE FOR YOU!");
                }
                changeText(true);
            }
            public  void onFinish(){
                reserveButton.setText("FINISH!");
            }
        };
        timer.start();
    }

    public void cancel() {
        timer.cancel();
    }

    public void continueCountDownTimer() {
        if (!HAS_RENTED_BEFORE && UP_TIME == 0) { // user hasn't used reserve yet.
            long remainingTime = DOWN_TIME;
            this.timer = new CountUpTimer(remainingTime * 1000) {
                @SuppressLint("SetTextI18n")
                public void onTick(int second) {
                    if (activity instanceof RentalActivity){
                        topText.setText("LOCKER "
                                + numOfFreeLocker
                                + " IS AVAILABLE FOR YOU!");
                    }
                    changeText(false);
                }

                public void onFinish() {
                    timer.cancel();
                    DOWN_TIME = FIFTEEN_MINUTES;
                    startCountUpTimer();
                }
            };
            timer.start();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void stopUpTimer(){
        timer.cancel();
        rentButton.setText(activity.getString(R.string.start_renting));
        rentButton.setBackground(activity.getDrawable(R.drawable.start_renting_button_design));
        resetUpTimer();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void stopDownTimer(){
        timer.cancel();
        resetDownTimer();
    }

    public boolean isDownTimer(){
        return DOWN_TIME < FIFTEEN_MINUTES && !HAS_RENTED_BEFORE;
    }

    public boolean isUpTimer(){
        return UP_TIME > 0;
    }

    public void resetUpTimer(){
        UP_TIME = 0;
    }

    public void resetDownTimer(){
        DOWN_TIME = FIFTEEN_MINUTES;
    }

    private void changeText(boolean isIncreasing){
        int time = isIncreasing ? UP_TIME : DOWN_TIME;
        int minutes = time / 60;
        int seconds = time - (minutes * 60);
        if (activity instanceof RentalActivity) {
            if (seconds > 9)
                reserveButton.setText(minutes + ":" + seconds);
            else
                reserveButton.setText(minutes + ":0" + seconds);
            reserveButton.setTextSize(25);
        }
        if (isIncreasing) UP_TIME++;
        else DOWN_TIME--;
    }

    public static int getTime(){
        return UP_TIME;
    }
}
