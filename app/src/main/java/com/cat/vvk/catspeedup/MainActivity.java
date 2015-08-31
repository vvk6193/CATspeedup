package com.cat.vvk.catspeedup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cat.vvk.catspeedup.modal.Constant;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private Button mul,add,sub, div;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView info_text = (TextView)findViewById(R.id.info_text);
        mul = (Button)findViewById(R.id.mul);
        add = (Button)findViewById(R.id.add);
        sub = (Button)findViewById(R.id.sub);
        div = (Button)findViewById(R.id.div);
        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        mul.setOnClickListener(this);
        div.setOnClickListener(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String num = sp.getString("number_of_digit_addition","2");
        num += sp.getString("example_text","2");
        info_text.setText(num);
//        String
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
            Intent i = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        Intent i;
        switch(v.getId()) {
            case R.id.add:
                 i = new Intent(MainActivity.this,AdditionTableActivity2.class);
                i.putExtra("operation_type", Constant.ADDITION);
                startActivity(i);
                break;

            case R.id.sub:
                 i = new Intent(MainActivity.this,AdditionTableActivity2.class);
                i.putExtra("operation_type", Constant.SUBTRACTION);
                startActivity(i);
                break;
            case R.id.mul:
                 i = new Intent(MainActivity.this,AdditionTableActivity2.class);
                i.putExtra("operation_type", Constant.MULTIPLICATION);
                startActivity(i);
                break;
            case R.id.div:
                 i = new Intent(MainActivity.this,AdditionTableActivity2.class);
                i.putExtra("operation_type", Constant.DIVISION);
                startActivity(i);
                break;
        }
    }
}
