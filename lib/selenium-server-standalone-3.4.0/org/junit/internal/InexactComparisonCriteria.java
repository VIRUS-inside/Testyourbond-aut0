package org.junit.internal;

import org.junit.Assert;

public class InexactComparisonCriteria extends ComparisonCriteria {
  public Object fDelta;
  
  public InexactComparisonCriteria(double delta) {
    fDelta = Double.valueOf(delta);
  }
  
  public InexactComparisonCriteria(float delta) {
    fDelta = Float.valueOf(delta);
  }
  
  protected void assertElementsEqual(Object expected, Object actual)
  {
    if ((expected instanceof Double)) {
      Assert.assertEquals(((Double)expected).doubleValue(), ((Double)actual).doubleValue(), ((Double)fDelta).doubleValue());
    } else {
      Assert.assertEquals(((Float)expected).floatValue(), ((Float)actual).floatValue(), ((Float)fDelta).floatValue());
    }
  }
}
