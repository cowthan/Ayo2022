package org.ayo.robot.paint.text;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 */

public class DemoDrawTextView extends DemoBase {

    @Override
    protected BaseView createTestView() {
        return new DrawTextView(getActivity());
    }

    @Override
    protected void onViewClicked(int x, int y) {
        DrawTextView v = (DrawTextView) getTestView();
        v.enableDemoFrame(!v.isFrameEnabled());
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        DrawTextView v = (DrawTextView) getTestView();
        v.moveTextBy(dx, dy);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        DrawTextView v = (DrawTextView) getTestView();
        v.changeTextSizeBy(dx);
    }


}
