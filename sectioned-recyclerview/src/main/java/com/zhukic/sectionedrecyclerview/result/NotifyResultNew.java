package com.zhukic.sectionedrecyclerview.result;

import com.zhukic.sectionedrecyclerview.Notifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public class NotifyResultNew {

    //TODO надо LinkedHashSet
    private final Set<Notifier> notifiers;

    public static NotifyResultNew create(List<Notifier> notifiers) {
        return new NotifyResultNew(new LinkedHashSet<>(notifiers));
    }

    public static NotifyResultNew create(Notifier notifier) {
        return create(Collections.singletonList(notifier));
    }

    public static NotifyResultNew empty() {
        return create(Collections.<Notifier>emptyList());
    }

    private NotifyResultNew(LinkedHashSet<Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    public void addNotifier(Notifier notifier) {
        notifiers.add(notifier);
    }

    //TODO
    public int getPositionStart() {
        return 0;
    }

    public Set<Notifier> getNotifiers() {
        return notifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotifyResultNew that = (NotifyResultNew) o;

        return notifiers.equals(that.notifiers);
    }

    @Override
    public int hashCode() {
        return notifiers.hashCode();
    }

    @Override
    public String toString() {
        return "NotifyResultNew{" +
                "notifiers=" + notifiers +
                '}';
    }

}
