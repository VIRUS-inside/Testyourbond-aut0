package org.apache.xerces.stax;

import javax.xml.stream.Location;

public class ImmutableLocation
  implements Location
{
  private final int fCharacterOffset;
  private final int fColumnNumber;
  private final int fLineNumber;
  private final String fPublicId;
  private final String fSystemId;
  
  public ImmutableLocation(Location paramLocation)
  {
    this(paramLocation.getCharacterOffset(), paramLocation.getColumnNumber(), paramLocation.getLineNumber(), paramLocation.getPublicId(), paramLocation.getSystemId());
  }
  
  public ImmutableLocation(int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2)
  {
    fCharacterOffset = paramInt1;
    fColumnNumber = paramInt2;
    fLineNumber = paramInt3;
    fPublicId = paramString1;
    fSystemId = paramString2;
  }
  
  public int getCharacterOffset()
  {
    return fCharacterOffset;
  }
  
  public int getColumnNumber()
  {
    return fColumnNumber;
  }
  
  public int getLineNumber()
  {
    return fLineNumber;
  }
  
  public String getPublicId()
  {
    return fPublicId;
  }
  
  public String getSystemId()
  {
    return fSystemId;
  }
}
