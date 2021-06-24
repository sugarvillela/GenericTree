package generictree.impl;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.IGTreeTask;
import generictree.ifacetree.IPathTree;
import generictree.node.ParseTreeNode;
import generictree.parse.GTreeParse;
import generictree.task.TaskToList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** For cases where the path to an element is known.
 *  Path is a 'splitChar' separated string that corresponds to
 *  the node identifiers on the path to the element (see tests)
 * @param <T> the IGTreeNode payload type
 */
public class PathTree <T> implements IPathTree<T> {
    private final char splitChar;
    protected IGTreeParse<T> parseObject;
    protected IGTreeNode<T> root;

    public PathTree(char splitChar) {
        this.splitChar = splitChar;
    }

    @Override
    public IGTreeNode<T> getRoot() {
        return root;
    }

    @Override
    public IGTreeParse<T> getParse() {
        return (parseObject == null)? (parseObject = new GTreeParse<>()) : parseObject;
    }

    @Override
    public IGTreeNode<T> add(T payload, String... path) {
        if(path.length == 1 && path[0].indexOf(splitChar) != -1){
            path = tokenizePathOnSingle(splitChar, path);
        }
        if(root == null){
            root = new ParseTreeNode<>();
            root.setLevel(0);
            root.setIdentifier(path[0]);
            root.setPayload(payload);
            return root;
        }
        else{
            return this.getParse().addByPath(payload, root, path);
        }
    }

    @Override
    public IGTreeNode<T> add(T payload, List<String> path) {
        if(path.size() == 1 && path.get(0).indexOf(splitChar) != -1){
            path = tokenizePathOnSingle(splitChar, path);
        }
        if(root == null){
            root = new ParseTreeNode<>();
            root.setLevel(0);
            root.setIdentifier(path.get(0));
            root.setPayload(payload);
            return root;
        }
        else{
            return this.getParse().addByPath(payload, root, path);
        }
    }



    private String[] tokenizePathOnSingle(char splitChar, String... path){
        return path[0].split("[" + splitChar + "]");
    }
    private List<String> tokenizePathOnSingle(char splitChar, List<String> path){
        return Arrays.asList(path.get(0).split("[" + splitChar + "]"));
    }


    @Override
    public void clear() {
        if(root != null){
            List<IGTreeNode<T>> list = new ArrayList<>();
            IGTreeTask<T> task = new TaskToList<T>(list);
            this.getParse().breadthFirst(this.getRoot(), task);
            for(int i = list.size() -1; i >= 0; i--){
                IGTreeNode<T> currNode = list.get(i);
                if(!currNode.isLeaf()){
                    currNode.getChildren().clear();
                }
            }
            root = null;
        }
    }
}
