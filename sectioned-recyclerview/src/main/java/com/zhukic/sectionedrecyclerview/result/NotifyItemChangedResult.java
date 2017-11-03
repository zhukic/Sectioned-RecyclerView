package com.zhukic.sectionedrecyclerview.result;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public class NotifyItemChangedResult extends NotifyResult {

    public NotifyItemChangedResult(int positionStart, int itemCount, int subheaderPosition) {
        super(positionStart, itemCount, subheaderPosition);
    }

}
