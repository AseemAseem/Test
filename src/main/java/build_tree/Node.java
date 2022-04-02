package build_tree;

import lombok.Data;

import java.util.List;

@Data
public class Node {
    private Long id;
    private Long pId;
    private Integer sort;
    private String name;

    private List<Node> subs;
}