package build_tree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Function<Node, Long> genIdF = node -> node.getId();
        Function<Node, Long> genPidF = node -> node.getPId();
        Comparator<Node> comparing = Comparator.comparing(Node::getSort, Comparator.nullsLast(Integer::compareTo));
        BiConsumer<Node, List<Node>> setSubsF = (node, subs) -> node.setSubs(subs);

        List<Node> result = TreeGenerateUtil.buildTree(genList(), 0L, genIdF, genPidF, setSubsF);
        System.out.println(result);
        String str = result.toString();

        List<Node> resul2 = TreeGenerateUtil.buildTreeAndSorted(genList(), 0L, genIdF, genPidF, setSubsF, comparing);
        System.out.println(resul2);
        String str2 = resul2.toString();

        System.out.println("排序后，生成结果是否一样： " + str.equals(str2));
    }


    private static List<Node> genList() {
        List<Node> list = Arrays.asList(
                new Node() {{
                    setPId(0L);
                    setId(1L);
                    setName("name");
                }}, new Node() {{
                    setPId(1L);
                    setId(2L);
                    setSort(3);
                    setName("name-1");
                }},
                new Node() {{
                    setPId(2L);
                    setId(4L);
                    setName("name-1");
                }},
                new Node() {{
                    setPId(1L);
                    setId(3L);
                    setSort(2);
                    setName("name-2");
                }});
        return list;
    }
}
