package generictree.node;

import generictree.iface.IGTreeNode;

public class PathTreeNode<T> extends GTreeNodeBase<T> {
    //level, isLeaf, children.size, identifier, parent.identifier, payload.toString
    private static final String FRIENDLY_FORMAT = "%s, %s, %s, %s, %s, %s";
    //level, isLeaf, children.size, identifier, payload.toString
    private static final String CSV_FORMAT = "%d,%d,%d,%s,%s";

    @Override
    protected IGTreeNode<T> newImplTypeNode() {
        return new PathTreeNode<>();
    }

    @Override
    public String friendlyString() {
        //level, isLeaf, children.size, identifier, parent.identifier, payload.toString
        return String.format(FRIENDLY_FORMAT,
                String.format("(level: %d)", level),
                (this.isLeaf()? "leaf" : "bran"),
                String.format("(children: %d)", children.size()),
                String.format("(id: %s)", this.friendlySelfInfoString()),
                String.format("(parent: %s)", this.friendlyParentInfoString()),
                ((payload == null)? NULL_STRING : payload.toString())
        );
    }

    @Override
    public String csvString() {
        //level, isLeaf, children.size, identifier, payload.toString
        return String.format(CSV_FORMAT,
                level,
                (this.isLeaf()? 1 : 0),
                children.size(),
                (identifier.isEmpty())? NULL_STRING : identifier,
                ((payload == null)? NULL_STRING : payload.toString())
        );
    }

    private String friendlyParentInfoString(){
        if(parent != null){
            if(parent.getParent() == null){
                return "root";
            }
            if(parent.getIdentifier() != null && !parent.getIdentifier().isEmpty()){
                return parent.getIdentifier();
            }
        }
        return NULL_STRING;
    }
    private String friendlySelfInfoString(){
        if(identifier == null || identifier.isEmpty()){
            return NULL_STRING;
        }
        return identifier;
    }
}
