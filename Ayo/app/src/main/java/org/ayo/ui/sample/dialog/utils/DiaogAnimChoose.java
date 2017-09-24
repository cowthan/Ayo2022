package org.ayo.ui.sample.dialog.utils;

import android.view.View;
import android.widget.AdapterView;

import org.ayo.animate.yoyo.Techniques;
import org.ayo.notify.dialog.listener.OnOperItemClickL;
import org.ayo.notify.dialog.widget.ActionSheetDialog;
import org.ayo.ui.sample.BasePage;
import org.ayo.ui.sample.dialog.ui.DialogHomeActivity;

import java.util.ArrayList;

public class DiaogAnimChoose {
    public static void showAnim(final BasePage a) {
        final Techniques cs[] = {Techniques.BounceIn,//
                Techniques.Flash,//
                Techniques.Landing,
                Techniques.Linear,
                Techniques.Jelly,
                Techniques.NewsPaperIn,
                Techniques.Pulse,
                Techniques.RubberBand,
                Techniques.Shake,
                Techniques.ShakeVertical,
                Techniques.Swing,
                Techniques.Wobble,
                Techniques.Bounce,
                Techniques.Tada,
                Techniques.StandUp,
                Techniques.Wave,
                Techniques.FallIn,
                Techniques.FallRotateIn,
                Techniques.RollIn,
                Techniques.BounceIn,
                Techniques.BounceInDown,
                Techniques.BounceInLeft,
                Techniques.BounceInRight,
                Techniques.BounceInUp,
                Techniques.FadeIn,
                Techniques.FadeInUp,
                Techniques.FadeInDown,
                Techniques.FadeInLeft,
                Techniques.FadeInRight,
                Techniques.FlipInX,
                Techniques.FlipInY,
                Techniques.RotateIn,
                Techniques.RotateInDownLeft,
                Techniques.RotateInDownRight,
                Techniques.RotateInUpLeft,
                Techniques.RotateInUpRight,
                Techniques.SlideInLeft,
                Techniques.SlideInRight,
                Techniques.SlideInUp,
                Techniques.SlideInDown,
                Techniques.ZoomIn,
                Techniques.ZoomInDown,
                Techniques.ZoomInLeft,
                Techniques.ZoomInRight,
                Techniques.ZoomInUp,


        };

        ArrayList<String> itemList = new ArrayList<>();
        for (Techniques c : cs) {
            itemList.add(c.name());
        }

        final String[] contents = new String[itemList.size()];
        final ActionSheetDialog dialog = new ActionSheetDialog(a.getActivity(), //
                itemList.toArray(contents), null);
        dialog.title("使用内置show动画设置对话框显示动画\r\n指定对话框将显示效果")//
                .titleTextSize_SP(14.5f)//
                .layoutAnimation(null)//
                .cancelText("cancel")
                .show();
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String animType = contents[position];
                    ((DialogHomeActivity) a).setBasIn(cs[position].getAnimator());
                    T.showShort(a.getActivity(), animType + "设置成功");
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void dissmissAnim(final BasePage a) {
        final Techniques cs[] = {Techniques.Hinge,//
                Techniques.TakingOff,
                Techniques.RollOut,
                Techniques.FadeOut,
                Techniques.FadeOutDown,
                Techniques.FadeOutLeft,
                Techniques.FadeOutRight,
                Techniques.FadeOutUp,
                Techniques.FlipOutX,
                Techniques.RotateOut,
                Techniques.RotateOutDownLeft,
                Techniques.RotateOutDownRight,
                Techniques.RotateOutUpLeft,
                Techniques.RotateOutUpRight,
                Techniques.SlideOutLeft,
                Techniques.SlideOutRight,
                Techniques.SlideOutUp,
                Techniques.SlideOutDown,
                Techniques.ZoomOut,
                Techniques.ZoomOutDown,
                Techniques.ZoomOutLeft,
                Techniques.ZoomOutRight,
                Techniques.ZoomOutUp,

        };

        ArrayList<String> itemList = new ArrayList<String>();
        for (Techniques c : cs) {
            itemList.add(c.name());
        }

        final String[] contents = new String[itemList.size()];
        final ActionSheetDialog dialog = new ActionSheetDialog(a.getActivity(), //
                itemList.toArray(contents), null);
        dialog.title("使用内置dismiss动画设置对话框消失动画\r\n指定对话框将消失效果")//
                .titleTextSize_SP(14.5f)//
                .cancelText("cancel")
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String animType = contents[position];
                    ((DialogHomeActivity) a).setBasOut(cs[position].getAnimator());
                    T.showShort(a.getActivity(), animType + "设置成功");
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
