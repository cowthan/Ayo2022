package com.app.core.utils;

import org.ayo.core.Lang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaoliang on 2017/10/16.
 *
 * 点选逻辑，如优惠券选择，地址选择
 * 支持两种特性，可选择：
 * 1 对同一条目，点击是否切换
 * 2 选择条目的个数限制
 */

public class CheckHelper {

    public interface CheckableModel{
        void onCheckChanged(boolean isChecked);
        boolean isChecked();
        boolean isCheckable();
        void setCheckable(boolean enable);
    }


    public interface OnCheckChangedCallback{
        void onCheckChanged(boolean isChecked, CheckableModel model);
        void onCheckTooMuch(int currentCount, int maxCount);
    }

    private List<? extends CheckableModel> mItems;
    private List<CheckableModel> mSelectedItems = new ArrayList<>();
    private OnCheckChangedCallback mCallback;
    private int mMaxCount = 1;
    private boolean mEnableCancel = true;
    private boolean isRadioButtonMode = false;


    private CheckHelper(){}

    public static CheckHelper from(List<? extends CheckableModel> items){
        CheckHelper checkHelper = new CheckHelper();
        checkHelper.mItems = items;
        if(checkHelper.mItems == null){
            checkHelper.mItems = new ArrayList<>();
        }
        return checkHelper;
    }

    public CheckHelper callback(OnCheckChangedCallback checkChangedCallback){
        this.mCallback = checkChangedCallback;
        return this;
    }
    public CheckHelper modeCheckBox(int max, boolean cancelable){
        this.mMaxCount = max;
        this.mEnableCancel = cancelable;
        return this;
    }

    public CheckHelper modeRadioButton(){
        this.mMaxCount = 1;
        isRadioButtonMode = true;
        this.mEnableCancel = false;
        return this;
    }

    /**
     * 点击后触发
     * @param model
     */
    public void onTrigger(CheckableModel model){
        if(model == null) return;
        if(model.isChecked()){
            // 即将取消选中
            if(!mEnableCancel){
                return;
            }else{
                if(!mSelectedItems.contains(model)){
                    return;
                }else{
                    model.onCheckChanged(false);
                    mSelectedItems.remove(model);
                    mCallback.onCheckChanged(false, model);
                }
            }
        }else{
            // 即将选中
            if(mSelectedItems.contains(model)){
                return;
            }else{
                if(model.isCheckable()){

                    int selectedCount = Lang.count(mSelectedItems);
                    if(isRadioButtonMode){
                        //单选互斥机制
                        if(selectedCount == 0){
                            model.onCheckChanged(true);
                            mSelectedItems.add(model);
                            mCallback.onCheckChanged(true, model);
                        }else{
                            //取消之前的
                            CheckableModel m = mSelectedItems.get(0);
                            m.onCheckChanged(false);
                            mSelectedItems.clear();
                            mCallback.onCheckChanged(false, m);

                            //选中新的
                            model.onCheckChanged(true);
                            mSelectedItems.add(model);
                            mCallback.onCheckChanged(true, model);
                        }
                    }else{
                        if(Lang.count(mSelectedItems) == mMaxCount){
                            mCallback.onCheckTooMuch(Lang.count(mSelectedItems), mMaxCount);
                        }else{
                            model.onCheckChanged(true);
                            mSelectedItems.add(model);
                            mCallback.onCheckChanged(true, model);
                        }
                    }
                }else{
                    return;
                }
            }
        }
    }

    /**
     * 代码里触发
     * @param check
     * @param model
     */
    public void forceChangeState(boolean check, CheckableModel model){
        if(check){
            model.onCheckChanged(true);
            mSelectedItems.add(model);
            mCallback.onCheckChanged(true, model);
        }else{
            model.onCheckChanged(false);
            mSelectedItems.remove(model);
            mCallback.onCheckChanged(false, model);
        }
    }

    public <T extends CheckableModel> List<T> getSelectedList(){
        List<T> list = new ArrayList<>();
        for(CheckableModel t: mSelectedItems){
            list.add((T) t);
        }
        return list;
    }

}
