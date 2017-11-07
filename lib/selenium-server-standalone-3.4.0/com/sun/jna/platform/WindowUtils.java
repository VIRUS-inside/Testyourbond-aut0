package com.sun.jna.platform;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.GC;
import com.sun.jna.platform.unix.X11.Pixmap;
import com.sun.jna.platform.unix.X11.VisualID;
import com.sun.jna.platform.unix.X11.Window;
import com.sun.jna.platform.unix.X11.WindowByReference;
import com.sun.jna.platform.unix.X11.XImage;
import com.sun.jna.platform.unix.X11.XRectangle;
import com.sun.jna.platform.unix.X11.XVisualInfo;
import com.sun.jna.platform.unix.X11.XWindowAttributes;
import com.sun.jna.platform.unix.X11.Xext;
import com.sun.jna.platform.unix.X11.Xrender;
import com.sun.jna.platform.unix.X11.Xrender.XRenderDirectFormat;
import com.sun.jna.platform.unix.X11.Xrender.XRenderPictFormat;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HRGN;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser.BLENDFUNCTION;
import com.sun.jna.platform.win32.WinUser.SIZE;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;



















































public class WindowUtils
{
  private static final String TRANSPARENT_OLD_BG = "transparent-old-bg";
  private static final String TRANSPARENT_OLD_OPAQUE = "transparent-old-opaque";
  private static final String TRANSPARENT_ALPHA = "transparent-alpha";
  public static final Shape MASK_NONE = null;
  


  public WindowUtils() {}
  



  private static class HeavyweightForcer
    extends Window
  {
    private static final long serialVersionUID = 1L;
    


    private final boolean packed;
    


    public HeavyweightForcer(Window parent)
    {
      super();
      pack();
      packed = true;
    }
    


    public boolean isVisible()
    {
      return packed;
    }
    
    public Rectangle getBounds() {
      return getOwner().getBounds();
    }
  }
  
  protected static class RepaintTrigger
    extends JComponent
  {
    private static final long serialVersionUID = 1L;
    
    protected class Listener extends WindowAdapter implements ComponentListener, HierarchyListener, AWTEventListener
    {
      protected Listener() {}
      
      public void windowOpened(WindowEvent e)
      {
        repaint();
      }
      
      public void componentHidden(ComponentEvent e) {}
      
      public void componentMoved(ComponentEvent e) {}
      
      public void componentResized(ComponentEvent e) {
        setSize(getParent().getSize());
        repaint();
      }
      
      public void componentShown(ComponentEvent e) {
        repaint();
      }
      
      public void hierarchyChanged(HierarchyEvent e) {
        repaint();
      }
      
      public void eventDispatched(AWTEvent e) {
        if ((e instanceof MouseEvent)) {
          Component src = ((MouseEvent)e).getComponent();
          if ((src != null) && (SwingUtilities.isDescendingFrom(src, content)))
          {
            MouseEvent me = SwingUtilities.convertMouseEvent(src, (MouseEvent)e, content);
            Component c = SwingUtilities.getDeepestComponentAt(content, me.getX(), me.getY());
            if (c != null) {
              setCursor(c.getCursor());
            }
          }
        }
      }
    }
    
    private final Listener listener = createListener();
    private final JComponent content;
    private Rectangle dirty;
    
    public RepaintTrigger(JComponent content) { this.content = content; }
    
    public void addNotify()
    {
      super.addNotify();
      Window w = SwingUtilities.getWindowAncestor(this);
      setSize(getParent().getSize());
      w.addComponentListener(listener);
      w.addWindowListener(listener);
      Toolkit.getDefaultToolkit().addAWTEventListener(listener, 48L);
    }
    
    public void removeNotify() {
      Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
      Window w = SwingUtilities.getWindowAncestor(this);
      w.removeComponentListener(listener);
      w.removeWindowListener(listener);
      super.removeNotify();
    }
    
    protected void paintComponent(Graphics g)
    {
      Rectangle bounds = g.getClipBounds();
      if ((dirty == null) || (!dirty.contains(bounds))) {
        if (dirty == null) {
          dirty = bounds;
        }
        else {
          dirty = dirty.union(bounds);
        }
        content.repaint(dirty);
      }
      else {
        dirty = null;
      }
    }
    

    protected Listener createListener() { return new Listener(); }
  }
  
  public static abstract class NativeWindowUtils {
    public NativeWindowUtils() {}
    
    protected abstract class TransparentContentPane extends JPanel implements AWTEventListener {
      private static final long serialVersionUID = 1L;
      private boolean transparent;
      
      public TransparentContentPane(Container oldContent) {
        super();
        add(oldContent, "Center");
        setTransparent(true);
        if ((oldContent instanceof JPanel))
          ((JComponent)oldContent).setOpaque(false);
      }
      
      public void addNotify() {
        super.addNotify();
        Toolkit.getDefaultToolkit().addAWTEventListener(this, 2L);
      }
      
      public void removeNotify() { Toolkit.getDefaultToolkit().removeAWTEventListener(this);
        super.removeNotify();
      }
      
      public void setTransparent(boolean transparent) { this.transparent = transparent;
        setOpaque(!transparent);
        setDoubleBuffered(!transparent);
        repaint();
      }
      
      public void eventDispatched(AWTEvent e) { if ((e.getID() == 300) && (SwingUtilities.isDescendingFrom(((ContainerEvent)e).getChild(), this)))
        {
          Component child = ((ContainerEvent)e).getChild();
          setDoubleBuffered(child, false);
        }
      }
      
      public void paint(Graphics gr) { if (transparent) {
          Rectangle r = gr.getClipBounds();
          int w = width;
          int h = height;
          if ((getWidth() > 0) && (getHeight() > 0)) {
            BufferedImage buf = new BufferedImage(w, h, 3);
            

            Graphics2D g = buf.createGraphics();
            g.setComposite(AlphaComposite.Clear);
            g.fillRect(0, 0, w, h);
            g.dispose();
            
            g = buf.createGraphics();
            g.translate(-x, -y);
            super.paint(g);
            g.dispose();
            
            paintDirect(buf, r);
          }
        }
        else {
          super.paint(gr);
        }
      }
      

      protected abstract void paintDirect(BufferedImage paramBufferedImage, Rectangle paramRectangle);
    }
    
    protected Window getWindow(Component c)
    {
      return (c instanceof Window) ? (Window)c : SwingUtilities.getWindowAncestor(c);
    }
    



    protected void whenDisplayable(Component w, final Runnable action)
    {
      if ((w.isDisplayable()) && ((!WindowUtils.Holder.requiresVisible) || (w.isVisible()))) {
        action.run();
      }
      else if (WindowUtils.Holder.requiresVisible) {
        getWindow(w).addWindowListener(new WindowAdapter() {
          public void windowOpened(WindowEvent e) {
            e.getWindow().removeWindowListener(this);
            action.run();
          }
          
          public void windowClosed(WindowEvent e) { e.getWindow().removeWindowListener(this);
          }

        });
      }
      else
      {
        w.addHierarchyListener(new HierarchyListener() {
          public void hierarchyChanged(HierarchyEvent e) {
            if (((e.getChangeFlags() & 0x2) != 0L) && (e.getComponent().isDisplayable()))
            {
              e.getComponent().removeHierarchyListener(this);
              action.run();
            }
          }
        });
      }
    }
    
    protected Raster toRaster(Shape mask) {
      Raster raster = null;
      if (mask != WindowUtils.MASK_NONE) {
        Rectangle bounds = mask.getBounds();
        if ((width > 0) && (height > 0)) {
          BufferedImage clip = new BufferedImage(x + width, y + height, 12);
          


          Graphics2D g = clip.createGraphics();
          g.setColor(Color.black);
          g.fillRect(0, 0, x + width, y + height);
          g.setColor(Color.white);
          g.fill(mask);
          raster = clip.getRaster();
        }
      }
      return raster;
    }
    
    protected Raster toRaster(Component c, Icon mask) {
      Raster raster = null;
      if (mask != null) {
        Rectangle bounds = new Rectangle(0, 0, mask.getIconWidth(), mask.getIconHeight());
        
        BufferedImage clip = new BufferedImage(width, height, 2);
        

        Graphics2D g = clip.createGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, width, height);
        g.setComposite(AlphaComposite.SrcOver);
        mask.paintIcon(c, g, 0, 0);
        raster = clip.getAlphaRaster();
      }
      return raster;
    }
    
    protected Shape toShape(Raster raster) {
      final Area area = new Area(new Rectangle(0, 0, 0, 0));
      RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput() {
        public boolean outputRange(int x, int y, int w, int h) {
          area.add(new Area(new Rectangle(x, y, w, h)));
          return true;
        }
      });
      return area;
    }
    



    public void setWindowAlpha(Window w, float alpha) {}
    



    public boolean isWindowAlphaSupported()
    {
      return false;
    }
    
    public GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration()
    {
      GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
      
      GraphicsDevice dev = env.getDefaultScreenDevice();
      return dev.getDefaultConfiguration();
    }
    



    public void setWindowTransparent(Window w, boolean transparent) {}
    



    protected void setDoubleBuffered(Component root, boolean buffered)
    {
      if ((root instanceof JComponent)) {
        ((JComponent)root).setDoubleBuffered(buffered);
      }
      if (((root instanceof JRootPane)) && (buffered)) {
        ((JRootPane)root).setDoubleBuffered(true);
      }
      else if ((root instanceof Container)) {
        Component[] kids = ((Container)root).getComponents();
        for (int i = 0; i < kids.length; i++) {
          setDoubleBuffered(kids[i], buffered);
        }
      }
    }
    
    protected void setLayersTransparent(Window w, boolean transparent)
    {
      Color bg = transparent ? new Color(0, 0, 0, 0) : null;
      if ((w instanceof RootPaneContainer)) {
        RootPaneContainer rpc = (RootPaneContainer)w;
        JRootPane root = rpc.getRootPane();
        JLayeredPane lp = root.getLayeredPane();
        Container c = root.getContentPane();
        JComponent content = (c instanceof JComponent) ? (JComponent)c : null;
        
        if (transparent) {
          lp.putClientProperty("transparent-old-opaque", Boolean.valueOf(lp.isOpaque()));
          
          lp.setOpaque(false);
          root.putClientProperty("transparent-old-opaque", Boolean.valueOf(root.isOpaque()));
          
          root.setOpaque(false);
          if (content != null) {
            content.putClientProperty("transparent-old-opaque", Boolean.valueOf(content.isOpaque()));
            
            content.setOpaque(false);
          }
          root.putClientProperty("transparent-old-bg", root.getParent().getBackground());
        }
        else
        {
          lp.setOpaque(Boolean.TRUE.equals(lp.getClientProperty("transparent-old-opaque")));
          lp.putClientProperty("transparent-old-opaque", null);
          root.setOpaque(Boolean.TRUE.equals(root.getClientProperty("transparent-old-opaque")));
          root.putClientProperty("transparent-old-opaque", null);
          if (content != null) {
            content.setOpaque(Boolean.TRUE.equals(content.getClientProperty("transparent-old-opaque")));
            content.putClientProperty("transparent-old-opaque", null);
          }
          bg = (Color)root.getClientProperty("transparent-old-bg");
          root.putClientProperty("transparent-old-bg", null);
        }
      }
      w.setBackground(bg);
    }
    


    protected void setMask(Component c, Raster raster)
    {
      throw new UnsupportedOperationException("Window masking is not available");
    }
    




    protected void setWindowMask(Component w, Raster raster)
    {
      if (w.isLightweight())
        throw new IllegalArgumentException("Component must be heavyweight: " + w);
      setMask(w, raster);
    }
    
    public void setWindowMask(Component w, Shape mask)
    {
      setWindowMask(w, toRaster(mask));
    }
    



    public void setWindowMask(Component w, Icon mask)
    {
      setWindowMask(w, toRaster(w, mask));
    }
    




    protected void setForceHeavyweightPopups(Window w, boolean force)
    {
      if (!(w instanceof WindowUtils.HeavyweightForcer)) {
        Window[] owned = w.getOwnedWindows();
        for (int i = 0; i < owned.length; i++) {
          if ((owned[i] instanceof WindowUtils.HeavyweightForcer)) {
            if (force)
              return;
            owned[i].dispose();
          }
        }
        Boolean b = Boolean.valueOf(System.getProperty("jna.force_hw_popups", "true"));
        if ((force) && (b.booleanValue())) {
          new WindowUtils.HeavyweightForcer(w);
        }
      }
    }
  }
  
  private static class Holder
  {
    public static boolean requiresVisible;
    public static final WindowUtils.NativeWindowUtils INSTANCE;
    
    private Holder() {}
    
    static
    {
      if (Platform.isWindows()) {
        INSTANCE = new WindowUtils.W32WindowUtils(null);
      }
      else if (Platform.isMac()) {
        INSTANCE = new WindowUtils.MacWindowUtils(null);
      }
      else if (Platform.isX11()) {
        INSTANCE = new WindowUtils.X11WindowUtils(null);
        requiresVisible = System.getProperty("java.version").matches("^1\\.4\\..*");
      }
      else
      {
        String os = System.getProperty("os.name");
        throw new UnsupportedOperationException("No support for " + os);
      }
    }
  }
  

  private static NativeWindowUtils getInstance() { return Holder.INSTANCE; }
  
  private static class W32WindowUtils extends WindowUtils.NativeWindowUtils {
    private W32WindowUtils() {}
    
    private WinDef.HWND getHWnd(Component w) { WinDef.HWND hwnd = new WinDef.HWND();
      hwnd.setPointer(Native.getComponentPointer(w));
      return hwnd;
    }
    



    public boolean isWindowAlphaSupported()
    {
      return Boolean.getBoolean("sun.java2d.noddraw");
    }
    
    private boolean usingUpdateLayeredWindow(Window w)
    {
      if ((w instanceof RootPaneContainer)) {
        JRootPane root = ((RootPaneContainer)w).getRootPane();
        return root.getClientProperty("transparent-old-bg") != null;
      }
      return false;
    }
    


    private void storeAlpha(Window w, byte alpha)
    {
      if ((w instanceof RootPaneContainer)) {
        JRootPane root = ((RootPaneContainer)w).getRootPane();
        Byte b = alpha == -1 ? null : new Byte(alpha);
        root.putClientProperty("transparent-alpha", b);
      }
    }
    
    private byte getAlpha(Window w)
    {
      if ((w instanceof RootPaneContainer)) {
        JRootPane root = ((RootPaneContainer)w).getRootPane();
        Byte b = (Byte)root.getClientProperty("transparent-alpha");
        if (b != null) {
          return b.byteValue();
        }
      }
      return -1;
    }
    
    public void setWindowAlpha(final Window w, final float alpha) {
      if (!isWindowAlphaSupported()) {
        throw new UnsupportedOperationException("Set sun.java2d.noddraw=true to enable transparent windows");
      }
      whenDisplayable(w, new Runnable() {
        public void run() {
          WinDef.HWND hWnd = WindowUtils.W32WindowUtils.this.getHWnd(w);
          User32 user = User32.INSTANCE;
          int flags = user.GetWindowLong(hWnd, -20);
          byte level = (byte)((int)(255.0F * alpha) & 0xFF);
          if (WindowUtils.W32WindowUtils.this.usingUpdateLayeredWindow(w))
          {

            WinUser.BLENDFUNCTION blend = new WinUser.BLENDFUNCTION();
            SourceConstantAlpha = level;
            AlphaFormat = 1;
            user.UpdateLayeredWindow(hWnd, null, null, null, null, null, 0, blend, 2);


          }
          else if (alpha == 1.0F) {
            flags &= 0xFFF7FFFF;
            user.SetWindowLong(hWnd, -20, flags);
          }
          else {
            flags |= 0x80000;
            user.SetWindowLong(hWnd, -20, flags);
            user.SetLayeredWindowAttributes(hWnd, 0, level, 2);
          }
          
          setForceHeavyweightPopups(w, alpha != 1.0F);
          WindowUtils.W32WindowUtils.this.storeAlpha(w, level);
        }
      });
    }
    

    private class W32TransparentContentPane
      extends WindowUtils.NativeWindowUtils.TransparentContentPane
    {
      private static final long serialVersionUID = 1L;
      
      private WinDef.HDC memDC;
      private WinDef.HBITMAP hBitmap;
      private Pointer pbits;
      private Dimension bitmapSize;
      
      public W32TransparentContentPane(Container content) { super(content); }
      
      private void disposeBackingStore() {
        GDI32 gdi = GDI32.INSTANCE;
        if (hBitmap != null) {
          gdi.DeleteObject(hBitmap);
          hBitmap = null;
        }
        if (memDC != null) {
          gdi.DeleteDC(memDC);
          memDC = null;
        }
      }
      
      public void removeNotify() { super.removeNotify();
        disposeBackingStore();
      }
      
      public void setTransparent(boolean transparent) { super.setTransparent(transparent);
        if (!transparent) {
          disposeBackingStore();
        }
      }
      
      protected void paintDirect(BufferedImage buf, Rectangle bounds) {
        Window win = SwingUtilities.getWindowAncestor(this);
        GDI32 gdi = GDI32.INSTANCE;
        User32 user = User32.INSTANCE;
        int x = x;
        int y = y;
        Point origin = SwingUtilities.convertPoint(this, x, y, win);
        int w = width;
        int h = height;
        int ww = win.getWidth();
        int wh = win.getHeight();
        WinDef.HDC screenDC = user.GetDC(null);
        WinNT.HANDLE oldBitmap = null;
        try {
          if (memDC == null) {
            memDC = gdi.CreateCompatibleDC(screenDC);
          }
          if ((hBitmap == null) || (!win.getSize().equals(bitmapSize))) {
            if (hBitmap != null) {
              gdi.DeleteObject(hBitmap);
              hBitmap = null;
            }
            WinGDI.BITMAPINFO bmi = new WinGDI.BITMAPINFO();
            bmiHeader.biWidth = ww;
            bmiHeader.biHeight = wh;
            bmiHeader.biPlanes = 1;
            bmiHeader.biBitCount = 32;
            bmiHeader.biCompression = 0;
            bmiHeader.biSizeImage = (ww * wh * 4);
            PointerByReference ppbits = new PointerByReference();
            hBitmap = gdi.CreateDIBSection(memDC, bmi, 0, ppbits, null, 0);
            

            pbits = ppbits.getValue();
            bitmapSize = new Dimension(ww, wh);
          }
          oldBitmap = gdi.SelectObject(memDC, hBitmap);
          Raster raster = buf.getData();
          int[] pixel = new int[4];
          int[] bits = new int[w];
          for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
              raster.getPixel(col, row, pixel);
              int alpha = (pixel[3] & 0xFF) << 24;
              int red = pixel[2] & 0xFF;
              int green = (pixel[1] & 0xFF) << 8;
              int blue = (pixel[0] & 0xFF) << 16;
              bits[col] = (alpha | red | green | blue);
            }
            int v = wh - (y + row) - 1;
            pbits.write((v * ww + x) * 4, bits, 0, bits.length);
          }
          WinUser.SIZE winSize = new WinUser.SIZE();
          cx = win.getWidth();
          cy = win.getHeight();
          WinDef.POINT winLoc = new WinDef.POINT();
          x = win.getX();
          y = win.getY();
          WinDef.POINT srcLoc = new WinDef.POINT();
          WinUser.BLENDFUNCTION blend = new WinUser.BLENDFUNCTION();
          WinDef.HWND hWnd = WindowUtils.W32WindowUtils.this.getHWnd(win);
          
          ByteByReference bref = new ByteByReference();
          IntByReference iref = new IntByReference();
          byte level = WindowUtils.W32WindowUtils.this.getAlpha(win);
          try
          {
            if ((user.GetLayeredWindowAttributes(hWnd, null, bref, iref)) && ((iref.getValue() & 0x2) != 0))
            {
              level = bref.getValue();
            }
          }
          catch (UnsatisfiedLinkError e) {}
          
          SourceConstantAlpha = level;
          AlphaFormat = 1;
          user.UpdateLayeredWindow(hWnd, screenDC, winLoc, winSize, memDC, srcLoc, 0, blend, 2);
        }
        finally {
          user.ReleaseDC(null, screenDC);
          if ((memDC != null) && (oldBitmap != null)) {
            gdi.SelectObject(memDC, oldBitmap);
          }
        }
      }
    }
    



    public void setWindowTransparent(final Window w, final boolean transparent)
    {
      if (!(w instanceof RootPaneContainer)) {
        throw new IllegalArgumentException("Window must be a RootPaneContainer");
      }
      if (!isWindowAlphaSupported()) {
        throw new UnsupportedOperationException("Set sun.java2d.noddraw=true to enable transparent windows");
      }
      boolean isTransparent = (w.getBackground() != null) && (w.getBackground().getAlpha() == 0);
      
      if (transparent == isTransparent)
        return;
      whenDisplayable(w, new Runnable() {
        public void run() {
          User32 user = User32.INSTANCE;
          WinDef.HWND hWnd = WindowUtils.W32WindowUtils.this.getHWnd(w);
          int flags = user.GetWindowLong(hWnd, -20);
          JRootPane root = ((RootPaneContainer)w).getRootPane();
          JLayeredPane lp = root.getLayeredPane();
          Container content = root.getContentPane();
          if ((content instanceof WindowUtils.W32WindowUtils.W32TransparentContentPane)) {
            ((WindowUtils.W32WindowUtils.W32TransparentContentPane)content).setTransparent(transparent);
          }
          else if (transparent) {
            WindowUtils.W32WindowUtils.W32TransparentContentPane w32content = new WindowUtils.W32WindowUtils.W32TransparentContentPane(WindowUtils.W32WindowUtils.this, content);
            
            root.setContentPane(w32content);
            lp.add(new WindowUtils.RepaintTrigger(w32content), JLayeredPane.DRAG_LAYER);
          }
          
          if ((transparent) && (!WindowUtils.W32WindowUtils.this.usingUpdateLayeredWindow(w))) {
            flags |= 0x80000;
            user.SetWindowLong(hWnd, -20, flags);
          }
          else if ((!transparent) && (WindowUtils.W32WindowUtils.this.usingUpdateLayeredWindow(w))) {
            flags &= 0xFFF7FFFF;
            user.SetWindowLong(hWnd, -20, flags);
          }
          setLayersTransparent(w, transparent);
          setForceHeavyweightPopups(w, transparent);
          setDoubleBuffered(w, !transparent);
        }
      });
    }
    
    public void setWindowMask(Component w, Shape mask) {
      if (((mask instanceof Area)) && (((Area)mask).isPolygonal())) {
        setMask(w, (Area)mask);
      }
      else {
        super.setWindowMask(w, mask);
      }
    }
    
    private void setWindowRegion(final Component w, final WinDef.HRGN hrgn)
    {
      whenDisplayable(w, new Runnable() {
        public void run() {
          GDI32 gdi = GDI32.INSTANCE;
          User32 user = User32.INSTANCE;
          WinDef.HWND hWnd = WindowUtils.W32WindowUtils.this.getHWnd(w);
          try {
            user.SetWindowRgn(hWnd, hrgn, true);
            setForceHeavyweightPopups(getWindow(w), hrgn != null);
          }
          finally {
            gdi.DeleteObject(hrgn);
          }
        }
      });
    }
    
    private void setMask(Component w, Area area)
    {
      GDI32 gdi = GDI32.INSTANCE;
      PathIterator pi = area.getPathIterator(null);
      int mode = pi.getWindingRule() == 1 ? 2 : 1;
      
      float[] coords = new float[6];
      List<WinDef.POINT> points = new ArrayList();
      int size = 0;
      List<Integer> sizes = new ArrayList();
      while (!pi.isDone()) {
        int type = pi.currentSegment(coords);
        if (type == 0) {
          size = 1;
          points.add(new WinDef.POINT((int)coords[0], (int)coords[1]));
        }
        else if (type == 1) {
          size++;
          points.add(new WinDef.POINT((int)coords[0], (int)coords[1]));
        }
        else if (type == 4) {
          sizes.add(new Integer(size));
        }
        else {
          throw new RuntimeException("Area is not polygonal: " + area);
        }
        pi.next();
      }
      WinDef.POINT[] lppt = (WinDef.POINT[])new WinDef.POINT().toArray(points.size());
      WinDef.POINT[] pts = (WinDef.POINT[])points.toArray(new WinDef.POINT[points.size()]);
      for (int i = 0; i < lppt.length; i++) {
        x = x;
        y = y;
      }
      int[] counts = new int[sizes.size()];
      for (int i = 0; i < counts.length; i++) {
        counts[i] = ((Integer)sizes.get(i)).intValue();
      }
      WinDef.HRGN hrgn = gdi.CreatePolyPolygonRgn(lppt, counts, counts.length, mode);
      setWindowRegion(w, hrgn);
    }
    
    protected void setMask(Component w, Raster raster) {
      GDI32 gdi = GDI32.INSTANCE;
      final WinDef.HRGN region = raster != null ? gdi.CreateRectRgn(0, 0, 0, 0) : null;
      
      if (region != null) {
        final WinDef.HRGN tempRgn = gdi.CreateRectRgn(0, 0, 0, 0);
        try {
          RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput() {
            public boolean outputRange(int x, int y, int w, int h) {
              GDI32 gdi = GDI32.INSTANCE;
              gdi.SetRectRgn(tempRgn, x, y, x + w, y + h);
              return gdi.CombineRgn(region, region, tempRgn, 2) != 0;
            }
          });
        }
        finally {
          gdi.DeleteObject(tempRgn);
        }
      }
      setWindowRegion(w, region); } }
  
  private static class MacWindowUtils extends WindowUtils.NativeWindowUtils { private static final String WDRAG = "apple.awt.draggableWindowBackground";
    
    private MacWindowUtils() {}
    
    public boolean isWindowAlphaSupported() { return true; }
    
    private OSXMaskingContentPane installMaskingPane(Window w)
    {
      OSXMaskingContentPane content;
      if ((w instanceof RootPaneContainer))
      {
        RootPaneContainer rpc = (RootPaneContainer)w;
        Container oldContent = rpc.getContentPane();
        OSXMaskingContentPane content; if ((oldContent instanceof OSXMaskingContentPane)) {
          content = (OSXMaskingContentPane)oldContent;
        }
        else {
          OSXMaskingContentPane content = new OSXMaskingContentPane(oldContent);
          
          rpc.setContentPane(content);
        }
      }
      else {
        Component oldContent = w.getComponentCount() > 0 ? w.getComponent(0) : null;
        OSXMaskingContentPane content; if ((oldContent instanceof OSXMaskingContentPane)) {
          content = (OSXMaskingContentPane)oldContent;
        }
        else {
          content = new OSXMaskingContentPane(oldContent);
          w.add(content);
        }
      }
      return content;
    }
    







    public void setWindowTransparent(Window w, boolean transparent)
    {
      boolean isTransparent = (w.getBackground() != null) && (w.getBackground().getAlpha() == 0);
      
      if (transparent != isTransparent) {
        setBackgroundTransparent(w, transparent, "setWindowTransparent");
      }
    }
    

    private void fixWindowDragging(Window w, String context)
    {
      if ((w instanceof RootPaneContainer)) {
        JRootPane p = ((RootPaneContainer)w).getRootPane();
        Boolean oldDraggable = (Boolean)p.getClientProperty("apple.awt.draggableWindowBackground");
        if (oldDraggable == null) {
          p.putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);
          if (w.isDisplayable()) {
            System.err.println(context + "(): To avoid content dragging, " + context + "() must be called before the window is realized, or " + "apple.awt.draggableWindowBackground" + " must be set to Boolean.FALSE before the window is realized.  If you really want content dragging, set " + "apple.awt.draggableWindowBackground" + " on the window's root pane to Boolean.TRUE before calling " + context + "() to hide this message.");
          }
        }
      }
    }
    







    public void setWindowAlpha(final Window w, final float alpha)
    {
      if ((w instanceof RootPaneContainer)) {
        JRootPane p = ((RootPaneContainer)w).getRootPane();
        p.putClientProperty("Window.alpha", new Float(alpha));
        fixWindowDragging(w, "setWindowAlpha");
      }
      whenDisplayable(w, new Runnable() {
        public void run() {
          Object peer = w.getPeer();
          try {
            peer.getClass().getMethod("setAlpha", new Class[] { Float.TYPE }).invoke(peer, new Object[] { new Float(alpha) });
          }
          catch (Exception e) {}
        }
      });
    }
    




    protected void setWindowMask(Component w, Raster raster)
    {
      if (raster != null) {
        setWindowMask(w, toShape(raster));
      }
      else {
        setWindowMask(w, new Rectangle(0, 0, w.getWidth(), w.getHeight()));
      }
    }
    
    public void setWindowMask(Component c, Shape shape)
    {
      if ((c instanceof Window)) {
        Window w = (Window)c;
        OSXMaskingContentPane content = installMaskingPane(w);
        content.setMask(shape);
        setBackgroundTransparent(w, shape != WindowUtils.MASK_NONE, "setWindowMask");
      }
    }
    

    private static class OSXMaskingContentPane
      extends JPanel
    {
      private static final long serialVersionUID = 1L;
      
      private Shape shape;
      

      public OSXMaskingContentPane(Component oldContent)
      {
        super();
        if (oldContent != null) {
          add(oldContent, "Center");
        }
      }
      
      public void setMask(Shape shape) {
        this.shape = shape;
        repaint();
      }
      
      public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics.create();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.dispose();
        if (shape != null) {
          g = (Graphics2D)graphics.create();
          g.setClip(shape);
          super.paint(g);
          g.dispose();
        }
        else {
          super.paint(graphics);
        }
      }
    }
    
    private void setBackgroundTransparent(Window w, boolean transparent, String context) {
      JRootPane rp = (w instanceof RootPaneContainer) ? ((RootPaneContainer)w).getRootPane() : null;
      
      if (transparent) {
        if (rp != null) {
          rp.putClientProperty("transparent-old-bg", w.getBackground());
        }
        w.setBackground(new Color(0, 0, 0, 0));

      }
      else if (rp != null) {
        Color bg = (Color)rp.getClientProperty("transparent-old-bg");
        


        if (bg != null) {
          bg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), bg.getAlpha());
        }
        w.setBackground(bg);
        rp.putClientProperty("transparent-old-bg", null);
      }
      else {
        w.setBackground(null);
      }
      
      fixWindowDragging(w, context); } }
  
  private static class X11WindowUtils extends WindowUtils.NativeWindowUtils { private boolean didCheck;
    private long[] alphaVisualIDs;
    private static final long OPAQUE = 4294967295L;
    private static final String OPACITY = "_NET_WM_WINDOW_OPACITY";
    
    private static X11.Pixmap createBitmap(X11.Display dpy, X11.Window win, Raster raster) { X11 x11 = X11.INSTANCE;
      Rectangle bounds = raster.getBounds();
      int width = x + width;
      int height = y + height;
      X11.Pixmap pm = x11.XCreatePixmap(dpy, win, width, height, 1);
      X11.GC gc = x11.XCreateGC(dpy, pm, new NativeLong(0L), null);
      if (gc == null) {
        return null;
      }
      x11.XSetForeground(dpy, gc, new NativeLong(0L));
      x11.XFillRectangle(dpy, pm, gc, 0, 0, width, height);
      List<Rectangle> rlist = new ArrayList();
      try {
        RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput() {
          public boolean outputRange(int x, int y, int w, int h) {
            val$rlist.add(new Rectangle(x, y, w, h));
            return true;
          }
        });
        X11.XRectangle[] rects = (X11.XRectangle[])new X11.XRectangle().toArray(rlist.size());
        
        for (int i = 0; i < rects.length; i++) {
          Rectangle r = (Rectangle)rlist.get(i);
          x = ((short)x);
          y = ((short)y);
          width = ((short)width);
          height = ((short)height);
          
          Pointer p = rects[i].getPointer();
          p.setShort(0L, (short)x);
          p.setShort(2L, (short)y);
          p.setShort(4L, (short)width);
          p.setShort(6L, (short)height);
          rects[i].setAutoSynch(false);
        }
        
        int UNMASKED = 1;
        x11.XSetForeground(dpy, gc, new NativeLong(1L));
        x11.XFillRectangles(dpy, pm, gc, rects, rects.length);
      }
      finally {
        x11.XFreeGC(dpy, gc);
      }
      return pm;
    }
    
    private X11WindowUtils() {
      alphaVisualIDs = new long[0];
    }
    
    public boolean isWindowAlphaSupported() { return getAlphaVisualIDs().length > 0; }
    

    private static long getVisualID(GraphicsConfiguration config)
    {
      try
      {
        Object o = config.getClass().getMethod("getVisual", (Class[])null).invoke(config, (Object[])null);
        

        return ((Number)o).longValue();
      }
      catch (Exception e) {
        e.printStackTrace(); }
      return -1L;
    }
    

    public GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration()
    {
      if (isWindowAlphaSupported()) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        
        GraphicsDevice[] devices = env.getScreenDevices();
        for (int i = 0; i < devices.length; i++) {
          GraphicsConfiguration[] configs = devices[i].getConfigurations();
          
          for (int j = 0; j < configs.length; j++) {
            long visualID = getVisualID(configs[j]);
            long[] ids = getAlphaVisualIDs();
            for (int k = 0; k < ids.length; k++) {
              if (visualID == ids[k]) {
                return configs[j];
              }
            }
          }
        }
      }
      return super.getAlphaCompatibleGraphicsConfiguration();
    }
    



    private synchronized long[] getAlphaVisualIDs()
    {
      if (didCheck) {
        return alphaVisualIDs;
      }
      didCheck = true;
      X11 x11 = X11.INSTANCE;
      X11.Display dpy = x11.XOpenDisplay(null);
      if (dpy == null)
        return alphaVisualIDs;
      X11.XVisualInfo info = null;
      try {
        int screen = x11.XDefaultScreen(dpy);
        X11.XVisualInfo template = new X11.XVisualInfo();
        screen = screen;
        depth = 32;
        c_class = 4;
        NativeLong mask = new NativeLong(14L);
        

        IntByReference pcount = new IntByReference();
        info = x11.XGetVisualInfo(dpy, mask, template, pcount);
        if (info != null) {
          List<X11.VisualID> list = new ArrayList();
          X11.XVisualInfo[] infos = (X11.XVisualInfo[])info.toArray(pcount.getValue());
          
          for (int i = 0; i < infos.length; i++) {
            X11.Xrender.XRenderPictFormat format = X11.Xrender.INSTANCE.XRenderFindVisualFormat(dpy, visual);
            

            if ((type == 1) && (direct.alphaMask != 0))
            {
              list.add(visualid);
            }
          }
          alphaVisualIDs = new long[list.size()];
          for (int i = 0; i < alphaVisualIDs.length; i++) {
            alphaVisualIDs[i] = ((Number)list.get(i)).longValue();
          }
          return alphaVisualIDs;
        }
      }
      finally {
        if (info != null) {
          x11.XFree(info.getPointer());
        }
        x11.XCloseDisplay(dpy);
      }
      return alphaVisualIDs;
    }
    
    private static X11.Window getContentWindow(Window w, X11.Display dpy, X11.Window win, Point offset)
    {
      if ((((w instanceof Frame)) && (!((Frame)w).isUndecorated())) || (((w instanceof Dialog)) && (!((Dialog)w).isUndecorated())))
      {
        X11 x11 = X11.INSTANCE;
        X11.WindowByReference rootp = new X11.WindowByReference();
        X11.WindowByReference parentp = new X11.WindowByReference();
        PointerByReference childrenp = new PointerByReference();
        IntByReference countp = new IntByReference();
        x11.XQueryTree(dpy, win, rootp, parentp, childrenp, countp);
        Pointer p = childrenp.getValue();
        int[] ids = p.getIntArray(0L, countp.getValue());
        int[] arr$ = ids;int len$ = arr$.length;int i$ = 0; if (i$ < len$) { int id = arr$[i$];
          
          X11.Window child = new X11.Window(id);
          X11.XWindowAttributes xwa = new X11.XWindowAttributes();
          x11.XGetWindowAttributes(dpy, child, xwa);
          x = (-x);
          y = (-y);
          win = child;
        }
        
        if (p != null) {
          x11.XFree(p);
        }
      }
      return win;
    }
    
    private static X11.Window getDrawable(Component w) {
      int id = (int)Native.getComponentID(w);
      if (id == 0)
        return null;
      return new X11.Window(id);
    }
    


    public void setWindowAlpha(final Window w, final float alpha)
    {
      if (!isWindowAlphaSupported()) {
        throw new UnsupportedOperationException("This X11 display does not provide a 32-bit visual");
      }
      Runnable action = new Runnable() {
        public void run() {
          X11 x11 = X11.INSTANCE;
          X11.Display dpy = x11.XOpenDisplay(null);
          if (dpy == null)
            return;
          try {
            X11.Window win = WindowUtils.X11WindowUtils.getDrawable(w);
            if (alpha == 1.0F) {
              x11.XDeleteProperty(dpy, win, x11.XInternAtom(dpy, "_NET_WM_WINDOW_OPACITY", false));

            }
            else
            {
              int opacity = (int)((alpha * 4.2949673E9F) & 0xFFFFFFFFFFFFFFFF);
              IntByReference patom = new IntByReference(opacity);
              x11.XChangeProperty(dpy, win, x11.XInternAtom(dpy, "_NET_WM_WINDOW_OPACITY", false), X11.XA_CARDINAL, 32, 0, patom.getPointer(), 1);

            }
            

          }
          finally
          {

            x11.XCloseDisplay(dpy);
          }
        }
      };
      whenDisplayable(w, action);
    }
    
    private static abstract interface PixmapSource { public abstract X11.Pixmap getPixmap(X11.Display paramDisplay, X11.Window paramWindow); }
    
    private class X11TransparentContentPane extends WindowUtils.NativeWindowUtils.TransparentContentPane { private static final long serialVersionUID = 1L;
      
      public X11TransparentContentPane(Container oldContent) { super(oldContent); }
      

      private Memory buffer;
      private int[] pixels;
      private final int[] pixel = new int[4];
      

      protected void paintDirect(BufferedImage buf, Rectangle bounds)
      {
        Window window = SwingUtilities.getWindowAncestor(this);
        X11 x11 = X11.INSTANCE;
        X11.Display dpy = x11.XOpenDisplay(null);
        X11.Window win = WindowUtils.X11WindowUtils.getDrawable(window);
        Point offset = new Point();
        win = WindowUtils.X11WindowUtils.getContentWindow(window, dpy, win, offset);
        X11.GC gc = x11.XCreateGC(dpy, win, new NativeLong(0L), null);
        
        Raster raster = buf.getData();
        int w = width;
        int h = height;
        if ((buffer == null) || (buffer.size() != w * h * 4)) {
          buffer = new Memory(w * h * 4);
          pixels = new int[w * h];
        }
        for (int y = 0; y < h; y++) {
          for (int x = 0; x < w; x++) {
            raster.getPixel(x, y, pixel);
            int alpha = pixel[3] & 0xFF;
            int red = pixel[2] & 0xFF;
            int green = pixel[1] & 0xFF;
            int blue = pixel[0] & 0xFF;
            

            pixels[(y * w + x)] = (alpha << 24 | blue << 16 | green << 8 | red);
          }
        }
        X11.XWindowAttributes xwa = new X11.XWindowAttributes();
        x11.XGetWindowAttributes(dpy, win, xwa);
        X11.XImage image = x11.XCreateImage(dpy, visual, 32, 2, 0, buffer, w, h, 32, w * 4);
        

        buffer.write(0L, pixels, 0, pixels.length);
        x += x;
        y += y;
        x11.XPutImage(dpy, win, gc, image, 0, 0, x, y, w, h);
        
        x11.XFree(image.getPointer());
        x11.XFreeGC(dpy, gc);
        x11.XCloseDisplay(dpy);
      }
    }
    
    public void setWindowTransparent(final Window w, final boolean transparent)
    {
      if (!(w instanceof RootPaneContainer)) {
        throw new IllegalArgumentException("Window must be a RootPaneContainer");
      }
      if (!isWindowAlphaSupported()) {
        throw new UnsupportedOperationException("This X11 display does not provide a 32-bit visual");
      }
      if (!w.getGraphicsConfiguration().equals(getAlphaCompatibleGraphicsConfiguration()))
      {
        throw new IllegalArgumentException("Window GraphicsConfiguration '" + w.getGraphicsConfiguration() + "' does not support transparency");
      }
      boolean isTransparent = (w.getBackground() != null) && (w.getBackground().getAlpha() == 0);
      
      if (transparent == isTransparent)
        return;
      whenDisplayable(w, new Runnable() {
        public void run() {
          JRootPane root = ((RootPaneContainer)w).getRootPane();
          JLayeredPane lp = root.getLayeredPane();
          Container content = root.getContentPane();
          if ((content instanceof WindowUtils.X11WindowUtils.X11TransparentContentPane)) {
            ((WindowUtils.X11WindowUtils.X11TransparentContentPane)content).setTransparent(transparent);
          }
          else if (transparent) {
            WindowUtils.X11WindowUtils.X11TransparentContentPane x11content = new WindowUtils.X11WindowUtils.X11TransparentContentPane(WindowUtils.X11WindowUtils.this, content);
            
            root.setContentPane(x11content);
            lp.add(new WindowUtils.RepaintTrigger(x11content), JLayeredPane.DRAG_LAYER);
          }
          
          setLayersTransparent(w, transparent);
          setForceHeavyweightPopups(w, transparent);
          setDoubleBuffered(w, !transparent);
        }
      });
    }
    



    private void setWindowShape(final Window w, final PixmapSource src)
    {
      Runnable action = new Runnable() {
        public void run() {
          X11 x11 = X11.INSTANCE;
          X11.Display dpy = x11.XOpenDisplay(null);
          if (dpy == null) {
            return;
          }
          X11.Pixmap pm = null;
          try {
            X11.Window win = WindowUtils.X11WindowUtils.getDrawable(w);
            pm = src.getPixmap(dpy, win);
            X11.Xext ext = X11.Xext.INSTANCE;
            ext.XShapeCombineMask(dpy, win, 0, 0, 0, pm == null ? X11.Pixmap.None : pm, 0);

          }
          finally
          {
            if (pm != null) {
              x11.XFreePixmap(dpy, pm);
            }
            x11.XCloseDisplay(dpy);
          }
          setForceHeavyweightPopups(getWindow(w), pm != null);
        }
      };
      whenDisplayable(w, action);
    }
    
    protected void setMask(Component w, final Raster raster) {
      setWindowShape(getWindow(w), new PixmapSource() {
        public X11.Pixmap getPixmap(X11.Display dpy, X11.Window win) {
          return raster != null ? WindowUtils.X11WindowUtils.createBitmap(dpy, win, raster) : null;
        }
      });
    }
  }
  




  public static void setWindowMask(Window w, Shape mask)
  {
    getInstance().setWindowMask(w, mask);
  }
  




  public static void setComponentMask(Component c, Shape mask)
  {
    getInstance().setWindowMask(c, mask);
  }
  




  public static void setWindowMask(Window w, Icon mask)
  {
    getInstance().setWindowMask(w, mask);
  }
  
  public static boolean isWindowAlphaSupported()
  {
    return getInstance().isWindowAlphaSupported();
  }
  



  public static GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration()
  {
    return getInstance().getAlphaCompatibleGraphicsConfiguration();
  }
  













  public static void setWindowAlpha(Window w, float alpha)
  {
    getInstance().setWindowAlpha(w, Math.max(0.0F, Math.min(alpha, 1.0F)));
  }
  











  public static void setWindowTransparent(Window w, boolean transparent)
  {
    getInstance().setWindowTransparent(w, transparent);
  }
}
