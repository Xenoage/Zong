package com.xenoage.utils;

import org.junit.Test;

import java.util.ArrayList;

import static com.xenoage.utils.StringUtils.concatenate;
import static com.xenoage.utils.StringUtils.trimRight;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static org.junit.Assert.*;

/**
 * Test cases for a {@link StringUtils} class.
 *
 * @author Andreas Wenger
 */
public class StringUtilsTest {

	@Test public void trimRightTest() {
		assertEquals("abc", trimRight("abc"));
		assertEquals("abc", trimRight("abc "));
		assertEquals("abc", trimRight("abc    "));
		assertEquals("a b c", trimRight("a b c"));
		assertEquals("a  b c", trimRight("a  b c    "));
	}

	@Test public void concatenateTest() {
		String separator = ":";
		assertEquals("", concatenate(new ArrayList<>(), separator));
		assertEquals("1", concatenate(alist("1"), separator));
		assertEquals("1:2", concatenate(alist("1", "2"), separator));
		assertEquals("1:2:3", concatenate(alist("1", "2", "3"), separator));
	}

}
