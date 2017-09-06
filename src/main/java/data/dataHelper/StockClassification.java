package data.dataHelper;

import org.apache.metamodel.DataContext;
import org.apache.metamodel.csv.CsvConfiguration;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.DataContextFactory;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.query.SelectItem;
import org.apache.metamodel.schema.Schema;
import org.apache.metamodel.schema.Table;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于分析庞大的原始Csv文件数据文件
 * <p>
 * Created by keenan on 03/03/2017.
 */
public class StockClassification {
    // CSV文档位置
    private static final File file = new File(System.getProperty("user.dir") + "/datasource/历史数据.csv");
    // CSV文件读取配置，包括列名的位置，解码方式，分隔符，quoteChar和escapeChar
    private CsvConfiguration csvConfiguration = new CsvConfiguration(1, "UTF-8", '\t', '\\', '\\');
    // 从CSV文档中提取的数据
    private DataContext csvContext = DataContextFactory.createCsvDataContext(file, csvConfiguration);

    /**
     * 从行中提取各属性值
     *
     * @param rows
     * @return 所有行组成的List，Object[]为各属性值组成的数组
     */
    private static List<Object[]> get2ArgArrayFromRows(List<Row> rows) {
        List<Object[]> myArray = new ArrayList<Object[]>();
        SelectItem[] cols = rows.get(0).getSelectItems();
        for (Row r : rows) {
            Object[] data = r.getValues();
            for (int j = 0; j < cols.length; j++) {
                if (data[j] == null) data[j] = ""; // force empty string where there are NULL values
            }
            myArray.add(data);
        }
//        System.out.println("Row count: " + rows.size());
//        System.out.println("Column names: " + Arrays.toString(cols));
        return myArray;
    }

    /**
     * 从CSV文档中获取信息
     *
     * @param key
     * @param value
     * @return 所有行组成的List，Object[]为各属性值组成的数组
     */
    private List<Object[]> getFromCSVFile(String key, String value) {

        Schema schema = csvContext.getDefaultSchema();
        Table[] tables = schema.getTables();
        Table table = tables[0]; // a representation of the csv file name including extension

        DataSet dataSet = csvContext.query()
                .from(table)
                .selectAll()
                .where(key).eq(value)
                .execute();

        List<Row> rows = dataSet.toRows();
        if (rows.isEmpty()) {
            return null;
        }
        List<Object[]> myArray = get2ArgArrayFromRows(rows);
        return myArray;
    }

    /**
     * 根据年份分解数据，在项目目录下生成txt文件
     * 这个方法只会在系统第一次初始化时用到
     * 之后增添数据来源，需要更改方法至：每一次有新数据的出现，现删除原有的所有数据，再加载新的数据
     *
     * @param startYear 数据起始年份
     * @param endYear   数据结束年份
     * @throws Exception
     */
    public void separateByYear(int startYear, int endYear) throws Exception {
        FileWriter fw = null;

        List<Object[]> result = getFromCSVFile("market", "SZ");

        int size = result.size();

        // 分解产物文档的地址
        String address;

        for (int i = startYear; i <= endYear; i++) {
            if (i < 10) {
                address = System.getProperty("user.dir")+"/datasource/yearInfo/" + "0" + i + ".txt";
            } else {
                address = System.getProperty("user.dir")+"/datasource/yearInfo/" + i + ".txt";
            }


            try {
                File file = new File(address);
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                if (!file.exists()) {
                    file.createNewFile();
                }

                fw = new FileWriter(file, true);

            } catch (IOException x) {
                x.printStackTrace();
            }

            // 分析总文档的数据，并根据年份进行拆分
            for (int j = 0; j < size; j++) {
                if (result.get(j)[3].equals(result.get(j)[4])) {
                    continue;
                } else if (result.get(j)[6].equals("0")) {
                    continue;
                }
                // 目前分析的条目所属年份
                String thisYear = (String) result.get(j)[1];

                // 年份标示
                String yearMark;

                if (i < 10) {
                    yearMark = "0" + i;
                } else {
                    yearMark = "" + i;
                }

                if (thisYear.endsWith("/" + yearMark)) {
                    for (int k = 0; k < result.get(j).length; k++) {
                        fw.write(result.get(j)[k] + "\t");
                    }
                    fw.write('\n');
                }
            }
            fw.flush();
            fw.close();
        }
    }

    /**
     * 根据股票代码分解数据，在项目目录下生成txt文件
     * 这个方法只会在系统第一次初始化时用到
     * 之后增添数据来源，需要更改方法至：每一次有新数据的出现，现删除原有的所有数据，再加载新的数据
     *
     * @throws Exception
     */
    public void separateByCode() throws Exception {
        List<Object[]> result = getFromCSVFile("market", "SZ");

        // 储存所有出现的股票代码
        List<String> stockCode = new ArrayList<String>();

        int size = result.size();

        for (int i = 0; i < size; i++) {
            String thisCode = (String) result.get(i)[8];
            if (stockCode.contains(thisCode)) {
                continue;
            } else {
                stockCode.add(thisCode);
            }
        }

        int codeSize = stockCode.size();

        FileWriter fw = null;

        for (int i = 0; i < codeSize; i++) {
            String tempAddress = System.getProperty("user.dir")+"/datasource/stocks/";

            String tempCode = stockCode.get(i);
            String codeClass = tempCode.substring(0, 1);

            tempAddress = tempAddress + codeClass + "/" + tempCode + ".txt";

            try {
                File file = new File(tempAddress);
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                if (!file.exists())
                    file.createNewFile();

                fw = new FileWriter(file, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < size; j++) {
                if (result.get(j)[3].equals(result.get(j)[4])) {
                    continue;
                } else if (result.get(j)[6].equals("0")) {
                    continue;
                }

                String code = (String) result.get(j)[8];
                if (code.equals(tempCode)) {
                    for (int k = 0; k < result.get(j).length; k++) {
                        fw.write(result.get(j)[k] + "\t");
                    }
                    fw.write('\n');
                }
            }
            fw.flush();
            fw.close();
        }
    }

}
