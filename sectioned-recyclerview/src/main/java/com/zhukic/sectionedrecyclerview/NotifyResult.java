package com.zhukic.sectionedrecyclerview;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class NotifyResult {

    private final LinkedHashSet<Notifier> notifiers;

    public static NotifyResult create(List<Notifier> notifiers) {
        return new NotifyResult(new LinkedHashSet<>(notifiers));
    }

    public static NotifyResult create(Notifier notifier) {
        return create(Collections.singletonList(notifier));
    }

    public static NotifyResult empty() {
        return create(Collections.<Notifier>emptyList());
    }

    private NotifyResult(LinkedHashSet<Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    //TODO выпилить
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

        NotifyResult that = (NotifyResult) o;

        return notifiers.equals(that.notifiers);
    }

    @Override
    public int hashCode() {
        return notifiers.hashCode();
    }

    @Override
    public String toString() {
        return "NotifyResult{" +
                "notifiers=" + notifiers +
                '}';
    }

}
