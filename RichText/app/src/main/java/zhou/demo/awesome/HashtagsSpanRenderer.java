/*
 * Copyright (C) 2015 José Manuel Pereira García.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zhou.demo.awesome;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.ayo.richtext.awesometext.AwesomeTextHandler;

import zhou.demo.R;

public class HashtagsSpanRenderer implements AwesomeTextHandler.ViewSpanRenderer {

    private final static int textSizeInDips = 18;
    private final static int backgroundResource = R.drawable.common_hashtags_background;
    private final static int textColorResource = android.R.color.white;

    @Override
    public View getView(String text, Context context) {
        TextView view = new TextView(context);
        view.setText(text.substring(1));
        view.setTextSize(ScreenUtils.dipsToPixels(context, textSizeInDips));
        view.setBackgroundResource(backgroundResource);
        int textColor = context.getResources().getColor(textColorResource);
        view.setTextColor(textColor);
        return view;
    }
}
