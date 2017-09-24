package org.ayo.ui.sample.avloding;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.ayo.component.Master;
import org.ayo.list.progress.AVLoadingIndicatorView;
import org.ayo.sample.R;
import org.ayo.ui.sample.BasePage;

/**
 * Created by Jack Wang on 2016/8/5.
 */

public class AVLoadingSampleActivity extends BasePage {

    private RecyclerView mRecycler;


    @Override
    protected int getLayoutId() {
        return R.layout.avloding_activity_sample;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {

        mRecycler= (RecyclerView) findViewById(R.id.recycler);

        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),4);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(new RecyclerView.Adapter<IndicatorHolder>() {
            @Override
            public IndicatorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView=getActivity().getLayoutInflater().inflate(R.layout.avloding_item_indicator,parent,false);
                return new IndicatorHolder(itemView);
            }

            @Override
            public void onBindViewHolder(IndicatorHolder holder, final int position) {
                holder.indicatorView.setIndicator(INDICATORS[position], Color.WHITE);
                holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle sb = new Bundle();
                        sb.putInt("indicator", INDICATORS[position]);
                        Master.startPage(getActivity(), IndicatorActivity.class, sb);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return INDICATORS.length;
            }
        });
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

    final static class IndicatorHolder extends RecyclerView.ViewHolder{

        public AVLoadingIndicatorView indicatorView;
        public View itemLayout;

        public IndicatorHolder(View itemView) {
            super(itemView);
            itemLayout= itemView.findViewById(R.id.itemLayout);
            indicatorView= (AVLoadingIndicatorView) itemView.findViewById(R.id.indicator);
        }
    }



    public static final int[] INDICATORS=new int[]{
            AVLoadingIndicatorView.BallPulse,
            AVLoadingIndicatorView.BallGridPulse,
            AVLoadingIndicatorView.BallClipRotate,
            AVLoadingIndicatorView.BallClipRotatePulse,
            AVLoadingIndicatorView.SquareSpin,
            AVLoadingIndicatorView.BallClipRotateMultiple,
            AVLoadingIndicatorView.BallPulseRise,
            AVLoadingIndicatorView.BallRotate,
            AVLoadingIndicatorView.CubeTransition,
            AVLoadingIndicatorView.BallZigZag,
            AVLoadingIndicatorView.BallZigZagDeflect,
            AVLoadingIndicatorView.BallTrianglePath,
            AVLoadingIndicatorView.BallScale,
            AVLoadingIndicatorView.LineScale,
            AVLoadingIndicatorView.LineScaleParty,
            AVLoadingIndicatorView.BallScaleMultiple,
            AVLoadingIndicatorView.BallPulseSync,
            AVLoadingIndicatorView.BallBeat,
            AVLoadingIndicatorView.LineScalePulseOut,
            AVLoadingIndicatorView.LineScalePulseOutRapid,
            AVLoadingIndicatorView.BallScaleRipple,
            AVLoadingIndicatorView.BallScaleRippleMultiple,
            AVLoadingIndicatorView.LineSpinFadeLoader,
            AVLoadingIndicatorView.TriangleSkewSpin,
            AVLoadingIndicatorView.Pacman,
            AVLoadingIndicatorView.BallGridBeat,
            AVLoadingIndicatorView.SemiCircleSpin
    };
}
