package generictree.impl;

import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.IGTreeTask;
import generictree.task.TreeTaskDisp;
import generictree.task.TreeTaskNegate;
import generictree.task.TreeTaskUnwrap;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;

import java.util.ArrayList;

public class SplitTree <T> extends GTreeBase <T> {
    private static final char AND = '&';
    private static final char OR = '|';
    private static final char NEGATE_SYMBOL = '!';
    private static final char WRAP_SYMBOL_OPEN = '(';
    private static final char WRAP_SYMBOL_CLOSE = ')';
    private final ITokenizer tokenizer;

    private final IGTreeTask<T> taskNegate;
    private final IGTreeTask<T> taskUnwrap;

    public SplitTree() {
        taskNegate = new TreeTaskNegate<>(NEGATE_SYMBOL);
        taskUnwrap = new TreeTaskUnwrap<>(WRAP_SYMBOL_OPEN, WRAP_SYMBOL_CLOSE);
        tokenizer = Tokenizer.builder().skipSymbols(WRAP_SYMBOL_OPEN +"'").keepSkipSymbol().build();
    }

    @Override
    public boolean put(String path, T payload) {
        root = new GTreeNode<>();
        root.setLevel(0);
        root.setIdentifier(path);
        root.setPayload(payload);
        System.out.println("root: " + root.csvString());
        boolean more;
        do{
            tokenizer.changeDelimiter(AND);
            more = this.split(root, AND);

            tokenizer.changeDelimiter(OR);
            more |= this.split(root, OR);

            more |= parseObject.preOrder(root, taskNegate);
            more |= parseObject.preOrder(root, taskUnwrap);
        }
        while(more);

        return false;
    }
    private boolean split(IGTreeNode<T> currNode, char delim) {
        //System.out.println("currNode: " + currNode.csvString());
        if(currNode.isLeaf()){
            String identifier = currNode.identifier();
            String[] tokens = tokenizer.parse(identifier).getArray();
            if(tokens.length > 1){
                currNode.setIdentifier("");
                currNode.setOp(delim);
                for (String token : tokens) {
                    System.out.println("token: " + token);
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
    /*
        boolean more;
        do{
            more = root.split(AND);
            more |= root.split(OR);
            more |= root.negate();
            more |= root.unwrap(OPAR.asChar, CPAR.asChar);
            more |= root.unquote(SQUOTE.asChar);
        }
        while(more);
    * */
}
