package com.osfac.dmt.geom;

/**
 * Indicates that an {@link AffineTransformation}
 * is non-invertible.
 * 
 * @author Martin Davis
 */
public class NoninvertibleTransformationException
	extends Exception
{
  public NoninvertibleTransformationException()
  {
    super();
  }
  public NoninvertibleTransformationException(String msg)
  {
    super(msg);
  }
}
