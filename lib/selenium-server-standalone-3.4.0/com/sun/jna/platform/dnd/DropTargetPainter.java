package com.sun.jna.platform.dnd;

import java.awt.Point;
import java.awt.dnd.DropTargetEvent;

public abstract interface DropTargetPainter
{
  public abstract void paintDropTarget(DropTargetEvent paramDropTargetEvent, int paramInt, Point paramPoint);
}
