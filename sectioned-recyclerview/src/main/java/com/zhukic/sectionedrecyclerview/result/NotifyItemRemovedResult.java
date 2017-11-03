package com.zhukic.sectionedrecyclerview.result;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public class NotifyItemRemovedResult extends NotifyResult {

    private final boolean isSectionRemoved;

    public NotifyItemRemovedResult(int positionStart, int itemCount, int subheaderPosition, boolean isSectionRemoved) {
        super(positionStart, itemCount, subheaderPosition);
        this.isSectionRemoved = isSectionRemoved;
    }

    public boolean isSectionRemoved() {
        return isSectionRemoved;
    }

}
