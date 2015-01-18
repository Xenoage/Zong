package com.xenoage.zong.renderer.awt.text;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.kernel.Tuple3.t3;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.kernel.Tuple3;
import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;

/**
 * This class combines several {@link TextLayout}s with
 * positioning information in a single object.
 * 
 * @author Andreas Wenger
 */
public class TextLayouts {

	/**
	 * A {@link TextLayout} and its position.
	 * 
	 * @author Andreas Wenger
	 */
	public static class Item {

		public final TextLayout textLayout;
		public final Point2f position;
		public final int length;


		public Item(TextLayout textLayout, Point2f position, int length) {
			this.textLayout = textLayout;
			this.position = position;
			this.length = length;
		}
	}


	public final ArrayList<Item> items;
	public final Rectangle2f boundingRect;


	public TextLayouts(ArrayList<Item> items) {
		this.items = items;
		//compute bounding rect
		Rectangle2f boundingRect = null;
		for (int i : range(items)) {
			Item item = items.get(i);
			Rectangle2f r = getTextLayoutBounds(item.textLayout).move(item.position);
			if (boundingRect == null)
				boundingRect = r;
			else
				boundingRect = boundingRect.extend(r);
		}
		this.boundingRect = boundingRect;
	}
	
	private Rectangle2f getTextLayoutBounds(TextLayout textLayout) {
		Rectangle2D r = textLayout.getBounds();
		return new Rectangle2f((float) r.getMinX(), (float) r.getMinY(), (float) r.getWidth(),
			(float) r.getHeight());
	}

	public void draw(Graphics2D g) {
		for (TextLayouts.Item item : items) {
			//iText workaround: since vertical offset has reverted sign in Java2D and iText
			//(at least here. this is confirmed by the author Paulo Soares, see
			// http://sourceforge.net/mailarchive/message.php?msg_name=000601cae89a$d0ea15d0$587ba8c0%40psoaresw
			//), we translate first, paint at y=0, and translate back
			AffineTransform t = g.getTransform();
			g.translate(item.position.x, item.position.y);
			float scale = Units.pxToMm_1_1;
			g.scale(scale, scale);
			item.textLayout.draw(g, 0, 0);
			g.setTransform(t);
		}
	}

	/**
	 * Draws the given shape relative to the position of the given item.
	 */
	public void drawShape(Graphics2D g, Shape shape, Item item) {
		AffineTransform t = g.getTransform();
		g.translate(item.position.x, item.position.y);
		float scale = Units.pxToMm_1_1;
		g.scale(scale, scale);
		g.draw(shape);
		g.setTransform(t);
	}

	/**
	 * Fills the given shape relative to the position of the given item.
	 */
	public void fillShape(Graphics2D g, Shape shape, Item item) {
		AffineTransform t = g.getTransform();
		g.translate(item.position.x, item.position.y);
		float scale = Units.pxToMm_1_1;
		g.scale(scale, scale);
		g.fill(shape);
		g.setTransform(t);
	}

	/**
	 * Gets the {@link Item} and the local position within this item
	 * at the given global position. If not found, null is returned.
	 */
	public Tuple2<Item, Integer> getItemAt(int globalPosition) {
		if (globalPosition >= 0) {
			int pos = 0;
			for (Item item : items) {
				int length = item.length;
				if (globalPosition <= pos + length) {
					return t(item, globalPosition - pos);
				}
				pos += length;
			}
		}
		return null;
	}

	/**
	 * Gets the {@link Item}s and the local positions within the first
	 * and last item. If not found, null is returned.
	 */
	public Tuple3<List<Item>, Integer, Integer> getItemsBetween(int leftGlobalPosition,
		int rightGlobalPosition) {
		List<Item> retItems = alist();
		boolean leftFound = false;
		int retLeft = 0, retRight = 0;
		if (rightGlobalPosition >= leftGlobalPosition && leftGlobalPosition >= 0) {
			int pos = 0;
			for (Item item : items) {
				int length = item.length;
				if (!leftFound && leftGlobalPosition < pos + length) {
					leftFound = true;
					retLeft = leftGlobalPosition - pos;
					retItems.add(item);
				}
				if (leftFound) {
					if (pos + length >= rightGlobalPosition) {
						retRight = rightGlobalPosition - pos;
						return t3(retItems, retLeft, retRight);
					}
					else {
						retItems.add(item);
					}
				}
				pos += length;
			}
		}
		return null;
	}

	/**
	 * Gets the global position from the given TextLayout and local position,
	 * or null if unknown.
	 */
	public Integer getGlobalPosition(TextLayout textLayout, int localPosition) {
		int pos = 0;
		for (Item item : items) {
			if (textLayout == item.textLayout)
				return pos + localPosition;
			pos += item.length;
		}
		return null;
	}

}
