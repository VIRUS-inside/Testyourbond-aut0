package org.japo.java.swing.samples;

import java.beans.MethodDescriptor;

public class MyPanelBeanInfo extends java.beans.SimpleBeanInfo { private static final int PROPERTY_accessibleContext = 0;
  private static final int PROPERTY_actionMap = 1;
  private static final int PROPERTY_alignmentX = 2;
  private static final int PROPERTY_alignmentY = 3;
  private static final int PROPERTY_ancestorListeners = 4;
  private static final int PROPERTY_autoscrolls = 5;
  private static final int PROPERTY_background = 6;
  private static final int PROPERTY_backgroundSet = 7;
  
  public MyPanelBeanInfo() {}
  private static final int PROPERTY_baselineResizeBehavior = 8;
  private static final int PROPERTY_border = 9;
  private static final int PROPERTY_bounds = 10;
  private static final int PROPERTY_colorModel = 11; private static final int PROPERTY_component = 12; private static final int PROPERTY_componentCount = 13; private static final int PROPERTY_componentListeners = 14; private static final int PROPERTY_componentOrientation = 15; private static final int PROPERTY_componentPopupMenu = 16; private static final int PROPERTY_components = 17; private static final int PROPERTY_containerListeners = 18; private static final int PROPERTY_cursor = 19; private static final int PROPERTY_cursorSet = 20; private static final int PROPERTY_debugGraphicsOptions = 21; private static final int PROPERTY_displayable = 22; private static final int PROPERTY_doubleBuffered = 23; private static final int PROPERTY_dropTarget = 24; private static final int PROPERTY_enabled = 25; private static final int PROPERTY_focusable = 26; private static final int PROPERTY_focusCycleRoot = 27; private static final int PROPERTY_focusCycleRootAncestor = 28; private static final int PROPERTY_focusListeners = 29; private static final int PROPERTY_focusOwner = 30; private static final int PROPERTY_focusTraversable = 31; private static final int PROPERTY_focusTraversalKeys = 32; private static final int PROPERTY_focusTraversalKeysEnabled = 33; private static final int PROPERTY_focusTraversalPolicy = 34; private static final int PROPERTY_focusTraversalPolicyProvider = 35; private static final int PROPERTY_focusTraversalPolicySet = 36; private static final int PROPERTY_font = 37; private static final int PROPERTY_fontSet = 38; private static final int PROPERTY_foreground = 39; private static final int PROPERTY_foregroundSet = 40; private static final int PROPERTY_graphics = 41; private static final int PROPERTY_graphicsConfiguration = 42; private static final int PROPERTY_height = 43; private static final int PROPERTY_hierarchyBoundsListeners = 44; private static final int PROPERTY_hierarchyListeners = 45; private static final int PROPERTY_ignoreRepaint = 46; private static final int PROPERTY_inheritsPopupMenu = 47; private static final int PROPERTY_inputContext = 48; private static final int PROPERTY_inputMap = 49; private static final int PROPERTY_inputMethodListeners = 50; private static final int PROPERTY_inputMethodRequests = 51; private static final int PROPERTY_inputVerifier = 52; private static final int PROPERTY_insets = 53; private static final int PROPERTY_jLabel1 = 54; private static final int PROPERTY_jTextField1 = 55; private static final int PROPERTY_keyListeners = 56; private static final int PROPERTY_layout = 57; private static final int PROPERTY_lightweight = 58; private static final int PROPERTY_locale = 59; private static final int PROPERTY_location = 60; private static final int PROPERTY_locationOnScreen = 61; private static final int PROPERTY_managingFocus = 62; private static final int PROPERTY_maximumSize = 63; private static final int PROPERTY_maximumSizeSet = 64; private static final int PROPERTY_minimumSize = 65; private static final int PROPERTY_minimumSizeSet = 66; private static final int PROPERTY_mouseListeners = 67; private static final int PROPERTY_mouseMotionListeners = 68; private static final int PROPERTY_mousePosition = 69; private static final int PROPERTY_mouseWheelListeners = 70; private static final int PROPERTY_name = 71; private static final int PROPERTY_nextFocusableComponent = 72; private static final int PROPERTY_opaque = 73; private static final int PROPERTY_optimizedDrawingEnabled = 74; private static final int PROPERTY_paintingForPrint = 75; private static final int PROPERTY_paintingTile = 76;
  private static java.beans.BeanDescriptor getBdescriptor() { java.beans.BeanDescriptor beanDescriptor = new java.beans.BeanDescriptor(MyPanel.class, null);
    

    return beanDescriptor;
  }
  

  private static final int PROPERTY_parent = 77;
  
  private static final int PROPERTY_peer = 78;
  
  private static final int PROPERTY_preferredSize = 79;
  
  private static final int PROPERTY_preferredSizeSet = 80;
  
  private static final int PROPERTY_propertyChangeListeners = 81;
  
  private static final int PROPERTY_registeredKeyStrokes = 82;
  
  private static final int PROPERTY_requestFocusEnabled = 83;
  
  private static final int PROPERTY_rootPane = 84;
  
  private static final int PROPERTY_showing = 85;
  
  private static final int PROPERTY_size = 86;
  
  private static final int PROPERTY_toolkit = 87;
  
  private static final int PROPERTY_toolTipText = 88;
  
  private static final int PROPERTY_topLevelAncestor = 89;
  
  private static final int PROPERTY_transferHandler = 90;
  
  private static final int PROPERTY_treeLock = 91;
  
  private static final int PROPERTY_UI = 92;
  
  private static final int PROPERTY_UIClassID = 93;
  
  private static final int PROPERTY_valid = 94;
  
  private static final int PROPERTY_validateRoot = 95;
  
  private static final int PROPERTY_verifyInputWhenFocusTarget = 96;
  
  private static final int PROPERTY_vetoableChangeListeners = 97;
  
  private static final int PROPERTY_visible = 98;
  
  private static final int PROPERTY_visibleRect = 99;
  
  private static final int PROPERTY_width = 100;
  
  private static final int PROPERTY_x = 101;
  private static final int PROPERTY_y = 102;
  private static final int EVENT_ancestorListener = 0;
  private static final int EVENT_componentListener = 1;
  private static final int EVENT_containerListener = 2;
  private static final int EVENT_focusListener = 3;
  private static final int EVENT_hierarchyBoundsListener = 4;
  private static final int EVENT_hierarchyListener = 5;
  private static final int EVENT_inputMethodListener = 6;
  private static final int EVENT_keyListener = 7;
  private static final int EVENT_mouseListener = 8;
  private static final int EVENT_mouseMotionListener = 9;
  private static final int EVENT_mouseWheelListener = 10;
  private static final int EVENT_propertyChangeListener = 11;
  private static final int EVENT_vetoableChangeListener = 12;
  private static final int METHOD_action0 = 0;
  private static final int METHOD_add1 = 1;
  private static final int METHOD_add2 = 2;
  private static final int METHOD_add3 = 3;
  private static final int METHOD_add4 = 4;
  private static final int METHOD_add5 = 5;
  private static final int METHOD_add6 = 6;
  private static final int METHOD_addNotify7 = 7;
  private static final int METHOD_addPropertyChangeListener8 = 8;
  private static final int METHOD_applyComponentOrientation9 = 9;
  private static final int METHOD_areFocusTraversalKeysSet10 = 10;
  private static final int METHOD_bounds11 = 11;
  private static final int METHOD_checkImage12 = 12;
  private static final int METHOD_checkImage13 = 13;
  private static final int METHOD_computeVisibleRect14 = 14;
  private static final int METHOD_contains15 = 15;
  private static final int METHOD_contains16 = 16;
  private static final int METHOD_countComponents17 = 17;
  private static final int METHOD_createImage18 = 18;
  private static final int METHOD_createImage19 = 19;
  private static final int METHOD_createToolTip20 = 20;
  private static final int METHOD_createVolatileImage21 = 21;
  private static final int METHOD_createVolatileImage22 = 22;
  private static final int METHOD_deliverEvent23 = 23;
  private static final int METHOD_disable24 = 24;
  private static final int METHOD_dispatchEvent25 = 25;
  private static final int METHOD_doLayout26 = 26;
  private static final int METHOD_enable27 = 27;
  private static final int METHOD_enable28 = 28;
  private static final int METHOD_enableInputMethods29 = 29;
  private static final int METHOD_findComponentAt30 = 30;
  private static final int METHOD_findComponentAt31 = 31;
  private static final int METHOD_firePropertyChange32 = 32;
  private static final int METHOD_firePropertyChange33 = 33;
  private static final int METHOD_firePropertyChange34 = 34;
  private static final int METHOD_firePropertyChange35 = 35;
  private static final int METHOD_firePropertyChange36 = 36;
  private static final int METHOD_firePropertyChange37 = 37;
  private static final int METHOD_firePropertyChange38 = 38;
  private static final int METHOD_firePropertyChange39 = 39;
  private static final int METHOD_getActionForKeyStroke40 = 40;
  
  private static java.beans.PropertyDescriptor[] getPdescriptor()
  {
    java.beans.PropertyDescriptor[] properties = new java.beans.PropertyDescriptor[103];
    try
    {
      properties[0] = new java.beans.PropertyDescriptor("accessibleContext", MyPanel.class, "getAccessibleContext", null);
      properties[1] = new java.beans.PropertyDescriptor("actionMap", MyPanel.class, "getActionMap", "setActionMap");
      properties[2] = new java.beans.PropertyDescriptor("alignmentX", MyPanel.class, "getAlignmentX", "setAlignmentX");
      properties[3] = new java.beans.PropertyDescriptor("alignmentY", MyPanel.class, "getAlignmentY", "setAlignmentY");
      properties[4] = new java.beans.PropertyDescriptor("ancestorListeners", MyPanel.class, "getAncestorListeners", null);
      properties[5] = new java.beans.PropertyDescriptor("autoscrolls", MyPanel.class, "getAutoscrolls", "setAutoscrolls");
      properties[6] = new java.beans.PropertyDescriptor("background", MyPanel.class, "getBackground", "setBackground");
      properties[7] = new java.beans.PropertyDescriptor("backgroundSet", MyPanel.class, "isBackgroundSet", null);
      properties[8] = new java.beans.PropertyDescriptor("baselineResizeBehavior", MyPanel.class, "getBaselineResizeBehavior", null);
      properties[9] = new java.beans.PropertyDescriptor("border", MyPanel.class, "getBorder", "setBorder");
      properties[10] = new java.beans.PropertyDescriptor("bounds", MyPanel.class, "getBounds", "setBounds");
      properties[11] = new java.beans.PropertyDescriptor("colorModel", MyPanel.class, "getColorModel", null);
      properties[12] = new java.beans.IndexedPropertyDescriptor("component", MyPanel.class, null, null, "getComponent", null);
      properties[13] = new java.beans.PropertyDescriptor("componentCount", MyPanel.class, "getComponentCount", null);
      properties[14] = new java.beans.PropertyDescriptor("componentListeners", MyPanel.class, "getComponentListeners", null);
      properties[15] = new java.beans.PropertyDescriptor("componentOrientation", MyPanel.class, "getComponentOrientation", "setComponentOrientation");
      properties[16] = new java.beans.PropertyDescriptor("componentPopupMenu", MyPanel.class, "getComponentPopupMenu", "setComponentPopupMenu");
      properties[17] = new java.beans.PropertyDescriptor("components", MyPanel.class, "getComponents", null);
      properties[18] = new java.beans.PropertyDescriptor("containerListeners", MyPanel.class, "getContainerListeners", null);
      properties[19] = new java.beans.PropertyDescriptor("cursor", MyPanel.class, "getCursor", "setCursor");
      properties[20] = new java.beans.PropertyDescriptor("cursorSet", MyPanel.class, "isCursorSet", null);
      properties[21] = new java.beans.PropertyDescriptor("debugGraphicsOptions", MyPanel.class, "getDebugGraphicsOptions", "setDebugGraphicsOptions");
      properties[22] = new java.beans.PropertyDescriptor("displayable", MyPanel.class, "isDisplayable", null);
      properties[23] = new java.beans.PropertyDescriptor("doubleBuffered", MyPanel.class, "isDoubleBuffered", "setDoubleBuffered");
      properties[24] = new java.beans.PropertyDescriptor("dropTarget", MyPanel.class, "getDropTarget", "setDropTarget");
      properties[25] = new java.beans.PropertyDescriptor("enabled", MyPanel.class, "isEnabled", "setEnabled");
      properties[26] = new java.beans.PropertyDescriptor("focusable", MyPanel.class, "isFocusable", "setFocusable");
      properties[27] = new java.beans.PropertyDescriptor("focusCycleRoot", MyPanel.class, "isFocusCycleRoot", "setFocusCycleRoot");
      properties[28] = new java.beans.PropertyDescriptor("focusCycleRootAncestor", MyPanel.class, "getFocusCycleRootAncestor", null);
      properties[29] = new java.beans.PropertyDescriptor("focusListeners", MyPanel.class, "getFocusListeners", null);
      properties[30] = new java.beans.PropertyDescriptor("focusOwner", MyPanel.class, "isFocusOwner", null);
      properties[31] = new java.beans.PropertyDescriptor("focusTraversable", MyPanel.class, "isFocusTraversable", null);
      properties[32] = new java.beans.IndexedPropertyDescriptor("focusTraversalKeys", MyPanel.class, null, null, null, "setFocusTraversalKeys");
      properties[33] = new java.beans.PropertyDescriptor("focusTraversalKeysEnabled", MyPanel.class, "getFocusTraversalKeysEnabled", "setFocusTraversalKeysEnabled");
      properties[34] = new java.beans.PropertyDescriptor("focusTraversalPolicy", MyPanel.class, "getFocusTraversalPolicy", "setFocusTraversalPolicy");
      properties[35] = new java.beans.PropertyDescriptor("focusTraversalPolicyProvider", MyPanel.class, "isFocusTraversalPolicyProvider", "setFocusTraversalPolicyProvider");
      properties[36] = new java.beans.PropertyDescriptor("focusTraversalPolicySet", MyPanel.class, "isFocusTraversalPolicySet", null);
      properties[37] = new java.beans.PropertyDescriptor("font", MyPanel.class, "getFont", "setFont");
      properties[38] = new java.beans.PropertyDescriptor("fontSet", MyPanel.class, "isFontSet", null);
      properties[39] = new java.beans.PropertyDescriptor("foreground", MyPanel.class, "getForeground", "setForeground");
      properties[40] = new java.beans.PropertyDescriptor("foregroundSet", MyPanel.class, "isForegroundSet", null);
      properties[41] = new java.beans.PropertyDescriptor("graphics", MyPanel.class, "getGraphics", null);
      properties[42] = new java.beans.PropertyDescriptor("graphicsConfiguration", MyPanel.class, "getGraphicsConfiguration", null);
      properties[43] = new java.beans.PropertyDescriptor("height", MyPanel.class, "getHeight", null);
      properties[44] = new java.beans.PropertyDescriptor("hierarchyBoundsListeners", MyPanel.class, "getHierarchyBoundsListeners", null);
      properties[45] = new java.beans.PropertyDescriptor("hierarchyListeners", MyPanel.class, "getHierarchyListeners", null);
      properties[46] = new java.beans.PropertyDescriptor("ignoreRepaint", MyPanel.class, "getIgnoreRepaint", "setIgnoreRepaint");
      properties[47] = new java.beans.PropertyDescriptor("inheritsPopupMenu", MyPanel.class, "getInheritsPopupMenu", "setInheritsPopupMenu");
      properties[48] = new java.beans.PropertyDescriptor("inputContext", MyPanel.class, "getInputContext", null);
      properties[49] = new java.beans.PropertyDescriptor("inputMap", MyPanel.class, "getInputMap", null);
      properties[50] = new java.beans.PropertyDescriptor("inputMethodListeners", MyPanel.class, "getInputMethodListeners", null);
      properties[51] = new java.beans.PropertyDescriptor("inputMethodRequests", MyPanel.class, "getInputMethodRequests", null);
      properties[52] = new java.beans.PropertyDescriptor("inputVerifier", MyPanel.class, "getInputVerifier", "setInputVerifier");
      properties[53] = new java.beans.PropertyDescriptor("insets", MyPanel.class, "getInsets", null);
      properties[54] = new java.beans.PropertyDescriptor("jLabel1", MyPanel.class, "getjLabel1", "setjLabel1");
      properties[55] = new java.beans.PropertyDescriptor("jTextField1", MyPanel.class, "getjTextField1", "setjTextField1");
      properties[56] = new java.beans.PropertyDescriptor("keyListeners", MyPanel.class, "getKeyListeners", null);
      properties[57] = new java.beans.PropertyDescriptor("layout", MyPanel.class, "getLayout", "setLayout");
      properties[58] = new java.beans.PropertyDescriptor("lightweight", MyPanel.class, "isLightweight", null);
      properties[59] = new java.beans.PropertyDescriptor("locale", MyPanel.class, "getLocale", "setLocale");
      properties[60] = new java.beans.PropertyDescriptor("location", MyPanel.class, "getLocation", "setLocation");
      properties[61] = new java.beans.PropertyDescriptor("locationOnScreen", MyPanel.class, "getLocationOnScreen", null);
      properties[62] = new java.beans.PropertyDescriptor("managingFocus", MyPanel.class, "isManagingFocus", null);
      properties[63] = new java.beans.PropertyDescriptor("maximumSize", MyPanel.class, "getMaximumSize", "setMaximumSize");
      properties[64] = new java.beans.PropertyDescriptor("maximumSizeSet", MyPanel.class, "isMaximumSizeSet", null);
      properties[65] = new java.beans.PropertyDescriptor("minimumSize", MyPanel.class, "getMinimumSize", "setMinimumSize");
      properties[66] = new java.beans.PropertyDescriptor("minimumSizeSet", MyPanel.class, "isMinimumSizeSet", null);
      properties[67] = new java.beans.PropertyDescriptor("mouseListeners", MyPanel.class, "getMouseListeners", null);
      properties[68] = new java.beans.PropertyDescriptor("mouseMotionListeners", MyPanel.class, "getMouseMotionListeners", null);
      properties[69] = new java.beans.PropertyDescriptor("mousePosition", MyPanel.class, "getMousePosition", null);
      properties[70] = new java.beans.PropertyDescriptor("mouseWheelListeners", MyPanel.class, "getMouseWheelListeners", null);
      properties[71] = new java.beans.PropertyDescriptor("name", MyPanel.class, "getName", "setName");
      properties[72] = new java.beans.PropertyDescriptor("nextFocusableComponent", MyPanel.class, "getNextFocusableComponent", "setNextFocusableComponent");
      properties[73] = new java.beans.PropertyDescriptor("opaque", MyPanel.class, "isOpaque", "setOpaque");
      properties[74] = new java.beans.PropertyDescriptor("optimizedDrawingEnabled", MyPanel.class, "isOptimizedDrawingEnabled", null);
      properties[75] = new java.beans.PropertyDescriptor("paintingForPrint", MyPanel.class, "isPaintingForPrint", null);
      properties[76] = new java.beans.PropertyDescriptor("paintingTile", MyPanel.class, "isPaintingTile", null);
      properties[77] = new java.beans.PropertyDescriptor("parent", MyPanel.class, "getParent", null);
      properties[78] = new java.beans.PropertyDescriptor("peer", MyPanel.class, "getPeer", null);
      properties[79] = new java.beans.PropertyDescriptor("preferredSize", MyPanel.class, "getPreferredSize", "setPreferredSize");
      properties[80] = new java.beans.PropertyDescriptor("preferredSizeSet", MyPanel.class, "isPreferredSizeSet", null);
      properties[81] = new java.beans.PropertyDescriptor("propertyChangeListeners", MyPanel.class, "getPropertyChangeListeners", null);
      properties[82] = new java.beans.PropertyDescriptor("registeredKeyStrokes", MyPanel.class, "getRegisteredKeyStrokes", null);
      properties[83] = new java.beans.PropertyDescriptor("requestFocusEnabled", MyPanel.class, "isRequestFocusEnabled", "setRequestFocusEnabled");
      properties[84] = new java.beans.PropertyDescriptor("rootPane", MyPanel.class, "getRootPane", null);
      properties[85] = new java.beans.PropertyDescriptor("showing", MyPanel.class, "isShowing", null);
      properties[86] = new java.beans.PropertyDescriptor("size", MyPanel.class, "getSize", "setSize");
      properties[87] = new java.beans.PropertyDescriptor("toolkit", MyPanel.class, "getToolkit", null);
      properties[88] = new java.beans.PropertyDescriptor("toolTipText", MyPanel.class, "getToolTipText", "setToolTipText");
      properties[89] = new java.beans.PropertyDescriptor("topLevelAncestor", MyPanel.class, "getTopLevelAncestor", null);
      properties[90] = new java.beans.PropertyDescriptor("transferHandler", MyPanel.class, "getTransferHandler", "setTransferHandler");
      properties[91] = new java.beans.PropertyDescriptor("treeLock", MyPanel.class, "getTreeLock", null);
      properties[92] = new java.beans.PropertyDescriptor("UI", MyPanel.class, "getUI", "setUI");
      properties[93] = new java.beans.PropertyDescriptor("UIClassID", MyPanel.class, "getUIClassID", null);
      properties[94] = new java.beans.PropertyDescriptor("valid", MyPanel.class, "isValid", null);
      properties[95] = new java.beans.PropertyDescriptor("validateRoot", MyPanel.class, "isValidateRoot", null);
      properties[96] = new java.beans.PropertyDescriptor("verifyInputWhenFocusTarget", MyPanel.class, "getVerifyInputWhenFocusTarget", "setVerifyInputWhenFocusTarget");
      properties[97] = new java.beans.PropertyDescriptor("vetoableChangeListeners", MyPanel.class, "getVetoableChangeListeners", null);
      properties[98] = new java.beans.PropertyDescriptor("visible", MyPanel.class, "isVisible", "setVisible");
      properties[99] = new java.beans.PropertyDescriptor("visibleRect", MyPanel.class, "getVisibleRect", null);
      properties[100] = new java.beans.PropertyDescriptor("width", MyPanel.class, "getWidth", null);
      properties[101] = new java.beans.PropertyDescriptor("x", MyPanel.class, "getX", null);
      properties[102] = new java.beans.PropertyDescriptor("y", MyPanel.class, "getY", null);
    }
    catch (java.beans.IntrospectionException e) {
      e.printStackTrace();
    }
    

    return properties;
  }
  

  private static final int METHOD_getBaseline41 = 41;
  
  private static final int METHOD_getBounds42 = 42;
  private static final int METHOD_getClientProperty43 = 43;
  private static final int METHOD_getComponentAt44 = 44;
  private static final int METHOD_getComponentAt45 = 45;
  private static final int METHOD_getComponentZOrder46 = 46;
  private static final int METHOD_getConditionForKeyStroke47 = 47;
  private static final int METHOD_getDefaultLocale48 = 48;
  private static final int METHOD_getFocusTraversalKeys49 = 49;
  private static final int METHOD_getFontMetrics50 = 50;
  private static final int METHOD_getInsets51 = 51;
  private static final int METHOD_getListeners52 = 52;
  
  private static java.beans.EventSetDescriptor[] getEdescriptor()
  {
    java.beans.EventSetDescriptor[] eventSets = new java.beans.EventSetDescriptor[13];
    try
    {
      eventSets[0] = new java.beans.EventSetDescriptor(MyPanel.class, "ancestorListener", javax.swing.event.AncestorListener.class, new String[] { "ancestorAdded", "ancestorRemoved", "ancestorMoved" }, "addAncestorListener", "removeAncestorListener");
      eventSets[1] = new java.beans.EventSetDescriptor(MyPanel.class, "componentListener", java.awt.event.ComponentListener.class, new String[] { "componentResized", "componentMoved", "componentShown", "componentHidden" }, "addComponentListener", "removeComponentListener");
      eventSets[2] = new java.beans.EventSetDescriptor(MyPanel.class, "containerListener", java.awt.event.ContainerListener.class, new String[] { "componentAdded", "componentRemoved" }, "addContainerListener", "removeContainerListener");
      eventSets[3] = new java.beans.EventSetDescriptor(MyPanel.class, "focusListener", java.awt.event.FocusListener.class, new String[] { "focusGained", "focusLost" }, "addFocusListener", "removeFocusListener");
      eventSets[4] = new java.beans.EventSetDescriptor(MyPanel.class, "hierarchyBoundsListener", java.awt.event.HierarchyBoundsListener.class, new String[] { "ancestorMoved", "ancestorResized" }, "addHierarchyBoundsListener", "removeHierarchyBoundsListener");
      eventSets[5] = new java.beans.EventSetDescriptor(MyPanel.class, "hierarchyListener", java.awt.event.HierarchyListener.class, new String[] { "hierarchyChanged" }, "addHierarchyListener", "removeHierarchyListener");
      eventSets[6] = new java.beans.EventSetDescriptor(MyPanel.class, "inputMethodListener", java.awt.event.InputMethodListener.class, new String[] { "inputMethodTextChanged", "caretPositionChanged" }, "addInputMethodListener", "removeInputMethodListener");
      eventSets[7] = new java.beans.EventSetDescriptor(MyPanel.class, "keyListener", java.awt.event.KeyListener.class, new String[] { "keyTyped", "keyPressed", "keyReleased" }, "addKeyListener", "removeKeyListener");
      eventSets[8] = new java.beans.EventSetDescriptor(MyPanel.class, "mouseListener", java.awt.event.MouseListener.class, new String[] { "mouseClicked", "mousePressed", "mouseReleased", "mouseEntered", "mouseExited" }, "addMouseListener", "removeMouseListener");
      eventSets[9] = new java.beans.EventSetDescriptor(MyPanel.class, "mouseMotionListener", java.awt.event.MouseMotionListener.class, new String[] { "mouseDragged", "mouseMoved" }, "addMouseMotionListener", "removeMouseMotionListener");
      eventSets[10] = new java.beans.EventSetDescriptor(MyPanel.class, "mouseWheelListener", java.awt.event.MouseWheelListener.class, new String[] { "mouseWheelMoved" }, "addMouseWheelListener", "removeMouseWheelListener");
      eventSets[11] = new java.beans.EventSetDescriptor(MyPanel.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] { "propertyChange" }, "addPropertyChangeListener", "removePropertyChangeListener");
      eventSets[12] = new java.beans.EventSetDescriptor(MyPanel.class, "vetoableChangeListener", java.beans.VetoableChangeListener.class, new String[] { "vetoableChange" }, "addVetoableChangeListener", "removeVetoableChangeListener");
    }
    catch (java.beans.IntrospectionException e) {
      e.printStackTrace();
    }
    

    return eventSets;
  }
  

  private static final int METHOD_getLocation53 = 53;
  
  private static final int METHOD_getMousePosition54 = 54;
  
  private static final int METHOD_getPopupLocation55 = 55;
  
  private static final int METHOD_getPropertyChangeListeners56 = 56;
  
  private static final int METHOD_getSize57 = 57;
  
  private static final int METHOD_getToolTipLocation58 = 58;
  
  private static final int METHOD_getToolTipText59 = 59;
  
  private static final int METHOD_gotFocus60 = 60;
  
  private static final int METHOD_grabFocus61 = 61;
  
  private static final int METHOD_handleEvent62 = 62;
  
  private static final int METHOD_hasFocus63 = 63;
  
  private static final int METHOD_hide64 = 64;
  
  private static final int METHOD_imageUpdate65 = 65;
  
  private static final int METHOD_insets66 = 66;
  
  private static final int METHOD_inside67 = 67;
  
  private static final int METHOD_invalidate68 = 68;
  
  private static final int METHOD_isAncestorOf69 = 69;
  
  private static final int METHOD_isFocusCycleRoot70 = 70;
  
  private static final int METHOD_isLightweightComponent71 = 71;
  
  private static final int METHOD_keyDown72 = 72;
  
  private static final int METHOD_keyUp73 = 73;
  
  private static final int METHOD_layout74 = 74;
  
  private static final int METHOD_list75 = 75;
  
  private static final int METHOD_list76 = 76;
  
  private static final int METHOD_list77 = 77;
  
  private static final int METHOD_list78 = 78;
  
  private static final int METHOD_list79 = 79;
  
  private static final int METHOD_locate80 = 80;
  
  private static final int METHOD_location81 = 81;
  
  private static final int METHOD_lostFocus82 = 82;
  
  private static final int METHOD_main83 = 83;
  
  private static final int METHOD_minimumSize84 = 84;
  
  private static final int METHOD_mouseDown85 = 85;
  
  private static final int METHOD_mouseDrag86 = 86;
  
  private static final int METHOD_mouseEnter87 = 87;
  
  private static final int METHOD_mouseExit88 = 88;
  
  private static final int METHOD_mouseMove89 = 89;
  
  private static final int METHOD_mouseUp90 = 90;
  
  private static final int METHOD_move91 = 91;
  
  private static final int METHOD_MyContent92 = 92;
  
  private static final int METHOD_nextFocus93 = 93;
  
  private static final int METHOD_paint94 = 94;
  
  private static final int METHOD_paintAll95 = 95;
  
  private static final int METHOD_paintComponents96 = 96;
  
  private static final int METHOD_paintImmediately97 = 97;
  
  private static final int METHOD_paintImmediately98 = 98;
  
  private static final int METHOD_postEvent99 = 99;
  
  private static final int METHOD_preferredSize100 = 100;
  
  private static final int METHOD_prepareImage101 = 101;
  
  private static final int METHOD_prepareImage102 = 102;
  
  private static final int METHOD_print103 = 103;
  
  private static final int METHOD_printAll104 = 104;
  
  private static final int METHOD_printComponents105 = 105;
  
  private static final int METHOD_putClientProperty106 = 106;
  private static final int METHOD_registerKeyboardAction107 = 107;
  private static final int METHOD_registerKeyboardAction108 = 108;
  private static final int METHOD_remove109 = 109;
  private static final int METHOD_remove110 = 110;
  private static final int METHOD_remove111 = 111;
  private static final int METHOD_removeAll112 = 112;
  private static final int METHOD_removeNotify113 = 113;
  private static final int METHOD_removePropertyChangeListener114 = 114;
  private static final int METHOD_repaint115 = 115;
  private static final int METHOD_repaint116 = 116;
  private static final int METHOD_repaint117 = 117;
  private static final int METHOD_repaint118 = 118;
  private static final int METHOD_repaint119 = 119;
  private static final int METHOD_requestDefaultFocus120 = 120;
  private static final int METHOD_requestFocus121 = 121;
  private static final int METHOD_requestFocus122 = 122;
  private static final int METHOD_requestFocusInWindow123 = 123;
  private static final int METHOD_resetKeyboardActions124 = 124;
  private static final int METHOD_reshape125 = 125;
  private static final int METHOD_resize126 = 126;
  private static final int METHOD_resize127 = 127;
  private static final int METHOD_revalidate128 = 128;
  private static final int METHOD_scrollRectToVisible129 = 129;
  private static final int METHOD_setBounds130 = 130;
  private static final int METHOD_setComponentZOrder131 = 131;
  private static final int METHOD_setDefaultLocale132 = 132;
  private static final int METHOD_show133 = 133;
  private static final int METHOD_show134 = 134;
  private static final int METHOD_size135 = 135;
  private static final int METHOD_toString136 = 136;
  private static final int METHOD_transferFocus137 = 137;
  private static final int METHOD_transferFocusBackward138 = 138;
  private static final int METHOD_transferFocusDownCycle139 = 139;
  private static final int METHOD_transferFocusUpCycle140 = 140;
  private static final int METHOD_unregisterKeyboardAction141 = 141;
  private static final int METHOD_update142 = 142;
  private static final int METHOD_updateUI143 = 143;
  private static final int METHOD_validate144 = 144;
  
  private static MethodDescriptor[] getMdescriptor()
  {
    MethodDescriptor[] methods = new MethodDescriptor[''];
    try
    {
      methods[0] = new MethodDescriptor(java.awt.Component.class.getMethod("action", new Class[] { java.awt.Event.class, Object.class }));
      methods[0].setDisplayName("");
      methods[1] = new MethodDescriptor(java.awt.Component.class.getMethod("add", new Class[] { java.awt.PopupMenu.class }));
      methods[1].setDisplayName("");
      methods[2] = new MethodDescriptor(java.awt.Container.class.getMethod("add", new Class[] { java.awt.Component.class }));
      methods[2].setDisplayName("");
      methods[3] = new MethodDescriptor(java.awt.Container.class.getMethod("add", new Class[] { String.class, java.awt.Component.class }));
      methods[3].setDisplayName("");
      methods[4] = new MethodDescriptor(java.awt.Container.class.getMethod("add", new Class[] { java.awt.Component.class, Integer.TYPE }));
      methods[4].setDisplayName("");
      methods[5] = new MethodDescriptor(java.awt.Container.class.getMethod("add", new Class[] { java.awt.Component.class, Object.class }));
      methods[5].setDisplayName("");
      methods[6] = new MethodDescriptor(java.awt.Container.class.getMethod("add", new Class[] { java.awt.Component.class, Object.class, Integer.TYPE }));
      methods[6].setDisplayName("");
      methods[7] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("addNotify", new Class[0]));
      methods[7].setDisplayName("");
      methods[8] = new MethodDescriptor(java.awt.Container.class.getMethod("addPropertyChangeListener", new Class[] { String.class, java.beans.PropertyChangeListener.class }));
      methods[8].setDisplayName("");
      methods[9] = new MethodDescriptor(java.awt.Container.class.getMethod("applyComponentOrientation", new Class[] { java.awt.ComponentOrientation.class }));
      methods[9].setDisplayName("");
      methods[10] = new MethodDescriptor(java.awt.Container.class.getMethod("areFocusTraversalKeysSet", new Class[] { Integer.TYPE }));
      methods[10].setDisplayName("");
      methods[11] = new MethodDescriptor(java.awt.Component.class.getMethod("bounds", new Class[0]));
      methods[11].setDisplayName("");
      methods[12] = new MethodDescriptor(java.awt.Component.class.getMethod("checkImage", new Class[] { java.awt.Image.class, java.awt.image.ImageObserver.class }));
      methods[12].setDisplayName("");
      methods[13] = new MethodDescriptor(java.awt.Component.class.getMethod("checkImage", new Class[] { java.awt.Image.class, Integer.TYPE, Integer.TYPE, java.awt.image.ImageObserver.class }));
      methods[13].setDisplayName("");
      methods[14] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("computeVisibleRect", new Class[] { java.awt.Rectangle.class }));
      methods[14].setDisplayName("");
      methods[15] = new MethodDescriptor(java.awt.Component.class.getMethod("contains", new Class[] { java.awt.Point.class }));
      methods[15].setDisplayName("");
      methods[16] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("contains", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[16].setDisplayName("");
      methods[17] = new MethodDescriptor(java.awt.Container.class.getMethod("countComponents", new Class[0]));
      methods[17].setDisplayName("");
      methods[18] = new MethodDescriptor(java.awt.Component.class.getMethod("createImage", new Class[] { java.awt.image.ImageProducer.class }));
      methods[18].setDisplayName("");
      methods[19] = new MethodDescriptor(java.awt.Component.class.getMethod("createImage", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[19].setDisplayName("");
      methods[20] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("createToolTip", new Class[0]));
      methods[20].setDisplayName("");
      methods[21] = new MethodDescriptor(java.awt.Component.class.getMethod("createVolatileImage", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[21].setDisplayName("");
      methods[22] = new MethodDescriptor(java.awt.Component.class.getMethod("createVolatileImage", new Class[] { Integer.TYPE, Integer.TYPE, java.awt.ImageCapabilities.class }));
      methods[22].setDisplayName("");
      methods[23] = new MethodDescriptor(java.awt.Container.class.getMethod("deliverEvent", new Class[] { java.awt.Event.class }));
      methods[23].setDisplayName("");
      methods[24] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("disable", new Class[0]));
      methods[24].setDisplayName("");
      methods[25] = new MethodDescriptor(java.awt.Component.class.getMethod("dispatchEvent", new Class[] { java.awt.AWTEvent.class }));
      methods[25].setDisplayName("");
      methods[26] = new MethodDescriptor(java.awt.Container.class.getMethod("doLayout", new Class[0]));
      methods[26].setDisplayName("");
      methods[27] = new MethodDescriptor(java.awt.Component.class.getMethod("enable", new Class[] { Boolean.TYPE }));
      methods[27].setDisplayName("");
      methods[28] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("enable", new Class[0]));
      methods[28].setDisplayName("");
      methods[29] = new MethodDescriptor(java.awt.Component.class.getMethod("enableInputMethods", new Class[] { Boolean.TYPE }));
      methods[29].setDisplayName("");
      methods[30] = new MethodDescriptor(java.awt.Container.class.getMethod("findComponentAt", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[30].setDisplayName("");
      methods[31] = new MethodDescriptor(java.awt.Container.class.getMethod("findComponentAt", new Class[] { java.awt.Point.class }));
      methods[31].setDisplayName("");
      methods[32] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] { String.class, Byte.TYPE, Byte.TYPE }));
      methods[32].setDisplayName("");
      methods[33] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] { String.class, Short.TYPE, Short.TYPE }));
      methods[33].setDisplayName("");
      methods[34] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] { String.class, Long.TYPE, Long.TYPE }));
      methods[34].setDisplayName("");
      methods[35] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] { String.class, Float.TYPE, Float.TYPE }));
      methods[35].setDisplayName("");
      methods[36] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] { String.class, Double.TYPE, Double.TYPE }));
      methods[36].setDisplayName("");
      methods[37] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("firePropertyChange", new Class[] { String.class, Boolean.TYPE, Boolean.TYPE }));
      methods[37].setDisplayName("");
      methods[38] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("firePropertyChange", new Class[] { String.class, Integer.TYPE, Integer.TYPE }));
      methods[38].setDisplayName("");
      methods[39] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("firePropertyChange", new Class[] { String.class, Character.TYPE, Character.TYPE }));
      methods[39].setDisplayName("");
      methods[40] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getActionForKeyStroke", new Class[] { javax.swing.KeyStroke.class }));
      methods[40].setDisplayName("");
      methods[41] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getBaseline", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[41].setDisplayName("");
      methods[42] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getBounds", new Class[] { java.awt.Rectangle.class }));
      methods[42].setDisplayName("");
      methods[43] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getClientProperty", new Class[] { Object.class }));
      methods[43].setDisplayName("");
      methods[44] = new MethodDescriptor(java.awt.Container.class.getMethod("getComponentAt", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[44].setDisplayName("");
      methods[45] = new MethodDescriptor(java.awt.Container.class.getMethod("getComponentAt", new Class[] { java.awt.Point.class }));
      methods[45].setDisplayName("");
      methods[46] = new MethodDescriptor(java.awt.Container.class.getMethod("getComponentZOrder", new Class[] { java.awt.Component.class }));
      methods[46].setDisplayName("");
      methods[47] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getConditionForKeyStroke", new Class[] { javax.swing.KeyStroke.class }));
      methods[47].setDisplayName("");
      methods[48] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getDefaultLocale", new Class[0]));
      methods[48].setDisplayName("");
      methods[49] = new MethodDescriptor(java.awt.Container.class.getMethod("getFocusTraversalKeys", new Class[] { Integer.TYPE }));
      methods[49].setDisplayName("");
      methods[50] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getFontMetrics", new Class[] { java.awt.Font.class }));
      methods[50].setDisplayName("");
      methods[51] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getInsets", new Class[] { java.awt.Insets.class }));
      methods[51].setDisplayName("");
      methods[52] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getListeners", new Class[] { Class.class }));
      methods[52].setDisplayName("");
      methods[53] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getLocation", new Class[] { java.awt.Point.class }));
      methods[53].setDisplayName("");
      methods[54] = new MethodDescriptor(java.awt.Container.class.getMethod("getMousePosition", new Class[] { Boolean.TYPE }));
      methods[54].setDisplayName("");
      methods[55] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getPopupLocation", new Class[] { java.awt.event.MouseEvent.class }));
      methods[55].setDisplayName("");
      methods[56] = new MethodDescriptor(java.awt.Component.class.getMethod("getPropertyChangeListeners", new Class[] { String.class }));
      methods[56].setDisplayName("");
      methods[57] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getSize", new Class[] { java.awt.Dimension.class }));
      methods[57].setDisplayName("");
      methods[58] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getToolTipLocation", new Class[] { java.awt.event.MouseEvent.class }));
      methods[58].setDisplayName("");
      methods[59] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getToolTipText", new Class[] { java.awt.event.MouseEvent.class }));
      methods[59].setDisplayName("");
      methods[60] = new MethodDescriptor(java.awt.Component.class.getMethod("gotFocus", new Class[] { java.awt.Event.class, Object.class }));
      methods[60].setDisplayName("");
      methods[61] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("grabFocus", new Class[0]));
      methods[61].setDisplayName("");
      methods[62] = new MethodDescriptor(java.awt.Component.class.getMethod("handleEvent", new Class[] { java.awt.Event.class }));
      methods[62].setDisplayName("");
      methods[63] = new MethodDescriptor(java.awt.Component.class.getMethod("hasFocus", new Class[0]));
      methods[63].setDisplayName("");
      methods[64] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("hide", new Class[0]));
      methods[64].setDisplayName("");
      methods[65] = new MethodDescriptor(java.awt.Component.class.getMethod("imageUpdate", new Class[] { java.awt.Image.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
      methods[65].setDisplayName("");
      methods[66] = new MethodDescriptor(java.awt.Container.class.getMethod("insets", new Class[0]));
      methods[66].setDisplayName("");
      methods[67] = new MethodDescriptor(java.awt.Component.class.getMethod("inside", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[67].setDisplayName("");
      methods[68] = new MethodDescriptor(java.awt.Container.class.getMethod("invalidate", new Class[0]));
      methods[68].setDisplayName("");
      methods[69] = new MethodDescriptor(java.awt.Container.class.getMethod("isAncestorOf", new Class[] { java.awt.Component.class }));
      methods[69].setDisplayName("");
      methods[70] = new MethodDescriptor(java.awt.Container.class.getMethod("isFocusCycleRoot", new Class[] { java.awt.Container.class }));
      methods[70].setDisplayName("");
      methods[71] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("isLightweightComponent", new Class[] { java.awt.Component.class }));
      methods[71].setDisplayName("");
      methods[72] = new MethodDescriptor(java.awt.Component.class.getMethod("keyDown", new Class[] { java.awt.Event.class, Integer.TYPE }));
      methods[72].setDisplayName("");
      methods[73] = new MethodDescriptor(java.awt.Component.class.getMethod("keyUp", new Class[] { java.awt.Event.class, Integer.TYPE }));
      methods[73].setDisplayName("");
      methods[74] = new MethodDescriptor(java.awt.Container.class.getMethod("layout", new Class[0]));
      methods[74].setDisplayName("");
      methods[75] = new MethodDescriptor(java.awt.Component.class.getMethod("list", new Class[0]));
      methods[75].setDisplayName("");
      methods[76] = new MethodDescriptor(java.awt.Component.class.getMethod("list", new Class[] { java.io.PrintStream.class }));
      methods[76].setDisplayName("");
      methods[77] = new MethodDescriptor(java.awt.Component.class.getMethod("list", new Class[] { java.io.PrintWriter.class }));
      methods[77].setDisplayName("");
      methods[78] = new MethodDescriptor(java.awt.Container.class.getMethod("list", new Class[] { java.io.PrintStream.class, Integer.TYPE }));
      methods[78].setDisplayName("");
      methods[79] = new MethodDescriptor(java.awt.Container.class.getMethod("list", new Class[] { java.io.PrintWriter.class, Integer.TYPE }));
      methods[79].setDisplayName("");
      methods[80] = new MethodDescriptor(java.awt.Container.class.getMethod("locate", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[80].setDisplayName("");
      methods[81] = new MethodDescriptor(java.awt.Component.class.getMethod("location", new Class[0]));
      methods[81].setDisplayName("");
      methods[82] = new MethodDescriptor(java.awt.Component.class.getMethod("lostFocus", new Class[] { java.awt.Event.class, Object.class }));
      methods[82].setDisplayName("");
      methods[83] = new MethodDescriptor(MyPanel.class.getMethod("main", new Class[] { [Ljava.lang.String.class }));
      methods[83].setDisplayName("");
      methods[84] = new MethodDescriptor(java.awt.Container.class.getMethod("minimumSize", new Class[0]));
      methods[84].setDisplayName("");
      methods[85] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseDown", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
      methods[85].setDisplayName("");
      methods[86] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseDrag", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
      methods[86].setDisplayName("");
      methods[87] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseEnter", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
      methods[87].setDisplayName("");
      methods[88] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseExit", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
      methods[88].setDisplayName("");
      methods[89] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseMove", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
      methods[89].setDisplayName("");
      methods[90] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseUp", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
      methods[90].setDisplayName("");
      methods[91] = new MethodDescriptor(java.awt.Component.class.getMethod("move", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[91].setDisplayName("");
      methods[92] = new MethodDescriptor(MyPanel.class.getMethod("MyContent", new Class[0]));
      methods[92].setDisplayName("");
      methods[93] = new MethodDescriptor(java.awt.Component.class.getMethod("nextFocus", new Class[0]));
      methods[93].setDisplayName("");
      methods[94] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("paint", new Class[] { java.awt.Graphics.class }));
      methods[94].setDisplayName("");
      methods[95] = new MethodDescriptor(java.awt.Component.class.getMethod("paintAll", new Class[] { java.awt.Graphics.class }));
      methods[95].setDisplayName("");
      methods[96] = new MethodDescriptor(java.awt.Container.class.getMethod("paintComponents", new Class[] { java.awt.Graphics.class }));
      methods[96].setDisplayName("");
      methods[97] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("paintImmediately", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
      methods[97].setDisplayName("");
      methods[98] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("paintImmediately", new Class[] { java.awt.Rectangle.class }));
      methods[98].setDisplayName("");
      methods[99] = new MethodDescriptor(java.awt.Component.class.getMethod("postEvent", new Class[] { java.awt.Event.class }));
      methods[99].setDisplayName("");
      methods[100] = new MethodDescriptor(java.awt.Container.class.getMethod("preferredSize", new Class[0]));
      methods[100].setDisplayName("");
      methods[101] = new MethodDescriptor(java.awt.Component.class.getMethod("prepareImage", new Class[] { java.awt.Image.class, java.awt.image.ImageObserver.class }));
      methods[101].setDisplayName("");
      methods[102] = new MethodDescriptor(java.awt.Component.class.getMethod("prepareImage", new Class[] { java.awt.Image.class, Integer.TYPE, Integer.TYPE, java.awt.image.ImageObserver.class }));
      methods[102].setDisplayName("");
      methods[103] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("print", new Class[] { java.awt.Graphics.class }));
      methods[103].setDisplayName("");
      methods[104] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("printAll", new Class[] { java.awt.Graphics.class }));
      methods[104].setDisplayName("");
      methods[105] = new MethodDescriptor(java.awt.Container.class.getMethod("printComponents", new Class[] { java.awt.Graphics.class }));
      methods[105].setDisplayName("");
      methods[106] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("putClientProperty", new Class[] { Object.class, Object.class }));
      methods[106].setDisplayName("");
      methods[107] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("registerKeyboardAction", new Class[] { java.awt.event.ActionListener.class, String.class, javax.swing.KeyStroke.class, Integer.TYPE }));
      methods[107].setDisplayName("");
      methods[108] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("registerKeyboardAction", new Class[] { java.awt.event.ActionListener.class, javax.swing.KeyStroke.class, Integer.TYPE }));
      methods[108].setDisplayName("");
      methods[109] = new MethodDescriptor(java.awt.Component.class.getMethod("remove", new Class[] { java.awt.MenuComponent.class }));
      methods[109].setDisplayName("");
      methods[110] = new MethodDescriptor(java.awt.Container.class.getMethod("remove", new Class[] { Integer.TYPE }));
      methods[110].setDisplayName("");
      methods[111] = new MethodDescriptor(java.awt.Container.class.getMethod("remove", new Class[] { java.awt.Component.class }));
      methods[111].setDisplayName("");
      methods[112] = new MethodDescriptor(java.awt.Container.class.getMethod("removeAll", new Class[0]));
      methods[112].setDisplayName("");
      methods[113] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("removeNotify", new Class[0]));
      methods[113].setDisplayName("");
      methods[114] = new MethodDescriptor(java.awt.Component.class.getMethod("removePropertyChangeListener", new Class[] { String.class, java.beans.PropertyChangeListener.class }));
      methods[114].setDisplayName("");
      methods[115] = new MethodDescriptor(java.awt.Component.class.getMethod("repaint", new Class[0]));
      methods[115].setDisplayName("");
      methods[116] = new MethodDescriptor(java.awt.Component.class.getMethod("repaint", new Class[] { Long.TYPE }));
      methods[116].setDisplayName("");
      methods[117] = new MethodDescriptor(java.awt.Component.class.getMethod("repaint", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
      methods[117].setDisplayName("");
      methods[118] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("repaint", new Class[] { Long.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
      methods[118].setDisplayName("");
      methods[119] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("repaint", new Class[] { java.awt.Rectangle.class }));
      methods[119].setDisplayName("");
      methods[120] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("requestDefaultFocus", new Class[0]));
      methods[120].setDisplayName("");
      methods[121] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("requestFocus", new Class[0]));
      methods[121].setDisplayName("");
      methods[122] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("requestFocus", new Class[] { Boolean.TYPE }));
      methods[122].setDisplayName("");
      methods[123] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("requestFocusInWindow", new Class[0]));
      methods[123].setDisplayName("");
      methods[124] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("resetKeyboardActions", new Class[0]));
      methods[124].setDisplayName("");
      methods[125] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("reshape", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
      methods[125].setDisplayName("");
      methods[126] = new MethodDescriptor(java.awt.Component.class.getMethod("resize", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[126].setDisplayName("");
      methods[127] = new MethodDescriptor(java.awt.Component.class.getMethod("resize", new Class[] { java.awt.Dimension.class }));
      methods[127].setDisplayName("");
      methods[''] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("revalidate", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("scrollRectToVisible", new Class[] { java.awt.Rectangle.class }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("setBounds", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Container.class.getMethod("setComponentZOrder", new Class[] { java.awt.Component.class, Integer.TYPE }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("setDefaultLocale", new Class[] { java.util.Locale.class }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("show", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("show", new Class[] { Boolean.TYPE }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("size", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("toString", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("transferFocus", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("transferFocusBackward", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Container.class.getMethod("transferFocusDownCycle", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("transferFocusUpCycle", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("unregisterKeyboardAction", new Class[] { javax.swing.KeyStroke.class }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("update", new Class[] { java.awt.Graphics.class }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(javax.swing.JPanel.class.getMethod("updateUI", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Container.class.getMethod("validate", new Class[0]));
      methods[''].setDisplayName("");
    }
    catch (Exception localException) {}
    

    return methods; }
  
  private static java.awt.Image iconColor16 = null;
  private static java.awt.Image iconColor32 = null;
  private static java.awt.Image iconMono16 = null;
  private static java.awt.Image iconMono32 = null;
  private static String iconNameC16 = "/org/japo/java/resources/images/16.png";
  private static String iconNameC32 = "/org/japo/java/resources/images/32.png";
  private static String iconNameM16 = "/org/japo/java/resources/images/16 b.png";
  private static String iconNameM32 = "/org/japo/java/resources/images/32 b.png";
  




  private static final int defaultPropertyIndex = -1;
  



  private static final int defaultEventIndex = -1;
  




  public java.beans.BeanDescriptor getBeanDescriptor()
  {
    return getBdescriptor();
  }
  












  public java.beans.PropertyDescriptor[] getPropertyDescriptors()
  {
    return getPdescriptor();
  }
  







  public java.beans.EventSetDescriptor[] getEventSetDescriptors()
  {
    return getEdescriptor();
  }
  







  public MethodDescriptor[] getMethodDescriptors()
  {
    return getMdescriptor();
  }
  










  public int getDefaultPropertyIndex()
  {
    return -1;
  }
  









  public int getDefaultEventIndex()
  {
    return -1;
  }
  





















  public java.awt.Image getIcon(int iconKind)
  {
    switch (iconKind) {
    case 1: 
      if (iconNameC16 == null) {
        return null;
      }
      if (iconColor16 == null) {
        iconColor16 = loadImage(iconNameC16);
      }
      return iconColor16;
    
    case 2: 
      if (iconNameC32 == null) {
        return null;
      }
      if (iconColor32 == null) {
        iconColor32 = loadImage(iconNameC32);
      }
      return iconColor32;
    
    case 3: 
      if (iconNameM16 == null) {
        return null;
      }
      if (iconMono16 == null) {
        iconMono16 = loadImage(iconNameM16);
      }
      return iconMono16;
    
    case 4: 
      if (iconNameM32 == null) {
        return null;
      }
      if (iconMono32 == null) {
        iconMono32 = loadImage(iconNameM32);
      }
      return iconMono32;
    }
    
    return null;
  }
}
