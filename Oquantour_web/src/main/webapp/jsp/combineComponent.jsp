<%--
  Created by IntelliJ IDEA.
  User: st
  Date: 2017/6/13
  Time: 23:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div style="width: 100%">
    <label style="display: inline-block;vertical-align: baseline;color: #7a7a7a;font-size: 16px;">组合名称：</label>
    <form class="ui form"
          style="display:inline-block;vertical-align:-webkit-baseline-middle;width: 60%">
        <div class="field">
            <input id="combineName" type="text" style="width: 300px" placeholder="取个好听的组合名字吧！">
        </div>
    </form>
    <br><br><br>
    <label style="display: inline-block;color: #7a7a7a;font-size: 16px;margin-top: 10px">组合成分：</label>

    <div align="left" style="display: inline-block;vertical-align: top">
        <div style="width: auto">
            <div style="display:inline-block;">
                <select id="search_ui" style="width:300px;outline: none"
                        onchange='addStockToCombine(this.value)'>
                    <!-- Dropdown List Option -->
                </select>
            </div>
        </div>
    </div>


    <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp"
           id="combineTable" style="width: 100%;margin-top: 20px">
        <thead>
        <tr>
            <th class="mdl-data-table__cell--non-numeric">股票</th>
            <th class="mdl-data-table__cell--non-numeric">代码</th>
            <th class="mdl-data-table__cell--non-numeric">调整仓位(共100%)</th>
            <th class="mdl-data-table__cell--non-numeric">操作</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>

    <div align="right" style="margin-top: 40px">
        <button id="confirmAddStock" onclick="confirmCreateCombine()">
            完成
        </button>
    </div>

    <%--<input type="range" min="0" max="100" value="90">--%>
</div>
