package easy_excel;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * excel读取Listener
 * <p>
 * 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 *
 * @author aseem.chen
 * @since 2022-03-31
 */
public class ExcelDataListener<S, T> extends AnalysisEventListener<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDataListener.class);

    // 是否批量读取。否则一次性读取
    private Boolean ifBatch = false;
    private Integer batchCount = 100;
    private List<T> list = new ArrayList<>();

    private S s;
    private BiConsumer<S, List<T>> handleF;

    /**
     * 一次性读取一个sheet并处理
     *
     * @param s
     */
    public ExcelDataListener(S s, BiConsumer<S, List<T>> handleF) {
        this.s = s;
        this.handleF = handleF;
        // 一次性读取
        this.ifBatch = false;
    }

    /**
     * 批量处理，默认每批100条
     *
     * @param s
     * @param batchCount 每次批量处理条数
     */
    public ExcelDataListener(S s, BiConsumer<S, List<T>> handleF, Integer batchCount) {
        this.s = s;
        this.handleF = handleF;
        // 批量读取
        this.ifBatch = true;
        if (batchCount != null && batchCount > 0) {
            this.batchCount = batchCount;
            this.ifBatch = true;
        } else {
            this.batchCount = 100;
        }
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        if (BeanUtil.isEmpty(data)) {
            return;
        }
        list.add(data);
        // 达到批量处理数量了则处理，防止数据几万条数据在内存且需要去存储一次数据库，容易OOM
        if (ifBatch && list.size() >= batchCount) {
            handleData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        handleData();
        LOGGER.info("所有数据解析完成！");
    }

    /**
     * 处理数据
     */
    private void handleData() {
        handleF.accept(s, list);
    }
}