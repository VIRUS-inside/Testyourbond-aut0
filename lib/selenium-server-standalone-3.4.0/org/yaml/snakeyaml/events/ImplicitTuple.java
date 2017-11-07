package org.yaml.snakeyaml.events;








public class ImplicitTuple
{
  private final boolean plain;
  






  private final boolean nonPlain;
  






  public ImplicitTuple(boolean plain, boolean nonplain)
  {
    this.plain = plain;
    nonPlain = nonplain;
  }
  



  public boolean canOmitTagInPlainScalar()
  {
    return plain;
  }
  



  public boolean canOmitTagInNonPlainScalar()
  {
    return nonPlain;
  }
  
  public boolean bothFalse() {
    return (!plain) && (!nonPlain);
  }
  
  public String toString()
  {
    return "implicit=[" + plain + ", " + nonPlain + "]";
  }
}
