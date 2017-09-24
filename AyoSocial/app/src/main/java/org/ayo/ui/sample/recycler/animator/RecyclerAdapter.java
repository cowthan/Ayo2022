package org.ayo.ui.sample.recycler.animator;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.ayo.notify.toaster.Toaster;
import org.ayo.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private List<Integer> mColors = new ArrayList<>();

    public RecyclerAdapter(Activity a, RecyclerView r){
        mActivity = a;
        mRecyclerView = r;
        generateData();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final MyViewHolder myHolder = (MyViewHolder) holder;
        int color = mColors.get(position);
        myHolder.container.setBackgroundColor(color);
        myHolder.textView.setText(position + " --- " + "#" + Integer.toHexString(color));

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toaster.toastShort("click--" + position);
                changeItem(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mColors.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = mActivity.getLayoutInflater().inflate(R.layout.item_flip_color, parent, false);

        return new MyViewHolder(container);
    }

    public static int generateColor() {
        int red = ((int) (Math.random() * 200));
        int green = ((int) (Math.random() * 200));
        int blue = ((int) (Math.random() * 200));
        return Color.rgb(red, green, blue);
    }
    private void generateData() {
        for (int i = 0; i < 100; ++i) {
            mColors.add(generateColor());
        }
    }
    private void changeItem(View view) {
        int position = mRecyclerView.getChildAdapterPosition(view);
        if (position != RecyclerView.NO_POSITION) {
            int color = generateColor();
            mColors.set(position, color);
            notifyItemChanged(position);
        }
    }

    public List<Integer> getData() {
        return mColors;
    }
}
