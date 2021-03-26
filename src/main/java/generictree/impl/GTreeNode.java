package generictree.impl;

import generictree.iface.IGTreeNode;

import java.util.ArrayList;

public class GTreeNode<T> implements IGTreeNode<T> {
    private static final String NULL_STRING = "-";
    private static final String CSV_FORMAT = "%d,%d,%d,%s,%s";//level, isLeaf, children.size, identifier, payload.toString
    private final ArrayList<IGTreeNode<T>> children;
    private String identifier;
    private int level;
    private T payload;

    public GTreeNode(int level, String identifier, T payload){
        this.level = level;
        this.identifier = identifier;
        this.payload = payload;
        children = new ArrayList<>();
    }

    @Override
    public String identifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean is(String identifier) {
        return (this.identifier != null && this.identifier.equals(identifier));
    }

    @Override
    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public T getPayload() {
        return payload;
    }


    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int level(int level) {
        return level;
    }


    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public ArrayList<IGTreeNode<T>> getChildren() {
        return children;
    }

    @Override
    public void addChild(IGTreeNode<T> child) {
        children.add(child);
    }

    @Override
    public void addChild(String identifier, T payload) {
        children.add(new GTreeNode<>(level + 1, identifier, payload));
    }

    @Override
    public String csvString() {
        return String.format(CSV_FORMAT, //level, isLeaf, children.size, identifier, payload.toString
            level,
            (this.isLeaf()? 1 : 0),
            children.size(),
            identifier,
            ((payload == null)? NULL_STRING : payload.toString())
        );
    }


}
