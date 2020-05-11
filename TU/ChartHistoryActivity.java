package com.example.tu1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class ChartHistoryActivity extends AppCompatActivity {

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_history);
        barChart = findViewById(R.id.barchart);

        BarData data = new BarData(getAxis(), getDataSet());
        //  BarData datas = new BarData();
        barChart.setData(data);
        barChart.setDescription("Grafik History Alat");
        //barChart.getXAxis().setEnabled(false);
//        barChart.setDescriptionPosition(0,0);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.animateXY(2000, 2000);
        barChart.invalidate();

    }
    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(1f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(0f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(0f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(0f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(0f, 4); // May
        valueSet1.add(v1e5);
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "JUMLAH ITEM");
        //barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet1.setValueFormatter(new Formaterku());
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String>getAxis()
    {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("MIC");
        xAxis.add("KOM");
        xAxis.add("PRO");
        xAxis.add("AC");
        xAxis.add("LCD");
        return xAxis;
    }
    private class Formaterku implements ValueFormatter
    {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return "" + ((int) value);
        }
    }
}
