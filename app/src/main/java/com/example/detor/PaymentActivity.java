package com.example.detor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    private TextView textViewSavedCards;
    private TextView textViewNoCards;
    private Button buttonAddCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewSavedCards = findViewById(R.id.textViewSavedCards);
        textViewNoCards = findViewById(R.id.textViewNoCards);
        buttonAddCard = findViewById(R.id.buttonAddCard);

        buttonAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the CardDetailsActivity to enter card details
                Intent intent = new Intent(PaymentActivity.this, CardDetailsActivity.class);
                startActivity(intent);
            }
        });

        // Check if there are saved cards to display
        boolean hasSavedCards = false; // Replace with your logic to determine if there are saved cards

        if (hasSavedCards) {
            textViewSavedCards.setVisibility(View.VISIBLE);
            textViewNoCards.setVisibility(View.GONE);
        } else {
            textViewSavedCards.setVisibility(View.GONE);
            textViewNoCards.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}