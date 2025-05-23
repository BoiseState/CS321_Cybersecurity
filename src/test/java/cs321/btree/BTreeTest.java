package cs321.btree;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Unit testing for BTree constructors, Insert, Search and
 * some TreeObject interactions in the BTree (such as counting duplicates)
 *
 * Note some tests use Alphabetic letters as keys to follow the examples
 * given in the textbook.
 *
 * @author CS321 instructors
 */
public class BTreeTest {

    /**
     * Use the same filename for each time a BTree is created.
     */
    private static String testFilename = "Test_BTree.tmp";

    /**
     * Avoid some test errors if the test file failed to clean up
     * in a previous run.
     */
    @BeforeClass
    public static void beforeAll() {

        deleteTestFile(testFilename);
    }

    /**
     * After each test case, remove the test file.
     */
    @After
    public void cleanUpTests() {

        deleteTestFile(testFilename);
    }

    // HINT:
    //  instead of checking all intermediate states of constructing a tree
    //  you can check the final state of the tree and
    //  assert that the constructed tree has the expected number of nodes and
    //  assert that some (or all) of the nodes have the expected values
    @Test
    public void btreeDegree4Test()
    {
//        //TODO instantiate and populate a bTree object
//        int expectedNumberOfNodes = TBD;
//
//        // it is expected that these nodes values will appear in the tree when
//        // using a level traversal (i.e., root, then level 1 from left to right, then
//        // level 2 from left to right, etc.)
//        String[] expectedNodesContent = new String[]{
//                "TBD, TBD",      //root content
//                "TBD",           //first child of root content
//                "TBD, TBD, TBD", //second child of root content
//        };
//
//        assertEquals(expectedNumberOfNodes, bTree.getNumberOfNodes());
//        for (int indexNode = 0; indexNode < expectedNumberOfNodes; indexNode++)
//        {
//            // root has indexNode=0,
//            // first child of root has indexNode=1,
//            // second child of root has indexNode=2, and so on.
//            assertEquals(expectedNodesContent[indexNode], bTree.getArrayOfNodeContentsForNodeIndex(indexNode).toString());
//        }
    }

    /**
     * Test simple creation of an empty BTree.
     * An empty BTree has 1 node with no keys and height of 0.
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     */
    @Test
    public void testCreate() throws BTreeException{


        BTree b = new BTree(testFilename);

        //height should be 0
        assertEquals(0, b.getHeight());

        //size should be 0
        assertEquals(0, b.getSize());

        //will have only 1 node, the root
        assertEquals(1, b.getNumberOfNodes());

    }

    /**
     * Test constructing a BTree with custom degree.
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     */
    @Test
    public void testCreateDegree () throws BTreeException {

        BTree b = new BTree(3, testFilename);

        assertEquals(3, b.getDegree());

    }

    /**
     * Test inserting a single key into an empty BTree.
     * BTree size now reflects the single key.
     * BTree structure is not validated in this test, as it would depend
     * on searching the tree or examining private members of BTree.
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     * @throws IOException Exception thrown when testing fails due to IO errors
     */
    @Test
    public void testInsertOneKey() throws BTreeException, IOException {

        BTree b = new BTree(2, testFilename);

        b.insert(new TreeObject("1"));

        assertEquals(1, b.getSize());
        assertEquals(0, b.getHeight());

        assertTrue(validateInserts(b, new String[]{"1"}));
    }

    /**
     * Ten Keys (0 -> 9) added to a tree of degree 2, ensuring full nodes will be split.
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     * @throws IOException Exception thrown when testing fails due to IO errors
     */
    @Test
    public void testInsertTenKeys() throws BTreeException, IOException{

        BTree b = new BTree(2, testFilename);

        String[] input = new String[10];

        for (int i = 0; i < 10; i++) {
            input[i] = i + "";
            b.insert(new TreeObject(i + ""));
        }

        assertEquals(10, b.getSize());
        assertEquals(2, b.getHeight());

        assertTrue(validateInserts(b, input));
    }


    /**
     * Ten keys (10 -> 1) inserted into a BTree of degree 2.
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     * @throws IOException Exception thrown when testing fails due to IO errors
     */
    @Test
    public void testInsertTenKeysReverseOrder() throws BTreeException, IOException {

        BTree b = new BTree(2, testFilename);

        String[] input = new String[10];

        for (int i = 10; i > 0; i--) {
            input[10 - i] = i + "";
            b.insert(new TreeObject(i + ""));
        }

        assertEquals(10, b.getSize());
        assertEquals(2, b.getHeight());

        assertTrue(validateInserts(b, input));
    }


    /**
     * Tests that adding duplicate key values to the tree doesn't create
     * duplicates within the tree.
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     * @throws IOException Exception thrown when testing fails due to IO errors
     */
    @Test
    public void testInsertTenDuplicates() throws BTreeException, IOException {

        BTree b = new BTree(2, testFilename);

        for (int i = 0; i < 10; i++) {
            b.insert(new TreeObject(1 + ""));
        }

        assertEquals(1, b.getSize());
        assertEquals(0, b.getHeight());

        assertTrue(validateInserts(b, new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1"}));
    }


    /**
     * Simply tests inserting many objects into the BTree (no duplicates).
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     * @throws IOException Exception thrown when testing fails due to IO errors
     */
    @Test
    public void testInsertTenThousandObjects() throws BTreeException, IOException {

        BTree b = new BTree(2, testFilename);

        String[] input = new String[10000];

        for (int i = 0; i < 10000; i++) {
            input[i] = i + "";
            b.insert(new TreeObject(i + ""));
        }

        assertEquals(10000, b.getSize());

        assertTrue(validateInserts(b, input));
    }

    /**
     * Test inserting into a tree using the example in Figure 18.6 in CLRS.
     * Note that Letters have been transposed to numbers corresponding to
     * position in the English Alphabet (e.g. A = 1, B = 2)
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     * @throws IOException Exception thrown when testing fails due to IO errors
     */
    @Test
    public void testCLRSExample18_6() throws BTreeException, IOException {

        BTree b = new BTree(4, testFilename);

        String[] input = new String[]{"A", "D", "F", "H", "L", "N", "P", "B"};

        for (int i = 0; i < input.length - 1; i++) {
            b.insert(new TreeObject(input[i]));
        }

        assertEquals(7, b.getSize());
        assertEquals(0, b.getHeight());
        assertEquals(1, b.getNumberOfNodes());

        b.insert(new TreeObject(input[7])); //Insert 'B'

        assertEquals(8, b.getSize());
        assertEquals(1, b.getHeight());
        assertEquals(3, b.getNumberOfNodes());

        assertTrue(validateInserts(b, input));
    }

    /**
     * Search test that queries an empty tree.
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     * @throws IOException Exception thrown when testing fails due to IO errors
     */
    @Test
    public void testSearchEmptyTree() throws BTreeException, IOException {

        BTree b = new BTree(2, testFilename);

        TreeObject t = b.search("1");

        assertNull(t);

    }

    /**
     * Search test that adds a TreeObject and then searches for it.
     * Assumes that BTree.insert() works properly and TreeObject.compareTo()
     * has been implemented.
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     * @throws IOException Exception thrown when testing fails due to IO errors
     */
    @Test
    public void testSearchOneKey() throws BTreeException, IOException {

        String key = "1";
        TreeObject t = new TreeObject(key);

        BTree b = new BTree(2, testFilename);

        b.insert(new TreeObject(key));

        TreeObject obj = b.search(key);

        assertEquals(0, t.compareTo(obj));

    }


    /**
     * More complex search test for searching recursively.
     * Test inserting a duplicate into a node that is not a leaf and has
     * a full child.
     * Assertion is that TreeObject with key 'A' has been found
     *
     * @throws BTreeException
     * @throws IOException
     */
    @Test
    public void testSearchToNotLeaf() throws BTreeException, IOException {

        BTree b = new BTree(2, testFilename); //Different degree than CLRS 18.6!

        b.insert(new TreeObject("A"));
        b.insert(new TreeObject("D"));
        b.insert(new TreeObject("F"));
        b.insert(new TreeObject("H"));
        b.insert(new TreeObject("L"));

        TreeObject obj = b.search("A");

        assertEquals("A", obj.getKey());
    }

    /**
     * TreeObject test that inserts 1 TreeObject and checks that its count is correct.
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     * @throws IOException Exception thrown when testing fails due to IO errors
     */
    @Test
    public void testTreeObjectCount() throws BTreeException, IOException {

        String key = "A";

        BTree b = new BTree(2, testFilename);

        b.insert(new TreeObject(key));

        TreeObject obj = b.search(key);

        assertEquals(1, obj.getCount());
    }

    /**
     * TreeObject test that validates duplicates are counted properly.
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     * @throws IOException Exception thrown when testing fails due to IO errors
     */
    @Test
    public void testCountingTreeObjectDuplicates() throws BTreeException, IOException {

        String duplicateKey = "A";

        BTree b = new BTree(2, testFilename);

        for (int i = 0; i < 10; i++) {
            b.insert(new TreeObject(duplicateKey));
        }

        TreeObject obj = b.search(duplicateKey);

        assertEquals(10, obj.getCount());
    }

    /**
     * TreeObject test of additional constructor.
     */
    @Test
    public void testSettingTreeObjectCount() {
        String key = "A";
        long count = 12;

        TreeObject t = new TreeObject(key, count);

        assertEquals(count, t.getCount());

    }


    /**
     * More complex insert test requiring working Search and duplicate counting.
     * Run a similar test to example 18.6 in the book, except
     * that a duplicate key ('H') is inserted again to the root when
     * it is no longer a leaf node.
     * Assertion is that TreeObject with key 'H' has count = 2.
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     * @throws IOException Exception thrown when testing fails due to IO errors
     */
    @Test
    public void testInsertToNotLeaf() throws BTreeException, IOException {

        BTree b = new BTree(4, testFilename);

        String[] input = new String[]{"A", "D", "F", "H", "L", "N", "P", "B", "H"};

        for (int i = 0; i < input.length - 1; i++) {
            b.insert(new TreeObject(input[i]));
        }

        //by inserting a duplicate into a non leaf node, another branch is tested.
        b.insert(new TreeObject(input[8])); //H

        TreeObject obj = b.search("H");

        assertEquals(2, obj.getCount());

        assertTrue(validateInserts(b, input));
    }


    /**
     * More complex insert test requiring working Search and duplicate counting.
     * Test inserting a duplicate into a node that is not a leaf and has
     * a full child.
     * Assertion is that key 'H' (8) has been counted twice in a search
     *
     * @throws BTreeException Exception thrown when BTree encounters an unexpected problem
     * @throws IOException Exception thrown when testing fails due to IO errors
     */
    @Test
    public void testInsertToNotLeafFullChild() throws BTreeException, IOException {

        BTree b = new BTree(2, testFilename); //Different degree than CLRS 18.6!

        String[] input = new String[]{"A", "D", "F", "H", "L", "H"};

        for (String l : input) {
            b.insert(new TreeObject(l));
        }

        TreeObject obj = b.search("H");

        assertEquals(2, obj.getCount());

        assertTrue(validateInserts(b, input));
    }


    /**
     * Helper method used to validate that all the keys in the BTree
     * are sorted by using an in order traversal of the tree
     *
     * @param b BTree to validate
     * @return true if there are no keys in the BTree, or if the keys are indeed in sorted order.
     *
     */
	@SuppressWarnings("unused")
    private boolean validateSearchTreeProperty(BTree b) throws IOException {

        String[] keys = b.getSortedKeyArray();

        /*if there are no keys, the tree is valid
            Beware, if keys have indeed been inserted but getKeysInOrder is not,
            this method will return true
        */
        if (keys == null | keys.length == 0) {
            return true;
        }

        String prev = keys[0];

        for (int i = 1; i < keys.length; i++) {
            if (prev.compareTo(keys[i]) > 0) {
                return false;
            }
        }
        return true;
    }


    /**
     * Checks BTree b against an array of keys to ensure that they were inserted into the BTree.
     * The important check here is that no keys are being lost during complex
     * operations such as splitting children in the tree.
     *
     * Also used to validate that all the keys in the BTree
     * are sorted by using an in order traversal of the tree
     *
     * IMPORTANT: handles duplicates in inputKeys by removing them.
     *
     * @return true if BTree in order traversal matches provide input
     */
    private boolean validateInserts(BTree b, String[] inputKeys) throws IOException {

        String[] bTreeKeys = b.getSortedKeyArray();

        //input may be unsorted
        Arrays.sort(inputKeys);

        //track input as a dynamic set to easily remove duplicates
        ArrayList<String> inputNoDuplicates = new ArrayList<>(inputKeys.length);

        //Copy with excluding duplicates
        for (int i = 0; i < inputKeys.length; i++) {

            if (i > 0) {
                //only add an element if it is different from the previous iteration.
                if (!inputKeys[i - 1].equals(inputKeys[i])) {
                    inputNoDuplicates.add(inputKeys[i]);
                }
            } else {
                inputNoDuplicates.add(inputKeys[i]);
            }
        }

        if (bTreeKeys.length != inputNoDuplicates.size()) {
            //if input and output arrays are different sizes, they can't be equal
            return false;
        }

        String prev = bTreeKeys[0];

        for (int i = 0; i < bTreeKeys.length; i++) {
            if (!bTreeKeys[i].equals(inputNoDuplicates.get(i))) {
                return false;
            }

            if (i > 0 && prev.compareTo(bTreeKeys[i]) > 0) {
                return false;
            }
        }

        return true;
    }


    /**
     * Utility method to help cleanup the system after unit testing
     *
     * @param filename - the file to delete from the system
     *                 WARNING: deletion is unchecked. Only directories are
     *                 prevented from being deleted.
     *                 Pass a filename only to a file that should be deleted
     *                 or could be restored.
     */
    private static void deleteTestFile(String filename) {
        File file = new File(filename);
        if (file.exists() && !file.isDirectory()) {
            System.out.println("Deleting " + filename);
            file.delete();
        }
    }
}
