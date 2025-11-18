package help;

import edu.princeton.cs.algs4.Digraph;

import java.util.*;
/** Breadth-First Search（广度优先搜索） 找到所有可达的下位词 ID */
public class GraphHelper {
    // 使用 BFS，Breadth-First Search（广度优先搜索） 找到所有可达的下位词 ID
    public static Set<Integer> descendants(Digraph g, Set<Integer> ids) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>(ids);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            visited.add(current);

            // 遍历当前节点的所有邻居（下位词）
            for (int neighbor : g.adj(current)) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }
        return visited;
    }
}
