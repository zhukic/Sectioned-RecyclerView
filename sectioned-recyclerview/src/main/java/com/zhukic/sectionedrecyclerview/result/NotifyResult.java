package com.zhukic.sectionedrecyclerview.result;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public class NotifyResult {

    private final int positionStart;

    private final int itemCount;

    private final int subheaderPosition;

    public NotifyResult(int positionStart, int itemCount, int subheaderPosition) {
        this.positionStart = positionStart;
        this.itemCount = itemCount;
        this.subheaderPosition = subheaderPosition;
    }

    public int getPositionStart() {
        return positionStart;
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getSubheaderPosition() {
        return subheaderPosition;
    }

}
