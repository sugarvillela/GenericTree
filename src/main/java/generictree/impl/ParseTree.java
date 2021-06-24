package generictree.impl;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.IGTreeTask;
import generictree.ifacetree.IParseTree;
import generictree.parse.GTreeParse;
import generictree.task.TaskNegate;
import generictree.task.TaskToList;
import generictree.task.TaskUnwrap;
import generictree.node.ParseTreeNode;
import tokenizer.composite.CharTok;
import tokenizer.iface.IStringParser;

import java.util.ArrayList;
import java.util.List;

/** For cases where a delimiter-separated string will be split
 *  into a tree, e.g. a&b&!(b|c)
 *
 * @param <T> the IGTreeNode payload type
 */
public class ParseTree<T> implements IParseTree<T> {
    private static final String AND = "&";
    private static final String OR = "|";
    private static final char NEGATE_SYMBOL = '!';
    private static final char WRAP_SYMBOL_OPEN = '(';
    private static final char WRAP_SYMBOL_CLOSE = ')';
    private final IStringParser tokenizer;

    private final IGTreeTask<T> taskNegate;
    private final IGTreeTask<T> taskUnwrap;

    protected IGTreeParse<T> parseObject;
    protected IGTreeNode<T> root;

    public ParseTree() {
        taskNegate = new TaskNegate<>(NEGATE_SYMBOL);
        taskUnwrap = new TaskUnwrap<>(WRAP_SYMBOL_OPEN, WRAP_SYMBOL_CLOSE);
        tokenizer = new CharTok().setSkipSymbols(WRAP_SYMBOL_OPEN +"'");
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
    public IGTreeNode<T> add(T payload, String parseString) {
        root = new ParseTreeNode<>();
        root.setLevel(0);
        root.setIdentifier(parseString);
        root.setPayload(payload);
        //System.out.println("root: " + root.csvString());
        boolean more;
        do{
            tokenizer.setDelimiter(AND);
            more = this.split(root, AND.charAt(0));

            tokenizer.setDelimiter(OR);
            more |= this.split(root, OR.charAt(0));

            more |= this.getParse().preOrder(root, taskNegate);
            more |= this.getParse().preOrder(root, taskUnwrap);
        }
        while(more);

        return root;
    }

    private boolean split(IGTreeNode<T> currNode, char delim) {
        if(currNode.isLeaf()){
            String identifier = currNode.getIdentifier();
            String[] tokens = tokenizer.setText(identifier).parse().toArray();
            if(tokens.length > 1){
                currNode.setIdentifier("");
                currNode.setOp(delim);
                for (String token : tokens) {
                    //System.out.println("token: " + token);
                    currNode.addChild(token, null);
                }
                return true;
            }
        }
        else{
            boolean more = false;
            for (IGTreeNode<T>  child : currNode.getChildren()) {
                more |= this.split(child, delim);
            }
            return more;
        }
        return false;
    }

    @Override
    public String toString(){
        return unParse(root);
    }

    private String unParse(IGTreeNode<T> currNode){
        String format = currNode.isWrapped()? "%s(%s)" : "%s%s";
        String negateSymbol = currNode.isNegated()? String.valueOf(NEGATE_SYMBOL) : "";

        if(currNode.isLeaf()){
            return String.format(format, negateSymbol, currNode.getIdentifier());
        }
        else{
            String op = String.format(" %c%c ", currNode.getOp(), currNode.getOp());
            ArrayList<String> childrenToList = new ArrayList<>();
            for(IGTreeNode<T> child : currNode.getChildren()){
                childrenToList.add(unParse(child));
            }

            return String.format(format, negateSymbol, String.join(op, childrenToList));
        }
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
