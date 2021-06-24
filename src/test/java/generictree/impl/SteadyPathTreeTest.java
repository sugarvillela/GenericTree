package generictree.impl;

import generictree.ifacetree.ISteadyPathTree;
import langdef.LangConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class SteadyPathTreeTest {
    @Test
    void givenSteadyPathTree_setAndCallRestore(){
        ISteadyPathTree<String> tree = new SteadyPathTree<>('-');
        tree.addBranch("a", "a");
        tree.setRestore();
        tree.addBranch("b", "b");
        tree.addBranch("c", "c");
        tree.restore();
        tree.addBranch("d", "d");
        tree.addBranch("e", "e");

        List<String> actualList = tree.getParse().getAllPaths(tree.getRoot(), LangConstants.PATH_TREE_SEP);
        String expected = "a.b.c|a.d.e";
        String actual = String.join("|", actualList);
        System.out.println(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenSteadyPathTree_pathBackByNegativeIndex(){
        ISteadyPathTree<String> tree = new SteadyPathTree<>('-');
        tree.addBranch("a", "a");
        tree.addBranch("b", "b");
        tree.addBranch("c", "c");
        tree.pathBack(2);
        tree.addBranch("d", "d");
        tree.addBranch("e", "e");

        List<String> actualList = tree.getParse().getAllPaths(tree.getRoot(), LangConstants.PATH_TREE_SEP);
        String expected = "a.b.c|a.d.e";
        String actual = String.join("|", actualList);
        System.out.println(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenSteadyPathTree_pathBackByIdentifier(){
        ISteadyPathTree<String> tree = new SteadyPathTree<>('-');
        tree.addBranch("a", "a");
        tree.addBranch("b", "b");
        tree.addBranch("c", "c");
        tree.pathBack("a");
        tree.addBranch("d", "d");
        tree.addBranch("e", "e");

        List<String> actualList = tree.getParse().getAllPaths(tree.getRoot(), LangConstants.PATH_TREE_SEP);
        String expected = "a.b.c|a.d.e";
        String actual = String.join("|", actualList);
        System.out.println(actual);
        Assertions.assertEquals(expected, actual);
    }
}