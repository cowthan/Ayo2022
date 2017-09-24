package org.ayo.ui.sample.template.recycler;

import org.ayo.list.adapter.ItemBean;
import org.ayo.ui.sample.template.recycler.model.Top;
import org.ayo.ui.sample.template.recycler.model.TopBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class Data {

    public static List<ItemBean> getTopList(){

        List<ItemBean> list = new ArrayList<>();

        Top t = new Top();
        list.add(t);

        t = new Top();
        list.add(t);

        t = new Top();
        list.add(t);

        t = new Top();
        list.add(t);

        t = new Top();
        t.type = 2;
        list.add(t);

        t = new Top();
        list.add(t);

        t = new Top();
        t.type = 2;
        list.add(t);

        t = new Top();
        list.add(t);

        t = new Top();
        t.type = 2;
        list.add(t);

        TopBanner b = new TopBanner();
        list.add(b);

        t = new Top();
        list.add(t);

        t = new Top();
        t.type = 2;
        list.add(t);

        t = new Top();
        list.add(t);

        t = new Top();
        t.type = 2;
        list.add(t);t = new Top();
        list.add(t);

        t = new Top();
        t.type = 2;
        list.add(t);

        t = new Top();
        t.type = 2;
        list.add(t);

        t = new Top();
        t.type = 2;
        list.add(t);

        t = new Top();
        t.type = 2;
        list.add(t);

        t = new Top();
        t.type = 2;
        list.add(t);

        t = new Top();
        t.type = 2;
        list.add(t);


        return list;

    }

}
