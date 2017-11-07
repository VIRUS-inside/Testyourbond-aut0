package com.sun.jna.platform;

import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;























public class RasterRangesUtils
{
  private static final int[] subColMasks = { 128, 64, 32, 16, 8, 4, 2, 1 };
  



  private static final Comparator<Object> COMPARATOR = new Comparator() {
    public int compare(Object o1, Object o2) {
      return x - x;
    }
  };
  










  public RasterRangesUtils() {}
  










  public static boolean outputOccupiedRanges(Raster raster, RangesOutput out)
  {
    Rectangle bounds = raster.getBounds();
    SampleModel sampleModel = raster.getSampleModel();
    boolean hasAlpha = sampleModel.getNumBands() == 4;
    

    if ((raster.getParent() == null) && (x == 0) && (y == 0))
    {

      DataBuffer data = raster.getDataBuffer();
      if (data.getNumBanks() == 1)
      {

        if ((sampleModel instanceof MultiPixelPackedSampleModel)) {
          MultiPixelPackedSampleModel packedSampleModel = (MultiPixelPackedSampleModel)sampleModel;
          if (packedSampleModel.getPixelBitStride() == 1)
          {
            return outputOccupiedRangesOfBinaryPixels(((DataBufferByte)data).getData(), width, height, out);
          }
        } else if (((sampleModel instanceof SinglePixelPackedSampleModel)) && 
          (sampleModel.getDataType() == 3))
        {
          return outputOccupiedRanges(((DataBufferInt)data).getData(), width, height, hasAlpha ? -16777216 : 16777215, out);
        }
      }
    }
    



    int[] pixels = raster.getPixels(0, 0, width, height, (int[])null);
    return outputOccupiedRanges(pixels, width, height, hasAlpha ? -16777216 : 16777215, out);
  }
  







  public static boolean outputOccupiedRangesOfBinaryPixels(byte[] binaryBits, int w, int h, RangesOutput out)
  {
    Set<Rectangle> rects = new HashSet();
    Set<Rectangle> prevLine = Collections.EMPTY_SET;
    int scanlineBytes = binaryBits.length / h;
    for (int row = 0; row < h; row++) {
      Set<Rectangle> curLine = new TreeSet(COMPARATOR);
      int rowOffsetBytes = row * scanlineBytes;
      int startCol = -1;
      
      for (int byteCol = 0; byteCol < scanlineBytes; byteCol++) {
        int firstByteCol = byteCol << 3;
        byte byteColBits = binaryBits[(rowOffsetBytes + byteCol)];
        if (byteColBits == 0)
        {
          if (startCol >= 0)
          {
            curLine.add(new Rectangle(startCol, row, firstByteCol - startCol, 1));
            startCol = -1;
          }
        } else if (byteColBits == 255)
        {
          if (startCol < 0)
          {
            startCol = firstByteCol;
          }
        }
        else {
          for (int subCol = 0; subCol < 8; subCol++) {
            int col = firstByteCol | subCol;
            if ((byteColBits & subColMasks[subCol]) != 0) {
              if (startCol < 0)
              {
                startCol = col;
              }
            }
            else if (startCol >= 0)
            {
              curLine.add(new Rectangle(startCol, row, col - startCol, 1));
              startCol = -1;
            }
          }
        }
      }
      
      if (startCol >= 0)
      {
        curLine.add(new Rectangle(startCol, row, w - startCol, 1));
      }
      Set<Rectangle> unmerged = mergeRects(prevLine, curLine);
      rects.addAll(unmerged);
      prevLine = curLine;
    }
    
    rects.addAll(prevLine);
    for (Iterator<Rectangle> i = rects.iterator(); i.hasNext();) {
      Rectangle r = (Rectangle)i.next();
      if (!out.outputRange(x, y, width, height)) {
        return false;
      }
    }
    return true;
  }
  









  public static boolean outputOccupiedRanges(int[] pixels, int w, int h, int occupationMask, RangesOutput out)
  {
    Set<Rectangle> rects = new HashSet();
    Set<Rectangle> prevLine = Collections.EMPTY_SET;
    for (int row = 0; row < h; row++) {
      Set<Rectangle> curLine = new TreeSet(COMPARATOR);
      int idxOffset = row * w;
      int startCol = -1;
      
      for (int col = 0; col < w; col++) {
        if ((pixels[(idxOffset + col)] & occupationMask) != 0) {
          if (startCol < 0) {
            startCol = col;
          }
        }
        else if (startCol >= 0)
        {
          curLine.add(new Rectangle(startCol, row, col - startCol, 1));
          startCol = -1;
        }
      }
      
      if (startCol >= 0)
      {
        curLine.add(new Rectangle(startCol, row, w - startCol, 1));
      }
      Set<Rectangle> unmerged = mergeRects(prevLine, curLine);
      rects.addAll(unmerged);
      prevLine = curLine;
    }
    
    rects.addAll(prevLine);
    for (Iterator<Rectangle> i = rects.iterator(); i.hasNext();) {
      Rectangle r = (Rectangle)i.next();
      if (!out.outputRange(x, y, width, height)) {
        return false;
      }
    }
    return true;
  }
  
  private static Set<Rectangle> mergeRects(Set<Rectangle> prev, Set<Rectangle> current) {
    Set<Rectangle> unmerged = new HashSet(prev);
    if ((!prev.isEmpty()) && (!current.isEmpty())) {
      Rectangle[] pr = (Rectangle[])prev.toArray(new Rectangle[prev.size()]);
      Rectangle[] cr = (Rectangle[])current.toArray(new Rectangle[current.size()]);
      int ipr = 0;
      int icr = 0;
      while ((ipr < pr.length) && (icr < cr.length)) {
        while (x < x) {
          icr++; if (icr == cr.length) {
            return unmerged;
          }
        }
        if ((x == x) && (width == width)) {
          unmerged.remove(pr[ipr]);
          y = y;
          height += 1;
          icr++;
        }
        else {
          ipr++;
        }
      }
    }
    return unmerged;
  }
  
  public static abstract interface RangesOutput
  {
    public abstract boolean outputRange(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  }
}
