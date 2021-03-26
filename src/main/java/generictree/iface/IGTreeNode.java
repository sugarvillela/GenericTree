package generictree.iface;

import java.util.ArrayList;

public interface IGTreeNode <T> {
    String identifier();
    void setIdentifier(String identifier);
    boolean is(String identifier);

    void setPayload(T payload);
    T getPayload();

    void setLevel(int level);
    int level(int level);

    boolean isLeaf();
    ArrayList<IGTreeNode <T>> getChildren();

    void addChild(IGTreeNode <T> child);

    void addChild(String identifier, T payload);// makes impl type independent of interface

    String csvString();
}
