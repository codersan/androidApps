package com.example.sanjeevkumar.interaction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import java.lang.Object;
//import android.util.log;
import android.net.Uri;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    int quantity = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setText("Text From java");
        textView.setTextColor(0);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void incrementQuantity(View view) {
        quantity+=1;
        displayQuantity(quantity);
    }

    public void decrementQuantity(View view) {
        quantity-=1;
        displayQuantity(quantity);
    }

    public void submitOrder(View view){
        int pricePerUnit = 5;
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
        boolean selected = checkBox.isChecked();
        displayPrice(pricePerUnit * quantity, selected);
    }

    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView)(findViewById(R.id.quantity_text_view));
        quantityTextView.setText(NumberFormat.getInstance().format(number));
    }

    private void displayPrice(int price, boolean selected) {
        TextView priceTextView = (TextView)(findViewById(R.id.price_text_view));
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(price) + " Selectd = " + selected);
        sendMail(price);
    }

    private void sendMail(int price) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.setType("text/plain");
        String[] TO = {"sanjeev.kr1779@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, TO);
        intent.putExtra(Intent.EXTRA_SUBJECT, "msg from android app");
        intent.putExtra(Intent.EXTRA_TEXT, "Total price to be paid = " + price);
        try {
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Mail sent successfully ", Toast.LENGTH_SHORT ).show();
            }
            //Log.i("Mail successfully sent");
        }
        catch (android.content.ActivityNotFoundException ex ) {
            Toast.makeText(MainActivity.this, "Mail not send due to "+ ex, Toast.LENGTH_SHORT ).show();
        }
    }
}
