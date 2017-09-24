package org.ayo.ui.sample.dialog.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import org.ayo.animate.yoyo.Techniques;
import org.ayo.animate.yoyo.YoYoAnimator;
import org.ayo.notify.dialog.entity.DialogMenuItem;
import org.ayo.notify.dialog.listener.OnBtnClickL;
import org.ayo.notify.dialog.listener.OnOperItemClickL;
import org.ayo.notify.dialog.widget.ActionSheetDialog;
import org.ayo.notify.dialog.widget.MaterialDialog;
import org.ayo.notify.dialog.widget.NormalDialog;
import org.ayo.notify.dialog.widget.NormalListDialog;
import org.ayo.sample.R;
import org.ayo.ui.sample.BasePage;
import org.ayo.ui.sample.dialog.adapter.HomeAdapter;
import org.ayo.ui.sample.dialog.adapter.TestAdapter;
import org.ayo.ui.sample.dialog.extra.CustomBaseDialog;
import org.ayo.ui.sample.dialog.extra.IOSTaoBaoDialog;
import org.ayo.ui.sample.dialog.extra.ShareBottomDialog;
import org.ayo.ui.sample.dialog.extra.ShareTopDialog;
import org.ayo.ui.sample.dialog.utils.DiaogAnimChoose;
import org.ayo.ui.sample.dialog.utils.T;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogHomeActivity extends BasePage implements ExpandableListView.OnChildClickListener {
    @BindView(R.id.elv) ExpandableListView mElv;
    private Context mContext;
    public static String[] mGroups = {"Default Inner Dialog", "Custom Dialog", "Default Inner Anim", "Custom Anim"};
    public static String[][] mChilds = {
            /**Default Inner Dialog*/
            {
                    "NormalDialog Default(two btns)",                    //0
                    "NormalDialog StyleTwo",                    //1
                    "NormalDialog Custom Attr",                 //2
                    "NormalDialog(one btn)",                 //3
                    "NormalDialog(three btns)",                 //4
                    "MaterialDialogDefault Default(two btns)",  //5
                    "MaterialDialogDefault(one btn)",           //6
                    "MaterialDialogDefault(three btns)",        //7
                    "NormalListDialog",                         //8
                    "NormalListDialog Custom Attr",             //10
                    "NormalListDialog No Title",                //11
                    "NormalListDialog DataSource String[]",     //12
                    "NormalListDialog DataSource Adapter",      //13
                    "ActionSheetDialog",                        //14
                    "ActionSheetDialog NoTitle"                 //15
            },
            /**Custom Dialog*/
            {
                    "Custome Dialog extends BaseDialog", "Custome Dialog extends BottomBaseDialog", "Custome Dialog extends TopBaseDialog"
            },
            /**Default Inner Animation*/
            {
                    "Show Anim", "Dismiss Anim"
            },
            /**Custom Anim*/
            {
                    "Custom Anim like ios taobao"
            }
    };
    private ArrayList<DialogMenuItem> mMenuItems = new ArrayList<>();
    private String[] mStringItems = {"收藏", "下载", "分享", "删除", "歌手", "专辑"};
    private YoYoAnimator mBasIn;
    private YoYoAnimator mBasOut;

    public void setBasIn(YoYoAnimator bas_in) {
        this.mBasIn = bas_in;
    }

    public void setBasOut(YoYoAnimator bas_out) {
        this.mBasOut = bas_out;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ac_dialog_home;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        mContext = getActivity();
        ButterKnife.bind(this, findViewById(R.id.elv));

        mMenuItems.add(new DialogMenuItem("收藏", R.mipmap.ic_winstyle_favor));
        mMenuItems.add(new DialogMenuItem("下载", R.mipmap.ic_winstyle_download));
        mMenuItems.add(new DialogMenuItem("分享", R.mipmap.ic_winstyle_share));
        mMenuItems.add(new DialogMenuItem("删除", R.mipmap.ic_winstyle_delete));
        mMenuItems.add(new DialogMenuItem("歌手", R.mipmap.ic_winstyle_artist));
        mMenuItems.add(new DialogMenuItem("专辑", R.mipmap.ic_winstyle_album));

        mBasIn = Techniques.BounceIn.getAnimator();
        mBasOut = Techniques.SlideOutDown.getAnimator(); //new SlideBottomExit();


        View decorView = getActivity().getWindow().getDecorView();
        HomeAdapter adapter = new HomeAdapter(mContext);
        mElv.setAdapter(adapter);
        // extend all group
        for (int i = 0; i < mGroups.length; i++) {
            mElv.expandGroup(i);
        }

        mElv.setOnChildClickListener(this);
        mElv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        /**Default Inner Dialog*/
        if (groupPosition == 0) {
            if (childPosition == 0) {
                NormalDialogStyleOne();
            } else if (childPosition == 1) {
                NormalDialogStyleTwo();
            } else if (childPosition == 2) {
                NormalDialogCustomAttr();
            } else if (childPosition == 3) {
                NormalDialogOneBtn();
            } else if (childPosition == 4) {
                NormalDialoThreeBtn();
            } else if (childPosition == 5) {
                MaterialDialogDefault();
            } else if (childPosition == 6) {
                MaterialDialogOneBtn();
            } else if (childPosition == 7) {
                MaterialDialogThreeBtns();
            } else if (childPosition == 8) {
                NormalListDialog();
            } else if (childPosition == 9) {
                NormalListDialogCustomAttr();
            } else if (childPosition == 10) {
                NormalListDialogNoTitle();
            } else if (childPosition == 11) {
                NormalListDialogStringArr();
            } else if (childPosition == 12) {
                NormalListDialogAdapter();
            } else if (childPosition == 13) {
                ActionSheetDialog();
            } else if (childPosition == 14) {
                ActionSheetDialogNoTitle();
            }
        }
        /**Custom Dialog*/
        if (groupPosition == 1) {
            if (childPosition == 0) {
                final CustomBaseDialog dialog = new CustomBaseDialog(mContext);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
            } else if (childPosition == 1) {
                final ShareBottomDialog dialog = new ShareBottomDialog(mContext, mElv);
                dialog.showAnim(mBasIn)//
                        .show();//
                // .show(0, 100);
            } else if (childPosition == 2) {
                final ShareTopDialog dialog = new ShareTopDialog(mContext);
                dialog.showAnim(mBasIn)//
                        .show();
                // .show(0, 100);
            }
        }
        /**Default Inner Anim*/
        if (groupPosition == 2) {
            if (childPosition == 0) {
                DiaogAnimChoose.showAnim(this);
            } else if (childPosition == 1) {
                DiaogAnimChoose.dissmissAnim(this);
            }
        }
        /**Custom Anim*/
        if (groupPosition == 3) {
            if (childPosition == 0) {
                final IOSTaoBaoDialog dialog = new IOSTaoBaoDialog(mContext, (View) mElv.getParent());
                dialog.show();
                // .show(0, 100);
            }
        }
        return false;
    }

    private void NormalDialogStyleOne() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.content("是否确定退出程序?")//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "left");
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "right");
                        dialog.dismiss();
                    }
                });
    }

    private void NormalDialogStyleTwo() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.content("为保证咖啡豆的新鲜度和咖啡的香味，并配以特有的传统烘焙和手工冲。")//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(23)//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "left");
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "right");
                        dialog.dismiss();
                    }
                });

    }

    private void NormalDialogCustomAttr() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.isTitleShow(false)//
                .bgColor(Color.parseColor("#383838"))//
                .cornerRadius(5)//
                .content("是否确定退出程序?")//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#ffffff"))//
                .dividerColor(Color.parseColor("#222222"))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"))//
                .btnPressColor(Color.parseColor("#2B2B2B"))//
                .widthScale(0.85f)//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "left");
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "right");
                        dialog.dismiss();
                    }
                });
    }

    private void NormalDialogOneBtn() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.content("你今天的抢购名额已用完~")//
                .btnNum(1)
                .btnText("继续逛逛")//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                T.showShort(mContext, "middle");
                dialog.dismiss();
            }
        });
    }

    private void NormalDialoThreeBtn() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.content("你今天的抢购名额已用完~")//
                .style(NormalDialog.STYLE_TWO)//
                .btnNum(3)
                .btnText("取消", "确定", "继续逛逛")//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "left");
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "right");
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "middle");
                        dialog.dismiss();
                    }
                });
    }

    private void MaterialDialogDefault() {
        final MaterialDialog dialog = new MaterialDialog(mContext);
        dialog.content(
                "嗨！这是一个 MaterialDialogDefault. 它非常方便使用，你只需将它实例化，这个美观的对话框便会自动地显示出来。"
                        + "它简洁小巧，完全遵照 Google 2014 年发布的 Material Design 风格，希望你能喜欢它！^ ^")//
                .btnText("取消", "确定")//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "left");
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "right");
                        dialog.dismiss();
                    }
                }
        );
    }


    private void MaterialDialogThreeBtns() {
        final MaterialDialog dialog = new MaterialDialog(mContext);
        dialog.isTitleShow(false)//
                .btnNum(3)
                .content("为保证咖啡豆的新鲜度和咖啡的香味，并配以特有的传统烘焙和手工冲。")//
                .btnText("确定", "取消", "知道了")//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "left");
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "right");
                        dialog.dismiss();
                    }
                }
                ,
                new OnBtnClickL() {//middle btn click listener
                    @Override
                    public void onBtnClick() {
                        T.showShort(mContext, "middle");
                        dialog.dismiss();
                    }
                }
        );
    }

    private void MaterialDialogOneBtn() {
        final MaterialDialog dialog = new MaterialDialog(mContext);
        dialog//
                .btnNum(1)
                .content("为保证咖啡豆的新鲜度和咖啡的香味，并配以特有的传统烘焙和手工冲。")//
                .btnText("确定")//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                T.showShort(mContext, "middle");
                dialog.dismiss();
            }
        });
    }

    private void NormalListDialog() {
        final NormalListDialog dialog = new NormalListDialog(mContext, mMenuItems);
        dialog.title("请选择")//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                T.showShort(mContext, mMenuItems.get(position).mOperName);
                dialog.dismiss();
            }
        });
    }


    private void NormalListDialogCustomAttr() {
        final NormalListDialog dialog = new NormalListDialog(mContext, mMenuItems);
        dialog.title("请选择")//
                .titleTextSize_SP(18)//
                .titleBgColor(Color.parseColor("#409ED7"))//
                .itemPressColor(Color.parseColor("#85D3EF"))//
                .itemTextColor(Color.parseColor("#303030"))//
                .itemTextSize(14)//
                .cornerRadius(0)//
                .widthScale(0.8f)//
                .show(R.style.myDialogAnim);

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                T.showShort(mContext, mMenuItems.get(position).mOperName);
                dialog.dismiss();
            }
        });
    }


    private void NormalListDialogNoTitle() {
        final NormalListDialog dialog = new NormalListDialog(mContext, mMenuItems);
        dialog.title("请选择")//
                .isTitleShow(false)//
                .itemPressColor(Color.parseColor("#85D3EF"))//
                .itemTextColor(Color.parseColor("#303030"))//
                .itemTextSize(15)//
                .cornerRadius(2)//
                .widthScale(0.75f)//
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                T.showShort(mContext, mMenuItems.get(position).mOperName);
                dialog.dismiss();
            }
        });
    }

    private void NormalListDialogStringArr() {
        final NormalListDialog dialog = new NormalListDialog(mContext, mStringItems);
        dialog.title("请选择")//
                .layoutAnimation(null)
                .show(R.style.myDialogAnim);
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                T.showShort(mContext, mMenuItems.get(position).mOperName);
                dialog.dismiss();
            }
        });
    }

    private void NormalListDialogAdapter() {
        final NormalListDialog dialog = new NormalListDialog(mContext, new TestAdapter(mContext, mMenuItems));
        dialog.title("请选择")//
                .show(R.style.myDialogAnim);
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                T.showShort(mContext, mMenuItems.get(position).mOperName);
                dialog.dismiss();
            }
        });
    }


    private void ActionSheetDialog() {
        final String[] stringItems = {"接收消息并提醒", "接收消息但不提醒", "收进群助手且不提醒", "屏蔽群消息"};
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, stringItems, null);
        dialog.title("选择群消息提醒方式\r\n(该群在电脑的设置:接收消息并提醒)")//
                .titleTextSize_SP(14.5f)//
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                T.showShort(mContext, stringItems[position]);
                dialog.dismiss();
            }
        });
    }

    private void ActionSheetDialogNoTitle() {
        final String[] stringItems = {"版本更新", "帮助与反馈", "退出QQ"};
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, stringItems, mElv);
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                T.showShort(mContext, stringItems[position]);
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.content("亲,真的要走吗?再看会儿吧~(●—●)")//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(23)//
                .btnText("继续逛逛", "残忍退出")//
                .btnTextColor(Color.parseColor("#383838"), Color.parseColor("#D4D4D4"))//
                .btnTextSize(16f, 16f)//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.superDismiss();
                        finish();
                    }
                });
        return true;
    }



}