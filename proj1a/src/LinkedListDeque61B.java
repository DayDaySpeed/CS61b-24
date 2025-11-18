import java.util.Iterator;

public class LinkedListDeque61B<T> implements Deque61B<T> {
    //构建内部节点类
    private class Node {
        T item;
        Node prev;
        Node next;
        //初始化
        Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }
    private Node sentinel;
    private int size;
    //初始化哨兵节点
    public LinkedListDeque61B() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }
    //假设原列表
    //sentinel <-> A <-> B <-> sentinel
    /** 在原列表的基础上向前添加*/
    @Override
    public void addFirst(T item) {
        Node newNode = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = newNode;//把新节点的引用告知哨兵节点
        sentinel.next = newNode;//向后添加节点
        size++;
        //sentinel <-> item <-> A <-> B <-> sentinel
    }

    /** 在原列表的基础上向后添加*/
    @Override
    public void addLast(T item) {
        Node newNode = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size++;
        //sentinel <-> A <-> B <-> item <-> sentinel
    }

/** 将对象以数组方式存储*/
    @Override
    public T[] toList() {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[size];
        Node current = sentinel.next;
        for (int i = 0; i < size; i++) {
            result[i] = current.item;
            current = current.next;
        }

        // 打印数组内容
        pritntDeque(result);

        return result;
    }

    /** 打印数组内容*/
    void pritntDeque(T[] result){
        System.out.print("Deque contents: ");
        for (T i: result) {
            System.out.print(i + " ");
        }
        System.out.println(); // 换行
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public int size() {
        return size;
    }

    //往后删
    @Override
    public T removeFirst() {
        if (isEmpty()) return null;//判断列表是否为空
        Node first = sentinel.next;//将second（哨兵节点的向后引用）赋值为first
        sentinel.next = first.next;//将second赋值为哨兵节点的向后引用
        first.next.prev = sentinel;//将当前哨兵节点赋值为second的向前引用
        size--;
        return first.item;
    }
    //往前删
    @Override
    public T removeLast() {
        if(isEmpty()) return null;
        Node last = sentinel.prev;
        sentinel.prev = last.prev;
        last.prev.next = sentinel;
        size--;
        return last.item;
    }

    @Override
    public T get(int index) {
        if(index < 0 || index >= size) return null;
        Node current = sentinel.next;
        for(int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.item;
    }

    @Override
    public T getRecursive(int index) {
        if (index < 0 || index >= size) return null;
        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(Node node, int index) {
        if (index == 0) return node.item;
        return getRecursiveHelper(node.next, index - 1);
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
