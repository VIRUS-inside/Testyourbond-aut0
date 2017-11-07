package javax.xml.stream.events;

public abstract interface StartDocument
  extends XMLEvent
{
  public abstract boolean encodingSet();
  
  public abstract String getCharacterEncodingScheme();
  
  public abstract String getSystemId();
  
  public abstract String getVersion();
  
  public abstract boolean isStandalone();
  
  public abstract boolean standaloneSet();
}
