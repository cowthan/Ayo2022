package org.ayo.fringe.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import org.ayo.core.Lang;
import org.ayo.fringe.model.MmListModel;
import org.ayo.fringe.model.MmModel;
import org.ayo.fringe.model.timeline.AyoTimeline;
import org.ayo.fringe.repo.http.Api;
import org.ayo.fringe.ui.adapter.MmItemTemplate;
import org.ayo.http.callback.BaseHttpCallback;
import org.ayo.http.callback.FailInfo;
import org.ayo.http.callback.HttpProblem;
import org.ayo.list.adapter.AyoItemTemplate;
import org.ayo.list.adapter.OnItemClickCallback;
import org.ayo.photo.ShowPicActivity;
import org.ayo.template.recycler.AyoListTemplateFragment;
import org.ayo.template.recycler.condition.AyoCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * 美女列表页
 */
public class MMListFragment extends AyoListTemplateFragment<MmModel>  {


    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        super.onCreate2(view, bundle);
    }

    @Override
    public void loadCache() {

    }

    @Override
    public void loadData(final AyoCondition cond) {
        final MmListCondition condition = (MmListCondition) cond;
        Api.getMmList(MMListFragment.class.getName(), condition.nextPageUrl, new BaseHttpCallback<MmListModel>() {
            @Override
            public void onFinish(boolean isSuccess, HttpProblem problem, FailInfo resp, MmListModel mmModels) {
                if(isSuccess){
                    condition.nextPageUrl = mmModels.next;
                    onLoadOk(mmModels.data);
                }else{
                    onLoadFail(false, "error", resp.reason, resp.code);
                }
            }
        });
    }


    @Override
    protected List<AyoItemTemplate<MmModel>> getTemplate() {
        List<AyoItemTemplate<MmModel>> templates = new ArrayList<>();
        templates.add(new MmItemTemplate(getActivity(), new OnItemClickCallback<MmModel>(){
            @Override
            public void onItemClick(ViewGroup listableView, View itemView, int position, MmModel model) {
                String[] urls = new String[Lang.count(model.all)];
                for (int i = 0; i < Lang.count(model.all); i++) {
                    urls[i] = model.all.get(i);
                }
                ShowPicActivity.showPictures(getActivity(), urls, 1);
            }
        }));
        return templates;
    }


    @Override
    public AyoCondition initCondition() {
        MmListCondition cond = new MmListCondition(1);
        cond.nextPageUrl = "http://www.dongnimei.net/api/index";
        return cond;
    }


    private class MmListCondition extends AyoCondition{

        public String nextPageUrl = "";

        public MmListCondition(int pageStart) {
            super(pageStart);
        }

    }

    public static class ResponseWrapper{
        public List<AyoTimeline> statuses;
    }
}
