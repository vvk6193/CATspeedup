package com.cat.vvk.catspeedup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;


public class AdditionTableActivity extends AppCompatActivity {

    TableLayout t1;
    private Toolbar mToolbar;
    private int[][] data;
    private int numCount = 10;
    private int numRaws = 0;
    private int numCol = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition_table);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String num = sp.getString("number_of_digit_addition", "2");
        numCount = Integer.parseInt(sp.getString("addition_table_size", "6"));
        data = new int[numCount+1][7];
        numRaws = numCount / 15;
        numCol = numCount % 15;
        TableLayout tl = (TableLayout) findViewById(R.id.main_table);
        Random rand = new Random();
        float pixelHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        float pixelWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        for(int i =0;i<numCount;i++) {

            TableRow tr_head = new TableRow(this);
            tr_head.setId(10);
            tr_head.setBackgroundColor(Color.WHITE);        // part1
            TableRow.LayoutParams lp= new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);

            int leftMargin = 5;
            int topMargin = 5;
            int rightMargin=5;
            int bottomMargin = 5;

//            lp.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

            tr_head.setLayoutParams(lp);

            TableRow.LayoutParams llp = new TableRow.LayoutParams((int)(50 * pixelWidth),(int)(30 * pixelHeight));
            TableRow.LayoutParams llpet = new TableRow.LayoutParams((int)(50 * pixelWidth),TableRow.LayoutParams.WRAP_CONTENT);
            for(int j =0;j<6;j++) {
                if(i == numCount - 1 ) {
                    EditText et = new EditText(this);
                    et.setLayoutParams(llp);
//                    et.setWidth(tvWidth);
//                    et.setHeight((int)(40 * pixelHeight));
                    et.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);
                    et.setGravity(Gravity.CENTER);
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    et.setText(""+data[numRaws][j]);
                    tr_head.addView(et);
                } else {
                    int randomNum = rand.nextInt((int)(Math.pow(10,Double.parseDouble(num)) - 1) + 1) + 1;
                    data[i][j] = randomNum;
                    data[i][6] += randomNum;
                    data[numRaws][j] += randomNum;
                    TextView number = new TextView(this);
                    number.setLayoutParams(llp);
//                    number.setWidth(tvWidth);
                    number.setBackgroundResource(R.drawable.cell_shape);
                    number.setId(20);
                    number.setText("" + randomNum);
                    number.setTextColor(Color.BLACK);          // part2
//                    number.setPadding(5, 5, 5, 5);
                    number.setGravity(Gravity.CENTER);
                    tr_head.addView(number);// add the column to the table row here
                }

            }
            if(i < numCount - 1) {
                EditText et = new EditText(this);
                et.setLayoutParams(llp);
//                et.setWidth(tvWidth);
//                et.setHeight(tvHeight);
                et.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);
                et.setText(""+data[i][6]);
                et.setGravity(Gravity.CENTER);
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                tr_head.addView(et);
            }
//            TextView number = new TextView(this);
//            TableRow.LayoutParams llp = new TableRow.LayoutParams(tvWidth,tvHeight);
//            number.setLayoutParams(llp);
//            number.setBackgroundResource(R.drawable.cell_shape);
//            number.setId(20);
//            number.setText("" + data[i][6]);
//            number.setTextColor(Color.BLACK);          // part2
//            number.setPadding(5, 5, 5, 5);
//            number.setGravity(Gravity.CENTER);
//            tr_head.addView(number);// add the column to the table row here



            tl.addView(tr_head, lp);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addition_table, menu);
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
            Intent i = new Intent(AdditionTableActivity.this,SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
