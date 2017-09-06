$(document).ready(function () {
    $.ajax({
        url: "getIndustryTree.action",
        type: 'POST',
        dataType: 'json',
        success: function (data) {
            var jsonObj = eval('(' + data + ')');
//                    var jsonObj = eval(data);
            getIndustryTree(jsonObj);
        },
        error: function () {
            fail_prompt("ajax error", 1500)
        }
    })
});

function splitGraphDiv(rawData) {
    var nodes = [];
    var links = [];

    for (var i = 0; i < rawData.length; i++) {
        if (rawData[i]["id"] == null) {
            links.push({
                'source': rawData[i]["edge"]["industryA_id"],
                'target': rawData[i]["edge"]["industryB_id"],
                'name': rawData[i]["edge"]["weight"].toFixed(2)
            })

        } else {
            nodes.push({
                'id': i,
                'name': rawData[i]["id"]
            })
        }
    }

    return {
        links: links,
        nodes: nodes
    }
}

function getIndustryTree(rawData) {
    var myChart = echarts.init(document.getElementById("industryTreeDiv"));
    var data = splitGraphDiv(rawData);

    myChart.setOption(option = {
        title: {
            text: 'Les Miserables',
            subtext: 'Default layout',
            top: 'bottom',
            left: 'right'
        },

        tooltip: {},
        legend: [{
            // selectedMode: 'single',
            data: function () {
                return data.nodes.name;
            }
        }],

        animation: true,
        series: [
            {
                width: '300%',
                height: '300%',
                name: 'Les Miserables',
                type: 'graph',
                layout: 'force',
                data: data.nodes,
                links: data.links,
//                        categories: categories,
                roam: true,
                label: {
                    normal: {
                        position: 'right'
                    }
                },
                force: {
                    repulsion: 400,
                    edgeLength: 300
                },
                edgeLabel: {
                    normal: {
                        show: true,
                        formatter: function (x) {
                            return x.data.name
                        }
                    }
                }
            }
        ]
    });
}