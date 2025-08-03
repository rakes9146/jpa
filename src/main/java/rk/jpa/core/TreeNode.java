package rk.jpa.core;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TreeNode {

    String name;
    Map<String, String> childKeyValMap = new HashMap<>();
    TreeMap<String, TreeNode> children = new TreeMap<>();

    public TreeNode(String name,  Map<String, String> childKeyValMap ) {
        this.name = name;
        this.childKeyValMap = childKeyValMap;
    }

    public void addChild(String name, Map<String, String> childKeyValMap) {

        children.putIfAbsent(name, new TreeNode(name, childKeyValMap));
    }

    public TreeNode getChildName(String childName) {
        return children.get(childName);
    }
}
