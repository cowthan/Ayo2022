package zhou.demo.markdown;

import org.ayo.lang.Strings;

import zhou.demo.R;

public class TestMarkdownActivity7 extends BaseMarkdownPage {

    @Override
    protected String getText() {
        return Strings.fromStream(getResources().openRawResource(R.raw.tt));
    }
}
