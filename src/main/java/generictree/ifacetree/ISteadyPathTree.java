package generictree.ifacetree;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;

import java.util.List;

/**Follows IGTree pattern but adds ability to remember its path.
 * Call addBranch or addLeaf with only the identifier, not the whole path.
 * Current path is saved locally.
 *
 * Tree navigation is done using pathBack-prefixed methods.
 * You can also set restore points and return to them in FIFO order.
 *
 * addLeaf includes a pathBack call, to keep you at the same path point
 * addBranch adds the current identifier to the path, taking you one level
 * deeper into the tree
 */
public interface ISteadyPathTree<T> {
    IGTreeNode<T> getRoot();

    // add to structure
    IGTreeNode <T> addBranch(T payload, String identifier);
    IGTreeNode <T> addLeaf(T payload, String identifier);
    void clear();

    void display();

    IGTreeParse<T> getParse();

    // navigation
    void pathClear();
    void pathBackTo(int newLast);
    void pathBack();
    void pathBack(int n);
    void pathBack(String identifier);
    void setRestore();
    void restore();

    List<String> pathAsList();
    String[] pathAsArray();
    String pathAsString();
}
