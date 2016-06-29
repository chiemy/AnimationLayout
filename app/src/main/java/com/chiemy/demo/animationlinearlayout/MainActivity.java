package com.chiemy.demo.animationlinearlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AnimationLinearLayout animationLayout = (AnimationLinearLayout) findViewById(R.id.layout);

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animationLayout.isShown()) {
                    animationLayout.gone();
                } else {
                    animationLayout.show();
                }
            }
        });
    }
}
