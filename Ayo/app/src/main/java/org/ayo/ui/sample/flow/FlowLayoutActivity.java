package org.ayo.ui.sample.flow;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ayo.layout.flow.FlowLayout;
import org.ayo.layout.flow.logic.CommonLogic;
import org.ayo.sample.R;

public class FlowLayoutActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_layout);

        final FlowLayout layout = (FlowLayout) getActivity().findViewById(R.id.flowLayout);

        final Button buttonOrientation = new Button(getActivity());
        buttonOrientation.setLayoutParams(new FlowLayout.LayoutParams(100, 100));
        buttonOrientation.setTextSize(8);
        buttonOrientation.setText("Switch Orientation (Current: Horizontal)");
        buttonOrientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.setOrientation(1 - layout.getOrientation());
                buttonOrientation.setText(layout.getOrientation() == CommonLogic.HORIZONTAL ?
                        "Switch Orientation (Current: Horizontal)" :
                        "Switch Orientation (Current: Vertical)");
            }
        });
        layout.addView(buttonOrientation, 0);

        final Button buttonGravity = new Button(getActivity());
        buttonGravity.setLayoutParams(new FlowLayout.LayoutParams(100, 100));
        buttonGravity.setTextSize(8);
        buttonGravity.setText("Switch Gravity (Current: FILL)");
        buttonGravity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (layout.getGravity()) {
                    case Gravity.LEFT | Gravity.TOP:
                        layout.setGravity(Gravity.FILL);
                        buttonGravity.setText("Switch Gravity (Current: FILL)");
                        break;
                    case Gravity.FILL:
                        layout.setGravity(Gravity.CENTER);
                        buttonGravity.setText("Switch Gravity (Current: CENTER)");
                        break;
                    case Gravity.CENTER:
                        layout.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                        buttonGravity.setText("Switch Gravity (Current: RIGHT | BOTTOM)");
                        break;
                    case Gravity.RIGHT | Gravity.BOTTOM:
                        layout.setGravity(Gravity.LEFT | Gravity.TOP);
                        buttonGravity.setText("Switch Gravity (Current: LEFT | TOP)");
                        break;
                }
            }
        });
        layout.addView(buttonGravity, 0);

        final Button buttonLayoutDirection = new Button(getActivity());
        buttonLayoutDirection.setLayoutParams(new FlowLayout.LayoutParams(100, 100));
        buttonLayoutDirection.setTextSize(8);
        buttonLayoutDirection.setText("Switch LayoutDirection (Current: LTR)");
        buttonLayoutDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.setLayoutDirection(1 - layout.getLayoutDirection());
                buttonLayoutDirection.setText(layout.getLayoutDirection() == FlowLayout.LAYOUT_DIRECTION_LTR ?
                        "Switch LayoutDirection (Current: LTR)" :
                        "Switch LayoutDirection (Current: RTL)");
            }

        });
        layout.addView(buttonLayoutDirection, 0);

        final Button buttonDebug = new Button(getActivity());
        buttonDebug.setLayoutParams(new FlowLayout.LayoutParams(100, 100));
        buttonDebug.setTextSize(8);
        buttonDebug.setText("Switch Debug (Current: true)");
        buttonDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.setDebugDraw(!layout.isDebugDraw());
                buttonDebug.setText(layout.isDebugDraw() ?
                        "Switch LayoutDirection (Current: true)" :
                        "Switch LayoutDirection (Current: false)");
            }

        });
        layout.addView(buttonDebug, 0);


        TextView textView = (TextView) LayoutInflater.from(layout.getContext())
                .inflate(R.layout.flow_inflating_layout, layout, false);
        textView.setText("inflated layout text");
        layout.addView(textView,0);
    }

    public Activity getActivity() {
        return this;
    }
}
