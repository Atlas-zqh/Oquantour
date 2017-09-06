package oquantour.service.servicehelper.analysis.industry;

import oquantour.po.util.Edge;

import java.util.*;

/**
 * 得到加权图的最小生成树:Kruskal
 * <p>
 * Created by keenan on 02/06/2017.
 */
public class MinimumSpanningTree {
    private String[] mVexs;// 顶点集合
    private double[][] mMatrix;// 权重矩阵
    private int length;

    protected MinimumSpanningTree(String[] mVexs, double[][] mMatrix) {
        this.mMatrix = mMatrix;
        this.mVexs = mVexs;
        length = mVexs.length;
    }

    /**
     * 得到生成树结果
     *
     * @return 结果
     */
    protected PriorityQueue<Edge> getSpanResult() {
        double[][] tree = findMiniSpanningTree();
        if (tree == null) {
            return new PriorityQueue<>();
        }
        PriorityQueue<Edge> edges = getPriorityQueue(tree);
        return edges;
    }

    /**
     * 寻找最小生成树
     * 给定一个带权值的邻接矩阵（其中若i=j，权值为0，若i和j不可达权值为Double.max），返回一个最小生成树，
     *
     * @return 经过处理的数据矩阵
     */
    private double[][] findMiniSpanningTree() {
        PriorityQueue<Edge> edges = getPriorityQueue(mMatrix);

        double[][] tree = new double[length][length];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (i == j) {
                    tree[i][j] = 0;
                } else {
                    tree[i][j] = Double.MAX_VALUE;
                }
            }
        }

        /**
         * map用于标识边的某个顶点属于哪个集合，认为顶点刚开始属于不同的集合，
         * 当选择一条边时，就合并两个集合，如果选择的边在同一个集合内，就代表有环路出现了
         */
        Map<Integer, Set<Integer>> map = new HashMap<>();
        int cnt = 0;
        while (cnt < length - 1 && !edges.isEmpty()) {
            Edge e = edges.poll();

            Set<Integer> setA = map.get(e.getIndustryA_id());
            Set<Integer> setB = map.get(e.getIndustryB_id());

            // 边的两个顶点都没有出现在集合中
            if (setA == null && setB == null) {
                Set<Integer> set = new HashSet<>();
                set.add(e.getIndustryA_id());
                set.add(e.getIndustryB_id());
                map.put(e.getIndustryA_id(), set);
                map.put(e.getIndustryB_id(), set);
            } else if (setA == null && setB != null) {// 有一个顶点出现在集合中，一个不在，将不在的那个合并
                map.put(e.getIndustryA_id(), setB);
                setB.add(e.getIndustryA_id());
            } else if (setA != null && setB == null) {
                map.put(e.getIndustryB_id(), setA);
                setA.add(e.getIndustryB_id());
            } else if (setA != setB) {// 在两个不同的集合中，两个集合合并
                for (int b : setB) {
                    map.put(b, setA);
                }
                setA.addAll(setB);
            } else {// 两个顶点在同一个集合中，出现环，舍弃
                continue;
            }

            tree[e.getIndustryA_id()][e.getIndustryB_id()] = e.getWeight();
            tree[e.getIndustryB_id()][e.getIndustryA_id()] = e.getWeight();
            cnt++;
        }

        if (cnt == length - 1) {
            return tree;
        } else {
            return null;
        }

    }

    /**
     * 对有效数据构造优先队列
     *
     * @param data 数据
     * @return 优先队列
     */
    private PriorityQueue<Edge> getPriorityQueue(double[][] data) {
        PriorityQueue<Edge> edges = new PriorityQueue<>();
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                if (data[i][j] >= 0 && data[i][j] < Double.MAX_VALUE) {
                    Edge e = new Edge(i, j, data[i][j], mVexs[i], mVexs[j]);
                    edges.add(e);
                }
            }
        }

        return edges;
    }

}
