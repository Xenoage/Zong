package com.xenoage.zong.io.symbols;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.geom.Point2f.p;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.symbols.path.ClosePath;
import com.xenoage.zong.symbols.path.CubicCurveTo;
import com.xenoage.zong.symbols.path.LineTo;
import com.xenoage.zong.symbols.path.MoveTo;
import com.xenoage.zong.symbols.path.Path;
import com.xenoage.zong.symbols.path.PathElement;
import com.xenoage.zong.symbols.path.QuadraticCurveTo;

/**
 * This class creates a path from a given SVG path
 * (d attribute value of a SVG path element).
 *
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class SvgPathReader {

	private final String svgPath;
	
	private int pos = 0;
	private List<PathElement> elements = alist();
	private Point2f pCurrent = p(-10, -10), pStart = pCurrent;
	

	/**
	 * Creates a path from the given d attribute value of a SVG path element.
	 * The type of the path is implementation dependent.
	 * The path and its bounding rect is returned.
	 */
	public Path read() {
		//parse commands
		char tokenChar = '?';
		String token = getNextToken();
		Point2f p, cp1, cp2;
		float x, y;
		while (token != null) {
			char nextTokenChar = token.charAt(0);

			if (Character.isDigit(nextTokenChar) || nextTokenChar == '-' || nextTokenChar == '+') {
				//number. reuse last command (if not 'M' or 'm' - then it is 'L' or 'l'. see SVG spec)
				pos -= token.length();
				if (tokenChar == 'M')
					tokenChar = 'L';
				else if (tokenChar == 'm')
					tokenChar = 'l';
			}
			else {
				//next command
				tokenChar = nextTokenChar;
			}

			switch (tokenChar) {
			//MoveTo (absolute)
				case 'M':
					p = readPointAbs();
					moveTo(p);
					break;
				//MoveTo (relative)
				case 'm':
					p = readPointRel();
					moveTo(pCurrent.add(p));
					break;
				//ClosePath
				case 'Z':
				case 'z':
					closePath();
					break;
				//LineTo (absolute)
				case 'L':
					p = readPointAbs();
					lineTo(p);
					break;
				//LineTo (relative)
				case 'l':
					p = readPointRel();
					lineTo(pCurrent.add(p));
					break;
				//Horizontal LineTo (absolute)
				case 'H':
					x = readCoordAbs();
					lineTo(p(x, pCurrent.y));
					break;
				//Horizontal LineTo (relative)
				case 'h':
					x = readCoordRel();
					lineTo(p(pCurrent.x + x, pCurrent.y));
					break;
				//Vertical LineTo (absolute)
				case 'V':
					y = readCoordAbs();
					lineTo(p(pCurrent.x, y));
					break;
				//Vertical LineTo (relative)
				case 'v':
					y = readCoordRel();
					lineTo(p(pCurrent.x, pCurrent.y + y));
					break;
				//Cubic CurveTo (absolute)
				case 'C':
					cp1 = readPointAbs();
					cp2 = readPointAbs();
					p = readPointAbs();
					cubicCurveTo(cp1, cp2, p);
					break;
				//Cubic CurveTo (relative)
				case 'c':
					cp1 = readPointRel();
					cp2 = readPointRel();
					p = readPointRel();
					cubicCurveTo(pCurrent.add(cp1), pCurrent.add(cp2), pCurrent.add(p));
					break;
				//Quadratic CurveTo (absolute)
				case 'Q':
					cp1 = readPointAbs();
					p = readPointAbs();
					quadraticCurveTo(cp1, p);
					break;
				//Quadratic CurveTo (relative)
				case 'q':
					cp1 = readPointRel();
					p = readPointRel();
					quadraticCurveTo(pCurrent.add(cp1), pCurrent.add(p));
					break;
				//not implemented commands
				case 'T':
				case 't':
				case 'S':
				case 's':
				case 'A':
				case 'a':
					throw new IllegalStateException("SVG command \"" + token + "\" not implemented yet.");
					//unknown command
				default:
					throw new IllegalStateException("Unknown SVG command: \"" + token + "\"");
			}
			token = getNextToken();
		}
		return new Path(elements);
	}

	private void closePath() {
		pCurrent = pStart;
		elements.add(new ClosePath());
	}

	private void lineTo(Point2f p) {
		pCurrent = p;
		elements.add(new LineTo(p));
	}

	private void moveTo(Point2f p) {
		pStart = pCurrent = p;
		elements.add(new MoveTo(p));
	}

	private void cubicCurveTo(Point2f cp1, Point2f cp2, Point2f p) {
		pCurrent = p;
		elements.add(new CubicCurveTo(cp1, cp2, p));
	}

	private void quadraticCurveTo(Point2f cp, Point2f p) {
		pCurrent = p;
		elements.add(new QuadraticCurveTo(cp, p));
	}

	/**
	 * Gets the next token of the svg string, starting at pos.
	 * Returns null, when there is no token any more.
	 */
	private String getNextToken() {
		//skip " " and "," and "\n" and "\r".
		while (pos < svgPath.length() && isWhitespace(svgPath.charAt(pos))) {
			pos++;
		}

		//when the end of the String is reached, return null
		if (pos >= svgPath.length())
			return null;

		//find the end of the token
		char c0 = svgPath.charAt(pos);
		boolean c0Numeric = isNumeric(c0);
		int posEnd = pos;
		for (int i = pos + 1; i < svgPath.length(); i++) {
			char ci = svgPath.charAt(i);
			boolean ciNumeric = isNumeric(ci);

			//if c0 is numeric, but c1 not (or the other way round), the token is finished
			if (c0Numeric != ciNumeric) {
				break;
			}

			//if ci is whitespace, the token is finished
			if (isWhitespace(ci)) {
				break;
			}

			posEnd++;
		}
		String ret = svgPath.substring(pos, posEnd + 1);

		//new starting point is current end point
		pos = posEnd + 1;

		return ret;
	}

	/**
	 * Returns true, if the given char is a digit, a dot,
	 * a plus or a minus.
	 */
	private boolean isNumeric(char c) {
		return (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' ||
			c == '7' || c == '8' || c == '9' || c == '.' || c == '-' || c == '+');
	}

	/**
	 * Returns true, if the given char is a whitespace
	 * (' ', ',', '\n', '\r').
	 */
	private boolean isWhitespace(char c) {
		return (c == ' ' || c == ',' || c == '\n' || c == '\r');
	}
	
	private Point2f readPointAbs() {
		return readPoint().sub(1000, 1000).scale(0.01f);
	}
	
	private Point2f readPointRel() {
		return readPoint().scale(0.01f);
	}
	
	private float readCoordAbs() {
		return (parseNumericToken(getNextToken()) - 1000) * 0.01f;
	}
	
	private float readCoordRel() {
		return parseNumericToken(getNextToken()) * 0.01f;
	}

	/**
	 * Parse a numeric token.
	 */
	private float parseNumericToken(String token)
		throws NumberFormatException {
		return Float.parseFloat(token);
	}

	/**
	 * Reads the next two tokens and interprets them as a point.
	 * The values are moved by -1000/-1000 and scaled by 0.01.
	 */
	private Point2f readPoint()
		throws NumberFormatException {
		float x = parseNumericToken(getNextToken());
		float y = parseNumericToken(getNextToken());
		return new Point2f(x, y);
	}

}
