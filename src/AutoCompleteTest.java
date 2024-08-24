package src;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class AutoCompleteTest {

    private AutoComplete autoComplete;

    @Before
    public void setUp() {
        autoComplete = new AutoComplete();
    }

    @Test
    public void testAdd() {
        assertTrue(autoComplete.add("test"));
        // Other assertions can be made here if add method returns additional information
    }

    @Test
    public void testAdvance() {
        autoComplete.add("test");
        assertTrue(autoComplete.advance('t'));
        assertFalse(autoComplete.advance('x')); // Assuming 'x' would not be a valid next character
    }

    @Test
    public void testRetreat() {
        autoComplete.add("test");
        autoComplete.advance('t');
        autoComplete.retreat();
        // Assert that the current state of autoComplete is as expected after retreat
    }

    @Test
    public void testReset() {
        autoComplete.add("test");
        autoComplete.advance('t');
        autoComplete.reset();
        // Assert that autoComplete has been reset to its initial state
    }

    @Test
    public void testIsWord() {
        autoComplete.add("test");
        assertFalse(autoComplete.isWord());
        autoComplete.advance('t');
        autoComplete.advance('e');
        autoComplete.advance('s');
        autoComplete.advance('t');
        assertTrue(autoComplete.isWord());
    }

    @Test
    public void testGetNumberOfPredictions() {
        autoComplete.add("test");
        autoComplete.add("testing");
        autoComplete.advance('t');
        autoComplete.advance('e');
        autoComplete.advance('s');
        // Assuming getNumberOfPredictions returns the number of words that start with "tes"
        assertEquals(2, autoComplete.getNumberOfPredictions());
    }

    @Test
    public void testRetrievePrediction() {
        autoComplete.add("test");
        autoComplete.advance('t');
        autoComplete.advance('e');
        autoComplete.advance('s');
        autoComplete.advance('t');
        assertEquals("test", autoComplete.retrievePrediction());
    }

    @Test
    public void testRetrievePredictions() {
        autoComplete.add("test");
        autoComplete.add("testing");
        autoComplete.advance('t');
        autoComplete.advance('e');
        autoComplete.advance('s');
        ArrayList<String> predictions = autoComplete.retrievePredictions();
        assertTrue(predictions.contains("test"));
        assertTrue(predictions.contains("testing"));
        assertEquals(2, predictions.size());
    }

    @Test
    public void testDelete() {
        autoComplete.add("test");
        assertTrue(autoComplete.delete("test"));
        assertFalse(autoComplete.isWord()); // Assuming isWord should now return false
    }
}
