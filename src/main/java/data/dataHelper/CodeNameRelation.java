package data.dataHelper;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * 得到股票代码和股票名称的映射关系
 * <p>
 * Created by keenan on 06/03/2017.
 */
public class CodeNameRelation {
    private static class SingletionHolder {
        private static final CodeNameRelation codeNameRelation = new CodeNameRelation();
    }

    private Map<String, String> map_name_code;

    private Map<String, String> map_code_name;

    private List<String> values;

    private CodeNameRelation() {
        if (map_name_code == null) {
            map_name_code = generateCodeNameRelation(9, 8);
        }
        if (map_code_name == null) {
            map_code_name = generateCodeNameRelation(8, 9);
        }
        if (values == null) {
            values = generateAllValues();
        }
    }

    public static final CodeNameRelation getInstance() {
        return SingletionHolder.codeNameRelation;
    }

    public Map<String, String> getNameCodeRelation() {
        return map_code_name;
    }

    public Map<String, String> getCodeNameRelation() {
        return map_name_code;
    }

    public List<String> getValues() {
        return values;
    }

    private List<String> generateAllValues() {
        Set<String> keySet = map_name_code.keySet();
        List<String> values = new ArrayList<>();
        for (Iterator it = keySet.iterator(); it.hasNext(); ) {
            String s = (String) it.next();
            values.add(map_name_code.get(s));
        }
        return values;
    }

    /**
     * 得到股票代码和股票名称的映射关系
     *
     * @param mapFirst  对应map的key
     * @param mapSecond 对应map的value
     * @return 对应映射
     */
    private Map<String, String> generateCodeNameRelation(int mapFirst, int mapSecond) {
        Map<String, String> map = new HashMap<>();

        // 查询的根目录
        String folder = System.getProperty("user.dir") + "/datasource/stocks/";

        // 使用Path封装
        Path path = Paths.get(folder);

        // 储存已经遍历过的文件
        List<File> files = new ArrayList<>();

        SimpleFileVisitor<Path> finder = new SimpleFileVisitor<Path>() {
            @Override
            // 访问单个文件
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                files.add(file.toFile());
                return super.visitFile(file, attrs);
            }
        };

        // 遍历文件树
        try {
            java.nio.file.Files.walkFileTree(path, finder);
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        try {
            for (int i = 0; i < files.size(); i++) {
                // 读出所有以".txt"结尾的文件（因为MacOS下会有一个额外的文件
                if (files.get(i).getAbsolutePath().endsWith(".txt")) {
                    inputStream = new BufferedInputStream(new FileInputStream(files.get(i).getAbsolutePath()));
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    String line = bufferedReader.readLine();

                    String[] attris = line.split(";");

                    //加入到map中
                    if (!map.containsKey(attris[mapFirst])) {
                        map.put(attris[mapFirst], attris[mapSecond]);
                    }
                }
                if (inputStream == null) {
                    continue;
                }
                inputStream.close();
                bufferedReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
