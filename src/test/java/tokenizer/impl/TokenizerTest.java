package tokenizer.impl;

import org.junit.jupiter.api.Test;
import tokenizer.iface.ITokenizer;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {
    @Test
    void givenTooManyDelimiters_shouldTokenizeWithoutExtraElements() {
        String text = "Sentence__with_(too_many_'delims')__and_quotes__";
        String[] tok = new SimpleTok('_').parse(text).getArray();
        String unTok = String.join("|", tok);
        assertEquals("Sentence|with|(too|many|'delims')|and|quotes", unTok);
    }
    @Test
    void givenSkipArea_shouldNotTokenizeInSkipArea() {
        String text = "Sentence__with_(too_many_'delims')__and_quotes__";
        String[] tok = Tokenizer.builder().delimiters(" _").skipSymbols("('").build().parse(text).getArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|with|too_many_'delims'|and|quotes", unTok);
    }
    @Test
    void givenNestedSkipAreas_shouldHandleOuterSymbols() {
        String text = "Sentence__with_(too_many_'delims')__and_quotes__";
        String[] tok = Tokenizer.builder().delimiters(" _").skipSymbols("('").keepSkipSymbol().build().parse(text).getArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|with|(too_many_'delims')|and|quotes", unTok);
    }
    @Test
    void givenSequentialSkipAreas_shouldHandleBothSetsOfSymbols() {
        String text = "Sentence__with_(many_delims)__and_'stuff_in_quotes'";
        String[] tok = Tokenizer.builder().delimiters(" _").skipSymbols("('").build().parse(text).getArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|with|many_delims|and|stuff_in_quotes", unTok);
    }
    @Test
    void givenTokenizeDelimiter_shouldKeepAllDelimiter() {
        String text = "Sentence__with_(too_many_'delims')__and_quotes__";
        String[] tok = Tokenizer.builder().delimiters(" _").skipSymbols("('").tokenizeDelimiter().build().parse(text).getArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|_|_|with|_|too_many_'delims'|_|_|and|_|quotes|_|_", unTok);
    }
    @Test
    void givenTokenizeDelimiterOnce_shouldKeepDelimiterOnce() {
        String text = "Sentence__with_(too_many_'delims')__and_quotes__";
        String[] tok = Tokenizer.builder().delimiters(" _").skipSymbols("('").tokenizeDelimiterOnce().build().parse(text).getArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|_|with|_|too_many_'delims'|and|_|quotes|_", unTok);
    }
    @Test
    void givenKeepSkipsAndConnectedChar_leavesCharConnected() {
        String text = "Sentence__with_a(too_many_'delims')__and_quotes__";
        String[] tok = Tokenizer.builder().delimiters(" _").skipSymbols("('").keepSkipSymbol().build().parse(text).getArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|with|a(too_many_'delims')|and|quotes", unTok);
    }
    @Test
    void givenKeepSkipsAndConnectedCharAfter_leavesCharConnected() {
        String text = "Sentence__with_(too_many_'delims')a__and_quotes__";
        String[] tok = Tokenizer.builder().delimiters(" _").skipSymbols("('").keepSkipSymbol().build().parse(text).getArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|with|(too_many_'delims')a|and|quotes", unTok);
    }
    @Test
    void givenNoKeepSkipsAndConnectedChar_splitsOnChar() {
        String text = "Sentence__with_a(too_many_'delims')__and_quotes__";
        String[] tok = Tokenizer.builder().delimiters(" _").skipSymbols("('").build().parse(text).getArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|with|a|too_many_'delims'|and|quotes", unTok);
    }
    @Test
    void givenNoKeepSkipsAndConnectedCharAfter_splitsOnChar() {
        String text = "Sentence__with_(too_many_'delims')a__and_quotes__";
        String[] tok = Tokenizer.builder().delimiters(" _").skipSymbols("('").build().parse(text).getArray();

        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("Sentence|with|too_many_'delims'|a|and|quotes", unTok);
    }
    @Test
    void givenSublang_splitOnAssignedDelim() {
        char AND = '&', OR = '|';
        String text = "zero|one&two|three";
        ITokenizer tokenizer = Tokenizer.builder().skipSymbols("('").keepSkipSymbol().build();

        tokenizer.changeDelimiter(AND);
        String[] splitAnd = tokenizer.parse(text).getArray();

        String unSplitAnd = String.join("-", splitAnd);
        System.out.println(unSplitAnd);
        assertEquals("zero|one-two|three", unSplitAnd);

        tokenizer.changeDelimiter(OR);
        String[] splitOr = tokenizer.parse(splitAnd[0]).getArray();
        String unSplitOr = String.join("-", splitOr);
        System.out.println(unSplitOr);
        assertEquals("zero-one", unSplitOr);

        splitOr = tokenizer.parse(splitAnd[1]).getArray();
        unSplitOr = String.join("-", splitOr);
        System.out.println(unSplitOr);
        assertEquals("two-three", unSplitOr);
    }
    @Test
    void givenSublangKeepSkipsAndConnectedChar_leavesCharConnected() {
        char AND = '&', OR = '|';
        String text = "zero|!(one&two)|three";
        ITokenizer tokenizer = Tokenizer.builder().skipSymbols("('").keepSkipSymbol().build();

        tokenizer.changeDelimiter(OR);
        String[] splitOr = tokenizer.parse(text).getArray();

        String unSplitOr = String.join("-", splitOr);
        System.out.println(unSplitOr);
        assertEquals("zero-!(one&two)-three", unSplitOr);

        splitOr[1] = splitOr[1].substring(2, splitOr[1].length() - 1);
        assertEquals("one&two", splitOr[1]);

        tokenizer.changeDelimiter(AND);
        String[] splitAnd = tokenizer.parse(splitOr[1]).getArray();
        String unSplitAnd = String.join("-", splitAnd);
        System.out.println(unSplitAnd);
        assertEquals("one-two", unSplitAnd);
//
//        splitOr = tokenizer.parse(splitAnd[1]).getArray();
//        unSplitOr = String.join("-", splitOr);
//        System.out.println(unSplitOr);
//        assertEquals("two-three", unSplitOr);
    }
}