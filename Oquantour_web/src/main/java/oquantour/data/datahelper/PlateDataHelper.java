package oquantour.data.datahelper;

import oquantour.data.dao.PlateDao;
import oquantour.po.PlateinfoPO;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by island on 2017/6/7.
 */
public class PlateDataHelper {
    static ClassPathXmlApplicationContext ctx;

    PlateDao plateDao;


    public PlateDataHelper(){
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        plateDao = (PlateDao)ctx.getBean("plateDao");
    }

    private List<String> readTxt(String path){
        File file = new File(path);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        BufferedReader bufferedReader = null;
        List<String> s = new ArrayList<>();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String str = null;
            while ((str = bufferedReader.readLine()) != null)
                s.add(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    public void addPlateInfo(){
        List<String> paths = new ArrayList<>();
        paths.add("/Users/island/IdeaProjects/Oquantour/datasource/plate/000001.csv");
        paths.add("/Users/island/IdeaProjects/Oquantour/datasource/plate/399001.csv");
        paths.add("/Users/island/IdeaProjects/Oquantour/datasource/plate/399005.csv");
        paths.add("/Users/island/IdeaProjects/Oquantour/datasource/plate/399006.csv");
        paths.add("/Users/island/IdeaProjects/Oquantour/datasource/plate/399107.csv");
        paths.add("/Users/island/IdeaProjects/Oquantour/datasource/plate/399300.csv");
        for(int i = 0; i < paths.size(); i++){
            final int count = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    List<PlateinfoPO> plateinfoPOS = new ArrayList<>();
                    List<String> strings = readTxt(paths.get(count));
                    for(int j = 1; j < strings.size(); j++){
                        String[] info = strings.get(j).split(",");
                        PlateinfoPO plateinfoPO = new PlateinfoPO();
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            plateinfoPO.setDateValue(new Date(sdf.parse(info[0]).getTime()));
                        }catch (ParseException e){
                            e.printStackTrace();
                        }

                        if(info[1].substring(1).equals("000001"))
                            plateinfoPO.setPlateName("上证指数");
                        if(info[1].substring(1).equals("399001"))
                            plateinfoPO.setPlateName("深证成指");
                        if(info[1].substring(1).equals("399005"))
                            plateinfoPO.setPlateName("中小板指");
                        if(info[1].substring(1).equals("399006"))
                            plateinfoPO.setPlateName("创业板指");
                        if(info[1].substring(1).equals("399107"))
                            plateinfoPO.setPlateName("深证Ａ指");
                        if(info[1].substring(1).equals("399300"))
                            plateinfoPO.setPlateName("沪深300");

                        if(!info[3].equals("None"))
                            plateinfoPO.setClosePrice(Double.parseDouble(info[3]));
                        if(!info[4].equals("None"))
                            plateinfoPO.setHighPrice(Double.parseDouble(info[4]));
                        if(!info[5].equals("None"))
                            plateinfoPO.setLowPrice(Double.parseDouble(info[5]));
                        if(!info[6].equals("None"))
                            plateinfoPO.setOpenPrice(Double.parseDouble(info[6]));
                        if(!info[7].equals("None"))
                            plateinfoPO.setAdjClosePrice(Double.parseDouble(info[7]));
                        if(!info[8].equals("None"))
                            plateinfoPO.setFluctuation(Double.parseDouble(info[8]));
                        if(!info[9].equals("None"))
                            plateinfoPO.setChg(Double.parseDouble(info[9]));
                        if(!info[10].equals("None"))
                            plateinfoPO.setVolume(Double.parseDouble(info[10]));
                        if(!info[11].equals("None"))
                            plateinfoPO.setAmount(Double.parseDouble(info[11]));

                        plateinfoPOS.add(plateinfoPO);
                    }
                    for(int j = 0 ; j < plateinfoPOS.size(); j++){
                        PlateinfoPO plateinfoPO = plateinfoPOS.get(j);
                        System.out.println(plateinfoPO.getPlateName() + plateinfoPO.getDateValue());
                        if(j < plateinfoPOS.size() - 1 && plateinfoPOS.get(j + 1).getAdjClosePrice() != null){
                            plateinfoPO.setReturnRate( (plateinfoPO.getAdjClosePrice() - plateinfoPOS.get(j + 1).getAdjClosePrice()) / plateinfoPOS.get(j + 1).getAdjClosePrice());
                        }
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date startDate = new Date(sdf.parse("2005-01-01").getTime());
                            if(plateinfoPO.getDateValue().after(startDate))
                                plateDao.updatePlateInfo(plateinfoPO);
                        }catch (ParseException e){
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.start();

        }

    }


    /**
     * Get XML String of utf-8
     *
     * @return XML-Formed string
     */
    private String getUTF8XMLString(String xml) {

        try {
            byte[] b = xml.getBytes("gbk");//编码
            String sa = new String(b, "gbk");//解码:用什么字符集编码就用什么字符集解码

            b = sa.getBytes("utf-8");//编码
            sa = new String(b, "utf-8");//解码
            //System.out.println(sa.replace(" ", ""));

            return sa.replace(" ", "");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return null;
    }

}
