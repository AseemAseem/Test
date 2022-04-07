package build_tree;


import cn.hutool.core.collection.CollectionUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用于构造树的工具类
 *
 * @author aseem.chen
 * @since 2022/4/2
 */
public class TreeGenerateUtil {

    private TreeGenerateUtil() {
    }

    /**
     * ----调用示例：
     * 节点类：
     *
     * @Data public class Tree {
     * private F id;
     * private F pId;
     * private F name;
     * <p>
     * private List<Tree> subs;
     * }
     * <p>
     * 传参，调用：
     * Function<Tree, F> genIdF = tree -> tree.getId(); // 获取id
     * Function<Tree, F> genPidF = tree -> tree.getPId(); // 获取父id
     * BiConsumer<Tree, List<Tree>> setSubsF = (tree, subs) -> tree.setSubs(subs); // 设置子节点列表
     * <p>
     * List<Tree> list = ; // 初始化所有节点列表。
     * 0L 最顶层节点id
     * List<Tree> result = TreeUtils.buildTree(list, 0L, genIdF, genPidF, setSubsF);
     */

    /**
     * 调用示例参考最上方注释
     *
     * @param list      所有节点列表
     * @param topNodeId 开始遍历的父节点id
     * @param getIdF    获取节点id的方法
     * @param getPidF   获取节点父id的方法
     * @param setSubsF  设置子节点列表的方法
     * @param <T>       节点类
     * @return
     */
    public static <T, F> List<T> buildTree(List<T> list, F topNodeId, Function<T, F> getIdF, Function<T, F> getPidF, BiConsumer<T, List<T>> setSubsF) {
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        Map<F, List<T>> pidMap = list.stream()
                .filter(sysCatalog -> getPidF.apply(sysCatalog) != null)
                .collect(Collectors.groupingBy(sysCatalog -> getPidF.apply(sysCatalog)));

        if (CollectionUtil.isEmpty(pidMap)) {
            return Collections.emptyList();
        }

        list.forEach(sysCatalog -> {
            F curNodeId = getIdF.apply(sysCatalog);
            List<T> subs = pidMap.get(curNodeId);
            setSubsF.accept(sysCatalog, subs);
        });

        List<T> result = pidMap.get(topNodeId);
        if (CollectionUtil.isEmpty(result)) {
            return Collections.emptyList();
        }
        return result;
    }

    /**
     * 调用示例参考最上方注释
     *
     * @param list      所有节点列表
     * @param topNodeId 开始遍历的父节点id
     * @param getIdF    获取节点id的方法
     * @param getPidF   获取节点父id的方法
     * @param setSubsF  设置子节点列表的方法
     * @param comparing 排序方法
     * @param <T>       节点类
     * @return
     */
    public static <T, F> List<T> buildTreeAndSorted(List<T> list, F topNodeId, Function<T, F> getIdF, Function<T, F> getPidF, BiConsumer<T, List<T>> setSubsF, Comparator<T> comparing) {
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        Map<F, List<T>> pidMap = list.stream()
                .filter(sysCatalog -> getPidF.apply(sysCatalog) != null)
                .collect(Collectors.groupingBy(sysCatalog -> getPidF.apply(sysCatalog)));

        if (CollectionUtil.isEmpty(pidMap)) {
            return Collections.emptyList();
        }

        list.forEach(sysCatalog -> {
            F curNodeId = getIdF.apply(sysCatalog);
            List<T> subs = pidMap.get(curNodeId);
            // 排序
            subs = doSort(subs, comparing);

            setSubsF.accept(sysCatalog, subs);
        });

        List<T> result = pidMap.get(topNodeId);
        if (CollectionUtil.isEmpty(result)) {
            return Collections.emptyList();
        }
        // 排序
        result = doSort(result, comparing);
        return result;
    }

    private static <T> List<T> doSort(List<T> list, Comparator<T> comparing) {
        if (CollectionUtil.isEmpty(list)) {
            return list;
        }
        return list.stream().sorted(comparing).collect(Collectors.toList());
    }
}