package com.zhukic.sectionedrecyclerview.result;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public class NotifyItemInsertedResult extends NotifyResult {

    private final boolean isNewSection;

    public NotifyItemInsertedResult(int positionStart, int itemCount, int subheaderPosition, boolean isNewSection) {
        super(positionStart, itemCount, subheaderPosition);
        this.isNewSection = isNewSection;
    }

    public boolean isNewSection() {
        return isNewSection;
    }

}
