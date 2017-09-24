package org.ayo.view;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/28.
 */
public class Views {

    private View root;
    private SparseArray<View> base = new SparseArray<>();
    private Views(){

    }

    public static Views bind(View v){
        Views vv = new Views();
        vv.root = v;
        return vv;
    }

    private View find(int id){
        if(base.get(id) != null) {
            return base.get(id);
        }else{
            View v = root.findViewById(id);
            base.put(id, v);
            return v;
        }
    }

    public <T> T findViewById(int id){
        return (T) find(id);
    }

    public String text(int id){
        View v = find(id);
        if(v instanceof TextView){
            return ((TextView)v).getText().toString().trim();
        }else{
            return "----not TextView----";
        }
    }

    public void text(int id, String s){
        View v = find(id);
        if(v instanceof TextView){
            ((TextView)v).setText(s);
        }else{
            //"----not TextView----";
        }
    }

}
