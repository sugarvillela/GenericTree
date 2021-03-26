package generictree.task;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeTask;

public class TreeTaskDisp<T> implements IGTreeTask<T> {
    @Override
    public boolean doTask(IGTreeNode<T> node) {
        System.out.println(node.csvString());
        return false;
    }
}
