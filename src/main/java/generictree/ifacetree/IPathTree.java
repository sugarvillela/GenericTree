package generictree.ifacetree;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import java.util.List;

public interface IPathTree<T>{
    IGTreeNode<T> getRoot();

    IGTreeNode <T> add(T payload, String... path);
    IGTreeNode <T> add(T payload, List<String> path);

    IGTreeParse<T> getParse();

    void clear();
}
