package org.ayo.robot.canvas.matrix;

/**
 * Created by Administrator on 2016/12/16.
 */

public class AA {
    /*

Canva可以看成图层的集合，我们可以对每一个图层进行单独处理，
这里所谓的处理，其实就是进行变换，draw的行为会受到变换的
影响，draw其实是对某一个图层上draw
而变换，其实就是Matrix

canvas.save系列和restore系列就是图层相关的操作
```
    /**
     * Saves the current matrix and clip onto a private stack.
     * <p>
     * Subsequent calls to translate,scale,rotate,skew,concat or clipRect,
     * clipPath will all operate as usual, but when the balancing call to
     * restore() is made, those calls will be forgotten, and the settings that
     * existed before the save() will be reinstated.
     *
     * @return The value to pass to restoreToCount() to balance this save()
     * /
    public int save() {
        return native_save(mNativeCanvasWrapper, MATRIX_SAVE_FLAG | CLIP_SAVE_FLAG);
    }

```
把Canvas看成一个容器，里面装的是一个一个的Bitmap

图层影响的是matrix和clip

save之后，发生的事：
形成新的图层，但还是在主Bitmap上操作，靠的是stack栈
主matrix和clip会被继承过来
在新图层上，可以自己translate,scale,rotate,skew,concat
也可以clipRect，clipPath
但restore之后，所有变换和裁剪会变回save之前的状态


int saveID1 = canvas.save(Canvas.CLIP_SAVE_FLAG);
int saveID2 = canvas.save(Canvas.MATRIX_SAVE_FLAG);
canvas.restoreToCount(saveID1);
一个restore，恢复了两次save

这时有个栈，结构如下：
saveID2
saveID1
default Stack ID
如果我们不做任何save，则我们是在default Stack ID下操作，
所做的draw，也受default对应的clip和matrix的影响

canvas.restoreToCount(saveID1);会将saveID2和saveID1都出栈

这里就分出了两种情况：
1 如果你总是save和restore一比一进行，则栈中始终都只有一个ID，你也不会奇怪当前draw到底受谁的影响
2 如果你save多次，后面慢慢restore，就很容易乱，不知道你当前对应的是哪个ID的matrix和clip


saveLayer之后，发生的事：
形成新的Bitmap，后面所有操作都是在这个新的Bitmap上进行的
这个新的Bitmap，有自己的一套matrix，clip，draw，
直到restore时，才会把绘制结果绘制到主Bitmap上
saveLayer的参数可以指定这个Bitmap的大小（Bitmap比较占资源，都是为了省）
官方说法就是，saveLayer产生了一个offscreen的Bitmap
此方法十分昂贵，不要随便用

```

saveLayer(float left, float top, float right, float bottom, @Nullable Paint paint,
        @Saveflags int saveFlags)
saveLayer(@Nullable RectF bounds, @Nullable Paint paint)
saveLayer(@Nullable RectF bounds, @Nullable Paint paint, @Saveflags int saveFlags)
saveLayer(@Nullable RectF bounds, @Nullable Paint paint, @Saveflags int saveFlags)

saveLayerAlpha(@Nullable RectF bounds, int alpha, @Saveflags int saveFlags)
saveLayerAlpha(@Nullable RectF bounds, int alpha)
saveLayerAlpha(float left, float top, float right, float bottom, int alpha,
        @Saveflags int saveFlags)
saveLayerAlpha(float left, float top, float right, float bottom, int alpha)

关于saveFlags：
ALL_SAVE_FLAG: restore everythingwhen restore() is called
CLIP_SAVE_FLAG：restore the current clip when restore() is called
CLIP_TOLAYER_SAVE_FLAG：clip against the layer's bound
FULL_COLOR_LAYER_SAVE_FLAG：the layer needs to 8 bits per color component
HAS_ALPHA_LAYER_SAVE_FLAG：the layer needs to per-pixel alpha
MATRIX_SAVE_FLAG：restore the current matrix when restore() is called

这里面涉及到android对色彩的处理，色彩深度和alpha的混合，我也不懂，一般我们用就用ALL_SAVE_FLAG就可以了

这六个常量值分别标识了我们在调用restore方法后还原什么，
六个标识位除了CLIP_SAVE_FLAG、MATRIX_SAVE_FLAG和ALL_SAVE_FLAG是save和saveLayerXXX方法都通用外
其余三个只能使saveLayerXXX方法有效，
ALL_SAVE_FLAG很简单也是我们新手级常用的标识保存所有，
CLIP_SAVE_FLAG和MATRIX_SAVE_FLAG也很好理解，一个是裁剪的标识位，一个是变换的标识位，
CLIP_TO_LAYER_SAVE_FLAG、FULL_COLOR_LAYER_SAVE_FLAG和HAS_ALPHA_LAYER_SAVE_FLAG只对saveLayer和saveLayerAlpha有效，
CLIP_TO_LAYER_SAVE_FLAG表示对当前图层执行裁剪操作需要对齐图层边界，
FULL_COLOR_LAYER_SAVE_FLAG表示当前图层的色彩模式至少需要是8位色，
HAS_ALPHA_LAYER_SAVE_FLAG表示在当前图层中将需要使用逐像素Alpha混合模式


translate
rotate
scale
skew
setMatrix()

```




     */
}
