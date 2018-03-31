package com.xenoage.utils.collections;

import java.util.*;

import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.iterators.ListIt.listIt;

/**
 * Immutable closeable list.
 * 
 * This is just a wrapper around an {@link ArrayList}. At the beginning,
 * the list is unclosed and can be written like a normal {@link ArrayList}.
 * After the {@link #close()} method is called, all calls to write methods
 * will throw an {@link IllegalStateException}.
 * 
 * An {@link CList} is "branched" when it is created based on an existing {@link IList}.
 * In this case it shares its data memory, until the first write
 * operation is performed. At this point, a full copy of the data memory is made.
 * Thus, if the list is not modified later, branching the list is very fast.
 * 
 * As long as the class is used as an {@link CList}, there are no compiler
 * warnings for the write methods. As soon as it is used as a {@link IList},
 * compiler warnings for the write methods will show up.
 * 
 * This class is a pragmatic combination of the idea of functional data structures
 * like PList from utils-pdlib and an efficient implementation like {@link ArrayList}.
 * 
 * @author Andreas Wenger
 */
public final class CList<T>
	implements IList<T> {

	private List<T> array;
	private boolean closed = false;
	private boolean sharedMemory = false; //true, when array is shared with another instance


	/**
	 * Creates an empty and unclosed {@link CList}.
	 */
	public CList() {
		this(true);
	}

	/**
	 * Creates an empty and unclosed {@link CList} with the
	 * given initial capacity.
	 */
	public CList(int initialCapacity) {
		array = new ArrayList<>(initialCapacity);
	}

	/**
	 * Creates an unclosed {@link CList} based on the given mutable
	 * collection. A shallow copy of the given collection is created.
	 */
	public CList(Collection<T> mutableCollection) {
		array = new ArrayList<>(mutableCollection);
	}

	private CList(boolean init) {
		if (init)
			array = new ArrayList<>();
		else
			array = null;
	}

	/**
	 * Creates an empty and unclosed {@link CList}.
	 */
	public static <T2> CList<T2> clist() {
		return new CList<>();
	}

	/**
	 * Creates an empty and unclosed {@link CList} with the
	 * given initial capacity.
	 */
	public static <T2> CList<T2> clist(int initialCapacity) {
		return new CList<>(initialCapacity);
	}

	/**
	 * Creates an unclosed {@link CList} based on the given mutable
	 * collection. A shallow copy of the given collection is created.
	 */
	public static <T2> CList<T2> clist(Collection<T2> mutableCollection) {
		return new CList<>(mutableCollection);
	}

	/**
	 * Creates an unclosed {@link CList} based on the given {@link IList} as a branch.
	 * This means, that the new list shares the data of the given list instance.
	 * The memory is shared until the new list receives the first write operation.
	 */
	public static <T2> CList<T2> clist(IList<T2> v) {
		CList<T2> ret = new CList<>(false);
		ret.sharedMemory = true;
		if (v instanceof CList)
			ret.array = ((CList<T2>) v).array; //avoid a stack of redirections. use array directly
		else
			ret.array = v; //no choice, we must use the public interface
		return ret;
	}

	/**
	 * Creates an unclosed {@link CList} with the given items.
	 */
	@SafeVarargs public static <T2> CList<T2> clist(T2... data) {
		ArrayList<T2> array = new ArrayList<>(data.length);
		for (T2 o : data)
			array.add(o);
		return new CList<>(array);
	}

	/**
	 * Creates a new {@link CList} with the given size and fills all
	 * elements with the given value.
	 */
	public static <T> CList<T> clistInit(T valueForAll, int size) {
		CList<T> ret = new CList<>(size);
		for (int i = 0; i < size; i++)
			ret.add(valueForAll);
		return ret;
	}
	
	/**
	 * Creates a {@link IList} with the given items.
	 * It is a closed {@link CList}.
	 */
	@SafeVarargs public static <T2> IList<T2> ilist(T2... data) {
		return clist(data).close();
	}
	
	/**
	 * Creates a {@link IList} with the given items.
	 * It is a closed {@link CList}.
	 */
	public static <T2> IList<T2> ilist(Collection<T2> data) {
		return clist(data).close();
	}

	/**
	 * Closes the list. All future calls to write methods will fail.
	 * Returns this list as a {@link IList} for convenience.
	 */
	public IList<T> close() {
		closed = true;
		return this;
	}

	/**
	 * Closes the list, like {@link #close()}, but also looks recursively for
	 * child {@link CList}s and {@link CMap}s and also closes them.
	 */
	public IList<T> closeDeep() {
		for (T item : array) {
			if (item instanceof CList)
				((CList)item).closeDeep();
			else if (item instanceof CMap)
				((CMap)item).closeDeep();
		}
		return close();
	}

	private void requestWrite() {
		//if closed, further write operations are forbidden
		if (closed)
			throw new IllegalStateException("list is closed");
		//if shared memory is used, create full copy instead
		if (sharedMemory) {
			array = new ArrayList<>(array);
			sharedMemory = false;
		}
	}

	@Override public T getFirst() {
		return array.get(0);
	}

	@Override public T getLast() {
		return array.get(array.size() - 1);
	}

	@Override public boolean add(T e) {
		requestWrite();
		array.add(e);
		return true;
	}

	@Override public void add(int index, T element) {
		requestWrite();
		array.add(index, element);
	}

	@Override public boolean addAll(Collection<? extends T> c) {
		requestWrite();
		return array.addAll(c);
	}

	@Override public boolean addAll(int index, Collection<? extends T> c) {
		requestWrite();
		return array.addAll(index, c);
	}

	@Override public void clear() {
		requestWrite();
		array.clear();
	}

	@Override public boolean contains(Object o) {
		return array.contains(o);
	}

	@Override public boolean containsAll(Collection<?> c) {
		return array.containsAll(c);
	}

	@Override public T get(int index) {
		return array.get(index);
	}

	@Override public int indexOf(Object o) {
		return array.indexOf(o);
	}

	@Override public boolean isEmpty() {
		return array.isEmpty();
	}

	@Override public Iterator<T> iterator() {
		if (closed)
			return it(array);
		else
			return array.iterator();
	}

	@Override public int lastIndexOf(Object o) {
		return array.lastIndexOf(o);
	}

	@Override public ListIterator<T> listIterator() {
		if (closed)
			return listIt(array.listIterator());
		else
			return array.listIterator();
	}

	@Override public ListIterator<T> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override public boolean remove(Object o) {
		requestWrite();
		return array.remove(o);
	}

	@Override public T remove(int index) {
		requestWrite();
		return array.remove(index);
	}

	@Override public boolean removeAll(Collection<?> c) {
		requestWrite();
		return array.retainAll(c);
	}

	@Override public boolean retainAll(Collection<?> c) {
		requestWrite();
		return array.retainAll(c);
	}

	@Override public T set(int index, T element) {
		requestWrite();
		return array.set(index, element);
	}

	@Override public int size() {
		return array.size();
	}

	@Override public IList<T> subList(int fromIndex, int toIndex) {
		return clist(array.subList(fromIndex, toIndex)).close();
	}

	@Override public Object[] toArray() {
		return array.toArray();
	}

	@Override public <T2> T2[] toArray(T2[] a) {
		return array.toArray(a);
	}

	/**
	 * Returns true, if the given collection has the same values as this one,
	 * otherwise false.
	 */
	@Override public boolean equals(Object o) {
		return array.equals(o);
	}

	@Override public int hashCode() {
		return array.hashCode();
	}

	@Override public String toString() {
		return "{size:" + array.size() + ", data:" + array.toString() + "}";
	}

}
