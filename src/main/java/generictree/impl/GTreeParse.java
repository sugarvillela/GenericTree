package generictree.impl;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.IGTreeTask;

import java.util.ArrayList;

public class GTreeParse<T> implements IGTreeParse<T> {
    @Override
    public IGTreeNode<T> find(IGTreeNode<T> root, String identifier) {
        if(root.is(identifier)){
            return root;
        }
        else{
            IGTreeNode<T> foundNode = null;
            for(IGTreeNode<T> child : root.getChildren()){
                if((foundNode = find(child, identifier)) != null){
                    return foundNode;
                }
            }
        }
        return null;
    }

    @Override
    public boolean put(T payload, int level, IGTreeNode<T> root, String... path) {
        if(level < path.length - 1){
            if(level == path.length - 2){
                if(root.is(path[level])){
                    root.addChild(path[level + 1], payload);
                    return true;
                }
            }
            else{
                for(IGTreeNode<T> child : root.getChildren()){
                    if(child.is(path[level + 1])){
                        return put(payload, level + 1, child, path);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ArrayList<IGTreeNode<T>> toListDepthFirst(IGTreeNode<T> root, ArrayList<IGTreeNode<T>> list) {
        if(list == null){
            list = new ArrayList<>();
        }
        list.add(root);
        for(IGTreeNode<T> child : root.getChildren()){
            toListDepthFirst(child, list);
        }
        return list;
    }

    @Override
    public ArrayList<IGTreeNode<T>> toListLeaves(IGTreeNode<T> root, ArrayList<IGTreeNode<T>> list) {
        if(list == null){
            list = new ArrayList<>();
        }
        if(root.isLeaf()){
            list.add(root);
        }
        else{
            for(IGTreeNode<T> child : root.getChildren()){
                toListLeaves(child, list);
            }
        }
        return list;
    }

    @Override
    public boolean preOrder(IGTreeNode<T> root, IGTreeTask task) {
        boolean result = task.doTask(root);
        for(IGTreeNode<T> child : root.getChildren()){
            result |= preOrder(child, task);
        }
        return result;
    }

    @Override
    public boolean postOrder(IGTreeNode<T> root, IGTreeTask task) {
        boolean result = false;
        for(IGTreeNode<T> child : root.getChildren()){
            result |= preOrder(child, task);
        }
        result |= task.doTask(root);
        return result;
    }
}
