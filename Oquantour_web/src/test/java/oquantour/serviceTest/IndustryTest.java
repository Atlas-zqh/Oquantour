package oquantour.serviceTest;

import oquantour.BaseTest;
import oquantour.po.StockRealTimePO;
import oquantour.po.util.Edge;
import oquantour.po.util.RelationGraph;
import oquantour.service.IndustryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by keenan on 09/06/2017.
 */
public class IndustryTest extends BaseTest {
    @Autowired
    IndustryService industryService;

    @Test
    public void testRelationGraph() {
        RelationGraph relationGraph = industryService.getIndustryTree();
        PriorityQueue<Edge> edges = relationGraph.getEdges();

        for (Edge edge : edges) {
            System.out.println(edge.getIndustryA_Name() + "  -  " + edge.getIndustryB_Name() + " : " + edge.getWeight());
        }
    }

    @Test
    public void testGetIndustry() {
        String name = "机床制造";

        Map<String, StockRealTimePO> map = industryService.getIndustryStocks(name);

        System.out.println(map.size());
    }
}
