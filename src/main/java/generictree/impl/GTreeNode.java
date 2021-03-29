package generictree.impl;

import generictree.iface.IGTreeNode;

import java.util.ArrayList;

public class GTreeNode<T> implements IGTreeNode<T> {
    private static final String NULL_STRING = "-";

    //level, isLeaf(B or L), children.size, op, negated, identifier, parent.identifier, payload.toString
    private static final String FRIENDLY_FORMAT = "%s, %s, %s, %s, %s, %s, %s, %s";
    //level, isLeaf(B or L), children.size, op, negated, identifier, payload.toString
    private static final String CSV_FORMAT = "%d,%d,%d,%s,%s,%s,%s";

    private final ArrayList<IGTreeNode<T>> children;
    private IGTreeNode<T> parent;
    private String identifier;
    private int level;
    private char op;
    private boolean negated;
    private T payload;

    public GTreeNode(){//int level, String identifier, T payload
//        this.level = level;
//        this.identifier = identifier;
//        this.payload = payload;
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
    public int level() {
        return level;
    }

    @Override
    public void setParent(IGTreeNode<T> parent) {
        this.parent = parent;
    }

    @Override
    public IGTreeNode<T> parent() {
        return parent;
    }

    @Override
    public void setOp(char op) {
        this.op = op;
    }

    @Override
    public char op() {
        return op;
    }

    @Override
    public void setNegated(boolean negated) {
        this.negated = negated;
    }

    @Override
    public boolean negated() {
        return negated;
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
        child.setParent(this);
        child.setLevel(level + 1);
        children.add(child);
    }

    @Override
    public void addChild(String identifier, T payload) {
        IGTreeNode<T> child = new GTreeNode<>();
        child.setIdentifier(identifier);
        child.setPayload(payload);
        this.addChild(child);
    }

    @Override
    public void addChild(String identifier, char op, T payload) {
        IGTreeNode<T> child = new GTreeNode<>();
        child.setIdentifier(identifier);
        child.setOp(op);
        child.setPayload(payload);
        this.addChild(child);
    }

    private String friendlyParent(){
        if(parent == null){
            return NULL_STRING;
        }
        if(!parent.identifier().isEmpty()){
            return parent.identifier();
        }
        if(parent.op() != '\0'){
            return String.valueOf(parent.op());
        }
        return "root";
    }
    private String friendlySelf(){
        if(parent == null){
            return "root";
        }
        if(!identifier.isEmpty()){
            return identifier;
        }
        if(op != '\0'){
            return String.valueOf(op);
        }
        return NULL_STRING;
    }
    @Override
    public String friendlyString() {
        //level, leaf, children.size, op, negated, identifier, parent.identifier, payload.toString
        return String.format(FRIENDLY_FORMAT,
                String.format("(level: %d)", level),
                (this.isLeaf()? "leaf" : "bran"),
                String.format("(children: %d)", children.size()),
                String.format("(op: %s)", ((op == '\0')? NULL_STRING : String.valueOf(op))),
                String.format("(negated: %s)", (negated? "!" : NULL_STRING)),
                String.format("(id: %s)", this.friendlySelf()),
                String.format("(parent: %s)", this.friendlyParent()),
                ((payload == null)? NULL_STRING : payload.toString())
        );
    }

    @Override
    public String csvString() {
        //level, isLeaf(B or L), children.size, op, negated, identifier, parent.identifier, payload.toString
        return String.format(CSV_FORMAT,
            level,
            (this.isLeaf()? 1 : 0),
            children.size(),
            ((op == '\0')? NULL_STRING : String.valueOf(op)),
            (negated? "!" : NULL_STRING),
            (identifier.isEmpty())? NULL_STRING : identifier,
            ((payload == null)? NULL_STRING : payload.toString())
        );
    }


}
