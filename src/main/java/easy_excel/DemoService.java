package easy_excel;

import java.util.List;

/**
 * 假设这个是你的DAO存储。当然还要这个类让spring管理，当然你不用需要存储，也不需要这个类。
 **/
public class DemoService {

    public void save(List<DemoModel> list) {
        // 如果是mybatis,尽量别直接调用多次insert,自己写一个mapper里面新增一个方法batchInsert,所有数据一次性插入
        System.out.println("----接收数据成功，共" + list.size() + "条");
//        System.out.println("其中第一天数据是： " + list.get(0));
    }
}