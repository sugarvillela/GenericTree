package generictree.impl;

/** For cases where the path to an element is known.
 *  Path is a 'splitChar' separated string that corresponds to
 *  the node identifiers on the path to the element (see tests)
 * @param <T> the IGTreeNode payload type
 */
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
            String[] tok = path.split("[" + splitChar + "]");
            return parseObject.putByPath(payload, 0, root, tok);
        }
    }
}
