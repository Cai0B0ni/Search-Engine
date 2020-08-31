package misc;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSorter extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void singleInsertion() {
        IList<Integer> l = new DoubleLinkedList<>();
        l.add(0);
        IList<Integer> min = Sorter.topKSort(100, l);
        assertEquals(0, min.get(0));
    }

    @Test(timeout=SECOND)
    public void negativeNumbers() {
        IList<Integer> negativeList = new DoubleLinkedList<>();
        for (int i = 1; i < 50; i++) {
            negativeList.add(i * -1);
        }
        IList<Integer> sort = Sorter.topKSort(10, negativeList);
        assertEquals(10, sort.size());
    }

    @Test(timeout=SECOND)
    public void throwException() {
        IList<Integer> l = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            l.add(i);
        }
        try {
            IList<Integer> top = Sorter.topKSort(-1, l);
            top.add(0);
        } catch (IllegalArgumentException ex) {
            //nothing
        }
    }

    @Test(timeout=SECOND)
    public void kValueTest() {
        IList<Integer> l = new DoubleLinkedList<>();
        for (int i = 0; i < 100; i++) {
            l.add(i);
        }
        IList<Integer> top = Sorter.topKSort(0, l);
        assertEquals(0, top.size());
    }

    @Test(timeout=SECOND)
    public void outputVerification() {
        IList<Integer> l = new DoubleLinkedList<>();
        for (int i = 0; i < 5; i++) {
            l.add(i);
        }
        IList<Integer>  sort = Sorter.topKSort(10, l);
        for (int i = 0; i < 5; i++) {
            assertEquals(i, sort.get(i));
        }
        assertEquals(5, sort.size());

    }

    @Test(timeout=SECOND)
    public void randomList() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
            list.add(i - 20);
        }
        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        assertEquals(95, top.get(0));
    }

    @Test (timeout=SECOND)
    public void testMutateInput(){
        IList<Integer> yupp = new DoubleLinkedList<>();
        for (int i = 1; i < 6; i++){
            yupp.add(i);
        }
        yupp.add(0);

        IList<Integer> realYuppp = yupp;
        realYuppp = Sorter.topKSort(2, yupp);
        assertEquals(1, yupp.get(0));
        assertEquals(2, yupp.get(1));
        assertEquals(3, yupp.get(2));
        assertEquals(4, yupp.get(3));
        assertEquals(5, yupp.get(4));
        assertEquals(0, yupp.get(5));
    }
}

//mutate input
// input list after sorter should not have changed
// make a list, copy it, sorter it
// compare copy of list with original sorter


