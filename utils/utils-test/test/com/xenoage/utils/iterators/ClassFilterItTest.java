package com.xenoage.utils.iterators;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for {@link ClassFilterIt}.
 * 
 * @author Andreas Wenger
 */
public class ClassFilterItTest {

	@Test public void test1() {
		List<Object> list = new LinkedList<>();
		list.add("Hello");
		list.add(1f);
		list.add(12);
		list.add("here");
		list.add("we");
		list.add(7f);
		//must deliver 1f, 12 and 7f
		ClassFilterIt<Number> it = new ClassFilterIt<>(list, Number.class);
		assertEquals(true, it.hasNext());
		assertEquals(1f, it.next());
		assertEquals(1, it.getIndex());
		assertEquals(true, it.hasNext());
		assertEquals(12, it.next());
		assertEquals(2, it.getIndex());
		assertEquals(true, it.hasNext());
		assertEquals(7f, it.next());
		assertEquals(5, it.getIndex());
		assertEquals(false, it.hasNext());
	}

	@Test public void test2() {
		List<Object> list = new LinkedList<>();
		list.add(1f);
		list.add(12);
		list.add("here");
		list.add("we");
		list.add(7f);
		list.add("go");
		//must deliver 1f, 12 and 7f
		ClassFilterIt<Number> it = new ClassFilterIt<>(list, Number.class);
		assertEquals(true, it.hasNext());
		assertEquals(1f, it.next());
		assertEquals(0, it.getIndex());
		assertEquals(true, it.hasNext());
		assertEquals(12, it.next());
		assertEquals(1, it.getIndex());
		assertEquals(true, it.hasNext());
		assertEquals(7f, it.next());
		assertEquals(4, it.getIndex());
		assertEquals(false, it.hasNext());
	}

}
