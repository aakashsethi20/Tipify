package com.kashmoney.tipcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SummaryActivity extends AppCompatActivity {

    SharedPreferences prefs;
    private TextView mTotalBillTextView;
    private TextView mTotalAmountTextView;
    private TextView mTipAmount;

    private TextView mTipPerPersonTextView;
    private TextView mAmountPerPersonTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent intent = getIntent();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        boolean dollar = prefs.getBoolean("dollar_check_box_pref", true);
        char currency = (dollar ? '$' : '£');

        mTotalBillTextView = (TextView) findViewById(R.id.summary_total_bill_text_view);
        mTotalAmountTextView = (TextView) findViewById(R.id.summary_total_amount_text_view);
        mTipAmount = (TextView) findViewById(R.id.summary_tip_amount_text_view);

        mTipPerPersonTextView = (TextView) findViewById(R.id.per_person_tip_text_view);
        mAmountPerPersonTextView = (TextView) findViewById(R.id.per_person_amount_text_view);


        mTotalBillTextView.setText("Total Bill: " + currency + intent.getDoubleExtra("totalBill", 0.0));
        mTotalAmountTextView.setText("Bill amount: " + currency + intent.getDoubleExtra("totalAmount", 0.0));
        mTipAmount.setText("Tip amount: " + currency + intent.getDoubleExtra("tipAmount", 0.0));

        mAmountPerPersonTextView.setText("Amount: " + currency + intent.getDoubleExtra("amountPerPerson", 0.0));
        mTipPerPersonTextView.setText("Tip: " + currency + intent.getDoubleExtra("tipPerPerson", 0.0));
    }
}
