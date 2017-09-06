package oquantour.po.util;

import oquantour.po.util.Edge;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by keenan on 06/06/2017.
 */
public class RelationGraph {
    /**
     * 边
     */
    private PriorityQueue<Edge> edges;
    /**
     * industries.get(industry_id)=industryName;
     */
    private List<String> industries;
    /**
     * 行业收益率
     */
    private Map<String, List<ChartInfo>> industryReturnRate;

    public RelationGraph(PriorityQueue<Edge> edges, List<String> industries, Map<String, List<ChartInfo>> industryReturnRate) {
        this.edges = edges;
        this.industries = industries;
        this.industryReturnRate = industryReturnRate;
    }

    public PriorityQueue<Edge> getEdges() {
        return edges;
    }

    public void setEdges(PriorityQueue<Edge> edges) {
        this.edges = edges;
    }

    public List<String> getIndustries() {
        return industries;
    }

    public void setIndustries(List<String> industries) {
        this.industries = industries;
    }

    public Map<String, List<ChartInfo>> getIndustryReturnRate() {
        return industryReturnRate;
    }

    public void setIndustryReturnRate(Map<String, List<ChartInfo>> industryReturnRate) {
        this.industryReturnRate = industryReturnRate;
    }
}
