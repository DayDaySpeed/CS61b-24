package deque;

import java.util.Comparator;

/**
 * MaxArrayDeque61B 是一个支持找最大元素的双端队列。
 * 它继承自 ArrayDeque61B，并添加了 max() 方法。
 *
 * @param <T> 队列中元素的类型，必须支持比较。
 */
public class MaxArrayDeque61B<T> extends ArrayDeque61B<T>{
    private Comparator<T> defaultComparator;

    /**
     * 构造函数，传入一个默认的比较器。
     * 这个比较器会用于 max() 方法。
     *
     * @param c 用于比较元素大小的 Comparator。
     */
    public MaxArrayDeque61B(Comparator<T> c) {
        super(); // 调用父类构造函数，初始化队列
        defaultComparator = c;
    }

    /**
     * 返回队列中最大的元素，使用默认比较器。
     *
     * @return 最大元素，如果队列为空则返回 null。
     */
    public T max() {
        return max(defaultComparator);
    }

    /**
     * 返回队列中最大的元素，使用传入的比较器。
     *
     * @param c 用于比较元素大小的 Comparator。
     * @return 最大元素，如果队列为空则返回 null。
     */
    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null; // 空队列没有最大值
        }

        T maxItem = get(0); // 假设第一个元素是最大值
        for (int i = 1; i < size(); i++) {
            T current = get(i);
            // 如果 current 比 maxItem 更大，则更新 maxItem
            if (c.compare(current, maxItem) > 0) {
                maxItem = current;
            }
        }
        return maxItem;
    }
}
