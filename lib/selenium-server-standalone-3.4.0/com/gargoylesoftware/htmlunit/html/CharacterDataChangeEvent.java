package com.gargoylesoftware.htmlunit.html;

import java.util.EventObject;

























public class CharacterDataChangeEvent
  extends EventObject
{
  private final String oldValue_;
  
  public CharacterDataChangeEvent(DomCharacterData characterData, String oldValue)
  {
    super(characterData);
    oldValue_ = oldValue;
  }
  



  public DomCharacterData getCharacterData()
  {
    return (DomCharacterData)getSource();
  }
  



  public String getOldValue()
  {
    return oldValue_;
  }
}
