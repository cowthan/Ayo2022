package org.ayo.appsist;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.ayo.appsist.monitor.FloatCurveView;
import org.ayo.appsist.monitor.MemoryMonitor;
import org.ayo.appsist.monitor.util.MemoryUtil;
import org.ayo.hover.wrapper.R;

import java.text.DecimalFormat;

import io.mattcarroll.hover.Navigator;
import io.mattcarroll.hover.NavigatorContent;
import io.mattcarroll.hover.hoverdemo.introduction.HoverMotion;

/**
 * Created by Administrator on 2017/2/4 0004.
 */

public class AssistMonitorView extends FrameLayout implements NavigatorContent {

    private static final String TAG = "LogcatTextViewDemo";

    public AssistMonitorView(Context context) {
        super(context);
        init();
    }

    public AssistMonitorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AssistMonitorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AssistMonitorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private HoverMotion mHoverMotion;
    private HoverMotion mHoverMotion_mem_heap;
    private HoverMotion mHoverMotion_mem_pss;
    View iv_refresh;

    Button btn_mem_heap;
    Button btn_mem_pss;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.ass_view_monitor, this, true);

        mHoverMotion = new HoverMotion();
        mHoverMotion_mem_heap = new HoverMotion();
        mHoverMotion_mem_pss = new HoverMotion();

        iv_refresh =  findViewById( R.id.iv_refresh );
        btn_mem_heap = (Button) findViewById(R.id.btn_mem_heap);
        btn_mem_pss = (Button) findViewById(R.id.btn_mem_pss);

        MemoryMonitor.getInstance().init(getContext().getApplicationContext());

        refreshUI();

        iv_refresh.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                refreshInfo();
            }
        });

        btn_mem_heap.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {

                if(MemoryMonitor.getInstance().isRunningHeap()){
                    MemoryMonitor.getInstance().stop();
                }else if(MemoryMonitor.getInstance().isRunningPss()){
                    MemoryMonitor.getInstance().stop();
                    MemoryMonitor.getInstance().start(FloatCurveView.MEMORY_TYPE_HEAP);
                }else{
                    MemoryMonitor.getInstance().start(FloatCurveView.MEMORY_TYPE_HEAP);
                }
                refreshUI();
            }
        });
        btn_mem_pss.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if(MemoryMonitor.getInstance().isRunningHeap()){
                    MemoryMonitor.getInstance().stop();
                    MemoryMonitor.getInstance().start(FloatCurveView.MEMORY_TYPE_PSS);
                }else if(MemoryMonitor.getInstance().isRunningPss()){
                    MemoryMonitor.getInstance().stop();
                }else{
                    MemoryMonitor.getInstance().start(FloatCurveView.MEMORY_TYPE_PSS);
                }
                refreshUI();
            }
        });

        refreshInfo();
    }

    private class RefreshMemInfoTask implements Runnable{
        @Override
        public void run() {

        }
    }

    public void refreshInfo() {
        final TextView tv_info = (TextView) findViewById(R.id.tv_info);
        MemoryUtil.getMemoryInfo(getContext(), new MemoryUtil.OnGetMemoryInfoCallback() {
            @Override
            public void onGetMemoryInfo(String pkgName, int pid, MemoryUtil.RamMemoryInfo ramMemoryInfo, MemoryUtil.PssInfo pssInfo, MemoryUtil.DalvikHeapMem dalvikHeapMem) {
                String s = "最大可用内存：" + formatByte(MemoryUtil.getMaxHeapSize(getContext())) + "\n"
                        + "每一个app都有一个硬性heap size限制，随RAM大小不同而不同，如果heap大小已经达到此值，则会抛出OOM，此值通过getMemoryClass获取，如果需要设置缓存大小，可以根据这个值来计算\n"
                        + "\n"
                        + "heap: 堆内存的使用情况, 通过Runtime.maxMemory系列获取\n"
                        + "heap total: " + formatByte(dalvikHeapMem.totalMem) + "\n"
                        + "heap max: " + formatByte(dalvikHeapMem.maxMem) + "\n"
                        + "heap free: " + formatByte(dalvikHeapMem.freeMem) + "\n"
                        + "heap allocated: " + formatByte(dalvikHeapMem.allocated) + "\n"
                        + "\n"
                        + "pss: 实际物理内存占用, 通过am.getProcessMemoryInfo获取\n"
                        + "pss totalPss: " + formatByte(pssInfo.totalPss) + "\n"
                        + "pss dalvikPss: " + formatByte(pssInfo.dalvikPss) + "\n"
                        + "pss nativePss: " + formatByte(pssInfo.nativePss) + "\n"
                        + "pss otherPss: " + formatByte(pssInfo.otherPss) + "\n"
                        + "\n"
                        + "RAM：手机内存\n"
                        + "RAM totalMem: " + formatByte(ramMemoryInfo.totalMem) + "\n"
                        +  "RAM availMem: " + formatByte(ramMemoryInfo.availMem) + "\n"
                        + "RAM isLowMemory: " + ramMemoryInfo.isLowMemory + "\n"
                        + "RAM lowMemThreshold: " + formatByte(ramMemoryInfo.lowMemThreshold) + "\n";
                tv_info.setText(s);
            }
        });
        MemoryUtil.getSystemRam(getContext(), new MemoryUtil.OnGetRamMemoryInfoCallback() {
            @Override
            public void onGetRamMemoryInfo(MemoryUtil.RamMemoryInfo ramMemoryInfo) {

            }
        });


    }

    private static String formatByte(long size){
        double d = size/1024.0;
        DecimalFormat df = new DecimalFormat("#.000");
        return df.format(d) + "M";
    }

    private void refreshUI(){
        if(MemoryMonitor.getInstance().isRunningHeap()){
            btn_mem_heap.setText("内存监控-heap-关闭");
        }else{
            btn_mem_heap.setText("内存监控-heap-开启");
        }

        if(MemoryMonitor.getInstance().isRunningPss()){
            btn_mem_pss.setText("内存监控-pss-关闭");
        }else{
            btn_mem_pss.setText("内存监控-pss-开启");
        }
    }



    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onShown(@NonNull Navigator navigator) {
        mHoverMotion.start(iv_refresh);
        mHoverMotion_mem_heap.start(btn_mem_heap);
        mHoverMotion_mem_pss.start(btn_mem_pss);
    }

    @Override
    public void onHidden() {
        mHoverMotion.stop();
        mHoverMotion_mem_heap.stop();
        mHoverMotion_mem_pss.stop();
    }



}
