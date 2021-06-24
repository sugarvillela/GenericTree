package generictree.ifacetree;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;

public interface IParseTree<T> {
    IGTreeNode<T> getRoot();

    IGTreeNode <T> add(T payload, String parseString);

    IGTreeParse<T> getParse();

    void clear();
}
