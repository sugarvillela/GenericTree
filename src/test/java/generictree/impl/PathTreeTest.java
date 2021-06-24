package generictree.impl;

import generictree.ifacetree.IParseTree;
import generictree.ifacetree.IPathTree;
import generictree.iface.IGTreeNode;
import generictree.task.TaskToList;
import langdef.LangConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

class PathTreeTest {
    private IPathTree<String> mockPathTree(){
        IPathTree<String> tree = new PathTree<>('-');
        tree.add("payload0", "id0");
        tree.add("payload1", "id0-id01");
        tree.add("payload2", "id0-id01-id02");
        tree.add("payload3", "id0-id01-id02-id03");
        tree.add("payload4", "id0-id01-id02-id03-id04");
        tree.add("payload3b","id0-id01-id02-id03b");
        tree.add("payloadb", "id0-idb");
        tree.add("payloadc", "id0-idc");
        tree.add("payloadd", "id0-idb-idd");
        return tree;
    }

    @Test
    void givenMockPathTree_toListBreadthFirst() {
        IPathTree<String> tree = mockPathTree();
        ArrayList<String> actualList = new ArrayList<>();
        ArrayList<IGTreeNode<String>> list = new ArrayList<>();
        tree.getParse().breadthFirst(tree.getRoot(), new TaskToList<>(list));
        for(IGTreeNode<String> node : list){
            actualList.add(node.friendlyString());
        }
        String expected =
            "(level: 0), bran, (children: 3), (op: -), (negated: -), (id: root), (parent: -), payload0|"+
            "(level: 1), bran, (children: 1), (op: -), (negated: -), (id: id01), (parent: root), payload1|"+
            "(level: 1), bran, (children: 1), (op: -), (negated: -), (id: idb), (parent: root), payloadb|"+
            "(level: 1), leaf, (children: 0), (op: -), (negated: -), (id: idc), (parent: root), payloadc|"+
            "(level: 2), bran, (children: 2), (op: -), (negated: -), (id: id02), (parent: id01), payload2|"+
            "(level: 2), leaf, (children: 0), (op: -), (negated: -), (id: idd), (parent: idb), payloadd|"+
            "(level: 3), bran, (children: 1), (op: -), (negated: -), (id: id03), (parent: id02), payload3|"+
            "(level: 3), leaf, (children: 0), (op: -), (negated: -), (id: id03b), (parent: id02), payload3b|"+
            "(level: 4), leaf, (children: 0), (op: -), (negated: -), (id: id04), (parent: id03), payload4";
        String actual = String.join("|", actualList);
        System.out.println(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenMockPathTree_getTreeNodeFromIdentifier() {
        IPathTree<String> tree = mockPathTree();
        String expected, actual;
        IGTreeNode<String> found = tree.getParse().treeNodeFromId(tree.getRoot(), "id02");
        expected = "(level: 2), bran, (children: 2), (op: -), (negated: -), (id: id02), (parent: id01), payload2";
        actual = (found == null)? "null" : found.friendlyString();
        System.out.println(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenMockPathTree_getTreeNodeFromPartialPath() {
        IPathTree<String> tree = mockPathTree();
        String expected, actual;
        IGTreeNode<String> found = tree.getParse().treeNodeFromPartialPath(tree.getRoot(), "id01", "id02", "id03");
        expected = "(level: 3), bran, (children: 1), (op: -), (negated: -), (id: id03), (parent: id02), payload3";
        actual = (found == null)? "null" : found.friendlyString();
        Assertions.assertEquals(expected, actual);

        found = tree.getParse().treeNodeFromPartialPath(tree.getRoot(), "id03");
        expected = "(level: 3), bran, (children: 1), (op: -), (negated: -), (id: id03), (parent: id02), payload3";
        actual = (found == null)? "null" : found.friendlyString();
        Assertions.assertEquals(expected, actual);

        found = tree.getParse().treeNodeFromPartialPath(tree.getRoot(), "id0", "id01", "id02", "id03", "id04");
        expected = "(level: 4), leaf, (children: 0), (op: -), (negated: -), (id: id04), (parent: id03), payload4";
        actual = (found == null)? "null" : found.friendlyString();
        Assertions.assertEquals(expected, actual);

        found = tree.getParse().treeNodeFromPartialPath(tree.getRoot(), "id04");
        expected = "(level: 4), leaf, (children: 0), (op: -), (negated: -), (id: id04), (parent: id03), payload4";
        actual = (found == null)? "null" : found.friendlyString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenMockPathTree_getIsPathToLeaf() {
        IPathTree<String> tree = mockPathTree();
        boolean actual;
        actual = tree.getParse().isPathToLeaf(tree.getRoot(), "id02");
        System.out.println(actual);
        Assertions.assertFalse(actual);

        actual = tree.getParse().isPathToLeaf(tree.getRoot(), "id04");
        System.out.println(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    void givenMockPathTree_getFullPathFromPartialPath() {
        IPathTree<String> tree = mockPathTree();
        String expected, actual;
        String[] path  = tree.getParse().pathFromPartialPath(tree.getRoot(), "id02", "id03");
        expected = "id0.id01.id02.id03";
        actual = (path == null)? "null" : String.join(".", path);
        Assertions.assertEquals(expected, actual);

        path  = tree.getParse().pathFromPartialPath(tree.getRoot(), "id04");
        expected = "id0.id01.id02.id03.id04";
        actual = (path == null)? "null" : String.join(".", path);
        Assertions.assertEquals(expected, actual);

        path  = tree.getParse().pathFromPartialPath(tree.getRoot(), "idb");
        expected = "id0.idb";
        actual = (path == null)? "null" : String.join(".", path);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenMockPathTree_getAllPaths() {
        IPathTree<String> tree = mockPathTree();
        List<String> actualList = tree.getParse().getAllPaths(tree.getRoot(), LangConstants.PATH_TREE_SEP);
        String expected =
            "id0.id01.id02.id03.id04|"+
            "id0.id01.id02.id03b|"+
            "id0.idb.idd|"+
            "id0.idc";
        String actual = String.join("|", actualList);
        System.out.println(actual);
        Assertions.assertEquals(expected, actual);
    }

}