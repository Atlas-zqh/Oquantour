package ui.charts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import vo.StockVO;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by st on 2017/3/12.
 */
public class SingleTableView extends Pane {

    private final TableView<StockVO> table = new TableView();

    /**
     * 根据传入的排好序的股票列表构造出榜单表格
     *
     * @param stockVOS
     * @param isUpOrder
     */
    public SingleTableView(List<StockVO> stockVOS, boolean isUpOrder) {

        // 新建列
        TableColumn<StockVO, String> idColumn = new TableColumn<>("股票编号");
        idColumn.setPrefWidth(125);
        TableColumn<StockVO, String> nameColumn = new TableColumn<>("股票名称");
        nameColumn.setPrefWidth(125);
        TableColumn<StockVO, String> marketColumn = new TableColumn<>("市场");
        marketColumn.setPrefWidth(100);
        TableColumn<StockVO, String> changeColumn = new TableColumn<>("涨跌幅");
        changeColumn.setPrefWidth(120);
        TableColumn<StockVO, String> openColumn = new TableColumn<>("开盘价");
        openColumn.setPrefWidth(120);
        TableColumn<StockVO, String> closeColumn = new TableColumn<>("收盘价");
        closeColumn.setPrefWidth(120);
        TableColumn<StockVO, String> highColumn = new TableColumn<>("最高价");
        highColumn.setPrefWidth(100);
        TableColumn<StockVO, String> lowColumn = new TableColumn<>("最低价");
        lowColumn.setPrefWidth(100);
        TableColumn<StockVO, String> volumeColumn = new TableColumn<>("成交量");
        volumeColumn.setPrefWidth(110);

        // 传入数据
        ObservableList<StockVO> stockVOObservableList = FXCollections.observableArrayList();
        for (int i = 0; i < stockVOS.size(); i++) {
            stockVOObservableList.add(new StockVO(stockVOS.get(i).code, stockVOS.get(i).name, stockVOS.get(i).market,
                    new DecimalFormat("#.##%").format(stockVOS.get(i).chg), String.valueOf(stockVOS.get(i).open),
                    String.valueOf(stockVOS.get(i).close), String.valueOf(stockVOS.get(i).high),
                    String.valueOf(stockVOS.get(i).low), String.valueOf(stockVOS.get(i).volume)));
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("specCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("specName"));
        marketColumn.setCellValueFactory(new PropertyValueFactory<>("specMarket"));
        changeColumn.setCellValueFactory(new PropertyValueFactory<>("specChg"));
        openColumn.setCellValueFactory(new PropertyValueFactory<>("specOpen"));
        closeColumn.setCellValueFactory(new PropertyValueFactory<>("specClose"));
        highColumn.setCellValueFactory(new PropertyValueFactory<>("specHigh"));
        lowColumn.setCellValueFactory(new PropertyValueFactory<>("specLow"));
        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("specVolume"));

        // 设置成不能排序
        idColumn.setSortable(false);
        nameColumn.setSortable(false);
        marketColumn.setSortable(false);
        changeColumn.setSortable(false);
        openColumn.setSortable(false);
        closeColumn.setSortable(false);
        highColumn.setSortable(false);
        lowColumn.setSortable(false);
        volumeColumn.setSortable(false);

        table.getColumns().addAll(idColumn, nameColumn, marketColumn, changeColumn, openColumn, closeColumn,
                highColumn, lowColumn, volumeColumn);
        table.setItems(stockVOObservableList);
        table.setEditable(false);

        // 设置样式，涨幅榜显示为红色，跌幅榜显示为绿色
        table.getStylesheets().add("ui/MyTableView.css");
        if (isUpOrder) {
            changeColumn.setStyle("-fx-text-fill: #d97555");
            openColumn.setStyle("-fx-text-fill: #d97555");
            closeColumn.setStyle("-fx-text-fill: #d97555");
            highColumn.setStyle("-fx-text-fill: #d97555");
            lowColumn.setStyle("-fx-text-fill: #d97555");
        } else {
            changeColumn.setStyle("-fx-text-fill: #4c9b8e");
            openColumn.setStyle("-fx-text-fill: #4c9b8e");
            closeColumn.setStyle("-fx-text-fill: #4c9b8e");
            highColumn.setStyle("-fx-text-fill: #4c9b8e");
            lowColumn.setStyle("-fx-text-fill: #4c9b8e");
        }

        table.setPrefSize(1060, 900);
        getChildren().add(table);
    }
}
