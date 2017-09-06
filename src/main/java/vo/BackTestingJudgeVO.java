package vo;

import java.util.List;

/**
 * 包含了：
 * 1／能否回测 boolean
 * 2／不能回测的股票信息（名字）
 * <p>
 * Created by keenan on 27/03/2017.
 */
public class BackTestingJudgeVO {
    //  能否回测
    private boolean canTest;

    // 可以回测的股票代码
    private List<String> validStocksCodes;

    // 不能回测的股票名字（没有数据的）
    private List<String> invalidStockNames_Statistics;

    // 不能回测的股票名字（停牌的）
    private List<String> invalidStockNames_Halt;

    public BackTestingJudgeVO(boolean canTest, List<String> validStocksCodes, List<String> invalidStockNames_Statistics,
                              List<String> invalidStockNames_Halt) {
        this.canTest = canTest;
        this.validStocksCodes = validStocksCodes;
        this.invalidStockNames_Statistics = invalidStockNames_Statistics;
        this.invalidStockNames_Halt = invalidStockNames_Halt;
    }

    public boolean getCanBackTest(){
        return canTest;
    }
    public List<String> getValidStocksCodes() {
        return validStocksCodes;
    }

    public List<String> getInvalidStockNames_Statistics() {
        return invalidStockNames_Statistics;
    }

    public List<String> getInvalidStockNames_Halt(){return invalidStockNames_Halt;}
}
