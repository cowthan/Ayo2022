package org.ayo.robot.anim.ease;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import org.ayo.robot.anim.R;
import org.ayo.robot.anim.ease.adapter.InterpolatorAdapter;


/**
 * 演示Interpolator
 */
public class InterpolatorActivity extends AppCompatActivity {

    private static final long DURATION = 2000;

    private ListView listView;
    private InterpolatorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interpolator);

        adapter = new InterpolatorAdapter(this, DURATION);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectIndex(position);
            }
        });
    }

}
