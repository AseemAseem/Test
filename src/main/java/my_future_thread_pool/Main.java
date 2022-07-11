package my_future_thread_pool;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {

    private static final int THREAD_COUNT = 20;
    private static final ThreadPoolFutureUtil THREAD_POOL = new ThreadPoolFutureUtil("ef-jpush-thread", THREAD_COUNT);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 任务数
        int allTaskSize = 10;

        // 每次多线程运行的个数
        int subNum = 2;
        // 当前任务位置标记
        int len = 0;

        while (len < allTaskSize) {
            long startTime = System.currentTimeMillis();

            for (int i = len; i < len + subNum; i++) {
                int finalI = i;
                THREAD_POOL.execute(() -> {
                    FutureResultDto result = new FutureResultDto("任务 " + finalI + "，返回的结果");
                    Thread.sleep(1000);
                    return result;
                });
            }

            List<FutureResultDto> efThreadPoolResultDtos = THREAD_POOL.waitResult();


            efThreadPoolResultDtos.stream().forEach(e -> {
                System.out.println(e.getMsg());
            });
            System.out.println("第" + len + "个至第" + (len + subNum - 1) + "个任务已执行完");
            len += subNum;
        }
        System.out.println("所有任务已执行完");
    }
}
