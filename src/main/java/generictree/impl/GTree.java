package generictree.impl;

import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;

public class GTree<T> implements IGTree<T> {

    private final IGTreeParse<T> parseObject;
    private final String splitChar;
    private IGTreeNode<T> root;

    public GTree(char splitChar) {
        this.splitChar = String.valueOf(splitChar);

        parseObject = new GTreeParse<>();
    }

    @Override
    public IGTreeNode<T> getRoot() {
        return root;
    }

    @Override
    public boolean put(String path) {
        return this.put(path, null);
    }

    @Override
    public boolean put(String path, T payload) {
        if(root == null){
            root = new GTreeNode<>(0, path, payload);
            return true;
        }
        else{
            String[] tok = path.split(splitChar);
            return parseObject.put(payload, 0, root, tok);
        }
    }

    @Override
    public IGTreeParse<T> getParse() {
        return parseObject;
    }
}
