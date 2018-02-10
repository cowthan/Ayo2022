
package com.app.core.prompt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.core.R;

import java.util.List;

public class ListDialog extends BaseDialog
        implements DialogInterface.OnCancelListener, View.OnClickListener {

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    public interface OnItemSelectedCallback {
        void onSelected(String item, int position);
    }

    private OnItemSelectedCallback callback;

    public void setOnItemSelectedCallback(OnItemSelectedCallback onItemSelectedCallback) {
        this.callback = onItemSelectedCallback;
    }

    ListView listview;

    private List<String> data;

    private TextView mTitle;

    private int index;

    private String title;
    private boolean cancelButtonEnable = true;

    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String entity = getItem(position);
            View layout = LayoutInflater.from(getContext()).inflate(R.layout.app_item_dialog_list,
                    null);
            TextView view = (TextView)layout.findViewById(R.id.title);
            View img = layout.findViewById(R.id.img);
            view.setText(entity);
            if (index == position) {
                view.setTextColor(getContext().getResources().getColor(R.color.app_dialog_title));
                img.setVisibility(View.VISIBLE);
            } else {
                view.setTextColor(0xff333333);
                img.setVisibility(View.GONE);
            }
            return layout;
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            cancel();
            onItemClicked(view, data.get(position), position);
        }
    };

    public ListDialog(Context context, List<String> data, int index) {
        super(context);
        setCanceledOnTouchOutside(true);
        this.index = index;
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_dlg_common_list);
        listview = (ListView)findViewById(R.id.listview);

        mTitle = (TextView)findViewById(R.id.download_title);
        findViewById(R.id.cancel).setOnClickListener(this);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        int width = getContext().getResources().getDisplayMetrics().widthPixels * 4 / 5;
        if (width > 800)
            width = 800;
        lp.width = width;

        dialogWindow.setAttributes(lp);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(onItemClickListener);
        if ((getContext().getResources()
                .getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && data.size() > 3)
                || (getContext().getResources()
                        .getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE
                        && data.size() > 5)) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)listview
                    .getLayoutParams();
            params.height = getContext().getResources().getDisplayMetrics().heightPixels / 2;
            listview.setLayoutParams(params);
        }
        setOnCancelListener(this);

        if (mTitle != null && !"".equals(title)) {
            mTitle.setText(title);
            mTitle.setVisibility(View.VISIBLE);
            findViewById(R.id.divider_top).setVisibility(View.VISIBLE);
        } else {
            mTitle.setVisibility(View.GONE);
            findViewById(R.id.divider_top).setVisibility(View.GONE);
        }

        findViewById(R.id.cancel).setVisibility(cancelButtonEnable ? View.VISIBLE : View.GONE);
        findViewById(R.id.divider_bottom).setVisibility(cancelButtonEnable ? View.VISIBLE : View.GONE);

    }

    public void onClick(View v) {
        cancel();
    }

    public void onItemClicked(View v, String entity, int position) {
        if (callback == null)
            return;
        callback.onSelected(entity, position);
    }

    public void setTitle(String title) {
        this.title = title;

    }

    public void setCancelButtonEnable(boolean enable) {
        this.cancelButtonEnable = enable;
    }

}
