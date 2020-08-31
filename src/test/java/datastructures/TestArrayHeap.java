package datastructures;

import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IPriorityQueue;
import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.InvalidElementException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

//ADD THE EMPTY CASES
/**
 * See spec for details on what kinds of tests this class should include.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestArrayHeap extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        assertEquals(1, heap.size());
        assertFalse(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testBasicAddReflection() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        Comparable<Integer>[] array = getArray(heap);
        assertEquals(3, array[0]);
    }

    @Test(timeout=SECOND)
    public void testUpdateDecrease() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4, 5});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        IntWrapper newValue = new IntWrapper(0);
        heap.replace(values[2], newValue);

        assertEquals(newValue, heap.removeMin());
        assertEquals(values[0], heap.removeMin());
        assertEquals(values[1], heap.removeMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.removeMin());
    }

    @Test// (timeout=SECOND)
    public void testAddandPercolateBasic(){
        IPriorityQueue<Integer> heap = this.makeInstance();

        heap.add(6);
        heap.add(5);
        heap.add(4);
        heap.add(3);

        assertEquals(3, heap.peekMin());
    }


    @Test(timeout=SECOND)
    public void testUpdateIncrease() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{0, 2, 4, 6, 8});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        IntWrapper newValue = new IntWrapper(5);
        heap.replace(values[0], newValue);

        assertEquals(values[1], heap.removeMin());
        assertEquals(values[2], heap.removeMin());
        assertEquals(newValue, heap.removeMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.removeMin());
    }

    // @Test(timeout=SECOND)
    // public void test

    @Test (timeout=SECOND)
    public void testStress(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 10000; i > 0; i--){
            heap.add(i);
        }
        for(int i = 10000; i > 0; i--){
            heap.removeMin();
        }
    }

    @Test (timeout=SECOND)
    public void testRemoveMin(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(18);
        heap.add(5);
        heap.add(9);
        heap.add(8);
        heap.add(3);
        heap.add(1);
        heap.removeMin();
        //assertEquals(5, heap.size());
        assertEquals(3, heap.peekMin());
    }

    @Test (timeout=SECOND)
    public void testPeekMinBasic(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(10);
        heap.add(12);
        assertEquals(10, heap.peekMin());
        heap.add(8);
        heap.add(4);
        heap.add(5);
        heap.add(6);
        assertEquals(4, heap.peekMin());
        heap.add(1);
        assertEquals(1, heap.peekMin());
    }

    @Test (timeout=SECOND)
    public void testPeekMinWithRemove(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(10);
        heap.add(12);
        //assertEquals(10, heap.peekMin());
        heap.removeMin();
        assertEquals(12, heap.peekMin());
    }

    @Test (timeout=SECOND)
    public void testRemoveMinBasic(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.add(5);
        heap.add(6);
        assertEquals(1, heap.removeMin());
    }

    @Test (timeout=SECOND)
    public void testReplaceChecksSize(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.replace(4, 7);
        assertEquals(4, heap.size());
    }

    @Test (timeout=SECOND)
    public void testRemoveMinMultiple(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(6);
        heap.add(5);
        heap.add(4);
        heap.add(3);
        heap.add(2);
        heap.add(1);
        assertEquals(1, heap.removeMin());
        assertEquals(2, heap.removeMin());
        assertEquals(3, heap.removeMin());
        assertEquals(4, heap.removeMin());
        assertEquals(5, heap.removeMin());
        assertEquals(6, heap.removeMin());
    }

    @Test (timeout=SECOND)
    public void testContains() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        assertTrue(heap.contains(1));

    }

    @Test (timeout=SECOND)
    public void testContainsFalse() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        assertFalse(heap.contains(3));
    }

    @Test (timeout=SECOND)
    public void testContainsAfterRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.add(5);
        assertTrue( heap.contains(1));
        assertTrue( heap.contains(2));
        assertTrue( heap.contains(3));
        assertTrue( heap.contains(4));
        assertTrue( heap.contains(5));
        heap.removeMin();
        assertFalse( heap.contains(1));
        assertTrue( heap.contains(2));
        assertTrue( heap.contains(3));
        assertTrue( heap.contains(4));
        assertTrue( heap.contains(5));
        heap.removeMin();
        assertFalse( heap.contains(1));
        assertFalse( heap.contains(2));
        assertTrue( heap.contains(3));
        assertTrue( heap.contains(4));
        assertTrue( heap.contains(5));
        heap.removeMin();
        assertFalse( heap.contains(1));
        assertFalse( heap.contains(2));
        assertFalse( heap.contains(3));
        assertTrue( heap.contains(4));
        assertTrue( heap.contains(5));
        heap.removeMin();
        assertFalse( heap.contains(1));
        assertFalse( heap.contains(2));
        assertFalse( heap.contains(3));
        assertFalse( heap.contains(4));
        assertTrue( heap.contains(5));
        heap.removeMin();
        assertFalse( heap.contains(1));
        assertFalse( heap.contains(2));
        assertFalse( heap.contains(3));
        assertFalse( heap.contains(4));
        assertFalse( heap.contains(5));

    }

    @Test (timeout=SECOND)
    public void testContainsAfterRemove() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.add(5);
        assertTrue( heap.contains(1));
        assertTrue( heap.contains(2));
        assertTrue( heap.contains(3));
        assertTrue( heap.contains(4));
        assertTrue( heap.contains(5));
        heap.remove(5);
        assertFalse(heap.contains(5));
        assertTrue(heap.contains(2));
        assertTrue(heap.contains(3));
        assertTrue(heap.contains(4));
        assertTrue(heap.contains(1));
        heap.remove(4);
        assertFalse(heap.contains(5));
        assertFalse(heap.contains(4));
        assertTrue(heap.contains(3));
        assertTrue(heap.contains(2));
        assertTrue(heap.contains(1));
        heap.remove(3);
        assertFalse(heap.contains(5));
        assertFalse(heap.contains(4));
        assertFalse(heap.contains(3));
        assertTrue(heap.contains(2));
        assertTrue(heap.contains(1));
        heap.remove(2);
        assertFalse(heap.contains(5));
        assertFalse(heap.contains(4));
        assertFalse(heap.contains(3));
        assertFalse(heap.contains(2));
        assertTrue(heap.contains(1));
        heap.remove(1);
        assertFalse(heap.contains(5));
        assertFalse(heap.contains(4));
        assertFalse(heap.contains(3));
        assertFalse(heap.contains(2));
        assertFalse(heap.contains(1));
    }

    @Test(timeout=SECOND)
    public void testRemoveAddSameItem(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 1000; i > 0; i--){
            heap.add(i);
        }
        for(int i = 1000; i > 0; i--){
            heap.remove(i);
        }
        for (int i = 1000; i > 0; i--){
            heap.add(i);
        }
    }

    @Test(timeout = SECOND)
    public void testRemoveIndicesInDictionary() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i <= 5; i++) {
            heap.add(i);
        }
        heap.remove(3);
        assertTrue(!heap.contains(3));
    }

    @Test(timeout=SECOND)
    public void testRemoveRootConsecutive(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 1000; i > 0; i--){
            heap.add(i);
        }
        for (int i = 1000; i > 0; i--){
            heap.remove(heap.peekMin());
        }
        assertEquals(0, heap.size());


    }

    @Test (timeout=SECOND)
    public void testRemoveBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.remove(3);
        assertFalse(heap.contains(3));
        assertEquals(3, heap.size());
    }

    @Test (timeout=SECOND)
    public void testReplace() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.replace(1, 6);
        assertTrue(heap.contains(6));
        assertFalse(heap.contains(1));
    }

    @Test (timeout=SECOND)
    public void testPeekMinEmptyContainer(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex){
            //Do Nothing
        }
    }

    @Test (timeout=SECOND)
    public void testAddElementAlreadyInUse(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        try {
            heap.add(1);
            fail("Invalid Element Exception, already in use");
        } catch (InvalidElementException ex){
            //Do Nothing
        }
    }

    @Test (timeout=SECOND)
    public void testAddNullElement(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        try{
            heap.add(null);
            fail("Illegal Argument Exception");
        } catch (IllegalArgumentException ex){
            //do nothing
        }
    }

    @Test (timeout=SECOND)
    public void testContainsNullItem(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        try{
            heap.contains(null);
            fail("Illegal Argument Exception");
        } catch (IllegalArgumentException ex){
            //do nothing
        }
    }

    @Test (timeout=SECOND)
    public void testRemoveNullItem(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        try{
            heap.remove(null);
            fail("Illegal Argument Exception");
        } catch (IllegalArgumentException ex){
            //do nothing
        }
    }

    @Test (timeout=SECOND)
    public void testRemoveMinEmpty(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        try{
            heap.removeMin();
            fail("heap is empty");
        } catch (EmptyContainerException ex){
            //do nothing
        }
    }

    @Test (timeout=SECOND)
    public void testReplaceUsedItem(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        try{
            heap.replace(2, 3);
            fail("New Item already in use");
        } catch (InvalidElementException ex){
            //do Nothing
        }
    }


    @Test //(timeout=SECOND)
    public void testAddLargeAmount(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 1000; i > 0; i--){
            heap.add(i);
        }


        for (int j = 1; j < 1000; j++){
           assertEquals(j, heap.removeMin());
        }

    }

    @Test (timeout=SECOND)
    public void testRemoveMinBigAmount(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        // for (int i = 1000; i > 0; i--){
        //     heap.add(i);
        // }
        heap.add(4);
        heap.add(3);
        heap.add(2);
        heap.add(1);
        assertEquals(1, heap.peekMin());


        // for(int j = 0; j < 999; j++){
        //     heap.removeMin();
        //
        // }
    }

    @Test (timeout=SECOND)
    public void testReplaceNonexistenItem(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        try{
            heap.replace(5, 2);
            fail("Old Item is not in queue");
        } catch (InvalidElementException ex){
            //do Nothing
        }
    }

    @Test (timeout=SECOND)
    public void testIsEmpty(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertTrue(heap.isEmpty());
    }

    @Test (timeout=SECOND)
    public void testNotEmpty(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        assertFalse(heap.isEmpty());
    }

    @Test (timeout=SECOND)
    public void testCheckSwap(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        IDictionary workingDictionary = getField(heap, "dictionary1", IDictionary.class);

        for (int i = 0; i < 3; i ++){
            heap.add(i);
        }
        heap.removeMin();
        assertEquals(0, workingDictionary.get(1));

    }

    @Test (timeout=SECOND)
    public void testRemoveEquivalentValuesThrowsException(){
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        IntWrapper first = new IntWrapper (3);
        IntWrapper second = new IntWrapper(3);
        heap.add(first);
        try{
            heap.remove(second);
            fail("n2 is not in the heap");
        } catch (InvalidElementException ex){
            //do Nothing
        }


    }

//     @Test (timeout=SECOND)
//     public void testSwapUpdates(){
//         IPriorityQueue<Integer> heap = this.makeInstance();
// ?????????
//     }

    //int wrapper, check if duplicate numbers are ok, but dupicate objects are not ok
    //testing if objects are unique or not, check if the objects are the exact same or not

    /**
     * A comparable wrapper class for ints. Uses reference equality so that two different IntWrappers
     * with the same value are not necessarily equal--this means that you may have multiple different
     * IntWrappers with the same value in a heap.
     */
    public static class IntWrapper implements Comparable<IntWrapper> {
        private final int val;

        public IntWrapper(int value) {
            this.val = value;
        }

        public static IntWrapper[] createArray(int[] values) {
            IntWrapper[] output = new IntWrapper[values.length];
            for (int i = 0; i < values.length; i++) {
                output[i] = new IntWrapper(values[i]);
            }
            return output;
        }

        @Override
        public int compareTo(IntWrapper o) {
            return Integer.compare(val, o.val);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public int hashCode() {
            return this.val;
        }

        @Override
        public String toString() {
            return Integer.toString(this.val);
        }
    }

    /**
     * A helper method for accessing the private array inside a heap using reflection.
     */
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> Comparable<T>[] getArray(IPriorityQueue<T> heap) {
        return getField(heap, "heap", Comparable[].class);
    }

    private static <T extends Comparable<T>> IDictionary getDictionary(IPriorityQueue<T> heap) {
        return getField(heap, "dictionary1", IDictionary.class);
    }



}

//use iterator from doublelinked list to iterate over sorter because it runs in O(1) and that matters
//test a resize on add call
