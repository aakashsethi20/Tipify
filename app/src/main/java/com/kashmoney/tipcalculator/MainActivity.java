package com.kashmoney.tipcalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTipPercentTextView;
    private TextView mCurrencyTextView;

    private EditText mTotalAmountEditText;
    private EditText mNumberOfPeopleEditText;

    private SeekBar mTipPercentSeekBar;

    private ImageButton mSuggestTip;

    private Button mCalculateButton;

    private RatingBar rating;

    SharedPreferences prefs;

    private int tipPercent = 15;
    private double totalAmount = 0.0;
    private int numberOfPeople = 1;
    private double totalBill = 0.0;
    private double tipAmount;
    private double tipPerPerson;
    private double amountPerPerson;

    private final boolean DOLLAR = true;
    private final boolean POUND = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get references to text and buttons
        mTipPercentTextView = (TextView) findViewById(R.id.tip_percent_text_view);
        mCurrencyTextView = (TextView) findViewById(R.id.currency_text_view);
        mTotalAmountEditText = (EditText) findViewById(R.id.total_amount_edit_text);
        mNumberOfPeopleEditText = (EditText) findViewById(R.id.people_paying_edit_text);
        mTipPercentSeekBar = (SeekBar) findViewById(R.id.tip_seek_bar);
        mSuggestTip = (ImageButton) findViewById(R.id.suggestTip_image_button);
        mCalculateButton = (Button) findViewById(R.id.calculate_button);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // change listener for seek bar
        mTipPercentSeekBar.setOnSeekBarChangeListener(seekBarListener);

        // change listener for bill amount and split bill
        mTotalAmountEditText.addTextChangedListener(totalAmountTextWatcher);
        mNumberOfPeopleEditText.addTextChangedListener(numberOfPeopleTextWatcher);

        // onClick listener for suggest a tip button
        mSuggestTip.setOnClickListener(suggestTipListener);

        // calculate listener
        mCalculateButton.setOnClickListener(calculateButtonListener);

        mTipPercentTextView.setText(String.valueOf(mTipPercentSeekBar.getProgress()) + "%");


        mTipPercentSeekBar.setProgress(Integer.parseInt(prefs.getString("default_tip", "15")));
        mCurrencyTextView.setText(prefs.getBoolean("dollar_check_box_pref", true) ? "$" : "£");
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTipPercentSeekBar.setProgress(Integer.parseInt(prefs.getString("default_tip", "15")));
        mCurrencyTextView.setText(prefs.getBoolean("dollar_check_box_pref", true) ? "$" : "£");
    }

    // listener object for SeekBar progress change
    private final SeekBar.OnSeekBarChangeListener seekBarListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tipPercent = progress;
                    mTipPercentTextView.setText(progress + "%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };

    // text watcher for Total Amount Edit Text
    private final TextWatcher totalAmountTextWatcher =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = mTotalAmountEditText.getText().toString();
                    if (str == null || str.isEmpty()) {
                        totalAmount = 0.0;
                        mTotalAmountEditText.setError("Total amount cannot be left empty!");
                    }
                    else {
                        totalAmount = Double.parseDouble(str);
                        mTotalAmountEditText.setError(null);
                    }
                }
            };

    private final TextWatcher numberOfPeopleTextWatcher =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = mNumberOfPeopleEditText.getText().toString();
                    if (str != null && !str.isEmpty()) {
                        if (Integer.parseInt(str) == 0) {
                            numberOfPeople = Integer.parseInt(str);
                            mNumberOfPeopleEditText.setError("Number of people cannot be 0!");
                        } else {
                            numberOfPeople = Integer.parseInt(str);
                            mNumberOfPeopleEditText.setError(null);
                        }
                    }
                }
            };

    private final View.OnClickListener suggestTipListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowRatingBar();
                }
            };

    public void ShowRatingBar() {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.rating_dialog_box, null);
        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("Rate your experience");
        popDialog.setView(v);

        rating = (RatingBar) v.findViewById(R.id.rating_bar);

        final TextView tipPercentDialogBox = (TextView) v.findViewById(R.id.tip_percent_dialog_box_text_view);

        tipPercentDialogBox.setText(String.valueOf(CalculateTipFromRating(rating.getRating())));

        rating.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        tipPercentDialogBox.setText(String.valueOf((int) CalculateTipFromRating(rating)) + "%");
                    }
                }
        );

        popDialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tipPercent = (int) CalculateTipFromRating(rating.getRating());
                        mTipPercentTextView.setText(String.valueOf(tipPercent) + "%");
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        popDialog.create();
        popDialog.show();
    }

    private float CalculateTipFromRating(float rating) {
        return (10 + (rating*2));
    }

    private final View.OnClickListener calculateButtonListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calculateEverything();
                    if (numberOfPeople != 0) {
                        Intent qIntent = new Intent(MainActivity.this, SummaryActivity.class);
                        qIntent.putExtra("tipAmount", tipAmount);
                        qIntent.putExtra("tipPerPerson", tipPerPerson);
                        qIntent.putExtra("totalBill", totalBill);
                        qIntent.putExtra("amountPerPerson", amountPerPerson);
                        qIntent.putExtra("tipPercent", tipPercent);
                        qIntent.putExtra("numberOfPeople", numberOfPeople);
                        qIntent.putExtra("totalAmount", totalAmount);

                        startActivity(qIntent);
                    }
                }
            };

    private void calculateEverything() {
        tipAmount = Math.round((totalAmount*(tipPercent/100.0))*100)/100.0;
        tipPerPerson = Math.round((tipAmount / numberOfPeople)*100) / 100.0;
        totalBill = Math.round((totalAmount*((100+tipPercent)/100.0))*100) / 100.0;
        amountPerPerson = Math.round((totalBill / numberOfPeople)*100) / 100.0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_title, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
