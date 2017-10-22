package com.example.matthew.cyclebit;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> dates;
    ArrayList<BarEntry> barEntries1;
    ArrayList<BarEntry> barEntries2;
    Random random;
    private FrameLayout content;
    boolean isMonth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ConstraintLayout today = (ConstraintLayout) findViewById(R.id.today);
            ConstraintLayout week = (ConstraintLayout) findViewById(R.id.week);
            ConstraintLayout month = (ConstraintLayout) findViewById(R.id.month);
            today.setVisibility(View.INVISIBLE);
            week.setVisibility(View.INVISIBLE);
            month.setVisibility(View.INVISIBLE);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    today.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    isMonth = true;
                    createRandomBarGraph(((BarChart)findViewById(R.id.monthChart1)), ((BarChart)findViewById(R.id.monthChart2)));
                    month.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    isMonth = false;
                    createRandomBarGraph(((BarChart)findViewById(R.id.weekChart1)), ((BarChart)findViewById(R.id.weekChart2)));
                    week.setVisibility(View.VISIBLE);
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout today = (ConstraintLayout) findViewById(R.id.today);
        ConstraintLayout week = (ConstraintLayout) findViewById(R.id.week);
        ConstraintLayout month = (ConstraintLayout) findViewById(R.id.month);
        content = (FrameLayout) findViewById(R.id.content);
        today.setVisibility(View.VISIBLE);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void createRandomBarGraph(BarChart barChart1, BarChart barChart2) {

        barEntries1 = new ArrayList<>();
        barEntries2 = new ArrayList<>();
        float max = 0f;
        float value = 0f;
        random = new Random();
        ArrayList<Float> calories = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            max = 10f;
            value = random.nextFloat()*max;
            calories.add(value);
        }
        ArrayList<Float> distance = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            max = 10f;
            value = random.nextFloat()*max;
            distance.add(value);
        }
        int values = 0;
        if (isMonth) {
            values = 30;
        } else {
            values = 7;
        }
        for(int j = 0; j < values; j++) {
            barEntries1.add(new BarEntry(j, distance.get(j)));
            barEntries2.add(new BarEntry(j, calories.get(j)));
        }
        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Miles");
        BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Calories");
        BarData barData1 = new BarData(barDataSet1);
        barChart1.setData(barData1);
        BarData barData2 = new BarData(barDataSet2);
        barChart2.setData(barData2);
    }
}
