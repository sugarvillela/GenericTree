package generictree.impl;

import generictree.iface.IGTreeNode;
import generictree.ifacetree.IParseTree;
import generictree.task.TaskToList;
import langdef.LangConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class ParseTreeTest {
    private IParseTree<String> mockParseTree(){
        IParseTree<String> tree = new ParseTree<>();
        tree.add(null, "zero|!(one&two)|!three");
        return tree;
    }

    @Test
    void givenMockParseTree_getBreadthFirstList() {
        IParseTree<String> tree = mockParseTree();
        ArrayList<String> actualList = new ArrayList<>();
        ArrayList<IGTreeNode<String>> list = new ArrayList<>();
        tree.getParse().breadthFirst(tree.getRoot(), new TaskToList<>(list));
        for(IGTreeNode<String> node : list){
            actualList.add(node.friendlyString());
        }
        String expected = "(level: 0), bran, (children: 3), (op: |), (negated: -), (id: root), (parent: -), -|" +
                "(level: 1), leaf, (children: 0), (op: -), (negated: -), (id: zero), (parent: root), -|" +
                "(level: 1), bran, (children: 2), (op: &), (negated: !), (id: &), (parent: root), -|" +
                "(level: 1), leaf, (children: 0), (op: -), (negated: !), (id: three), (parent: root), -|" +
                "(level: 2), leaf, (children: 0), (op: -), (negated: -), (id: one), (parent: &), -|" +
                "(level: 2), leaf, (children: 0), (op: -), (negated: -), (id: two), (parent: &), -";
        String actual = String.join("|", actualList);
        System.out.println(tree.toString());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenMockParseTree_getAllPaths() {
        IParseTree<String> tree = mockParseTree();
        List<String> actualList = tree.getParse().getAllPaths(tree.getRoot(), LangConstants.PATH_TREE_SEP);
        String expected = ".zero|..one|..two|.three";
        String actual = String.join("|", actualList);
        System.out.println(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenMockParseTree_unParseToString() {
        IParseTree<String> tree = mockParseTree();
        String expected = "zero || !(one && two) || !three";
        String actual = tree.toString();
        System.out.println(tree.toString());
        Assertions.assertEquals(expected, actual);
    }
}
