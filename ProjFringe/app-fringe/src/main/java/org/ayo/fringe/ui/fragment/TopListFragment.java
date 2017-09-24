package org.ayo.fringe.ui.fragment;

import android.view.View;

import org.ayo.fringe.R;
import org.ayo.http.callback.BaseHttpCallback;
import org.ayo.http.callback.FailInfo;
import org.ayo.http.callback.HttpProblem;
import org.ayo.list.adapter.AyoItemTemplate;
import org.ayo.template.recycler.AyoListTemplateFragment;
import org.ayo.template.recycler.condition.AyoCondition;
import org.ayo.template.status.DefaultStatus;
import org.ayo.template.status.DefaultStatusProvider;
import org.ayo.template.status.StatusProvider;
import org.ayo.template.status.StatusUIManager;
import org.ayo.fringe.api2.TopApi;
import org.ayo.fringe.model.top.Top;
import org.ayo.fringe.ui.adapter.TopSingleAdapter;
import org.ayo.fringe.ui.adapter.TopTrippleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 微博列表页
 */
public class TopListFragment extends AyoListTemplateFragment<Top>  {

    @Override
    protected void initStatusUI(StatusUIManager statusUIManager) {
        super.initStatusUI(statusUIManager);
        statusUIManager.addStatusProvider(DefaultStatus.STATUS_SERVER_ERROR,
                new DefaultStatusProvider.DefaultServerErrorStatusView(getActivity(),
                        DefaultStatus.STATUS_SERVER_ERROR, mXRecyclerView, new StatusProvider.OnStatusViewCreateCallback() {
                    @Override
                    public void onCreate(String status, View statusView) {
                        View v = statusView.findViewById(R.id.btn_retry);
                        v.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                clearStatus();
                                refreshAuto();
                            }
                        });
                    }
                }));
    }

    @Override
    public void loadCache() {

    }

    @Override
    public void loadData(AyoCondition cond) {
//        Async.post(new Runnable() {
//            @Override
//            public void run() {
//                String json = Files.file.getContentFromAssets("top/beauty_json_2.json");
//                List<Top> r = JsonUtils.getBeanList(json, Top.class);
//                onLoadOk(r);
//            }
//        }, 1000);
        TopApi.getTopList("top list", cond.page, new BaseHttpCallback<List<Top>>(){
            @Override
            public void onFinish(boolean isSuccess, HttpProblem problem, FailInfo resp, List<Top> tops) {
                if(isSuccess){
                    onLoadOk(tops);
                }else{
                    onLoadFail(false, DefaultStatus.STATUS_SERVER_ERROR, resp.reason, resp.code);
                }
            }
        });
    }


    @Override
    protected List<AyoItemTemplate> getTemplate() {
        List<AyoItemTemplate> templates = new ArrayList<>();
        templates.add(new TopSingleAdapter(getActivity()));
        templates.add(new TopTrippleAdapter(getActivity()));
        return templates;
    }


    @Override
    public TopCondition initCondition() {
        return new TopCondition(1);
    }


    private class TopCondition extends AyoCondition{

        public TopCondition(int pageStart) {
            super(pageStart);
        }

    }

}
