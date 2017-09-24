package com.ayo.sdk.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.JobApi;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import net.vrallev.android.cat.Cat;

import org.ayo.sample.R;

/**
 * @author rwondratschek
 */
public class DemoJobActivity extends AppCompatActivity {

    private static final String LAST_JOB_ID = "LAST_JOB_ID";

    private int mLastJobId;

    private CompoundButton mEnableGcm;
    private CompoundButton mRequiresCharging;
    private CompoundButton mRequiresDeviceIdle;
    private Spinner mNetworkTypeSpinner;

    private JobManager mJobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_activity_main);

        mJobManager = JobManager.instance();

        if (savedInstanceState != null) {
            mLastJobId = savedInstanceState.getInt(LAST_JOB_ID, 0);
        }

        mEnableGcm = (CompoundButton) findViewById(R.id.enable_gcm);
        mRequiresCharging = (CompoundButton) findViewById(R.id.check_requires_charging);
        mRequiresDeviceIdle = (CompoundButton) findViewById(R.id.check_requires_device_idle);
        mNetworkTypeSpinner = (Spinner) findViewById(R.id.spinner_network_type);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getNetworkTypesAsString());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNetworkTypeSpinner.setAdapter(adapter);

        mEnableGcm.setChecked(mJobManager.getConfig().isGcmApiEnabled());
        mEnableGcm.setEnabled(JobApi.GCM.isSupported(this));
        mEnableGcm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mJobManager.getConfig().setGcmApiEnabled(isChecked);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LAST_JOB_ID, mLastJobId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.job_activity_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (JobApi.V_24.isSupported(this)) {
            menu.findItem(R.id.action_force_24).setChecked(false);
        } else {
            menu.findItem(R.id.action_force_24).setVisible(false);
        }
        if (JobApi.V_21.isSupported(this)) {
            menu.findItem(R.id.action_force_21).setChecked(false);
        } else {
            menu.findItem(R.id.action_force_21).setVisible(false);
        }
        if (JobApi.V_19.isSupported(this)) {
            menu.findItem(R.id.action_force_19).setChecked(false);
        } else {
            menu.findItem(R.id.action_force_19).setVisible(false);
        }
        if (JobApi.V_14.isSupported(this)) {
            menu.findItem(R.id.action_force_14).setChecked(false);
        } else {
            menu.findItem(R.id.action_force_14).setVisible(false);
        }
        if (JobApi.GCM.isSupported(this)) {
            menu.findItem(R.id.action_force_gcm).setChecked(false);
        } else {
            menu.findItem(R.id.action_force_gcm).setVisible(false);
        }

        switch (mJobManager.getApi()) {
            case V_24:
                menu.findItem(R.id.action_force_24).setChecked(true);
                break;
            case V_21:
                menu.findItem(R.id.action_force_21).setChecked(true);
                break;
            case V_19:
                menu.findItem(R.id.action_force_19).setChecked(true);
                break;
            case V_14:
                menu.findItem(R.id.action_force_14).setChecked(true);
                break;
            case GCM:
                menu.findItem(R.id.action_force_gcm).setChecked(true);
                break;
            default:
                throw new IllegalStateException("not implemented");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_force_24:
                mJobManager.forceApi(JobApi.V_24);
                return true;
            case R.id.action_force_21:
                mJobManager.forceApi(JobApi.V_21);
                return true;
            case R.id.action_force_19:
                mJobManager.forceApi(JobApi.V_19);
                return true;
            case R.id.action_force_14:
                mJobManager.forceApi(JobApi.V_14);
                return true;
            case R.id.action_force_gcm:
                mJobManager.forceApi(JobApi.GCM);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_simple:
                testSimple();
                break;

            case R.id.button_all_impl:
                testAllImpl();
                break;

            case R.id.button_periodic:
                testPeriodic();
                break;

            case R.id.button_cancel_last:
                testCancelLast();
                break;

            case R.id.button_cancel_all:
                testCancelAll();
                break;

            case R.id.button_exact:
                testExact();
                break;

            case R.id.button_sync_history:
                startActivity(new Intent(this, SyncHistoryActivity.class));
                break;
        }
    }

    private void testSimple() {
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString("key", "Hello world");

        mLastJobId = new JobRequest.Builder(DemoSyncJob.TAG)
//                .setExecutionWindow(3_000L, 4_000L)
//                .setBackoffCriteria(5_000L, JobRequest.BackoffPolicy.LINEAR)
                .setRequiresCharging(mRequiresCharging.isChecked())
                .setRequiresDeviceIdle(mRequiresDeviceIdle.isChecked())
                .setRequiredNetworkType(JobRequest.NetworkType.values()[mNetworkTypeSpinner.getSelectedItemPosition()])
                .setExtras(extras)
                //设置只有此任务的执行条件被满足时，才执行此任务
                .setRequirementsEnforced(true)
                .setPersisted(true)
                .setPeriodic(JobRequest.MIN_INTERVAL)
                .build()
                .schedule();
    }

    private void testAllImpl() {
        JobApi currentApi = mJobManager.getApi();

        for (JobApi api : JobApi.values()) {
            if (api.isSupported(this)) {
                mJobManager.forceApi(api);
                testSimple();
            } else {
                Cat.w("%s is not supported", api);
            }
        }

        mJobManager.forceApi(currentApi);
    }

    private void testPeriodic() {
        mLastJobId = new JobRequest.Builder(DemoSyncJob.TAG)
                .setPeriodic(JobRequest.MIN_INTERVAL, JobRequest.MIN_FLEX)
                .setRequiresCharging(mRequiresCharging.isChecked())
                .setRequiresDeviceIdle(mRequiresDeviceIdle.isChecked())
                .setRequiredNetworkType(JobRequest.NetworkType.values()[mNetworkTypeSpinner.getSelectedItemPosition()])
                .setPersisted(true)
                .build()
                .schedule();
    }

    private void testExact() {
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString("key", "Hello world");

        mLastJobId = new JobRequest.Builder(DemoSyncJob.TAG)
                .setBackoffCriteria(5_000L, JobRequest.BackoffPolicy.EXPONENTIAL)
                .setExtras(extras)
                .setExact(20_000L)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    private void testCancelLast() {
        mJobManager.cancel(mLastJobId);
    }

    private void testCancelAll() {
        mJobManager.cancelAll();
    }

    private String[] getNetworkTypesAsString() {
        String[] result = new String[JobRequest.NetworkType.values().length];
        for (int i = 0; i < JobRequest.NetworkType.values().length; i++) {
            result[i] = JobRequest.NetworkType.values()[i].toString();
        }
        return result;
    }
}
