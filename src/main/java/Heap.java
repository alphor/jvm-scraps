import java.util.*;

public final class Heap<T> {

    private final List<T> backing;
    private final Comparator<T> comparator;

    public Heap(Comparator<T> comparator) {
        this(comparator, new ArrayList<>());
    }

    private Heap(Comparator<T> comparator, List<T> backing) {
        this.backing = backing;
        this.comparator = comparator;
    }

    public static <T> void inPlaceSort(List<T> things, Comparator<T> comparator) {
        Heap<T> heap = new Heap<>(comparator, things);
        heap.heapify();
        int heapThreshold = heap.backing.size() - 1;
        while (heapThreshold >= 0) {
            heap.backing.add(heap.pop(heapThreshold));
            heapThreshold -= 1;
        }
    }

    public void add(T element) {
        backing.add(element);
        int currIdx = backing.size() - 1;
        if (currIdx == 0) {
            return;
        }
        int parentIdx = (currIdx - 1) / 2;
        while (obeysComparator(currIdx, parentIdx)) {
            swap(currIdx, parentIdx);
            if (parentIdx != 0) {
                currIdx = parentIdx;
                parentIdx = (currIdx - 1) / 2;
            } else {
                break;
            }
        }
    }

    public T remove() {
        int size = backing.size();
        if (size == 0) {
            throw new IllegalStateException("Remove called on an empty heap!");
        } else if (size == 1) {
            return backing.remove(0);
        } else {
            return pop(size - 1);
        }
    }

    public int size() {
        return backing.size();
    }

    private void heapify() {
        int heapSize = backing.size();
        int heapThreshold = heapSize / 2;

        while (heapThreshold >= 0) {
            siftDown(heapThreshold, heapSize);
            heapThreshold -= 1;
        }
    }

    private T pop(int heapBound) {
        swap(0, heapBound);
        T returned = backing.remove(heapBound);
        siftDown(0, heapBound);
        return returned;
    }

    private void siftDown(int toBeSifted, int heapBound) {
        int currIdx = toBeSifted;
        OptionalInt swapIdx = getSwapIndex(currIdx, heapBound);

        while (swapIdx.isPresent()) {
            int swapTarget = swapIdx.getAsInt();
            swap(currIdx, swapTarget);
            currIdx = swapTarget;
            swapIdx = getSwapIndex(currIdx, heapBound);
        }
    }

    private void swap(int idxA, int idxB) {
        T temp = backing.get(idxA);
        backing.set(idxA, backing.get(idxB));
        backing.set(idxB, temp);
    }

    private OptionalInt getSwapIndex(int parentIdx, int heapBound) {
        int leftIdx = 2 * parentIdx + 1;
        int rightIdx = 2 * parentIdx + 2;

        if (leftIdx < heapBound) {
            if (obeysComparator(leftIdx, parentIdx)) {
                if (rightIdx < heapBound && obeysComparator(rightIdx, leftIdx)) {
                    return OptionalInt.of(rightIdx);
                }
                return OptionalInt.of(leftIdx);
            } else if (rightIdx < heapBound && obeysComparator(rightIdx, parentIdx)) {
                return OptionalInt.of(rightIdx);
            }
        }
        return OptionalInt.empty();
    }

    private boolean obeysComparator(int greaterIdx, int lesserIdx) {
        boolean toBeReturned = comparator.compare(
                backing.get(greaterIdx),
                backing.get(lesserIdx)
        ) < 0;
        return toBeReturned;
    }
}
