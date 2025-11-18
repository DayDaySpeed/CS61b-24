package deque;

import java.util.Iterator;

/** 实现一个基于数组的双端队列，支持在队首和队尾添加/删除元素 */
public class ArrayDeque61B<T> implements Deque61B<T> {
    /** 存储元素的数组 */
    private T[] items;
    /** 当前队列中元素的数量 */
    private int size;
    /** 下一个 addFirst 插入的位置 */
    private int nextFirst;
    /** 下一个 addLast 插入的位置 */
    private int nextLast;

    /** 构造函数：初始化数组容量为8，设置初始指针位置 */
    public ArrayDeque61B() {
        items = (T[]) new Object[8]; // Java 不允许直接创建泛型数组
        size = 0;
        nextFirst = 7; // 初始时从数组末尾向前插入
        nextLast = 0;  // 初始时从数组开头向后插入
    }

    /** 辅助方法：向前移动一个位置（循环数组）*/
    private int minusOne(int index) {
        return (index - 1 + items.length) % items.length;
    }

    /** 辅助方法：向后移动一个位置（循环数组）*/
    private int plusOne(int index) {
        return (index + 1) % items.length;
    }

    /** 扩容或缩容数组，并重新排列元素 */
    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        int start = plusOne(nextFirst); // 原队列的第一个元素位置
        for (int i = 0; i < size; i++) {
            // 将旧数组中的元素按顺序复制到新数组
            newItems[i] = items[(start + i) % items.length];
        }
        items = newItems;
        nextFirst = capacity - 1; // 重置指针
        nextLast = size;
    }

    /** 在队首添加元素 */
    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(items.length * 2); // 扩容
        }
        items[nextFirst] = item; // 插入元素
        nextFirst = minusOne(nextFirst); // 更新指针
        size++;
    }

    /** 在队尾添加元素 */
    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(items.length * 2); // 扩容
        }
        items[nextLast] = item; // 插入元素
        nextLast = plusOne(nextLast); // 更新指针
        size++;
    }

    /** 从队首删除元素并返回 */
    @Override
    public T removeFirst() {
        if (isEmpty()) return null; // 空队列返回 null
        int firstIndex = plusOne(nextFirst); // 找到第一个元素位置
        T item = items[firstIndex];
        items[firstIndex] = null; // 清除引用，帮助垃圾回收
        nextFirst = firstIndex; // 更新指针
        size--;
        // 缩容条件：数组长度 ≥ 16 且元素数量小于 25%
        if (items.length >= 16 && size < items.length / 4) {
            resize(items.length / 2);
        }
        return item;
    }

    /** 从队尾删除元素并返回 */
    @Override
    public T removeLast() {
        if (isEmpty()) return null;
        int lastIndex = minusOne(nextLast); // 找到最后一个元素位置
        T item = items[lastIndex];
        items[lastIndex] = null;
        nextLast = lastIndex;
        size--;
        if (items.length >= 16 && size < items.length / 4) {
            resize(items.length / 2);
        }
        return item;
    }

    /** 获取指定索引的元素（从队首开始计数）*/
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) return null;
        int actualIndex = (plusOne(nextFirst) + index) % items.length;
        return items[actualIndex];
    }

    /** 不需要实现递归获取，直接抛出异常 */
    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }

    /** 判断队列是否为空 */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** 返回队列中元素数量 */
    @Override
    public int size() {
        return size;
    }

    /** 将队列转换为 数组（用于测试或调试）*/
    @Override
    public T[] toList() {
        for (T i : items) {
            System.out.print("[" + i + "]" + " ");
        }
        return items;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int pos = 0;

            public boolean hasNext() {
                return pos < size();
            }

            public T next() {
                T item = get(pos);
                pos++;
                return item;
            }
        };
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deque61B)) return false;

        Deque61B<?> other = (Deque61B<?>) o;
        if (this.size() != other.size()) return false;

        for (int i = 0; i < size(); i++) {
            if (!this.get(i).equals(other.get(i))) return false;
        }
        return true;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size(); i++) {
            sb.append(get(i));
            if (i < size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}