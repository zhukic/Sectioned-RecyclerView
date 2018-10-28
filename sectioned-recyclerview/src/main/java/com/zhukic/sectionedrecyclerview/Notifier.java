package com.zhukic.sectionedrecyclerview;

class Notifier {

    enum Type {
        ALL_DATA_CHANGED, CHANGED, INSERTED, REMOVED
    }

    private final Type type;

    private final int positionStart;

    private final int itemCount;

    private Notifier(Type type, int positionStart, int itemCount) {
        this.type = type;
        this.positionStart = positionStart;
        this.itemCount = itemCount;
    }

    private static Notifier create(Type type, int positionStart, int itemCount) {
        return new Notifier(type, positionStart, itemCount);
    }

    public static Notifier createAllDataChanged() {
        return create(Type.ALL_DATA_CHANGED, 0, 0);
    }

    public static Notifier createChanged(int positionStart, int itemCount) {
        return create(Type.CHANGED, positionStart, itemCount);
    }

    public static Notifier createChanged(int positionStart) {
        return createChanged(positionStart, 1);
    }

    public static Notifier createInserted(int positionStart, int itemCount) {
        return create(Type.INSERTED, positionStart, itemCount);
    }

    public static Notifier createInserted(int positionStart) {
        return createInserted(positionStart, 1);
    }

    public static Notifier createRemoved(int positionStart, int itemCount) {
        return create(Type.REMOVED, positionStart, itemCount);
    }

    public static Notifier createRemoved(int positionStart) {
        return createRemoved(positionStart, 1);
    }

    public Type getType() {
        return type;
    }

    public int getPositionStart() {
        return positionStart;
    }

    public int getItemCount() {
        return itemCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notifier notifier = (Notifier) o;

        if (positionStart != notifier.positionStart) return false;
        if (itemCount != notifier.itemCount) return false;
        return type == notifier.type;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + positionStart;
        result = 31 * result + itemCount;
        return result;
    }

    @Override
    public String toString() {
        return "Notifier{" +
                "type=" + type +
                ", positionStart=" + positionStart +
                ", itemCount=" + itemCount +
                '}';
    }

}
