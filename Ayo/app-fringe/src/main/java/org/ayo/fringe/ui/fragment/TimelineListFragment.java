package org.ayo.fringe.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.ayo.core.Async;
import org.ayo.file.IO;
import org.ayo.http.utils.JsonUtils;
import org.ayo.list.adapter.AyoItemTemplate;
import org.ayo.template.recycler.AyoListTemplateFragment;
import org.ayo.template.recycler.condition.AyoCondition;
import org.ayo.fringe.model.timeline.AyoTimeline;
import org.ayo.fringe.ui.adapter.AyoTimeLineAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 微博列表页
 */
public class TimelineListFragment extends AyoListTemplateFragment<AyoTimeline>  {


    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        super.onCreate2(view, bundle);
    }

    @Override
    public void loadCache() {

    }

    @Override
    public void loadData(AyoCondition cond) {
        Async.post(new Runnable() {
            @Override
            public void run() {
                String json = IO.string(IO.fromAssets("default/appDir/1.json")); //Files Files.file.getContentFromAssets();
                ResponseWrapper r = JsonUtils.getBean(json, ResponseWrapper.class);
                onLoadOk(r.statuses);
            }
        }, 1000);
    }


    @Override
    protected List<AyoItemTemplate> getTemplate() {
        List<AyoItemTemplate> templates = new ArrayList<>();
        templates.add(new AyoTimeLineAdapter(getActivity()));
        return templates;
    }


    @Override
    public AyoCondition initCondition() {
        return new TimeLineCondition(1);
    }


    private class TimeLineCondition extends AyoCondition{

        public TimeLineCondition(int pageStart) {
            super(pageStart);
        }

    }

    public static class ResponseWrapper{
        public List<AyoTimeline> statuses;
    }
}
