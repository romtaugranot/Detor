package com.example.detor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CardDetailsActivity extends AppCompatActivity {

    private EditText editTextCardNumber;
    private EditText editTextCVV;
    private EditText editTextCardholderName;
    private EditText editTextID;
    private Button buttonSaveCard;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextCVV = findViewById(R.id.editTextCVV);
        editTextCardholderName = findViewById(R.id.editTextCardholderName);
        editTextID = findViewById(R.id.editTextID);
        buttonSaveCard = findViewById(R.id.buttonSaveCard);

        buttonSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save card details logic goes here
                String cardNumber = editTextCardNumber.getText().toString();
                String cvv = editTextCVV.getText().toString();
                String cardholderName = editTextCardholderName.getText().toString();
                String ID = editTextID.getText().toString();

                // Perform card details validation and save the card information
            }
        });
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