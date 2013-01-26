package com.gdg.london.match;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AccountSetupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_account_setup, menu);
        return true;
    }
    
}
