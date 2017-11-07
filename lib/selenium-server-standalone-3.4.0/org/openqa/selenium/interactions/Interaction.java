package org.openqa.selenium.interactions;












public abstract class Interaction
{
  private final InputSource source;
  










  protected Interaction(InputSource source)
  {
    if (source == null) {
      throw new NullPointerException("Input source must not be null");
    }
    this.source = source;
  }
  
  protected boolean isValidFor(SourceType sourceType) {
    return source.getInputType() == sourceType;
  }
  
  public InputSource getSource() {
    return source;
  }
}
