package generictree.impl;

import generictree.iface.IGTreeNode;

public class PathTree <T> extends GTreeBase <T> {
    private final String splitChar;

    public PathTree(char splitChar) {
        this.splitChar = String.valueOf(splitChar);
    }

    @Override
    public boolean put(String path, T payload) {
        if(root == null){
            root = new GTreeNode<>();
            root.setLevel(0);
            root.setIdentifier(path);
            root.setPayload(payload);
            return true;
        }
        else{
            String[] tok = path.split(splitChar);
            return parseObject.put(payload, 0, root, tok);
        }
    }
}
