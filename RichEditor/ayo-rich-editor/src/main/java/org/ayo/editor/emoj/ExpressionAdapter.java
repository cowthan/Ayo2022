package org.ayo.editor.emoj;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import org.ayo.editor.R;
import org.ayo.editor.emoj.engine.EmojiUtils;
import org.ayo.editor.emoj.model.EmojiModel;

import java.util.List;

public class ExpressionAdapter extends ArrayAdapter<EmojiModel> {
    Context context;
    private List<EmojiModel> objects;

    private String mType;

    public ExpressionAdapter(Context context, int textViewResourceId,
                             List<EmojiModel> objects) {
        this(context,textViewResourceId,objects,ExpressionSelectView.EXPRESSION_TYPE_SMALL);
    }

    public ExpressionAdapter(Context context, int textViewResourceId,
                             List<EmojiModel> objects, String type) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
        this.context = context;
        this.mType=type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if(ExpressionSelectView.EXPRESSION_TYPE_SMALL.equals(mType)){
                convertView = View.inflate(getContext(), R.layout.row_expression,
                        null);
            }else if(ExpressionSelectView.EXPRESSION_TYPE_BIG.equals(mType)){
                convertView = View.inflate(getContext(), R.layout.row_expression_big,
                        null);
            }else {
                convertView = View.inflate(getContext(), R.layout.row_expression,
                        null);
            }
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_expression);
//        String filename = getItem(position).name;
//        String filename = getItem(position).name;
//        Log.d("ExpressionAdapter", filename);
//        int resId = getContext().getResources().getIdentifier(filename, "drawable", getContext().getPackageName());

        if("icon_gv_delete".equals(objects.get(position).alias)){
            imageView.setImageResource(R.drawable.icon_gv_delete);
        }else{
            String uri = objects.get(position).icon;
            Bitmap bm = EmojiUtils.createBitmapByLocalUri(uri);
            if(bm != null) imageView.setImageBitmap(bm);
        }
        return convertView;
    }
}
