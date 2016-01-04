package com.example.sanjeevkumar.oops;

import android.app.Activity;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by sanjeevkumar on 10/23/15.
 */
public class AppMethods extends Activity{

    Button btn  = (Button) findViewById(R.id.button);
    a = MainActivity.a;
    public void goTo() {
        Toast.makeText(this, "From app methods", Toast.LENGTH_SHORT).show();
    }


}
