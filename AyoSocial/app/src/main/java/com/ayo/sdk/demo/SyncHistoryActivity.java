package com.ayo.sdk.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.ayo.sample.R;

/**
 * @author rwondratschek
 */
public class SyncHistoryActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_activity_sync_history);

        mTextView = (TextView) findViewById(R.id.textView_log);
        refreshView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.job_activity_sync_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync_now:
                syncAsynchronously();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void syncAsynchronously() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return new DemoSyncEngine(SyncHistoryActivity.this).sync();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                refreshView();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void refreshView() {
        mTextView.setText(new DemoSyncEngine(this).getSuccessHistory());
    }
}
