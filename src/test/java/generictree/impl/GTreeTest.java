package generictree.impl;

import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeTask;
import generictree.task.TreeTaskDisp;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class GTreeTest {

    @Test
    void add() {
        IGTree<String> tree = new GTree<>('-');
        tree.put("id0", "payload0");
        tree.put("id0-id01", "payload1");
        tree.put("id0-id01-id02", "payload2");
        tree.put("id0-id01-id02-id03a", "payload3a");
        tree.put("id0-id01-id02-id03b", "payload3b");
        tree.put("id0-id_b", "payloadb");
        tree.put("id0-id_c0", "payloadc0");
        tree.put("id0-id_b-id_c1", "payloadc1");
        ArrayList<IGTreeNode<String>> depthFirst = tree.getParse().toListDepthFirst(tree.getRoot(), null);
        for(IGTreeNode<String> node : depthFirst){
            System.out.println(node.csvString());
        }

        System.out.println("======");
        ArrayList<IGTreeNode<String>> leaves = tree.getParse().toListLeaves(tree.getRoot(), null);
        for(IGTreeNode<String> node : leaves){
            System.out.println(node.csvString());
        }

        System.out.println("======");
        IGTreeTask<String> task = new TreeTaskDisp<>();
        tree.getParse().preOrder(tree.getRoot(), task);

        System.out.println("======");
        IGTreeNode<String> found;
        if((found = tree.getParse().find(tree.getRoot(), "id_c1")) != null){
            System.out.println("found: " + found.csvString());
        }
        else{
            System.out.println("======");
        }

    }
}