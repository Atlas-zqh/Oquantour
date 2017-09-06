package vo;


import javafx.beans.property.SimpleStringProperty;

/**
 * Created by island on 2017/4/9.
 */
public class StockNameVO {

    //股票ID
    public String stockID;
    //股票名字
    public String stockName;

    public SimpleStringProperty specCode;
    public SimpleStringProperty specName;


    public StockNameVO(String specCode, String specName) {
        this.specCode = new SimpleStringProperty(specCode);
        this.specName = new SimpleStringProperty(specName);
    }


    public String getSpecCode() {
        return specCode.get();
    }

    public String getSpecName() {
        return specName.get();
    }
}
