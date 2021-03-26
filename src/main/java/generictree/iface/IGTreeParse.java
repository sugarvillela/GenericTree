package generictree.iface;

import java.util.ArrayList;

public interface IGTreeParse <T> {
    IGTreeNode<T> find(IGTreeNode<T> root, String identifier);

    boolean put(T payload, int level, IGTreeNode<T> root, String... path);

    ArrayList<IGTreeNode<T>> toListDepthFirst(IGTreeNode<T> root, ArrayList<IGTreeNode<T>> list);
    ArrayList<IGTreeNode<T>> toListLeaves(IGTreeNode<T> root, ArrayList<IGTreeNode<T>> list);

    boolean preOrder(IGTreeNode<T> root, IGTreeTask task);
    boolean postOrder(IGTreeNode<T> root, IGTreeTask task);

}
