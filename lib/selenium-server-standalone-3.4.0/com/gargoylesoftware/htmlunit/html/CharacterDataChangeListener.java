package com.gargoylesoftware.htmlunit.html;

import java.io.Serializable;

public abstract interface CharacterDataChangeListener
  extends Serializable
{
  public abstract void characterDataChanged(CharacterDataChangeEvent paramCharacterDataChangeEvent);
}
