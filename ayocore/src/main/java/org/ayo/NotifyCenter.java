
package org.ayo;

import android.support.annotation.StringDef;

import org.ayo.core.Lang;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by qiaoliang on 2017/5/6. 小红点和数字气泡管理中心
 */

public class NotifyCenter {

    /** 商城客服未读消息 */
    public static final String TYPE_SERVICE = "1";

    /** 订单：待付款 */
    public static final String TYPE_ORDER_WAIT_PAY = "2";

    /** 订单：待发货 */
    public static final String TYPE_ORDER_WAIT_DELIVERTY = "3";

    /** 订单：待收货 */
    public static final String TYPE_ORDER_WAIT_OPEN = "4";

    /** 订单：待评价 */
    public static final String TYPE_ORDER_WAIT_COMMENT = "5";

    /** 订单：售后订单 */
    public static final String TYPE_ORDER_AFTER_SERVICE = "6";

    /** 优惠券：有新优惠券，新是相对于上次进优惠券列表 */
    public static final String TYPE_COUPON = "7";

    /** 有新的系统消息：来自后台 */
    public static final String TYPE_SYSTEM_ALERT = "8";

    /** 有新的商城消息：来自后台 */
    public static final String TYPE_MALL_ALERT = "9";

    @StringDef({
            TYPE_SERVICE, TYPE_ORDER_WAIT_PAY, TYPE_ORDER_WAIT_DELIVERTY, TYPE_ORDER_WAIT_OPEN,
            TYPE_ORDER_WAIT_COMMENT, TYPE_ORDER_AFTER_SERVICE, TYPE_COUPON, TYPE_SYSTEM_ALERT,
            TYPE_MALL_ALERT
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface NotifyType {
    }

    private NotifyCenter() {
    }

    private static final class H {
        private static final NotifyCenter instance = new NotifyCenter();
    }

    public static NotifyCenter getDefault() {
        return H.instance;
    }

    private Map<String, Integer> alertNums = new HashMap<>();

    private List<ListnerPair> listeners = new ArrayList<>();

    public interface OnAlertChangeLisenter {
        void onAlertChanged(Map<String, Integer> alerts, int totalNum);
    }

    public static class ListnerPair {
        public Object tag;

        public List<String> types;

        public OnAlertChangeLisenter lisenter;

        public ListnerPair(Object tag, List<String> types, OnAlertChangeLisenter lisenter) {
            this.types = types;
            this.lisenter = lisenter;
            this.tag = tag;
        }

        // @Override
        // public boolean equals(Object obj) {
        // if(obj instanceof ListnerPair){
        // if(this.tag == ((ListnerPair) obj).tag) return true;
        // }
        // return false;
        // }
    }

    public void register(Object tag, List<String> types, OnAlertChangeLisenter listener) {
        listeners.add(new ListnerPair(tag, types, listener));

        // 第一次注册，也得通知一下个数，否则只能等个数变了才更新界面，就晚了
        notifyAlertChanged(types.get(0));
    }

    public void register(Object tag, @NotifyType String type, OnAlertChangeLisenter listener) {
        listeners.add(new ListnerPair(tag, Lang.newArrayList(type), listener));

        // 第一次注册，也得通知一下个数，否则只能等个数变了才更新界面，就晚了
        notifyAlertChanged(type);
    }

    public void unregister(Object tag) {
        Iterator<ListnerPair> it = listeners.iterator();
        while (it.hasNext()) {
            ListnerPair x = it.next();
            if (x.tag == tag) {
                it.remove();
            }
        }
    }

    public void alert(@NotifyType String type, int num) {
        alertNums.put(type, num);
        notifyAlertChanged(type);
    }

    public void alertIncrement(@NotifyType String type, int addNum) {
        int num = alertNums.containsKey(type) ? alertNums.get(type) : 0;
        num += addNum;
        alertNums.put(type, num);
        notifyAlertChanged(type);
    }

    public void alertDecrement(@NotifyType String type, int addNum) {
        int num = alertNums.containsKey(type) ? alertNums.get(type) : 0;
        num -= addNum;
        alertNums.put(type, num);
        notifyAlertChanged(type);
    }

    private void notifyAlertChanged(String type) {
        for (ListnerPair pair : listeners) {
            if (pair.types != null && pair.types.contains(type)) {
                Map<String, Integer> map = new HashMap<>();
                map.put(type, 0);
                int totalNum = 0;
                for (String t : pair.types) {
                    int c = alertNums.containsKey(t) ? alertNums.get(t) : 0;
                    map.put(t, c);
                    totalNum += c;
                }
                pair.lisenter.onAlertChanged(map, totalNum);
            }
        }
    }

}
