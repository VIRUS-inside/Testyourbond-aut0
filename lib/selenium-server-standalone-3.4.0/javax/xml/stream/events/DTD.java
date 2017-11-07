package javax.xml.stream.events;

import java.util.List;

public abstract interface DTD
  extends XMLEvent
{
  public abstract String getDocumentTypeDeclaration();
  
  public abstract List getEntities();
  
  public abstract List getNotations();
  
  public abstract Object getProcessedDTD();
}
