package com.chiemy.demo.animationlinearlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SlideAlphaLayoutAnimator animator;
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

                if (recyclerView.isShown()) {
                    animator.invisibleAnimation(View.INVISIBLE);
                } else {
                    animator.visibleAnimation();
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());

        animator = new SlideAlphaLayoutAnimator(recyclerView);

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                animator.visibleAnimation();
            }
        }, 2000);
    }


    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return MyViewHolder.create(parent);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void bind(int position) {
            TextView tv = (TextView) itemView;
            tv.setText("Item " + String.valueOf(position));
        }

        public static MyViewHolder create(ViewGroup parent) {
            TextView tv = new TextView(parent.getContext());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            return new MyViewHolder(tv);
        }
    }
}
