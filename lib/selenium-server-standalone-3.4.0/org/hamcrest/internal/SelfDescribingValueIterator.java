package org.hamcrest.internal;

import java.util.Iterator;

public class SelfDescribingValueIterator<T> implements Iterator<org.hamcrest.SelfDescribing>
{
  private Iterator<T> values;
  
  public SelfDescribingValueIterator(Iterator<T> values)
  {
    this.values = values;
  }
  
  public boolean hasNext()
  {
    return values.hasNext();
  }
  
  public org.hamcrest.SelfDescribing next()
  {
    return new SelfDescribingValue(values.next());
  }
  
  public void remove()
  {
    values.remove();
  }
}
