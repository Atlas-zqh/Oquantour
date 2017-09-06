package oquantour.service.servicehelper.analysis.industry;

import oquantour.po.util.Edge;
import oquantour.util.tools.Calculator;

import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by keenan on 13/06/2017.
 */
public class IndustryRelativity {
    private List<String> industries;

    private List<List<Double>> returnRates;

    public IndustryRelativity(List<String> industries, List<List<Double>> returnRates) {
        this.industries = industries;
        this.returnRates = returnRates;
    }

    /**
     * 获得行业相关度最强相关关系
     *
     * @return
     */
    public PriorityQueue<Edge> getRelativity() {
        String[] ind = industries.toArray(new String[1]);
        double[][] relatives = new double[industries.size()][industries.size()];
        for (int i = 0; i < industries.size(); i++) {
            for (int j = i + 1; j < industries.size(); j++) {
                double relative = Calculator.relative(returnRates.get(i), returnRates.get(j));
                double weight = Math.sqrt(2 * (1 - relative));
                relatives[i][j] = weight;
                relatives[j][i] = weight;
            }
        }
        MinimumSpanningTree mst = new MinimumSpanningTree(ind, relatives);
        PriorityQueue<Edge> edges = mst.getSpanResult();
        return edges;
    }
}
