<%--
  Created by IntelliJ IDEA.
  User: st
  Date: 2017/6/12
  Time: 11:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navBar.jsp" %>
<html>
<head>
    <title>指标说明</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/verticalMenu.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/indexInfo.css">
    <script src="${pageContext.request.contextPath}/js/semantic.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/verticalMenu.js"></script>
    <script src="${pageContext.request.contextPath}/js/smoothscroll.js"></script>

    <script>
        function scrollIntoView(id) {
            console.log(id);
            $("#" + id)[0].scrollIntoView({
                behavior: "smooth"
            })
        }
    </script>

    <style>
        .totalHead {
            color: #9e9e9e;
            font-size: 26px;
            font-weight: 500;
            margin-top: 30px;
        }

        .head {
            color: #a4b6c4;
            font-size: 22px;
            font-weight: normal;
            margin-top: 20px;
        }

        .word {
            line-height: 30px;
            font-size: 16px;
            color: #6b6b6b;
            font-weight: 300;
        }

        .range {
            line-height: 30px;
            font-size: 16px;
            color: #6b6b6b;
            font-weight: 300;
        }
    </style>
</head>
<body>

<div align="center">
    <div style="width: 1100px;margin-top: 40px">
        <div style="display: inline-block;vertical-align: top;width: 225px;height:auto;">
            <%--<div class="ui left vertical menu">--%>
            <%--<div class="item">--%>
            <%--<img class="ui mini image" src="${pageContext.request.contextPath}/css/pic/titleIcon.png">--%>
            <%--</div>--%>
            <%--<a class="item">Features</a>--%>
            <%--<a class="item">Testimonials</a>--%>
            <%--<a class="item">Sign-in</a>--%>
            <%--</div>--%>
            <%--<div class="ui secondary vertical pointing menu">--%>
            <%--<a class="item">--%>
            <%--Home--%>
            <%--</a>--%>
            <%--<a class="item">--%>
            <%--Messages--%>
            <%--</a>--%>
            <%--<a class="item active">--%>
            <%--Friends--%>
            <%--</a>--%>
            <%--</div>--%>

            <div id='cssmenu' align="left" style="position: fixed">
                <ul>
                    <li><a href='#'><i class="Share Alternate icon"></i>多因子选股指标</a></li>
                    <li class='has-sub'><a href='#'>价值能力因子</a>
                        <ul>
                            <li onclick="scrollIntoView(1)"><a href="#1">市净率</a></li>
                            <li onclick="scrollIntoView(2)"><a href="#2">市盈率</a></li>
                            <li onclick="scrollIntoView(3)"><a href="#3">每股收益同比</a></li>
                            <li onclick="scrollIntoView(4)"><a href="#4">收入同比</a></li>
                            <li onclick="scrollIntoView(5)"><a href="#5">利润同比</a></li>
                            <li onclick="scrollIntoView(6)"><a href="#6">净利润率</a></li>
                        </ul>
                    </li>
                    <li class='has-sub'><a href='#'>盈利能力因子</a>
                        <ul>
                            <li onclick="scrollIntoView(7)"><a href='#7'>净资产收益率</a></li>
                            <li onclick="scrollIntoView(8)"><a href='#8'>净利润同比</a></li>
                            <li onclick="scrollIntoView(9)"><a href='#9'>净利率</a></li>
                            <li onclick="scrollIntoView(10)"><a href='#10'>毛利率</a></li>
                        </ul>
                    </li>
                    <li class='has-sub'><a href='#'>营运能力因子</a>
                        <ul>
                            <li onclick="scrollIntoView(11)"><a href='#11'>应收帐款周转率</a></li>
                            <li onclick="scrollIntoView(12)"><a href='#12'>存货周转率</a></li>
                        </ul>
                    </li>
                    <li class='has-sub'><a href='#'>成长能力因子</a>
                        <ul>
                            <li onclick="scrollIntoView(13)"><a href='#13'>主营业务收入增长率</a></li>
                            <li onclick="scrollIntoView(14)"><a href='#14'>净利率增长率</a></li>
                            <li onclick="scrollIntoView(15)"><a href='#15'>净资产增长率</a></li>
                            <li onclick="scrollIntoView(16)"><a href='#16'>总资产增长率</a></li>
                            <li onclick="scrollIntoView(17)"><a href='#17'>每股收益增长率</a></li>
                        </ul>
                    </li>
                    <li class='has-sub'><a href='#'>偿债能力因子</a>
                        <ul>
                            <li onclick="scrollIntoView(18)"><a href='#18'>流动比率</a></li>
                            <li onclick="scrollIntoView(19)"><a href='#19'>速动比率</a></li>
                            <li onclick="scrollIntoView(20)"><a href='#20'>现金比率</a></li>
                            <li onclick="scrollIntoView(21)"><a href='#21'>股东权益比率</a></li>
                            <li onclick="scrollIntoView(22)"><a href='#22'>股东权益增长率</a></li>
                        </ul>
                    </li>
                    <li class='has-sub'><a href='#'>现金流量因子</a>
                        <ul>
                            <li onclick="scrollIntoView(23)"><a href='#23'>经营现金净流量对销售收入比率</a></li>
                            <li onclick="scrollIntoView(24)"><a href='#24'>资产的经营现金流量回报率</a></li>
                            <li onclick="scrollIntoView(25)"><a href='#25'>经营现金净流量与净利润的比率</a></li>
                            <li onclick="scrollIntoView(26)"><a href='#26'>经营现金净流量对负债比率</a></li>
                            <li onclick="scrollIntoView(27)"><a href='#27'>现金流量比率</a></li>
                        </ul>
                    </li>

                    <li class='has-sub'><a href='#'>技术指标</a>
                        <ul>
                            <li onclick="scrollIntoView(36)"><a href='#28'>KDJ</a></li>
                            <li onclick="scrollIntoView(37)"><a href='#29'>MACD</a></li>
                            <li onclick="scrollIntoView(38)"><a href='#30'>BOLL</a></li>
                            <li onclick="scrollIntoView(39)"><a href='#31'>DMI</a></li>
                        </ul>
                    </li>
                </ul>
            </div>

        </div>
        <div id="main"
             style="display: inline-block;vertical-align: top;width: 855px;height:auto;padding:30px;margin-left: 15px;background-color: #fff;">
            <h1 class="totalHead">价值能力因子</h1>
            <div id="1" align="left">
                <h2 class="head">市净率</h2>
                <p class="word">市净率指的是每股股价与每股净资产的比率。
                    市净率可用于股票投资分析，一般来说市净率较低的股票，投资价值较高，相反，则投资价值较低；但在判断投资价值时还要考虑当时的市场环境以及公司经营情况、盈利能力等因素。</p>
                <p class="range">数值范围: 最大 124.0 最小 1.797</p>
            </div>
            <div id="2" align="left">
                <h2 class="head">市盈率</h2>
                <p class="word">市盈率由股价除以年度每股盈余得出</p>
                <p class="range">数值范围: 最大 23.0 最小 1.797</p>
            </div>
            <div id="3" align="left">
                <h2 class="head">每股收益同比</h2>
                <p class="word">每股收益同比增长率是指第二年的每股收益比第一年的每股收益多出的净收益占第一年每股收益的百分比</p>
                <p class="range">数值范围: 最大 23.0 最小 1.797</p>
            </div>
            <div id="4" align="left">
                <h2 class="head">收入同比</h2>
                <p class="word">收入同比增长率是企业在一定期间内取得的营业收入与其上年同期营业收入的增长的百分比，以反映企业在此期间内营业收入的增长或下降等情况。</p>
                <p class="range">数值范围: 最大 1.797 最小 -10.0</p>
            </div>
            <div id="5" align="left">
                <h2 class="head">利润同比</h2>
                <p class="word">利润同比增长率是企业在一定期间内取得的利润与其上年同期利润的增长的百分比，以反映企业在此期间内利润的增长或下降等情况。</p>
                <p class="range">数值范围: 最大 1.797 最小 0.0</p>
            </div>
            <div id="6" align="left">
                <h2 class="head">净利润率</h2>
                <p class="word">扣除所有成本、费用和企业所得税后的利润率</p>
                <p class="range">数值范围: 最大 1.797 最小 0.0</p>
            </div>
            <h1 class="totalHead">盈利能力因子</h1>
            <div id="7" align="left">
                <h2 class="head">净资产收益率</h2>
                <p class="word">净资产收益率是净利润与平均股东权益的百分比，是公司税后利润除以净资产得到的百分比率，该指标反映股东权益的收益水平，用以衡量公司运用自有资本的效率。</p>
                <p class="range">数值范围: 最大 1.797 最小 0.0</p>
            </div>
            <div id="8" align="left">
                <h2 class="head">净利润同比</h2>
                <p class="word">利润同比增长率是企业在一定期间内取得的净利润与其上年同期净利润的增长的百分比，以反映企业在此期间内净利润的增长或下降等情况。</p>
                <p class="range">数值范围: 最大 1.797 最小 0.0</p>
            </div>
            <div id="9" align="left">
                <h2 class="head">净利率</h2>
                <p class="word">说明企业收入1块钱能净赚多少钱，看该指标的时候可以和毛利率比较一下，两者越接近说明企业的期间费用越低。</p>
                <p class="range">数值范围: 最大 1.089 最小 -2.878</p>
            </div>
            <div id="10" align="left">
                <h2 class="head">毛利率</h2>
                <p class="word">毛利率是毛利与销售收入的百分比，其中毛利是收入和与收入相对应的营业成本之间的差额。</p>
                <p class="range">数值范围: 最大 386.89 最小 -9977.290</p>
            </div>
            <h1 class="totalHead">营运能力因子</h1>
            <div id="11" align="left">
                <h2 class="head">应收帐款周转率</h2>
                <p class="word">应收账款周转率就是反映公司应收账款周转速度的比率。它说明一定期间内公司应收账款转为现金的平均次数。</p>
                <p class="range">数值范围: 最大 4.904 最小 -0.3269</p>
            </div>
            <div id="12" align="left">
                <h2 class="head">存货周转率</h2>
                <p class="word">存货周转率是企业一定时期销货成本与平均存货余额的比率，用于反映存货的周转速度。</p>
                <p class="range">数值范围: 最大 3668117.7168 最小 -21.3634</p>
            </div>
            <h1 class="totalHead">成长能力因子</h1>
            <div id="13" align="left">
                <h2 class="head">主营业务收入增长率</h2>
                <p class="word">
                    主营业务收入增长率可以用来衡量公司的产品生命周期。超过10%说明公司产品处于成长期，将继续保持较好的增长势头；在5%～10%之间，说明公司产品已进入稳定期，需要着手开发新产品；低于5%，说明公司产品已进入衰退期。</p>
                <p class="range">数值范围: 最大 1.346 最小 -118.729</p>
            </div>
            <div id="14" align="left">
                <h2 class="head">净利率增长率</h2>
                <p class="word">净利润增长率代表企业当期净利润比上期净利润的增长幅度，指标值越大代表企业盈利能力越强。</p>
                <p class="range">数值范围: 最大 4086723.168 最小 -477176.249</p>
            </div>
            <div id="15" align="left">
                <h2 class="head">净资产增长率</h2>
                <p class="word">净资产增长率是指企业本期净资产增加额与上期净资产总额的比率。净资产增长率反映了企业资本规模的扩张速度，是衡量企业总量规模变动和成长状况的重要指标。</p>
                <p class="range">数值范围: 最大 1472759.021 最小 -1.507</p>
            </div>
            <div id="16" align="left">
                <h2 class="head">总资产增长率</h2>
                <p class="word">总资产增长率是企业本年总资产增长额同年初资产总额的比率，反映企业本期资产规模的增长情况。</p>
                <p class="range">数值范围: 最大 2530507.805 最小 -100.0</p>
            </div>
            <div id="17" align="left">
                <h2 class="head">每股收益增长率</h2>
                <p class="word">每股收益增长率,是指反映了每一份公司股权可以分得的利润的增长程度。</p>
                <p class="range">数值范围: 最大 643900.0 最小 -201000.0</p>
            </div>
            <h1 class="totalHead">偿债能力因子</h1>
            <div id="18" align="left">
                <h2 class="head">流动比率</h2>
                <p class="word">流动比率是流动资产对流动负债的比率，用来衡量企业流动资产在短期债务到期以前，可以变为现金用于偿还负债的能力。</p>
                <p class="range">数值范围: 最大 4010.502 最小 -60.956</p>
            </div>
            <div id="19" align="left">
                <h2 class="head">速动比率</h2>
                <p class="word">速动比率是指速动资产对流动负债的比率。它是衡量企业流动资产中可以立即变现用于偿还流动负债的能力。</p>
                <p class="range">数值范围: 最大 3875.224 最小 -20.080</p>
            </div>
            <div id="20" align="left">
                <h2 class="head">现金比率</h2>
                <p class="word">现金比率通过计算公司现金以及现金等价资产总量与当前流动负债的比率，来衡量公司资产的流动性。</p>
                <p class="range">数值范围: 最大 352946.710 最小 -1773.497</p>
            </div>
            <div id="21" align="left">
                <h2 class="head">股东权益比例</h2>
                <p class="word">股东权益比率是股东权益与资产总额的比率，该比率反映企业资产中有多少是所有者投入的。</p>
                <p class="range">数值范围: 最大 168.382 最小 -133197.822</p>
            </div>
            <div id="22" align="left">
                <h2 class="head">股东权益增长率</h2>
                <p class="word">股东权益增长率反映了企业当年股东权益的变化水平，体现了企业资本的积累能力，是评价企业发展潜力的重要财务指标。</p>
                <p class="range">数值范围: 最大 130333.867 最小 -68.382</p>
            </div>
            <h1 class="totalHead">现金流量因子</h1>
            <div id="23" align="left">
                <h2 class="head">经营现金净流量对销售收入比率</h2>
                <p class="word">销售现金比率是指经营现金净流入和投入资源的比值。该指标反映企业销售质量的高低，与企业的赊销政策有关。如果企业有虚假收入，也会使该指标过低。</p>
                <p class="range">数值范围: 最大 175688.995 最小 -21456.687</p>
            </div>
            <div id="24" align="left">
                <h2 class="head">资产的经营现金流量回报率</h2>
                <p class="word">资产的经营现金流量回报率是由经营活动产生的现金流量净额除以总资产产生，是体现企业收现能力的指标之一。</p>
                <p class="range">数值范围: 最大 62.789 最小 -41.402</p>
            </div>
            <div id="25" align="left">
                <h2 class="head">经营现金净流量与净利润的比率</h2>
                <p class="word">经营现金净流量与净利润的比率是指企业本期经营活动产生的现金净流量与净利润之间的比率关系。</p>
                <p class="range">数值范围: 最大 13065.053 最小 -31989.404</p>
            </div>
            <div id="26" align="left">
                <h2 class="head">经营现金净流量对负债比率</h2>
                <p class="word">现金流动负债比率是企业一定时期的经营现金净流量同流动负债的比率，它可以从现金流量角度来反映企业当期偿付短期负债的能力。</p>
                <p class="range">数值范围: 最大 1338.732 最小 -3203.270</p>
            </div>
            <div id="27" align="left">
                <h2 class="head">现金流量比率</h2>
                <p class="word">经营现金流量比率是指现金流量与其他项目数据相比所得的值。</p>
                <p class="range">数值范围: 最大 133873.216 最小 -320327.031</p>
            </div>
            <h1 class="totalHead">技术指标</h1>
            <div id="28" align="left">
                <h2 class="head">KDJ</h2>
                <p class="word">随机指标（KDJ）由 George C．Lane
                    创制。它综合了动量观念、强弱指标及移动平均线的优点，用来度量股价脱离价格正常范围的变异程度。KDJ指标考虑的不仅是收盘价，而且有近期的最高价和最低价，这避免了仅考虑收盘价而忽视真正波动幅度的弱点。</p>
            </div>
            <div id="29" align="left">
                <h2 class="head">MACD</h2>
                <p class="word">MACD 是根据移动平均线较易掌握趋势变动的方向之优点所发展出来的，它是利用二条不同速度（一条变动的速率快──短期的移动平均线，另一条较慢──长期的移动平
                    均线）的指数平滑移动平均线来计算二者之间的差离状况（DIF）作为研判行情的基础，然后再求取其DIF之9日平滑移动平均线，即MACD线。MACD实际就是运用快速与慢速移动平均线聚合与分离的征兆，来研判买进与卖进的时机和讯号。</p>
            </div>
            <div id="30" align="left">
                <h2 class="head">BOLL</h2>
                <p class="word">
                    在众多技术分析指标中，BOLL指标属于比较特殊的一类指标。绝大多数技术分析指标都是通过数量的方法构造出来的，它们本身不依赖趋势分析和形态分析，而BOLL指标却股价的形态和趋势有着密不可分的联系。BOLL指标中的“股价通道”概念正是股价趋势理论的直观表现形式。BOLL是利用“股价通道”来显示股价的各种价位，当股价波动很小，处于盘整时，股价通道就会变窄，这可能预示着股价的波动处于暂时的平静期；当股价波动超出狭窄的股价通道的上轨时，预示着股价的异常激烈的向上波动即将开始；当股价波动超出狭窄的股价通道的下轨时，同样也预示着股价的异常激烈的向下波动将开始。</p>
            </div>
            <div id="31" align="left">
                <h2 class="head">DMI</h2>
                <p class="word">
                    动向指数又叫移动方向指数或趋向指数。是属于趋势判断的技术性指标，其基本原理是通过分析股票价格在上升及下跌过程中供需关系的均衡点，即供需关系受价格变动之影响而发生由均衡到失衡的循环过程，从而提供对趋势判断的依据。动向的指数有三条线：上升指标线，下降指标线和平均动向指数线。三条线均可设定天数，一般为14天。</p>
            </div>
        </div>
    </div>
</div>

</body>


</html>
