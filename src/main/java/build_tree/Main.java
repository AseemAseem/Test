package build_tree;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Set<Long> set = Collections.emptySet();
        Set<Object> xxx = new HashSet<>();
        xxx.addAll(set);
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
