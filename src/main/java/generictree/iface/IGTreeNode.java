package generictree.iface;

import iface_global.ICsv;

import java.util.ArrayList;

/*
is
getChildren
isLeaf
getChildren
addChild(level, payload)
identifier
parent
---
csvString
setNegated
setWrapped
* */
public interface IGTreeNode <T> extends ICsv {
    void setIdentifier(String identifier);
    String getIdentifier();

    void setPayload(T payload);
    T getPayload();

    void setLevel(int level);
    int getLevel();

    void setParent(IGTreeNode <T> parent);
    IGTreeNode<T> getParent();

    boolean is(String identifier);
    boolean isRoot();
    boolean isLeaf();

    ArrayList<IGTreeNode <T>> getChildren();

    IGTreeNode <T> addChild(IGTreeNode <T> child);
    IGTreeNode <T> addChild(String identifier, T payload);// add child of same subtype

    /*=====Accessors specific to ParseTree tasks======================================================================*/

    void setOp(char op);
    char getOp();

    void setNegated(boolean negated);
    boolean isNegated();

    void setWrapped(boolean wrapped);
    boolean isWrapped();

    /*=====Extended from ICsv=========================================================================================*/

    //String friendlyString();
    //String csvString();
}
