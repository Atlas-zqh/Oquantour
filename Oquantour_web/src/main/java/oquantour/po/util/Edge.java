package oquantour.po.util;

/**
 * Created by keenan on 03/06/2017.
 */
public class Edge implements Comparable<Edge> {
    private int industryA_id;

    private int industryB_id;

    private double weight;

    private String industryA_Name;

    private String industryB_Name;

    public Edge(int industryA, int industryB, double weight, String industryA_Name, String industryB_Name) {
        super();
        this.industryA_id = industryA;
        this.industryB_id = industryB;
        this.weight = weight;
        this.industryA_Name = industryA_Name;
        this.industryB_Name = industryB_Name;
    }

    @Override
    public int compareTo(Edge o) {
        if (o.weight == weight) {
            return 0;
        } else if (weight < o.weight) {
            return -1;
        } else {
            return 1;
        }
    }

    public int getIndustryA_id() {
        return industryA_id;
    }

    public int getIndustryB_id() {
        return industryB_id;
    }

    public double getWeight() {
        return weight;
    }

    public String getIndustryA_Name() {
        return industryA_Name;
    }

    public String getIndustryB_Name() {
        return industryB_Name;
    }
}

