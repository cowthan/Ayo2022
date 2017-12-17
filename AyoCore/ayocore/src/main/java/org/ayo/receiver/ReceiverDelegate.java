package org.ayo.receiver;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/8.
 */

public class ReceiverDelegate {
    private ReceiverDelegate(){}

    private static final class Holder{
        private static final ReceiverDelegate instance = new ReceiverDelegate();
    }

    public static ReceiverDelegate getDefault(){
        return Holder.instance;
    }

    private List<ReceiverAction> receivers = new LinkedList<>();

    public void register(ReceiverAction receiver){
        if(!receivers.contains(receiver)) receivers.add(receiver);
    }

    public void unregister(ReceiverAction receiver){
        if(receivers.contains(receiver)) receivers.remove(receiver);
    }

    public List<ReceiverAction> getReceivers(){
        return Collections.unmodifiableList(receivers);
    }


}
