package easy_excel;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * excel读取工具类
 *
 * @author aseem.chen
 * @since 2022-03-31
 */
public class EcoflowExcelReadUtil {

    /**
     * 一次性读取一个sheet并处理
     *
     * @param inputStream   文件
     * @param handler       处理读取数据的类，例如：new Service()
     * @param model         模型类，例如：new User()
     * @param handleF       处理方法，例如：BiConsumer<Service, List<User>> handleF = (service, users) -> service.save(users);
     * @param sheetIndexArr sheet的index素组，例如：Integer[] sheetIndexA = {0, 1};
     * @param headRowNumber 表头是第几行，默认1
     * @param <S>
     * @param <T>
     */
    public static <S, T> void repeatedReadExcel(InputStream inputStream, S handler, T model, BiConsumer<S, List<T>> handleF, Integer[] sheetIndexArr, Integer headRowNumber) {
        // 参数校验
        if (ArrayUtil.isEmpty(sheetIndexArr)) {
            return;
        }
        if (headRowNumber == null || headRowNumber <= 0) {
            headRowNumber = 1;
        }

        // 按照sheet的索引素组，读取sheet
        ExcelReader excelReader = EasyExcel.read(inputStream).build();

        List<ReadSheet> readSheets = new ArrayList<>();
        for (int i = 0; i < sheetIndexArr.length; i++) {
            ExcelDataListener readListener = new ExcelDataListener<>(handler, handleF);

            ReadSheet temp = EasyExcel.readSheet(sheetIndexArr[i]).head(model.getClass()).registerReadListener(readListener).headRowNumber(headRowNumber).build();
            readSheets.add(temp);
        }
        excelReader.read(readSheets);

        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();
    }

    /**
     * 对于每个sheet，按指定数量批量读取。
     *
     * @param inputStream   文件
     * @param handler       处理读取数据vv 的类，例如：new Service()
     * @param model         模型类，例如：new User()
     * @param handleF       处理方法，例如：BiConsumer<Service, List<User>> handleF = (service, users) -> service.save(users);
     * @param sheetIndexArr sheet的index素组，例如：Integer[] sheetIndexA = {0, 1};
     * @param batchCount    每次批量读取数量，默认100
     * @param headRowNumber 表头是第几行，默认1
     * @param <S>
     * @param <T>
     */
    public static <S, T> void batchRepeatedReadExcel(InputStream inputStream, S handler, T model, BiConsumer<S, List<T>> handleF, Integer[] sheetIndexArr, Integer headRowNumber, Integer batchCount) {
        // 参数校验
        if (ArrayUtil.isEmpty(sheetIndexArr)) {
            return;
        }
        if (headRowNumber == null || headRowNumber <= 0) {
            headRowNumber = 1;
        }

        // 按照sheet的索引素组，读取sheet
        ExcelReader excelReader = EasyExcel.read(inputStream).build();

        List<ReadSheet> readSheets = new ArrayList<>();
        for (int i = 0; i < sheetIndexArr.length; i++) {
            ExcelDataListener readListener = new ExcelDataListener<>(handler, handleF, batchCount);

            ReadSheet temp = EasyExcel.readSheet(sheetIndexArr[i]).head(model.getClass()).registerReadListener(readListener).headRowNumber(headRowNumber).build();
            readSheets.add(temp);
        }
        excelReader.read(readSheets);

        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();
    }
}
