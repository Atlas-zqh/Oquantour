package ui.charts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import vo.StockBackTestWinnerVO;
import vo.StockBasicInfoVO;
import vo.StockVO;
import vo.WinnerInfoVO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by island on 2017/4/18.
 */
public class SingleTableViewOfBackTest extends Pane {

    private final TableView<StockBackTestWinnerVO> table = new TableView();

    /**
     * 根据传入的股票列表构造出榜单表格
     *
     */
    public SingleTableViewOfBackTest(WinnerInfoVO winnerInfoVO, int height) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(winnerInfoVO.getChangeDate());
        // 新建列
        TableColumn<StockBackTestWinnerVO, String> dateColumn = new TableColumn<>(date);
        dateColumn.setPrefWidth(1000);
        TableColumn<StockBackTestWinnerVO, String> idColumn = new TableColumn<>("股票编号");
        idColumn.setPrefWidth(135);
        TableColumn<StockBackTestWinnerVO, String> nameColumn = new TableColumn<>("股票名称");
        nameColumn.setPrefWidth(135);
        TableColumn<StockBackTestWinnerVO, String> openColumn = new TableColumn<>("开盘价");
        openColumn.setPrefWidth(135);
        TableColumn<StockBackTestWinnerVO, String> closeColumn = new TableColumn<>("收盘价");
        closeColumn.setPrefWidth(135);
        TableColumn<StockBackTestWinnerVO, String> stdcloseColumn = new TableColumn<>("复权收盘价");
        stdcloseColumn.setPrefWidth(135);
        TableColumn<StockBackTestWinnerVO, String> highColumn = new TableColumn<>("最高价");
        highColumn.setPrefWidth(135);
        TableColumn<StockBackTestWinnerVO, String> lowColumn = new TableColumn<>("最低价");
        lowColumn.setPrefWidth(135);


        dateColumn.getColumns().addAll(idColumn, nameColumn, openColumn, closeColumn, stdcloseColumn, highColumn, lowColumn);

        // 传入数据
        DecimalFormat df = new DecimalFormat("#.##");
        ObservableList<StockBackTestWinnerVO> stockBackTestWinnerVOS = FXCollections.observableArrayList();
        StockBasicInfoVO stockBasicInfoVO;
        for (int i = 0; i < winnerInfoVO.getWinners().size(); i++) {
            stockBasicInfoVO = winnerInfoVO.getWinners().get(i);
            stockBackTestWinnerVOS.add(new StockBackTestWinnerVO(date, stockBasicInfoVO.getStockCode(), stockBasicInfoVO.getStockName(),
                    df.format(stockBasicInfoVO.getOpen()), stockBasicInfoVO.getClose() + "", stockBasicInfoVO.getAdjClose() + "", stockBasicInfoVO.getHigh() + "", stockBasicInfoVO.getLow() + ""));
        }

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("specDate"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("specCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("specName"));
        openColumn.setCellValueFactory(new PropertyValueFactory<>("specOpen"));
        closeColumn.setCellValueFactory(new PropertyValueFactory<>("specClose"));
        stdcloseColumn.setCellValueFactory(new PropertyValueFactory<>("specStdClose"));
        highColumn.setCellValueFactory(new PropertyValueFactory<>("specHigh"));
        lowColumn.setCellValueFactory(new PropertyValueFactory<>("specLow"));

        // 设置成不能排序
        idColumn.setSortable(false);
        dateColumn.setSortable(false);
        stdcloseColumn.setSortable(false);
        nameColumn.setSortable(false);
        openColumn.setSortable(false);
        closeColumn.setSortable(false);
        highColumn.setSortable(false);
        lowColumn.setSortable(false);

        table.getColumns().addAll(dateColumn);
        table.setItems(stockBackTestWinnerVOS);
        table.setEditable(false);

        // 设置样式，涨幅榜显示为红色，跌幅榜显示为绿色
        table.getStylesheets().add("ui/MyTableView2.css");



        table.setPrefSize(1000, height);
        getChildren().add(table);
    }
}
