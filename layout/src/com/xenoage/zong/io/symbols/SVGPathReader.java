package com.xenoage.zong.io.symbols;

import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.geom.Point2f.p;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;


/**
 * This class creates a path from a given SVG path.
 * It must be subclassed for the implementation of the geometry functions.
 * 
 * This class is not thread safe. Only one symbol may be read at the same time.
 *
 * @author Andreas Wenger
 */
public abstract class SVGPathReader
{
  
	String d;
	int pos;
  
  
  /**
   * Creates a path from the given d attribute value of a SVG path element.
   * The type of the path is implementation dependent.
   * The path and its bounding rect is returned.
   */
  public Tuple2<Object, Rectangle2f> read(String d)
  {
  	this.d = d;
    pos = 0;
    init();
    
    //parse commands
    char tokenChar = '?';
    String token = getNextToken();
    Point2f p, p1, p2, p3;
    float x, y;
    while (token != null)
    {
      Point2f currentPoint = getCurrentPoint();
      char nextTokenChar = token.charAt(0);
      
      if (Character.isDigit(nextTokenChar) || nextTokenChar == '-' ||
      	nextTokenChar == '+') {
      	//number. reuse last command (if not 'M' or 'm' - then it is 'L' or 'l'. see SVG spec)
      	pos -= token.length();
      	if (tokenChar == 'M')
      		tokenChar = 'L';
      	else if (tokenChar == 'm')
      		tokenChar = 'l';
      } else {
      	//next command
      	tokenChar = nextTokenChar;
      }

      switch (tokenChar)
      {
	      //MoveTo (absolute)
      	case 'M':
	        p = readPoint();
	        moveTo(p);
	        break;
	      //MoveTo (relative)
      	case 'm':
	        p = readPoint();
	        moveTo(currentPoint.add(p));
	        break;
	      //ClosePath
      	case 'Z': case 'z':
	        closePath();
	        break;
	      //LineTo (absolute)
      	case 'L':
	        p = readPoint();
	        lineTo(p);
	        break;
	      //LineTo (relative)
      	case 'l':
      		p = readPoint();
	        lineTo(currentPoint.add(p));
	        break;
	      //Horizontal LineTo (absolute)
      	case 'H':
	        x = parseNumericToken(getNextToken());
	        lineTo(p(x, currentPoint.y));
	        break;
	      //Horizontal LineTo (relative)
      	case 'h':
	        x = parseNumericToken(getNextToken());
	        lineTo(p(currentPoint.x + x, currentPoint.y));
	        break;
	      //Vertical LineTo (absolute)
      	case 'V':
	        y = parseNumericToken(getNextToken());
	        lineTo(p(currentPoint.x, y));
	        break;
	      //Vertical LineTo (relative)
      	case 'v':
	        y = parseNumericToken(getNextToken());
	        lineTo(p(currentPoint.x, currentPoint.y + y));
	        break;
	      //CurveTo (absolute)
      	case 'C':
	        p1 = readPoint();
	        p2 = readPoint();
	        p3 = readPoint();
	        curveTo(p1, p2, p3);
	        break;
	      //CurveTo (relative)
      	case 'c':
	        p1 = readPoint();
	        p2 = readPoint();
	        p3 = readPoint();
	        curveTo(currentPoint.add(p1), currentPoint.add(p2), currentPoint.add(p3));
	        break;
	      //Quadratic CurveTo (absolute)
      	case 'Q':
	        p1 = readPoint();
	        p2 = readPoint();
	        quadTo(p1, p2);
	        break;
	      //Quadratic CurveTo (relative)
      	case 'q':
	        p1 = readPoint();
	        p2 = readPoint();
	        quadTo(currentPoint.add(p1), currentPoint.add(p2));
	        break;
	      //not implemented commands
      	case 'T': case 't': case 'S': case 's': case 'A': case 'a':
	        throw new IllegalStateException("SVG command \"" + token +
	          "\" not implemented yet.");
	      //unknown command
	      default:
	        throw new IllegalStateException("Unknown SVG command: \"" + token + "\"");
      }
      
      token = getNextToken();
    }
    finish();
    return t(getPath(), getBoundingRect());
  }
  
  
  /**
   * Gets the resulting path object, whose type is implementation dependent.
   */
  public abstract Object getPath();
  
  
  /**
   * Initializes the path, before geometry is added.
   */
  abstract void init();
  
  
  /**
   * Gets the coordinates most recently added to the end of the path.
   */
  abstract Point2f getCurrentPoint();
  
  
  /**
   * Closes the current subpath. 
   */
  abstract void closePath();
  
  
  /**
   * Draws a straight line to the given point.
   */
  abstract void lineTo(Point2f p);
  
  
  /**
   * Moves to the given point without drawing.
   */
  abstract void moveTo(Point2f p);
  
  
  /**
   * Adds a curved segment, defined by the three given points.
   */
  abstract void curveTo(Point2f p1, Point2f p2, Point2f p3);
  
  
  /**
   * Adds a curved segment, defined by the two given points.
   */
  abstract void quadTo(Point2f p1, Point2f p2);
  
  
  /**
   * Perform postprocessing on the resulting path.
   */
  abstract void finish();
  
  
  /**
   * Gets the bounding rect of the given path.
   */
  abstract Rectangle2f getBoundingRect();
  
  
  /**
   * Gets the next token of the d-String, starting at pos.
   * Returns null, when there is no token any more.
   */
  private String getNextToken()
  {
    //skip " " and "," and "\n" and "\r".
    while (pos < d.length() && isWhitespace(d.charAt(pos)))
    {
      pos++;
    }
    
    //when the end of the String is reached, return null
    if (pos >= d.length())
      return null;
    
    //find the end of the token
    char c0 = d.charAt(pos);
    boolean c0Numeric = isNumeric(c0);
    int posEnd = pos;
    for (int i = pos + 1; i < d.length(); i++)
    {
      char ci = d.charAt(i);
      boolean ciNumeric = isNumeric(ci);
      
      //if c0 is numeric, but c1 not (or the other way round), the token is finished
      if (c0Numeric != ciNumeric)
      {
        break;
      }
      
      //if ci is whitespace, the token is finished
      if (isWhitespace(ci))
      {
        break;
      }
      
      posEnd++;
    }
    String ret = d.substring(pos, posEnd + 1);
    
    //new starting point is current end point
    pos = posEnd + 1;
    
    return ret;
  }
  
  
  /**
   * Returns true, if the given char is a digit, a dot,
   * a plus or a minus.
   */
  private boolean isNumeric(char c)
  {
    return (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' ||
      c == '5' || c == '6' || c == '7' || c == '8' || c == '9' ||
      c == '.' || c == '-' || c == '+');
  }
  
  
  /**
   * Returns true, if the given char is a whitespace
   * (' ', ',', '\n', '\r').
   */
  private boolean isWhitespace(char c)
  {
    return (c == ' ' || c == ',' || c == '\n' || c == '\r');
  }
  
  
  /**
   * Parse a numeric token.
   */
  private float parseNumericToken(String token)
    throws NumberFormatException
  {
    return Float.parseFloat(token);
  }
  
  
  /**
   * Reads the next two tokens and interprets them as a point.
   */
  private Point2f readPoint()
    throws NumberFormatException
  {
    String x = getNextToken();
    String y = getNextToken();
    return new Point2f(parseNumericToken(x), parseNumericToken(y));
  }

}
