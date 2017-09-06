package oquantour.data.datagetter;

import oquantour.po.NewsPO;
import oquantour.po.StockRealTimePO;
import oquantour.po.TopListPO;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 2017/6/12.
 */
public class NewsInfoGetter {
    PythonExecutor executor;

    Paramaters paramaters;

    public NewsInfoGetter() {
        executor = new PythonExecutor();
        paramaters = new Paramaters();
    }

    public List<NewsPO> getNews() {
        List<NewsPO> newsPOS = new ArrayList<>();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            executor.excute("News.py", new Object[]{paramaters.getPath() + "news.csv"});
            BufferedReader reader = paramaters.getBufferedReader("news.csv");
            int count = 0;
            if (reader != null) {
                String tem = reader.readLine();
                String content = "";
                NewsPO newsPO = new NewsPO();
                while ((tem = reader.readLine()) != null) {
                    if(tem.startsWith(count + ",")) {
                        if(count > 0) {
                            if(newsPOS.size() > 0)
                                newsPOS.get(newsPOS.size() - 1).setContent(content);
                            count++;
                            NewsPO newsPO1 = new NewsPO();
                            String[] infos = tem.split(",");
                            content = "";
                            System.out.println(tem);
                            if (infos[1].equals("证券") || infos[1].equals("国内财经")) {
                                newsPOS.add(newsPO1);
                                newsPO1.setTitle(infos[2]);
                                newsPO1.setDateValue(infos[3]);
                                newsPO1.setUrl(infos[4]);
                                content += infos[5];
                                content += "<br>";
                            }
                        }else{
                            count++;
                            String[] infos = tem.split(",");
                            content = "";
                            System.out.println(tem);
                            if (infos[1].equals("证券") || infos[1].equals("国内财经")) {
                                newsPOS.add(newsPO);
                                newsPO.setTitle(infos[2]);
                                newsPO.setDateValue(infos[3]);
                                newsPO.setUrl(infos[4]);
                                content += infos[5];
                                content += "<br>";
                            }
                        }
                    }else{
                        content += tem;
                        content += "<br>";
                        System.out.println(tem);
                    }

                }
            }
        }catch (IOException e1){
            e1.printStackTrace();
        }catch (InterruptedException e2){
            e2.printStackTrace();
        }
        return newsPOS;
    }
}
