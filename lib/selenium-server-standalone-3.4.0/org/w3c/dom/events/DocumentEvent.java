package org.w3c.dom.events;

import org.w3c.dom.DOMException;

public abstract interface DocumentEvent
{
  public abstract Event createEvent(String paramString)
    throws DOMException;
}
