package java_cup.runtime;








public class Symbol
{
  public int sym;
  






  public int parse_state;
  






  public Symbol(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    this(paramInt1);
    left = paramInt2;
    right = paramInt3;
    value = paramObject;
  }
  



  public Symbol(int paramInt, Object paramObject)
  {
    this(paramInt, -1, -1, paramObject);
  }
  



  public Symbol(int paramInt1, int paramInt2, int paramInt3)
  {
    this(paramInt1, paramInt2, paramInt3, null);
  }
  



  public Symbol(int paramInt)
  {
    this(paramInt, -1);
    left = -1;
    right = -1;
    value = null;
  }
  



  Symbol(int paramInt1, int paramInt2)
  {
    sym = paramInt1;
    parse_state = paramInt2;
  }
  














  boolean used_by_parser = false;
  

  public int left;
  
  public int right;
  
  public Object value;
  

  public String toString()
  {
    return "#" + sym;
  }
}
