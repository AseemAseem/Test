package easy_excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.function.BiConsumer;

public class Main {

    public static void main(String[] args) {
        // 一次性读取
        readExcel();
        // 批量读取
        batchReadExcel();
    }

    private static void batchReadExcel() {
        System.out.println("开始处理excel");
        long begin = System.currentTimeMillis();

        DemoService demoService = new DemoService();
        DemoModel demoModel = new DemoModel();
        BiConsumer<DemoService, List<DemoModel>> handleF = (dao, datas) -> dao.save(datas);

        File file = new File("C:\\Users\\aseem.chen\\Desktop\\test.xlsx");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            Integer[] sheetIndexA = {0, 1};

            int batchCount = 10;
            System.out.println("----批量读取，每次 " + batchCount + " 条");
            EcoflowExcelReadUtil.batchRepeatedReadExcel(fileInputStream, demoService, demoModel, handleF, sheetIndexA, 5, batchCount);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("处理excel结束，耗时：" + (System.currentTimeMillis() - begin) / 1000.000 + " 秒");
        System.out.println("文件不需要请删除");
    }

    private static void readExcel() {
        System.out.println("开始处理excel");
        long begin = System.currentTimeMillis();

        DemoService demoService = new DemoService();
        DemoModel demoModel = new DemoModel();
        BiConsumer<DemoService, List<DemoModel>> handleF = (dao, datas) -> dao.save(datas);

        File file = new File("C:\\Users\\aseem.chen\\Desktop\\test.xlsx");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            Integer[] sheetIndexA = {0, 1};

            // 批量读取
            System.out.println("----一次性读取");
            EcoflowExcelReadUtil.repeatedReadExcel(fileInputStream, demoService, demoModel, handleF, sheetIndexA, 5);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("处理excel结束，耗时：" + (System.currentTimeMillis() - begin) / 1000.000 + " 秒");
        System.out.println("文件不需要请删除");
    }
}
