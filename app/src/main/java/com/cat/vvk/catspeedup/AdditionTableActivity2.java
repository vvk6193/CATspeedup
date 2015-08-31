package com.cat.vvk.catspeedup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.vvk.catspeedup.db.DatabaseHelper;
import com.cat.vvk.catspeedup.modal.Constant;
import com.cat.vvk.catspeedup.modal.Record;

import java.util.List;
import java.util.Random;


public class AdditionTableActivity2 extends AppCompatActivity implements View.OnFocusChangeListener{

    TableLayout t1;
    private Toolbar mToolbar;
    private int[] data;
    private int numCount = 10;
    private int numRaws = 0;
    private int numCol = 0;
    private boolean flag = false, isTimerRunning = false;
    private int numDigit = 2;
    private DatabaseHelper dh;
    TimerTextView timer = null;
    EditText editText = null;
    long givenAnswer = 0, actualAnswer = 0;
    float answerDiv = 0;
    String operationType="";
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        if(isTimerRunning) {
            Toast.makeText(this,"Press again to exit",Toast.LENGTH_SHORT).show();
            isTimerRunning = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition_table_activity2);
        operationType = getIntent().getExtras().getString("operation_type",Constant.ADDITION);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle(operationType);
        dh = DatabaseHelper.getInstance(getApplicationContext());
        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        setup();
        timer = (com.cat.vvk.catspeedup.TimerTextView) findViewById(R.id.chron);
        timer.setText("0.000");
        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                timer.start();
                if(flag) {
                    flag = false;
                    setup();
                    timer.setText("0.000");
                    timer.reset();
                    timer.setTextColor(Color.GRAY);
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timer.stop();

            }
        });
    }

    void setup(){
        if (!flag) {
//            flag = true;
            setData();
//            setData();
            numCount = numCount >= 15 ? 15 : numCount;
            numRaws = numCount >= 15 ? 15 : numCount;
            numCol = numCount % 15;
            int count = 0;
            final TableLayout tl = (TableLayout) findViewById(R.id.main_table);
            tl.removeAllViews();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            Log.d("density","density :" + metrics.density + "density dpi : " + metrics.densityDpi );
            final float pixelHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
            float pixelWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            final TableRow.LayoutParams llp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < numRaws; i++) {
                TableRow tr_head = new TableRow(this);
                tr_head.setId(10);
                tr_head.setBackgroundColor(Color.WHITE);        // part1
                TableRow.LayoutParams lp = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);

//                int leftMargin = 5;
//                int topMargin = 5;
//                int rightMargin = 5;
//                int bottomMargin = 5;
                lp.setMargins(0,3,0,0);
                tr_head.setLayoutParams(lp);

                for (int j = 0; j <= (numCount - i - 1) / numRaws; j++) {
                    int randomNum = data[count++];
                    TextView number = new TextView(this);
                    int margin = (int)(5*pixelHeight);
//                    llp.pa(margin,margin,margin,margin);
                    number.setPadding(margin,margin,margin,margin);
                    number.setLayoutParams(llp);
                    number.setBackgroundResource(R.drawable.cell_shape);
                    number.setText("" + randomNum);
                    number.setTextColor(Color.BLACK);          // part2
                    number.setGravity(Gravity.CENTER);

                    tr_head.addView(number);// add the column to the table row here
                }
                tl.addView(tr_head, lp);
            }

            TableRow tr_head = new TableRow(this);
            tr_head.setId(10);
            tr_head.setBackgroundColor(Color.WHITE);        // part1
            TableRow.LayoutParams lp = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            tr_head.setLayoutParams(lp);

            EditText et = new EditText(this);
            et.setId(100);
            et.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            et.setGravity(Gravity.CENTER);
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            et.setText("");
            int maxLength = (""+actualAnswer).length();
            Log.d("vvk","max length is " + maxLength + " : " + (int)(pixelHeight * maxLength * 14));
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            TableRow.LayoutParams llpet = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            et.setLayoutParams(llpet);
            if(maxLength < 8) {
                maxLength = 8;
            }
            et.setHint(""+(int)Math.pow(10,maxLength));
            et.setOnFocusChangeListener(this);
            et.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE && !flag) {
                        isTimerRunning = false;
                        timer.stop();
                        Record record = new Record();
                        record.setNumDigit(numDigit);
                        record.setCreatedDate(System.currentTimeMillis());
                        record.setTimeTaken(Float.parseFloat(timer.getText().toString()));
                        record.setRecordType(operationType);
                        int isCorrect = 0;
                        if(editText != null) {
                            String answerText = editText.getText().toString();
                            if(answerText != null && answerText.length() > 0)
                            givenAnswer = Long.parseLong(answerText);
                        } else {
                            editText = (EditText)findViewById(100);
                            if(editText != null) {
                                String answerText = editText.getText().toString();
                                if(answerText != null && answerText.length() > 0)
                                givenAnswer = Integer.parseInt(answerText);
                            } else {
                                isCorrect = 1;
                            }
                        }
                        if( givenAnswer == actualAnswer || isCorrect == 1) {
                            Log.d("vvk","iscorrect " + isCorrect);
                            isCorrect = 0;
                            record.setIsCorrect(1);
                            timer.setTextColor(Color.GREEN);
                        } else {
                            record.setIsCorrect(0);
                            timer.setTextColor(Color.RED);
                            TableRow tr_head1 = new TableRow(getApplicationContext());
                            tr_head1.setId(10);
                            tr_head1.setBackgroundColor(Color.WHITE);        // part1
                            TextView number1 = new TextView(getApplicationContext());
                            int margin = (int)(5*pixelHeight);
                            number1.setPadding(margin,margin,margin,margin);
                            number1.setLayoutParams(llp);
                            number1.setBackgroundResource(R.drawable.cell_shape);
                            number1.setText("" + actualAnswer);
                            number1.setTextColor(Color.BLACK);          // part2
                            number1.setGravity(Gravity.CENTER);

                            tr_head1.addView(number1);
                            tl.addView(tr_head1);
                        }


                        dh.insertRecord(record);
                        List<Record> records = dh.getAllRecords();
                        String msg = "List is null";
                        if(records != null) {
                            msg = "list size : " + records.size();
                        }
//                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                        flag = true;
                    }
                    return false;
                }
            });
            tr_head.addView(et);
            editText = et;
            tl.addView(tr_head, lp);
        }
    }

    void setData() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Random rand = new Random();
        switch(operationType) {
            case Constant.ADDITION :
                numDigit = Integer.parseInt(sp.getString("number_of_digit_addition", "2"));
                numCount = Integer.parseInt(sp.getString("numCountAddition", "6"));
                data = new int[numCount + 1];
                for(int i = 0;i<numCount;i++) {
                    int randomNum = rand.nextInt((int) (Math.pow(10, numDigit) - 1)) + 1;
                    data[i] = randomNum;
                    actualAnswer += randomNum;
//                    int randomNum = rand.nextInt((int) (Math.pow(10, numDigit) - 1) + 1) + 1;
//                    data[count++] = randomNum;
//                    data[numCount] += randomNum;
                }
                break;
            case Constant.SUBTRACTION :
                numDigit = Integer.parseInt(sp.getString("number_of_digit_subtraction", "2"));
                numCount = Integer.parseInt(sp.getString("numCountSubtraction", "6"));
                data = new int[numCount + 1];
                for(int i = 0;i<numCount;i++) {
                    int randomNum = rand.nextInt((int) (Math.pow(10, numDigit) - 1)) + 1;
                    data[i] = randomNum;
                    actualAnswer -= randomNum;
//                    int randomNum = rand.nextInt((int) (Math.pow(10, numDigit) - 1) + 1) + 1;
//                    data[count++] = randomNum;
//                    data[numCount] += randomNum;
                }
                break;
            case Constant.MULTIPLICATION :
                int numDigit1 = Integer.parseInt(sp.getString("number_of_digit_mul1", "2"));
                int numDigit2 = Integer.parseInt(sp.getString("number_of_digit_mul2", "2"));
//                numCount = Integer.parseInt(sp.getString("numCountMul", "2"));
                numDigit = numDigit1;
                numCount = 2;
                actualAnswer = 1;
                givenAnswer = 1;
                data = new int[numCount + 1];
                for(int i = 0;i<numCount;i++) {
                    if(i == 1) {
                        numDigit = numDigit2;
                    }
                    int randomNum = rand.nextInt((int) (Math.pow(10, numDigit) - 1)) + 1;
                    data[i] = randomNum;
                    actualAnswer *= randomNum;

//                    int randomNum = rand.nextInt((int) (Math.pow(10, numDigit) - 1) + 1) + 1;
//                    data[count++] = randomNum;
//                    data[numCount] += randomNum;
                }
                break;
            case Constant.DIVISION :
                int divDigit1 = Integer.parseInt(sp.getString("number_of_digit_div1", "2"));
                int divDigit2 = Integer.parseInt(sp.getString("number_of_digit_div2", "1"));
//                numCount = Integer.parseInt(sp.getString("numCountMul", "2"));
                if(divDigit1 < divDigit2) {
                    int tmp = divDigit2;
                    divDigit2 = divDigit1;
                    divDigit1 = tmp;
                }
                numDigit = divDigit1;
                numCount = 2;
                actualAnswer = 1;
                givenAnswer = 1;
                data = new int[numCount + 1];
                for(int i = 0;i<numCount;i++) {
                    if(i == 1) {
                        numDigit = divDigit2;
                    }
                    int randomNum = rand.nextInt((int) (Math.pow(10, numDigit) - 1)) + 1;
                    data[i] = randomNum;
//                    actualAnswer *= randomNum;
//                    int randomNum = rand.nextInt((int) (Math.pow(10, numDigit) - 1) + 1) + 1;
//                    data[count++] = randomNum;
//                    data[numCount] += randomNum;
                }

                actualAnswer = data[0] / data[1];
                break;
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
            Intent i = new Intent(AdditionTableActivity2.this, GraphActivity.class);
            i.putExtra("operationType",operationType);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
//        Log.d("vvk",""+v.getId());
        switch (v.getId()) {
            case 100:
                if(hasFocus) {
                    isTimerRunning = true;
                    timer.start();
                }
                break;
        }

    }
}
