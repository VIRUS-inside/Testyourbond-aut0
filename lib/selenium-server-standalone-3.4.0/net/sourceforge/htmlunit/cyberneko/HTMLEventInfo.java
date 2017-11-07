package net.sourceforge.htmlunit.cyberneko;







public abstract interface HTMLEventInfo
{
  public abstract int getBeginLineNumber();
  






  public abstract int getBeginColumnNumber();
  






  public abstract int getBeginCharacterOffset();
  






  public abstract int getEndLineNumber();
  





  public abstract int getEndColumnNumber();
  





  public abstract int getEndCharacterOffset();
  





  public abstract boolean isSynthesized();
  





  public static class SynthesizedItem
    implements HTMLEventInfo
  {
    public SynthesizedItem() {}
    





    public int getBeginLineNumber()
    {
      return -1;
    }
    

    public int getBeginColumnNumber()
    {
      return -1;
    }
    

    public int getBeginCharacterOffset()
    {
      return -1;
    }
    

    public int getEndLineNumber()
    {
      return -1;
    }
    

    public int getEndColumnNumber()
    {
      return -1;
    }
    

    public int getEndCharacterOffset()
    {
      return -1;
    }
    



    public boolean isSynthesized()
    {
      return true;
    }
    





    public String toString()
    {
      return "synthesized";
    }
  }
}
