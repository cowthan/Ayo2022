package org.ayo.robot;

import android.os.Bundle;

import org.ayo.robot.canvas.clip.DemoClipPath;
import org.ayo.robot.canvas.clip.DemoClipRect;
import org.ayo.robot.canvas.clip.DemoClipRegionOp;
import org.ayo.robot.canvas.matrix.DemoMatrix;
import org.ayo.robot.canvas.matrix.DemoRotate;
import org.ayo.robot.canvas.layer.DemoSave;
import org.ayo.robot.canvas.matrix.DemoScale;
import org.ayo.robot.canvas.matrix.DemoSkew;
import org.ayo.robot.canvas.matrix.DemoTranslate;
import org.ayo.robot.canvas.path.DemoDrawPathEffect;
import org.ayo.robot.canvas.path.DemoDrawPath_arc;
import org.ayo.robot.canvas.path.DemoDrawPath_bezier2;
import org.ayo.robot.canvas.path.DemoDrawPath_bezier3;
import org.ayo.robot.canvas.path.DemoDrawPath_line;
import org.ayo.robot.canvas.shape.DemoDrawARGB;
import org.ayo.robot.canvas.shape.DemoDrawArc;
import org.ayo.robot.canvas.shape.DemoDrawBitmap;
import org.ayo.robot.canvas.shape.DemoDrawBitmapMesh;
import org.ayo.robot.canvas.shape.DemoDrawCircile;
import org.ayo.robot.canvas.shape.DemoDrawColor;
import org.ayo.robot.canvas.shape.DemoDrawLine;
import org.ayo.robot.canvas.shape.DemoDrawOval;
import org.ayo.robot.canvas.shape.DemoDrawPaint;
import org.ayo.robot.canvas.shape.DemoDrawPicture;
import org.ayo.robot.canvas.shape.DemoDrawPoint;
import org.ayo.robot.canvas.shape.DemoDrawRect;
import org.ayo.robot.canvas.shape.DemoDrawRoundRect;
import org.ayo.robot.canvas.shape.DemoDrawVertical;
import org.ayo.robot.paint.colormatrix.DemoColorMatrixColorFilter;
import org.ayo.robot.paint.colormatrix.DemoColorMatrixColorFilter2;
import org.ayo.robot.paint.colormatrix.DemoLightingColorFilter;
import org.ayo.robot.paint.colormatrix.DemoLightingColorFilter2;
import org.ayo.robot.paint.colormatrix.DemoPorterDuffColorFilter2;
import org.ayo.robot.paint.maskfilter.DemoBlurMaskFilter;
import org.ayo.robot.paint.maskfilter.DemoBlurMaskFilter2;
import org.ayo.robot.paint.maskfilter.DemoBlurMaskFilter3;
import org.ayo.robot.paint.shader.DemoBitmapShader;
import org.ayo.robot.paint.shader.DemoBitmapShader2;
import org.ayo.robot.paint.shader.DemoLinearGradient;
import org.ayo.robot.paint.shader.DemoLinearGradient2;
import org.ayo.robot.paint.shader.DemoLinearGradient3;
import org.ayo.robot.paint.shader.DemoRadialGradient;
import org.ayo.robot.paint.shader.DemoRadialGradient2;
import org.ayo.robot.paint.shader.DemoRadialGradient3;
import org.ayo.robot.paint.shader.DemoSweepGradient;
import org.ayo.robot.paint.shader.DemoSweepGradient2;
import org.ayo.robot.paint.text.DemoDrawTextView;
import org.ayo.robot.paint.xfermode.DemoPorterDuffXfermode;
import org.ayo.robot.paint.xfermode.DemoPorterDuffXfermode2;
import org.ayo.robot.paint.xfermode.DemoPorterDuffXfermode3;
import org.ayo.robot.paint.xfermode.DemoPorterDuffXfermode4;
import org.ayo.sample.menu.Leaf;
import org.ayo.sample.menu.MainPagerActivity;
import org.ayo.sample.menu.Menu;
import org.ayo.sample.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MainPagerActivity {

    private static List<Menu> menus;

    @Override
    public List<Menu> getMenus() {
        return menus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);

    }

    private void init(){
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    static{
        menus = new ArrayList<>();

        ///--------------------------菜单1：View
        Menu m = new Menu("Canvas", R.drawable.weixin_normal, R.drawable.weixin_pressed);
        menus.add(m);
        {
            MenuItem menuItem = new MenuItem("图形", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("draw color", "", DemoDrawColor.class));
                menuItem.addLeaf(new Leaf("draw argb", "", DemoDrawARGB.class));
                menuItem.addLeaf(new Leaf("draw paint", "", DemoDrawPaint.class));
                menuItem.addLeaf(new Leaf("draw Point", "", DemoDrawPoint.class));
                menuItem.addLeaf(new Leaf("draw Line", "", DemoDrawLine.class));
                menuItem.addLeaf(new Leaf("draw Rect：矩形，可FILL", "", DemoDrawRect.class));
                menuItem.addLeaf(new Leaf("draw RoundRect：圆角矩形，可FILL", "", DemoDrawRoundRect.class));
                menuItem.addLeaf(new Leaf("draw circle：圆，可FILL", "", DemoDrawCircile.class));
                menuItem.addLeaf(new Leaf("draw Oval：椭圆，可FILL", "", DemoDrawOval.class));
                menuItem.addLeaf(new Leaf("draw Arc：扇形或圆弧线：可FILL", "", DemoDrawArc.class));
                menuItem.addLeaf(new Leaf("draw Bitmap", "", DemoDrawBitmap.class));
                menuItem.addLeaf(new Leaf("draw Picture", "", DemoDrawPicture.class));
                menuItem.addLeaf(new Leaf("draw Vertical", "", DemoDrawVertical.class));
            }

            menuItem = new MenuItem("Path", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("lineTo：直线相连", "", DemoDrawPath_line.class));
                menuItem.addLeaf(new Leaf("arcTo：弧线（或直线+弧线）", "", DemoDrawPath_arc.class));
                menuItem.addLeaf(new Leaf("quadTo：二阶贝塞尔曲线", "", DemoDrawPath_bezier2.class));
                menuItem.addLeaf(new Leaf("cubicTo：三阶贝塞尔曲线", "", DemoDrawPath_bezier3.class));
                menuItem.addLeaf(new Leaf("add系列方法：添加非连续路径", "", null));
                menuItem.addLeaf(new Leaf("PathEffect", "", DemoDrawPathEffect.class));
            }

            menuItem = new MenuItem("BitmapMesh", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("draw BitmapMesh：网格变换", "", DemoDrawBitmapMesh.class));
            }

            menuItem = new MenuItem("图层", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("save和restore", "", DemoSave.class));
                menuItem.addLeaf(new Leaf("saveLayer", "", null));
            }


            menuItem = new MenuItem("Clip", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("clip rect", "", DemoClipRect.class));
                menuItem.addLeaf(new Leaf("clip path", "", DemoClipPath.class));
                menuItem.addLeaf(new Leaf("clip region：已被废弃，因为不支持matrix", "", null));
                menuItem.addLeaf(new Leaf("clip region op：两个剪切区域叠加的不同效果", "", DemoClipRegionOp.class));
            }

            menuItem = new MenuItem("变换", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("旋转", "", DemoRotate.class));
                menuItem.addLeaf(new Leaf("平移", "", DemoTranslate.class));
                menuItem.addLeaf(new Leaf("缩放", "", DemoScale.class));
                menuItem.addLeaf(new Leaf("错切", "", DemoSkew.class));
                menuItem.addLeaf(new Leaf("自己配置Matrix", "", DemoMatrix.class));
                menuItem.addLeaf(new Leaf("Matrix入门：给Canvas，给Shader，给Camera", "", null));
                menuItem.addLeaf(new Leaf("Matrix入门：ImageView缩放，旋转", "", null));
                menuItem.addLeaf(new Leaf("Matrix入门：Camera控制透视变换（Z轴）", "", null));
            }
        }

        ///--------------------------菜单1：开源
        m = new Menu("Paint", R.drawable.find_normal, R.drawable.find_pressed);
        menus.add(m);
        {
            MenuItem menuItem = new MenuItem("ColorFilter", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("ColorMatrixColorFilter:色彩矩阵颜色过滤器", "", DemoColorMatrixColorFilter.class));
                menuItem.addLeaf(new Leaf("ColorMatrixColorFilter:处理Bitmap", "", DemoColorMatrixColorFilter2.class));
                menuItem.addLeaf(new Leaf("LightingColorFilter:光照颜色过滤器", "", DemoLightingColorFilter.class));
                menuItem.addLeaf(new Leaf("LightingColorFilter:处理Bitmap", "", DemoLightingColorFilter2.class));
                menuItem.addLeaf(new Leaf("PorterDuffColorFilter：指定一个颜色，和draw的东西混合", "", DemoPorterDuffColorFilter2.class));
            }

            menuItem = new MenuItem("Xfermode", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("AvoidXfermode：已废弃", "", null));
                menuItem.addLeaf(new Leaf("PixelXfermode：已废弃", "", null));
                menuItem.addLeaf(new Leaf("PorterDuffXfermode：图形混合", "", DemoPorterDuffXfermode.class));
                menuItem.addLeaf(new Leaf("PorterDuffXfermode：聚焦美女", "", DemoPorterDuffXfermode2.class));
                menuItem.addLeaf(new Leaf("PorterDuffXfermode：聚焦美女2", "", DemoPorterDuffXfermode3.class));
                menuItem.addLeaf(new Leaf("PorterDuffXfermode：画画板，橡皮擦", "", DemoPorterDuffXfermode4.class));
            }

            menuItem = new MenuItem("Shader", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("BitmapShader：着色器", "", DemoBitmapShader.class));
                menuItem.addLeaf(new Leaf("BitmapShader：聚焦", "", DemoBitmapShader2.class));
                menuItem.addLeaf(new Leaf("BitmapShader：Matrix变换", "", null));
                menuItem.addLeaf(new Leaf("LinearGradient：线性渐变---单色", "", DemoLinearGradient.class));
                menuItem.addLeaf(new Leaf("LinearGradient：线性渐变---多色", "", DemoLinearGradient2.class));
                menuItem.addLeaf(new Leaf("LinearGradient：遮罩", "", DemoLinearGradient3.class));
                menuItem.addLeaf(new Leaf("LinearGradient：遮罩 + 倒影", "", null));
                menuItem.addLeaf(new Leaf("RadialGradient：辐射渐变--单色", "", DemoRadialGradient.class));
                menuItem.addLeaf(new Leaf("RadialGradient：多色", "", DemoRadialGradient2.class));
                menuItem.addLeaf(new Leaf("RadialGradient：遮罩--图片突出重点", "", DemoRadialGradient3.class));
                menuItem.addLeaf(new Leaf("SweepGradient：扫描渐变--单色", "", DemoSweepGradient.class));
                menuItem.addLeaf(new Leaf("SweepGradient：扫描渐变--多色", "", DemoSweepGradient2.class));
                menuItem.addLeaf(new Leaf("ComposeShader：组合着色器", "", null));
                menuItem.addLeaf(new Leaf("ComposeShader：组合着色器", "", null));
                menuItem.addLeaf(new Leaf("ComposeShader：让径向渐变支持任意矩形", "", null));
            }

            menuItem = new MenuItem("MaskFilter", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("BlurMaskFilter：阴影效果，必须关闭硬件加速", "", DemoBlurMaskFilter.class));
                menuItem.addLeaf(new Leaf("BlurMaskFilter：给Bitmap设置阴影，有点麻烦", "", DemoBlurMaskFilter2.class));
                menuItem.addLeaf(new Leaf("EmbossMaskFilter：浮雕，光照", "", DemoBlurMaskFilter3.class));
            }


            menuItem = new MenuItem("字体", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("draw text：认识baseline等参数", "", DemoDrawTextView.class));
                menuItem.addLeaf(new Leaf("draw text：实践--让文本居中", "", null));
                menuItem.addLeaf(new Leaf("处理文字换行：StaticLayout", "", null));
                menuItem.addLeaf(new Leaf("draw on path", "", null));
                menuItem.addLeaf(new Leaf("paint的stroke能使字体空心", "", null));
                menuItem.addLeaf(new Leaf("删除线", "", null));
            }
        }

        ///--------------------------菜单1：开源
//        m3 = new Menu("其他", R.drawable.find_normal, R.drawable.find_pressed);
//        menus.add(m3);
//        {
//            MenuItem menuItem = new MenuItem("Drawable", R.drawable.weixin_normal, R.drawable.weixin_pressed);
//            m3.addMenuItem(menuItem);
//            {
//                menuItem.addLeaf(new Leaf("自定义Drawable", "", null));
//            }
//
//            menuItem = new MenuItem("SurfaceView", R.drawable.weixin_normal, R.drawable.weixin_pressed);
//            m3.addMenuItem(menuItem);
//            {
//                menuItem.addLeaf(new Leaf("用法", "", null));
//            }
//
//            menuItem = new MenuItem("TextureView", R.drawable.weixin_normal, R.drawable.weixin_pressed);
//            m3.addMenuItem(menuItem);
//            {
//                menuItem.addLeaf(new Leaf("用法", "", null));
//            }
//
//        }

//        ///--------------------------菜单1：开源
//        m3 = new Menu("Bitmap", R.drawable.find_normal, R.drawable.find_pressed);
//        menus.add(m3);
//        {
//            MenuItem menuItem = new MenuItem("Bitmap", R.drawable.weixin_normal, R.drawable.weixin_pressed);
//            m3.addMenuItem(menuItem);
//            {
//                menuItem.addLeaf(new Leaf("解析", "", null));
//                menuItem.addLeaf(new Leaf("压缩", "", null));
//                menuItem.addLeaf(new Leaf("手势缩放", "", null));
//                menuItem.addLeaf(new Leaf("剪切", "", null));
//                menuItem.addLeaf(new Leaf("合并", "", null));
//            }
//        }
//
//        ///--------------------------菜单1：开源
//        m3 = new Menu("多媒体", R.drawable.find_normal, R.drawable.find_pressed);
//        menus.add(m3);
//        {
//            MenuItem menuItem = new MenuItem("照相", R.drawable.weixin_normal, R.drawable.weixin_pressed);
//            m3.addMenuItem(menuItem);
//            {
//                menuItem.addLeaf(new Leaf("照相", "", null));
//                menuItem.addLeaf(new Leaf("滤镜", "", null));
//            }
//
//            menuItem = new MenuItem("视频", R.drawable.weixin_normal, R.drawable.weixin_pressed);
//            m3.addMenuItem(menuItem);
//            {
//                menuItem.addLeaf(new Leaf("录视频", "", null));
//                menuItem.addLeaf(new Leaf("视频滤镜", "", null));
//                menuItem.addLeaf(new Leaf("视频编码", "", null));
//                menuItem.addLeaf(new Leaf("流媒体", "", null));
//            }
//
//            menuItem = new MenuItem("音频", R.drawable.weixin_normal, R.drawable.weixin_pressed);
//            m3.addMenuItem(menuItem);
//            {
//                menuItem.addLeaf(new Leaf("录音", "", null));
//                menuItem.addLeaf(new Leaf("编解码", "", null));
//                menuItem.addLeaf(new Leaf("音乐播放器", "", null));
//            }
//        }

    }
}
