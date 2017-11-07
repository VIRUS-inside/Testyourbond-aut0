package org.hamcrest;









public abstract interface Description
{
  public static final Description NONE = new NullDescription();
  



  public abstract Description appendText(String paramString);
  



  public abstract Description appendDescriptionOf(SelfDescribing paramSelfDescribing);
  



  public abstract Description appendValue(Object paramObject);
  


  public abstract <T> Description appendValueList(String paramString1, String paramString2, String paramString3, T... paramVarArgs);
  


  public abstract <T> Description appendValueList(String paramString1, String paramString2, String paramString3, Iterable<T> paramIterable);
  


  public abstract Description appendList(String paramString1, String paramString2, String paramString3, Iterable<? extends SelfDescribing> paramIterable);
  


  public static final class NullDescription
    implements Description
  {
    public NullDescription() {}
    


    public Description appendDescriptionOf(SelfDescribing value)
    {
      return this;
    }
    

    public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values)
    {
      return this;
    }
    
    public Description appendText(String text)
    {
      return this;
    }
    
    public Description appendValue(Object value)
    {
      return this;
    }
    

    public <T> Description appendValueList(String start, String separator, String end, T... values)
    {
      return this;
    }
    

    public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values)
    {
      return this;
    }
    
    public String toString()
    {
      return "";
    }
  }
}
