package ui.charts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import vo.StockNameVO;
import vo.StockVO;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by island on 2017/4/8.
 */
public class SingleTableViewOfStockInfo extends Pane {

    private final TableView<StockNameVO> table = new TableView();

    /**
     * 根据传入的股票列表构造出表格
     *
     * @param stocks
     */
    public SingleTableViewOfStockInfo(List<String> stocks) {
        // 新建列
        TableColumn<StockNameVO,String> idColumn = new TableColumn<>("股票编号");
        idColumn.setPrefWidth(275);
        TableColumn<StockNameVO, String> nameColumn = new TableColumn<>("股票名称");
        nameColumn.setPrefWidth(275);


        // 传入数据
        ObservableList<StockNameVO> stockNames = FXCollections.observableArrayList();
        for (int i = 0; i < stocks.size(); i++) {
            stockNames.add(new StockNameVO(stocks.get(i).split(";")[0], stocks.get(i).split(";")[1]));
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("specCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("specName"));

        // 设置成不能排序
        idColumn.setSortable(false);
        nameColumn.setSortable(false);

        table.getColumns().addAll(idColumn, nameColumn);
        table.setItems(stockNames);
        table.setEditable(false);

        // 设置样式
        table.getStylesheets().add("ui/MyTableView2.css");


        table.setPrefSize(575, 240);
        getChildren().add(table);
    }
}
