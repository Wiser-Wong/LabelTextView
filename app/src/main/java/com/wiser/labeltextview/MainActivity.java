package com.wiser.labeltextview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wiser.label.LabelTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LabelTextView tvLabel = findViewById(R.id.tv_label);
        tvLabel.setLabels("新鲜的大水果新鲜的大水果",getLabels(),getRs());
    }

    private List<String> getLabels(){
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            labels.add("标签" + i);
        }
        return labels;
    }

    private List<Integer> getRs(){
        List<Integer> resources = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            resources.add(R.drawable.bg);
        }
        return resources;
    }
}
