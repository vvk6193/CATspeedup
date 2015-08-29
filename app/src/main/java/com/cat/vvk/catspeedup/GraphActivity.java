package com.cat.vvk.catspeedup;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.cat.vvk.catspeedup.db.DatabaseHelper;
import com.cat.vvk.catspeedup.modal.Record;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;


public class GraphActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseHelper dh;
    GraphView graph;
    Spinner spinnerDigit, spinnerMonthValue, spinnerDateValue,spinnerCalculationType;
    RadioGroup radioDurationType;
    ArrayAdapter<Integer> adapterDurationValueDay;
    Toolbar mToolbar;
    TextView tvEfficiency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dh = DatabaseHelper.getInstance(getApplicationContext());
        graph = (GraphView) findViewById(R.id.graph);

        spinnerDigit = (Spinner) findViewById(R.id.select_digit);
        spinnerMonthValue = (Spinner) findViewById(R.id.select_month_value);
        spinnerDateValue = (Spinner) findViewById(R.id.select_date_value);
        spinnerCalculationType = (Spinner) findViewById(R.id.select_calculation_type);
        tvEfficiency = (TextView) findViewById(R.id.tvEfficiency);

        radioDurationType = (RadioGroup) findViewById(R.id.radioDurationType);

        ArrayAdapter<CharSequence> adapterDigit = ArrayAdapter.createFromResource(this,
                R.array.pref_addition_list_values, android.R.layout.simple_spinner_item);
        adapterDigit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        ArrayAdapter<CharSequence> adapterDurationType = ArrayAdapter.createFromResource(this,
//                R.array.pref_addition_list_values, android.R.layout.simple_spinner_item);
//        adapterDurationType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapterDurationValueDay = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item);
        adapterDurationValueDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterDurationValueMonth = ArrayAdapter.createFromResource(this,
                R.array.duration_type_month_titles, android.R.layout.simple_spinner_item);
        adapterDurationValueMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterCalculationType = ArrayAdapter.createFromResource(this,
                R.array.calculation_types_titles, android.R.layout.simple_spinner_item);
        adapterDigit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        setupDateSpinner();
        spinnerDigit.setAdapter(adapterDigit);
        spinnerMonthValue.setAdapter(adapterDurationValueMonth);
        spinnerCalculationType.setAdapter(adapterCalculationType);
//        spinnerDateValue.setAdapter(adapterDurationValueDay);


        spinnerDigit.setOnItemSelectedListener(this);
        spinnerMonthValue.setOnItemSelectedListener(this);
        spinnerDateValue.setOnItemSelectedListener(this);
        spinnerCalculationType.setOnItemSelectedListener(this);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioDay:
                if (checked) {
                    setupDateSpinner();
//                    int month = spinnerMonthValue.getSelectedItemPosition();
//                    List<Integer> days = new ArrayList<Integer>();
//                    Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
//                    calendar.setTime(new Date(System.currentTimeMillis()));
//                    calendar.set(Calendar.MONTH, month);
//                    int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//                    for (int i = 1; i <= numDays; i++) {
//                        days.add(i);
//                    }
//                    adapterDurationValueDay.clear();
//                    adapterDurationValueDay.addAll(days);
//                    spinnerDateValue.setAdapter(adapterDurationValueDay);
//                    spinnerDateValue.setVisibility(View.VISIBLE);
//                    getRecordList();
                    setupGraph(getRecordList());
                }
                break;
            case R.id.radioMonth:
                if (checked) {
                    spinnerDateValue.setVisibility(View.INVISIBLE);// Ninjas rule
                    setupGraph(getRecordList());
//                    getRecordList();
                }
                break;
        }
    }

    void setupDateSpinner() {
        if (R.id.radioDay == radioDurationType.getCheckedRadioButtonId()) {
            int month = spinnerMonthValue.getSelectedItemPosition();
            List<Integer> days = new ArrayList<Integer>();
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(new Date(System.currentTimeMillis()));
            calendar.set(Calendar.MONTH, month);
            int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int i = 1; i <= numDays; i++) {
                days.add(i);
            }
            adapterDurationValueDay.clear();
            adapterDurationValueDay.addAll(days);
            spinnerDateValue.setAdapter(adapterDurationValueDay);
            spinnerDateValue.setVisibility(View.VISIBLE);
        }
    }

    void displayEfficiency(){
        List<Date> selectedDates = getStartDate();
        Date t1 = new Date();
        Date t2 = new Date();
        int digit = Integer.parseInt(spinnerDigit.getSelectedItem().toString());
        if(selectedDates != null && selectedDates.size() > 1) {
            t1 = selectedDates.get(0);
            t2 = selectedDates.get(1);
        }
        String calcTypePosition = spinnerCalculationType.getSelectedItem().toString();
        int posCount = dh.getNumberOfCorrectRecords(t1.getTime(), t2.getTime(), digit, calcTypePosition, 1);
        int negCount = dh.getNumberOfCorrectRecords(t1.getTime(),t2.getTime(),digit,calcTypePosition,0);
        Log.d("vvkcount","pos "+ posCount + " neg " + negCount);
        float eff = 0;
        tvEfficiency.setText("hi");
        if(posCount + negCount > 0){
            eff = ((posCount / (posCount + negCount)) * 100) ;
            Log.d("vvkcount", "in if pos " + posCount + " neg " + negCount + " eff " + (posCount * 100) / (posCount + negCount));
            tvEfficiency.setText("");
            tvEfficiency.setText("Your efficiency is " + (posCount * 100) / (posCount + negCount)+" %");
        }
    }

    void setupGraph(List<Record> recordList) {
        displayEfficiency();
        int digit = Integer.parseInt(spinnerDigit.getSelectedItem().toString());
        List<Record> list = recordList;

//        public setDates(int year, int month, TimeZone zone) {
//            Calendar calendar = Calendar.getInstance(zone);
//
//            // Do you really want 0-based months, like Java has? Consider month - 1.
//            calendar.set(year, month, 1, 0, 0, 0);
//            calendar.clear(Calendar.MILLISECOND);
//            startDate = calendar.getTime();
//
//            // Get to the last millisecond in the month
//            calendar.add(Calendar.MONTH, 1);
//            calendar.add(Calendar.MILLISECOND, -1);
//            endDate = calendar.getTime();
//        }


        Log.d("vvk", "digit is " + digit + " size is " + list.size());
        if (list.size() > 0) {
            graph.removeAllSeries();
            DataPoint[] dataCorrect = null;//= new DataPoint[list.size()];
            DataPoint[] dataInCorrect = null;//= new DataPoint[list.size()];
            DataPoint[] data = new DataPoint[list.size()];
            int correctCount = 0, inCorrectCount = 0, i = 0;
            float min = 0, max = 10;
            Iterator<Record> ite = list.iterator();
            while (ite.hasNext()) {
                Record rec = ite.next();
                float timetaken = rec.getTimeTaken();
                if (min > timetaken) {
                    min = timetaken;
                }
                if (timetaken > max) {
                    max = timetaken;
                }
                Date d = new Date(rec.getCreatedDate());
                Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                calendar.setTime(d);   // assigns calendar to given date
                int h = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
                calendar.get(Calendar.HOUR);        // gets hour in 12h format
                int m = calendar.get(Calendar.MINUTE);

                if (rec.getIsCorrect() == 1) {
                    correctCount++;
                } else {
                    inCorrectCount++;
                }
                data[i++] = new DataPoint(rec.getCreatedDate(), rec.getTimeTaken());
                Log.d("vvk1", "" + h + ":" + m + " val : " + rec.getTimeTaken() + " " + rec.getCreatedDate() + " digit " + rec.getNumDigit() + " correct " + rec.getIsCorrect());

            }
            Log.d("vvk", "count " + correctCount + " " + inCorrectCount);
            if (correctCount > 0)
                dataCorrect = new DataPoint[correctCount];
            if (inCorrectCount > 0)
                dataInCorrect = new DataPoint[inCorrectCount];
            correctCount = 0;
            inCorrectCount = 0;
            ite = list.iterator();
            while (ite.hasNext()) {
                Record rec = ite.next();
                if (rec.getIsCorrect() == 1) {
                    dataCorrect[correctCount++] = new DataPoint(rec.getCreatedDate(), rec.getTimeTaken());
                } else {
                    dataInCorrect[inCorrectCount++] = new DataPoint(rec.getCreatedDate(), rec.getTimeTaken());
                }
            }
            if (dataCorrect != null && dataCorrect.length > 0) {
                Log.d("vvk","positive point series added");
                PointsGraphSeries<DataPoint> seriesOfCorrectpoints = new PointsGraphSeries<DataPoint>(dataCorrect);
                graph.addSeries(seriesOfCorrectpoints);
                seriesOfCorrectpoints.setShape(PointsGraphSeries.Shape.POINT);
                seriesOfCorrectpoints.setSize(10);
                seriesOfCorrectpoints.setColor(Color.GREEN);
            }

            if (dataInCorrect != null && dataInCorrect.length > 0) {
                Log.d("vvk","negative point series added");
                PointsGraphSeries<DataPoint> seriesOfInCorrectPoints = new PointsGraphSeries<DataPoint>(dataInCorrect);
                graph.addSeries(seriesOfInCorrectPoints);
                seriesOfInCorrectPoints.setShape(PointsGraphSeries.Shape.POINT);
                seriesOfInCorrectPoints.setSize(10);
                seriesOfInCorrectPoints.setColor(Color.RED);
            }

            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(data);
            graph.addSeries(series2);
            Log.d("vvk", "line series added");
            int id = radioDurationType.getCheckedRadioButtonId();
            graph.getGridLabelRenderer().setNumHorizontalLabels(5); // only 4 because of the space
            if( id == R.id.radioDay) {
                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            Date d = new Date((long) value);
                            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                            calendar.setTime(d);   // assigns calendar to given date
                            int h = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
                            calendar.get(Calendar.HOUR);        // gets hour in 12h format
                            int m = calendar.get(Calendar.MINUTE);
                            return "" + h + ":" + m;
                        } else {
                            return super.formatLabel(value, isValueX);
                        }
                    }
                });
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(list.get(0).getCreatedDate() - 3600 * 1000);
                graph.getViewport().setMaxX(list.get(0).getCreatedDate() + 3600 * 1000 * 3);
                graph.getViewport().setScalable(true);

            } else {
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(GraphActivity.this));
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(list.get(0).getCreatedDate() - 3600 * 1000 * 5);
                graph.getViewport().setMaxX(list.get(0).getCreatedDate() + 3600 * 1000 * 67 );
                graph.getGridLabelRenderer().setNumHorizontalLabels(3);
            }

            // set manual Y bounds   (list.get(list.size()-1).getCreatedDate() - list.get(0).getCreatedDate())/5
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min);
            graph.getViewport().setMaxY(max);
            graph.onDataChanged(false, false);

            graph.getViewport().setScrollable(true);
        }
    }

     List<Date> getStartDate() {
        List<Date> date = new ArrayList<Date>();
         String calcTypePosition = spinnerCalculationType.getSelectedItem().toString();
         int numDigit = spinnerDigit.getSelectedItemPosition();
         int monthPosition = spinnerMonthValue.getSelectedItemPosition();
         int durationTypeId = radioDurationType.getCheckedRadioButtonId();
         int datePosition = spinnerDateValue.getSelectedItemPosition() + 1;
         Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
         calendar.setTime(new Date(System.currentTimeMillis()));
         int year = calendar.get(Calendar.YEAR);
         if(durationTypeId == R.id.radioDay) {
             calendar.set(year, monthPosition, datePosition, 0, 0, 0);
             Date t1 = calendar.getTime();
             date.add(t1);
             calendar.add(Calendar.DATE,1);
             Date t2 = calendar.getTime();
             date.add(t2);
         } else {
             calendar.set(year, monthPosition, 1, 0, 0, 0);
             Date t1 = calendar.getTime();
             date.add(t1);
             calendar.add(Calendar.MONTH, 1);
             Date t2 = calendar.getTime();
             date.add(t2);
         }
         return date;
     }

    List<Record> getRecordList() {
        List<Record> list = new ArrayList<Record>();
        String calcTypePosition = spinnerCalculationType.getSelectedItem().toString();
        int numDigit = spinnerDigit.getSelectedItemPosition();
        int monthPosition = spinnerMonthValue.getSelectedItemPosition();
        int durationTypeId = radioDurationType.getCheckedRadioButtonId();
        int datePosition = spinnerDateValue.getSelectedItemPosition() + 1;
        List<Date> selectedDates = getStartDate();
        Date t1 = new Date();
        Date t2 = new Date();
        if(selectedDates != null && selectedDates.size() > 1) {
            t1 = selectedDates.get(0);
            t2 = selectedDates.get(1);
        }
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(new Date(System.currentTimeMillis()));
        int year = calendar.get(Calendar.YEAR);
        if(durationTypeId == R.id.radioDay) {
            list = dh.getRecords(t1.getTime(),t2.getTime(),numDigit,calcTypePosition);
            return list;
        } else {
            list = dh.getRecords(t1.getTime(),t2.getTime(),numDigit,calcTypePosition);
            Collections.sort(list, new Comparator<Record>() {
                @Override
                public int compare(Record lhs, Record rhs) {
                    long lhsdate = lhs.getCreatedDate();
                    long rhsdate = rhs.getCreatedDate();
                    if (lhsdate > rhsdate) {
                        return 1;
                    } else if (lhsdate == rhsdate) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
            List<Record> list2 = new ArrayList<Record>();
            Iterator<Record> ite = list.iterator();
            String date = "";
            float avgTimeTaken = 0;
            int count = 0;
            int cPositive = 0;
            long curTime = System.currentTimeMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
            while(ite.hasNext()) {
                Record rec = ite.next();
                Date d = new Date(rec.getCreatedDate());
                String recDate = sdf.format(d);
                if(recDate.equalsIgnoreCase(date)) {
                    count ++;
                    avgTimeTaken += rec.getTimeTaken();
                    if(rec.getIsCorrect() == 1)
                    cPositive++;
                } else {
                    if(date.length() > 0) {
                        Record r = new Record();
                        calendar.setTime(new Date(curTime));
                        int reDate = calendar.get(Calendar.DATE);
                        calendar.set(year, monthPosition, reDate, 0, 0, 0);
                        r.setCreatedDate(calendar.getTimeInMillis());
                        r.setRecordType(calcTypePosition);
                        r.setNumDigit(numDigit);
                        r.setTimeTaken(avgTimeTaken / count);
                        if(cPositive >= (count - cPositive)) {
                            r.setIsCorrect(1);
                        } else {
                            r.setIsCorrect(0);
                        }
                        list2.add(r);
                    }
                    Date d1 = new Date(rec.getCreatedDate());
                    date = sdf.format(d1);
                    count = 1;
                    if(rec.getIsCorrect() == 1) {
                        cPositive = 1;
                    } else {
                        cPositive = 0;
                    }
                    avgTimeTaken = rec.getTimeTaken();
                    curTime = rec.getCreatedDate();
                }
            }
            if(date.length() > 0) {
                Record r = new Record();
                calendar.setTime(new Date(curTime));
                int reDate = calendar.get(Calendar.DATE);
                calendar.set(year, monthPosition, reDate , 0, 0, 0);
                r.setCreatedDate(calendar.getTimeInMillis());
                r.setRecordType(calcTypePosition);
                r.setNumDigit(numDigit);
                r.setTimeTaken(avgTimeTaken / count);
                if(cPositive >= count / 2) {
                    r.setIsCorrect(1);
                } else {
                    r.setIsCorrect(0);
                }
                list2.add(r);
                return list2;
            }
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        setupDateSpinner();
        setupGraph(getRecordList());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
