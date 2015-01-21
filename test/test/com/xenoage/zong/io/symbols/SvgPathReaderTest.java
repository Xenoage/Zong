package com.xenoage.zong.io.symbols;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.geom.Point2f.p;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.symbols.path.ClosePath;
import com.xenoage.zong.symbols.path.LineTo;
import com.xenoage.zong.symbols.path.MoveTo;
import com.xenoage.zong.symbols.path.Path;

/**
 * Tests for {@link SvgPathReader}.
 *
 * @author Andreas Wenger
 */
public class SvgPathReaderTest {

	@Test public void test1() {
		String validPath = "M 1100 1100 L 1300 1100 L 1200 1300 z";
		Path p = new SvgPathReader(validPath).read();
		assertEquals(alist(new MoveTo(p(1, 1)), new LineTo(p(3, 1)), new LineTo(p(2, 3)),
			new ClosePath()), p.getElements());
	}
	
	@Test public void test2() {
		String validPath = "M1200,1300 L1400,1050 L1600,1300 1800,1550 M2000,1300z";
		Path p = new SvgPathReader(validPath).read();
		assertEquals(alist(new MoveTo(p(2, 3)), new LineTo(p(4, 0.5f)), new LineTo(p(6, 3)),
			new LineTo(p(8, 5.5f)), new MoveTo(p(10, 3)), new ClosePath()), p.getElements());
	}
	
	@Test public void testImplicitLineToAbs() {
		String validPath = "M1200,1300 1400,1050 1600,1300 1800,1550 2000,1300z";
		Path p = new SvgPathReader(validPath).read();
		assertEquals(alist(new MoveTo(p(2, 3)), new LineTo(p(4, 0.5f)), new LineTo(p(6, 3)),
			new LineTo(p(8, 5.5f)), new LineTo(p(10, 3)), new ClosePath()), p.getElements());
	}
	
	@Test public void testImplicitLineToRel() {
		String validPath = "m1200,1300 100,100 z";
		Path p = new SvgPathReader(validPath).read();
		assertEquals(alist(new MoveTo(p(2, 3)), new LineTo(p(3, 4f)), new ClosePath()), p.getElements());
	}

}
