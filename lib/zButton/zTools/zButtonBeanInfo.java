package zTools;

import java.beans.MethodDescriptor;

public class zButtonBeanInfo extends java.beans.SimpleBeanInfo {
  private static final int PROPERTY_accessibleContext = 0;
  private static final int PROPERTY_action = 1;
  private static final int PROPERTY_actionCommand = 2;
  private static final int PROPERTY_actionListeners = 3;
  private static final int PROPERTY_actionMap = 4;
  private static final int PROPERTY_alignmentX = 5;
  private static final int PROPERTY_alignmentY = 6;
  private static final int PROPERTY_ancestorListeners = 7;
  private static final int PROPERTY_autoscrolls = 8;
  
  public zButtonBeanInfo() {}
  
  private static java.beans.BeanDescriptor getBdescriptor() {
    java.beans.BeanDescriptor beanDescriptor = new java.beans.BeanDescriptor(zButton.class, null);
    

    return beanDescriptor;
  }
  
  private static final int PROPERTY_background = 9;
  private static final int PROPERTY_backgroundSet = 10;
  private static final int PROPERTY_baselineResizeBehavior = 11;
  private static final int PROPERTY_border = 12;
  private static final int PROPERTY_borderPainted = 13;
  private static final int PROPERTY_bounds = 14;
  private static final int PROPERTY_changeListeners = 15;
  private static final int PROPERTY_colorModel = 16;
  private static final int PROPERTY_component = 17;
  private static final int PROPERTY_componentCount = 18;
  private static final int PROPERTY_componentListeners = 19;
  private static final int PROPERTY_componentOrientation = 20;
  private static final int PROPERTY_componentPopupMenu = 21;
  private static final int PROPERTY_components = 22;
  private static final int PROPERTY_containerListeners = 23;
  private static final int PROPERTY_contentAreaFilled = 24;
  private static final int PROPERTY_cursor = 25;
  private static final int PROPERTY_cursorSet = 26;
  private static final int PROPERTY_debugGraphicsOptions = 27;
  private static final int PROPERTY_defaultButton = 28;
  private static final int PROPERTY_defaultCapable = 29;
  private static final int PROPERTY_disabledIcon = 30;
  private static final int PROPERTY_disabledSelectedIcon = 31;
  private static final int PROPERTY_displayable = 32;
  private static final int PROPERTY_displayedMnemonicIndex = 33;
  private static final int PROPERTY_doubleBuffered = 34;
  private static final int PROPERTY_dropTarget = 35;
  private static final int PROPERTY_enabled = 36;
  private static final int PROPERTY_focusable = 37;
  private static final int PROPERTY_focusCycleRoot = 38;
  private static final int PROPERTY_focusCycleRootAncestor = 39;
  private static final int PROPERTY_focusListeners = 40;
  private static final int PROPERTY_focusOwner = 41;
  private static final int PROPERTY_focusPainted = 42;
  private static final int PROPERTY_focusTraversable = 43;
  private static final int PROPERTY_focusTraversalKeys = 44;
  private static final int PROPERTY_focusTraversalKeysEnabled = 45;
  private static final int PROPERTY_focusTraversalPolicy = 46;
  private static final int PROPERTY_focusTraversalPolicyProvider = 47;
  private static final int PROPERTY_focusTraversalPolicySet = 48;
  private static final int PROPERTY_font = 49;
  private static final int PROPERTY_fontSet = 50;
  private static final int PROPERTY_foreground = 51;
  private static final int PROPERTY_foregroundSet = 52;
  private static final int PROPERTY_graphics = 53;
  private static final int PROPERTY_graphicsConfiguration = 54;
  private static final int PROPERTY_height = 55;
  private static final int PROPERTY_hideActionText = 56;
  private static final int PROPERTY_hierarchyBoundsListeners = 57;
  private static final int PROPERTY_hierarchyListeners = 58;
  private static final int PROPERTY_horizontalAlignment = 59;
  private static final int PROPERTY_horizontalTextPosition = 60;
  private static final int PROPERTY_icon = 61;
  private static final int PROPERTY_iconTextGap = 62;
  private static final int PROPERTY_ignoreRepaint = 63;
  private static final int PROPERTY_inheritsPopupMenu = 64;
  private static final int PROPERTY_inputContext = 65;
  private static final int PROPERTY_inputMap = 66;
  private static final int PROPERTY_inputMethodListeners = 67;
  private static final int PROPERTY_inputMethodRequests = 68;
  private static final int PROPERTY_inputVerifier = 69;
  private static final int PROPERTY_insets = 70;
  private static final int PROPERTY_itemListeners = 71;
  private static final int PROPERTY_keyListeners = 72;
  private static final int PROPERTY_label = 73;
  private static final int PROPERTY_layout = 74;
  private static final int PROPERTY_lightweight = 75;
  private static final int PROPERTY_locale = 76;
  private static final int PROPERTY_location = 77;
  private static final int PROPERTY_locationOnScreen = 78;
  private static final int PROPERTY_managingFocus = 79;
  private static final int PROPERTY_margin = 80;
  private static final int PROPERTY_maximumSize = 81;
  private static final int PROPERTY_maximumSizeSet = 82;
  private static final int PROPERTY_minimumSize = 83;
  private static final int PROPERTY_minimumSizeSet = 84;
  private static final int PROPERTY_mnemonic = 85;
  private static final int PROPERTY_model = 86;
  private static final int PROPERTY_mouseListeners = 87;
  private static final int PROPERTY_mouseMotionListeners = 88;
  private static final int PROPERTY_mousePosition = 89;
  private static final int PROPERTY_mouseWheelListeners = 90;
  private static final int PROPERTY_multiClickThreshhold = 91;
  private static final int PROPERTY_name = 92;
  private static final int PROPERTY_nextFocusableComponent = 93;
  private static final int PROPERTY_opaque = 94;
  private static final int PROPERTY_optimizedDrawingEnabled = 95;
  private static final int PROPERTY_paintingForPrint = 96;
  private static final int PROPERTY_paintingTile = 97;
  private static final int PROPERTY_parent = 98;
  private static final int PROPERTY_peer = 99;
  private static final int PROPERTY_preferredSize = 100;
  private static final int PROPERTY_preferredSizeSet = 101;
  private static final int PROPERTY_pressedIcon = 102;
  private static final int PROPERTY_propertyChangeListeners = 103;
  private static final int PROPERTY_registeredKeyStrokes = 104;
  private static final int PROPERTY_requestFocusEnabled = 105;
  private static final int PROPERTY_rolloverEnabled = 106;
  private static final int PROPERTY_rolloverIcon = 107;
  private static final int PROPERTY_rolloverSelectedIcon = 108;
  private static final int PROPERTY_rootPane = 109;
  private static final int PROPERTY_selected = 110;
  private static final int PROPERTY_selectedIcon = 111;
  private static final int PROPERTY_selectedObjects = 112;
  private static final int PROPERTY_showing = 113;
  private static final int PROPERTY_size = 114;
  private static final int PROPERTY_text = 115;
  private static final int PROPERTY_toolkit = 116;
  private static final int PROPERTY_toolTipText = 117;
  private static final int PROPERTY_topLevelAncestor = 118;
  private static final int PROPERTY_transferHandler = 119;
  private static final int PROPERTY_treeLock = 120;
  private static final int PROPERTY_UI = 121;
  private static final int PROPERTY_UIClassID = 122;
  private static final int PROPERTY_valid = 123;
  private static final int PROPERTY_validateRoot = 124;
  private static final int PROPERTY_verifyInputWhenFocusTarget = 125;
  private static final int PROPERTY_verticalAlignment = 126;
  private static final int PROPERTY_verticalTextPosition = 127;
  private static final int PROPERTY_vetoableChangeListeners = 128;
  private static final int PROPERTY_visible = 129;
  private static final int PROPERTY_visibleRect = 130;
  private static final int PROPERTY_width = 131;
  private static final int PROPERTY_x = 132;
  private static final int PROPERTY_y = 133;
  private static final int PROPERTY_zUI = 134;
  private static final int EVENT_actionListener = 0;
  private static final int EVENT_ancestorListener = 1;
  private static final int EVENT_changeListener = 2;
  private static final int EVENT_componentListener = 3;
  private static final int EVENT_containerListener = 4;
  private static final int EVENT_focusListener = 5;
  private static final int EVENT_hierarchyBoundsListener = 6;
  private static final int EVENT_hierarchyListener = 7;
  private static final int EVENT_inputMethodListener = 8;
  private static final int EVENT_itemListener = 9;
  private static final int EVENT_keyListener = 10;
  private static final int EVENT_mouseListener = 11;
  private static final int EVENT_mouseMotionListener = 12;
  private static final int EVENT_mouseWheelListener = 13;
  private static java.beans.PropertyDescriptor[] getPdescriptor() { java.beans.PropertyDescriptor[] properties = new java.beans.PropertyDescriptor[''];
    try
    {
      properties[0] = new java.beans.PropertyDescriptor("accessibleContext", zButton.class, "getAccessibleContext", null);
      properties[1] = new java.beans.PropertyDescriptor("action", zButton.class, "getAction", "setAction");
      properties[2] = new java.beans.PropertyDescriptor("actionCommand", zButton.class, "getActionCommand", "setActionCommand");
      properties[3] = new java.beans.PropertyDescriptor("actionListeners", zButton.class, "getActionListeners", null);
      properties[4] = new java.beans.PropertyDescriptor("actionMap", zButton.class, "getActionMap", "setActionMap");
      properties[5] = new java.beans.PropertyDescriptor("alignmentX", zButton.class, "getAlignmentX", "setAlignmentX");
      properties[6] = new java.beans.PropertyDescriptor("alignmentY", zButton.class, "getAlignmentY", "setAlignmentY");
      properties[7] = new java.beans.PropertyDescriptor("ancestorListeners", zButton.class, "getAncestorListeners", null);
      properties[8] = new java.beans.PropertyDescriptor("autoscrolls", zButton.class, "getAutoscrolls", "setAutoscrolls");
      properties[9] = new java.beans.PropertyDescriptor("background", zButton.class, "getBackground", "setBackground");
      properties[10] = new java.beans.PropertyDescriptor("backgroundSet", zButton.class, "isBackgroundSet", null);
      properties[11] = new java.beans.PropertyDescriptor("baselineResizeBehavior", zButton.class, "getBaselineResizeBehavior", null);
      properties[12] = new java.beans.PropertyDescriptor("border", zButton.class, "getBorder", "setBorder");
      properties[13] = new java.beans.PropertyDescriptor("borderPainted", zButton.class, "isBorderPainted", "setBorderPainted");
      properties[14] = new java.beans.PropertyDescriptor("bounds", zButton.class, "getBounds", "setBounds");
      properties[15] = new java.beans.PropertyDescriptor("changeListeners", zButton.class, "getChangeListeners", null);
      properties[16] = new java.beans.PropertyDescriptor("colorModel", zButton.class, "getColorModel", null);
      properties[17] = new java.beans.IndexedPropertyDescriptor("component", zButton.class, null, null, "getComponent", null);
      properties[18] = new java.beans.PropertyDescriptor("componentCount", zButton.class, "getComponentCount", null);
      properties[19] = new java.beans.PropertyDescriptor("componentListeners", zButton.class, "getComponentListeners", null);
      properties[20] = new java.beans.PropertyDescriptor("componentOrientation", zButton.class, "getComponentOrientation", "setComponentOrientation");
      properties[21] = new java.beans.PropertyDescriptor("componentPopupMenu", zButton.class, "getComponentPopupMenu", "setComponentPopupMenu");
      properties[22] = new java.beans.PropertyDescriptor("components", zButton.class, "getComponents", null);
      properties[23] = new java.beans.PropertyDescriptor("containerListeners", zButton.class, "getContainerListeners", null);
      properties[24] = new java.beans.PropertyDescriptor("contentAreaFilled", zButton.class, "isContentAreaFilled", "setContentAreaFilled");
      properties[25] = new java.beans.PropertyDescriptor("cursor", zButton.class, "getCursor", "setCursor");
      properties[26] = new java.beans.PropertyDescriptor("cursorSet", zButton.class, "isCursorSet", null);
      properties[27] = new java.beans.PropertyDescriptor("debugGraphicsOptions", zButton.class, "getDebugGraphicsOptions", "setDebugGraphicsOptions");
      properties[28] = new java.beans.PropertyDescriptor("defaultButton", zButton.class, "isDefaultButton", null);
      properties[29] = new java.beans.PropertyDescriptor("defaultCapable", zButton.class, "isDefaultCapable", "setDefaultCapable");
      properties[30] = new java.beans.PropertyDescriptor("disabledIcon", zButton.class, "getDisabledIcon", "setDisabledIcon");
      properties[31] = new java.beans.PropertyDescriptor("disabledSelectedIcon", zButton.class, "getDisabledSelectedIcon", "setDisabledSelectedIcon");
      properties[32] = new java.beans.PropertyDescriptor("displayable", zButton.class, "isDisplayable", null);
      properties[33] = new java.beans.PropertyDescriptor("displayedMnemonicIndex", zButton.class, "getDisplayedMnemonicIndex", "setDisplayedMnemonicIndex");
      properties[34] = new java.beans.PropertyDescriptor("doubleBuffered", zButton.class, "isDoubleBuffered", "setDoubleBuffered");
      properties[35] = new java.beans.PropertyDescriptor("dropTarget", zButton.class, "getDropTarget", "setDropTarget");
      properties[36] = new java.beans.PropertyDescriptor("enabled", zButton.class, "isEnabled", "setEnabled");
      properties[37] = new java.beans.PropertyDescriptor("focusable", zButton.class, "isFocusable", "setFocusable");
      properties[38] = new java.beans.PropertyDescriptor("focusCycleRoot", zButton.class, "isFocusCycleRoot", "setFocusCycleRoot");
      properties[39] = new java.beans.PropertyDescriptor("focusCycleRootAncestor", zButton.class, "getFocusCycleRootAncestor", null);
      properties[40] = new java.beans.PropertyDescriptor("focusListeners", zButton.class, "getFocusListeners", null);
      properties[41] = new java.beans.PropertyDescriptor("focusOwner", zButton.class, "isFocusOwner", null);
      properties[42] = new java.beans.PropertyDescriptor("focusPainted", zButton.class, "isFocusPainted", "setFocusPainted");
      properties[43] = new java.beans.PropertyDescriptor("focusTraversable", zButton.class, "isFocusTraversable", null);
      properties[44] = new java.beans.IndexedPropertyDescriptor("focusTraversalKeys", zButton.class, null, null, null, "setFocusTraversalKeys");
      properties[45] = new java.beans.PropertyDescriptor("focusTraversalKeysEnabled", zButton.class, "getFocusTraversalKeysEnabled", "setFocusTraversalKeysEnabled");
      properties[46] = new java.beans.PropertyDescriptor("focusTraversalPolicy", zButton.class, "getFocusTraversalPolicy", "setFocusTraversalPolicy");
      properties[47] = new java.beans.PropertyDescriptor("focusTraversalPolicyProvider", zButton.class, "isFocusTraversalPolicyProvider", "setFocusTraversalPolicyProvider");
      properties[48] = new java.beans.PropertyDescriptor("focusTraversalPolicySet", zButton.class, "isFocusTraversalPolicySet", null);
      properties[49] = new java.beans.PropertyDescriptor("font", zButton.class, "getFont", "setFont");
      properties[50] = new java.beans.PropertyDescriptor("fontSet", zButton.class, "isFontSet", null);
      properties[51] = new java.beans.PropertyDescriptor("foreground", zButton.class, "getForeground", "setForeground");
      properties[52] = new java.beans.PropertyDescriptor("foregroundSet", zButton.class, "isForegroundSet", null);
      properties[53] = new java.beans.PropertyDescriptor("graphics", zButton.class, "getGraphics", null);
      properties[54] = new java.beans.PropertyDescriptor("graphicsConfiguration", zButton.class, "getGraphicsConfiguration", null);
      properties[55] = new java.beans.PropertyDescriptor("height", zButton.class, "getHeight", null);
      properties[56] = new java.beans.PropertyDescriptor("hideActionText", zButton.class, "getHideActionText", "setHideActionText");
      properties[57] = new java.beans.PropertyDescriptor("hierarchyBoundsListeners", zButton.class, "getHierarchyBoundsListeners", null);
      properties[58] = new java.beans.PropertyDescriptor("hierarchyListeners", zButton.class, "getHierarchyListeners", null);
      properties[59] = new java.beans.PropertyDescriptor("horizontalAlignment", zButton.class, "getHorizontalAlignment", "setHorizontalAlignment");
      properties[60] = new java.beans.PropertyDescriptor("horizontalTextPosition", zButton.class, "getHorizontalTextPosition", "setHorizontalTextPosition");
      properties[61] = new java.beans.PropertyDescriptor("icon", zButton.class, "getIcon", "setIcon");
      properties[62] = new java.beans.PropertyDescriptor("iconTextGap", zButton.class, "getIconTextGap", "setIconTextGap");
      properties[63] = new java.beans.PropertyDescriptor("ignoreRepaint", zButton.class, "getIgnoreRepaint", "setIgnoreRepaint");
      properties[64] = new java.beans.PropertyDescriptor("inheritsPopupMenu", zButton.class, "getInheritsPopupMenu", "setInheritsPopupMenu");
      properties[65] = new java.beans.PropertyDescriptor("inputContext", zButton.class, "getInputContext", null);
      properties[66] = new java.beans.PropertyDescriptor("inputMap", zButton.class, "getInputMap", null);
      properties[67] = new java.beans.PropertyDescriptor("inputMethodListeners", zButton.class, "getInputMethodListeners", null);
      properties[68] = new java.beans.PropertyDescriptor("inputMethodRequests", zButton.class, "getInputMethodRequests", null);
      properties[69] = new java.beans.PropertyDescriptor("inputVerifier", zButton.class, "getInputVerifier", "setInputVerifier");
      properties[70] = new java.beans.PropertyDescriptor("insets", zButton.class, "getInsets", null);
      properties[71] = new java.beans.PropertyDescriptor("itemListeners", zButton.class, "getItemListeners", null);
      properties[72] = new java.beans.PropertyDescriptor("keyListeners", zButton.class, "getKeyListeners", null);
      properties[73] = new java.beans.PropertyDescriptor("label", zButton.class, "getLabel", "setLabel");
      properties[74] = new java.beans.PropertyDescriptor("layout", zButton.class, "getLayout", "setLayout");
      properties[75] = new java.beans.PropertyDescriptor("lightweight", zButton.class, "isLightweight", null);
      properties[76] = new java.beans.PropertyDescriptor("locale", zButton.class, "getLocale", "setLocale");
      properties[77] = new java.beans.PropertyDescriptor("location", zButton.class, "getLocation", "setLocation");
      properties[78] = new java.beans.PropertyDescriptor("locationOnScreen", zButton.class, "getLocationOnScreen", null);
      properties[79] = new java.beans.PropertyDescriptor("managingFocus", zButton.class, "isManagingFocus", null);
      properties[80] = new java.beans.PropertyDescriptor("margin", zButton.class, "getMargin", "setMargin");
      properties[81] = new java.beans.PropertyDescriptor("maximumSize", zButton.class, "getMaximumSize", "setMaximumSize");
      properties[82] = new java.beans.PropertyDescriptor("maximumSizeSet", zButton.class, "isMaximumSizeSet", null);
      properties[83] = new java.beans.PropertyDescriptor("minimumSize", zButton.class, "getMinimumSize", "setMinimumSize");
      properties[84] = new java.beans.PropertyDescriptor("minimumSizeSet", zButton.class, "isMinimumSizeSet", null);
      properties[85] = new java.beans.PropertyDescriptor("mnemonic", zButton.class, null, "setMnemonic");
      properties[86] = new java.beans.PropertyDescriptor("model", zButton.class, "getModel", "setModel");
      properties[87] = new java.beans.PropertyDescriptor("mouseListeners", zButton.class, "getMouseListeners", null);
      properties[88] = new java.beans.PropertyDescriptor("mouseMotionListeners", zButton.class, "getMouseMotionListeners", null);
      properties[89] = new java.beans.PropertyDescriptor("mousePosition", zButton.class, "getMousePosition", null);
      properties[90] = new java.beans.PropertyDescriptor("mouseWheelListeners", zButton.class, "getMouseWheelListeners", null);
      properties[91] = new java.beans.PropertyDescriptor("multiClickThreshhold", zButton.class, "getMultiClickThreshhold", "setMultiClickThreshhold");
      properties[92] = new java.beans.PropertyDescriptor("name", zButton.class, "getName", "setName");
      properties[93] = new java.beans.PropertyDescriptor("nextFocusableComponent", zButton.class, "getNextFocusableComponent", "setNextFocusableComponent");
      properties[94] = new java.beans.PropertyDescriptor("opaque", zButton.class, "isOpaque", "setOpaque");
      properties[95] = new java.beans.PropertyDescriptor("optimizedDrawingEnabled", zButton.class, "isOptimizedDrawingEnabled", null);
      properties[96] = new java.beans.PropertyDescriptor("paintingForPrint", zButton.class, "isPaintingForPrint", null);
      properties[97] = new java.beans.PropertyDescriptor("paintingTile", zButton.class, "isPaintingTile", null);
      properties[98] = new java.beans.PropertyDescriptor("parent", zButton.class, "getParent", null);
      properties[99] = new java.beans.PropertyDescriptor("peer", zButton.class, "getPeer", null);
      properties[100] = new java.beans.PropertyDescriptor("preferredSize", zButton.class, "getPreferredSize", "setPreferredSize");
      properties[101] = new java.beans.PropertyDescriptor("preferredSizeSet", zButton.class, "isPreferredSizeSet", null);
      properties[102] = new java.beans.PropertyDescriptor("pressedIcon", zButton.class, "getPressedIcon", "setPressedIcon");
      properties[103] = new java.beans.PropertyDescriptor("propertyChangeListeners", zButton.class, "getPropertyChangeListeners", null);
      properties[104] = new java.beans.PropertyDescriptor("registeredKeyStrokes", zButton.class, "getRegisteredKeyStrokes", null);
      properties[105] = new java.beans.PropertyDescriptor("requestFocusEnabled", zButton.class, "isRequestFocusEnabled", "setRequestFocusEnabled");
      properties[106] = new java.beans.PropertyDescriptor("rolloverEnabled", zButton.class, "isRolloverEnabled", "setRolloverEnabled");
      properties[107] = new java.beans.PropertyDescriptor("rolloverIcon", zButton.class, "getRolloverIcon", "setRolloverIcon");
      properties[108] = new java.beans.PropertyDescriptor("rolloverSelectedIcon", zButton.class, "getRolloverSelectedIcon", "setRolloverSelectedIcon");
      properties[109] = new java.beans.PropertyDescriptor("rootPane", zButton.class, "getRootPane", null);
      properties[110] = new java.beans.PropertyDescriptor("selected", zButton.class, "isSelected", "setSelected");
      properties[111] = new java.beans.PropertyDescriptor("selectedIcon", zButton.class, "getSelectedIcon", "setSelectedIcon");
      properties[112] = new java.beans.PropertyDescriptor("selectedObjects", zButton.class, "getSelectedObjects", null);
      properties[113] = new java.beans.PropertyDescriptor("showing", zButton.class, "isShowing", null);
      properties[114] = new java.beans.PropertyDescriptor("size", zButton.class, "getSize", "setSize");
      properties[115] = new java.beans.PropertyDescriptor("text", zButton.class, "getText", "setText");
      properties[116] = new java.beans.PropertyDescriptor("toolkit", zButton.class, "getToolkit", null);
      properties[117] = new java.beans.PropertyDescriptor("toolTipText", zButton.class, "getToolTipText", "setToolTipText");
      properties[118] = new java.beans.PropertyDescriptor("topLevelAncestor", zButton.class, "getTopLevelAncestor", null);
      properties[119] = new java.beans.PropertyDescriptor("transferHandler", zButton.class, "getTransferHandler", "setTransferHandler");
      properties[120] = new java.beans.PropertyDescriptor("treeLock", zButton.class, "getTreeLock", null);
      properties[121] = new java.beans.PropertyDescriptor("UI", zButton.class, "getUI", "setUI");
      properties[122] = new java.beans.PropertyDescriptor("UIClassID", zButton.class, "getUIClassID", null);
      properties[123] = new java.beans.PropertyDescriptor("valid", zButton.class, "isValid", null);
      properties[124] = new java.beans.PropertyDescriptor("validateRoot", zButton.class, "isValidateRoot", null);
      properties[125] = new java.beans.PropertyDescriptor("verifyInputWhenFocusTarget", zButton.class, "getVerifyInputWhenFocusTarget", "setVerifyInputWhenFocusTarget");
      properties[126] = new java.beans.PropertyDescriptor("verticalAlignment", zButton.class, "getVerticalAlignment", "setVerticalAlignment");
      properties[127] = new java.beans.PropertyDescriptor("verticalTextPosition", zButton.class, "getVerticalTextPosition", "setVerticalTextPosition");
      properties[''] = new java.beans.PropertyDescriptor("vetoableChangeListeners", zButton.class, "getVetoableChangeListeners", null);
      properties[''] = new java.beans.PropertyDescriptor("visible", zButton.class, "isVisible", "setVisible");
      properties[''] = new java.beans.PropertyDescriptor("visibleRect", zButton.class, "getVisibleRect", null);
      properties[''] = new java.beans.PropertyDescriptor("width", zButton.class, "getWidth", null);
      properties[''] = new java.beans.PropertyDescriptor("x", zButton.class, "getX", null);
      properties[''] = new java.beans.PropertyDescriptor("y", zButton.class, "getY", null);
      properties[''] = new java.beans.PropertyDescriptor("zUI", zButton.class, "getzUI", "setzUI");
    }
    catch (java.beans.IntrospectionException e) {
      e.printStackTrace();
    }
    

    return properties;
  }
  


















  private static java.beans.EventSetDescriptor[] getEdescriptor()
  {
    java.beans.EventSetDescriptor[] eventSets = new java.beans.EventSetDescriptor[16];
    try
    {
      eventSets[0] = new java.beans.EventSetDescriptor(zButton.class, "actionListener", java.awt.event.ActionListener.class, new String[] { "actionPerformed" }, "addActionListener", "removeActionListener");
      eventSets[1] = new java.beans.EventSetDescriptor(zButton.class, "ancestorListener", javax.swing.event.AncestorListener.class, new String[] { "ancestorAdded", "ancestorRemoved", "ancestorMoved" }, "addAncestorListener", "removeAncestorListener");
      eventSets[2] = new java.beans.EventSetDescriptor(zButton.class, "changeListener", javax.swing.event.ChangeListener.class, new String[] { "stateChanged" }, "addChangeListener", "removeChangeListener");
      eventSets[3] = new java.beans.EventSetDescriptor(zButton.class, "componentListener", java.awt.event.ComponentListener.class, new String[] { "componentResized", "componentMoved", "componentShown", "componentHidden" }, "addComponentListener", "removeComponentListener");
      eventSets[4] = new java.beans.EventSetDescriptor(zButton.class, "containerListener", java.awt.event.ContainerListener.class, new String[] { "componentAdded", "componentRemoved" }, "addContainerListener", "removeContainerListener");
      eventSets[5] = new java.beans.EventSetDescriptor(zButton.class, "focusListener", java.awt.event.FocusListener.class, new String[] { "focusGained", "focusLost" }, "addFocusListener", "removeFocusListener");
      eventSets[6] = new java.beans.EventSetDescriptor(zButton.class, "hierarchyBoundsListener", java.awt.event.HierarchyBoundsListener.class, new String[] { "ancestorMoved", "ancestorResized" }, "addHierarchyBoundsListener", "removeHierarchyBoundsListener");
      eventSets[7] = new java.beans.EventSetDescriptor(zButton.class, "hierarchyListener", java.awt.event.HierarchyListener.class, new String[] { "hierarchyChanged" }, "addHierarchyListener", "removeHierarchyListener");
      eventSets[8] = new java.beans.EventSetDescriptor(zButton.class, "inputMethodListener", java.awt.event.InputMethodListener.class, new String[] { "inputMethodTextChanged", "caretPositionChanged" }, "addInputMethodListener", "removeInputMethodListener");
      eventSets[9] = new java.beans.EventSetDescriptor(zButton.class, "itemListener", java.awt.event.ItemListener.class, new String[] { "itemStateChanged" }, "addItemListener", "removeItemListener");
      eventSets[10] = new java.beans.EventSetDescriptor(zButton.class, "keyListener", java.awt.event.KeyListener.class, new String[] { "keyTyped", "keyPressed", "keyReleased" }, "addKeyListener", "removeKeyListener");
      eventSets[11] = new java.beans.EventSetDescriptor(zButton.class, "mouseListener", java.awt.event.MouseListener.class, new String[] { "mouseClicked", "mousePressed", "mouseReleased", "mouseEntered", "mouseExited" }, "addMouseListener", "removeMouseListener");
      eventSets[12] = new java.beans.EventSetDescriptor(zButton.class, "mouseMotionListener", java.awt.event.MouseMotionListener.class, new String[] { "mouseDragged", "mouseMoved" }, "addMouseMotionListener", "removeMouseMotionListener");
      eventSets[13] = new java.beans.EventSetDescriptor(zButton.class, "mouseWheelListener", java.awt.event.MouseWheelListener.class, new String[] { "mouseWheelMoved" }, "addMouseWheelListener", "removeMouseWheelListener");
      eventSets[14] = new java.beans.EventSetDescriptor(zButton.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] { "propertyChange" }, "addPropertyChangeListener", "removePropertyChangeListener");
      eventSets[15] = new java.beans.EventSetDescriptor(zButton.class, "vetoableChangeListener", java.beans.VetoableChangeListener.class, new String[] { "vetoableChange" }, "addVetoableChangeListener", "removeVetoableChangeListener");
    }
    catch (java.beans.IntrospectionException e) {
      e.printStackTrace();
    }
    

    return eventSets;
  }
  
  private static final int EVENT_propertyChangeListener = 14;
  private static final int EVENT_vetoableChangeListener = 15;
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
  private static final int METHOD_doClick26 = 26;
  private static final int METHOD_doClick27 = 27;
  private static final int METHOD_doLayout28 = 28;
  private static final int METHOD_enable29 = 29;
  private static final int METHOD_enable30 = 30;
  private static final int METHOD_enableInputMethods31 = 31;
  private static final int METHOD_findComponentAt32 = 32;
  private static final int METHOD_findComponentAt33 = 33;
  private static final int METHOD_firePropertyChange34 = 34;
  private static final int METHOD_firePropertyChange35 = 35;
  private static final int METHOD_firePropertyChange36 = 36;
  private static final int METHOD_firePropertyChange37 = 37;
  private static final int METHOD_firePropertyChange38 = 38;
  private static final int METHOD_firePropertyChange39 = 39;
  private static final int METHOD_firePropertyChange40 = 40;
  private static final int METHOD_firePropertyChange41 = 41;
  private static final int METHOD_getActionForKeyStroke42 = 42;
  private static final int METHOD_getBaseline43 = 43;
  private static final int METHOD_getBounds44 = 44;
  private static final int METHOD_getClientProperty45 = 45;
  private static final int METHOD_getComponentAt46 = 46;
  private static final int METHOD_getComponentAt47 = 47;
  private static final int METHOD_getComponentZOrder48 = 48;
  private static final int METHOD_getConditionForKeyStroke49 = 49;
  private static final int METHOD_getDefaultLocale50 = 50;
  private static final int METHOD_getFocusTraversalKeys51 = 51;
  private static final int METHOD_getFontMetrics52 = 52;
  private static final int METHOD_getInsets53 = 53;
  private static final int METHOD_getListeners54 = 54;
  private static final int METHOD_getLocation55 = 55;
  private static final int METHOD_getMnemonic56 = 56;
  private static final int METHOD_getMousePosition57 = 57;
  private static final int METHOD_getPopupLocation58 = 58;
  private static final int METHOD_getPropertyChangeListeners59 = 59;
  private static final int METHOD_getSize60 = 60;
  private static final int METHOD_getToolTipLocation61 = 61;
  private static final int METHOD_getToolTipText62 = 62;
  private static final int METHOD_gotFocus63 = 63;
  private static final int METHOD_grabFocus64 = 64;
  private static final int METHOD_handleEvent65 = 65;
  private static final int METHOD_hasFocus66 = 66;
  private static final int METHOD_hide67 = 67;
  private static final int METHOD_imageUpdate68 = 68;
  private static final int METHOD_insets69 = 69;
  private static final int METHOD_inside70 = 70;
  private static final int METHOD_invalidate71 = 71;
  private static final int METHOD_isAncestorOf72 = 72;
  private static final int METHOD_isFocusCycleRoot73 = 73;
  private static final int METHOD_isLightweightComponent74 = 74;
  private static final int METHOD_keyDown75 = 75;
  private static final int METHOD_keyUp76 = 76;
  private static final int METHOD_layout77 = 77;
  private static final int METHOD_list78 = 78;
  private static final int METHOD_list79 = 79;
  private static final int METHOD_list80 = 80;
  private static final int METHOD_list81 = 81;
  private static final int METHOD_list82 = 82;
  private static final int METHOD_locate83 = 83;
  private static final int METHOD_location84 = 84;
  private static final int METHOD_lostFocus85 = 85;
  private static final int METHOD_minimumSize86 = 86;
  private static final int METHOD_mouseDown87 = 87;
  private static final int METHOD_mouseDrag88 = 88;
  private static final int METHOD_mouseEnter89 = 89;
  private static final int METHOD_mouseExit90 = 90;
  private static final int METHOD_mouseMove91 = 91;
  private static final int METHOD_mouseUp92 = 92;
  private static final int METHOD_move93 = 93;
  private static final int METHOD_nextFocus94 = 94;
  private static final int METHOD_paint95 = 95;
  private static final int METHOD_paintAll96 = 96;
  private static final int METHOD_paintComponents97 = 97;
  private static final int METHOD_paintImmediately98 = 98;
  private static final int METHOD_paintImmediately99 = 99;
  private static final int METHOD_postEvent100 = 100;
  private static final int METHOD_preferredSize101 = 101;
  private static final int METHOD_prepareImage102 = 102;
  private static final int METHOD_prepareImage103 = 103;
  private static final int METHOD_print104 = 104;
  private static final int METHOD_printAll105 = 105;
  private static final int METHOD_printComponents106 = 106;
  private static final int METHOD_putClientProperty107 = 107;
  private static final int METHOD_registerKeyboardAction108 = 108;
  private static final int METHOD_registerKeyboardAction109 = 109;
  private static final int METHOD_remove110 = 110;
  private static final int METHOD_remove111 = 111;
  private static final int METHOD_remove112 = 112;
  private static final int METHOD_removeAll113 = 113;
  private static final int METHOD_removeNotify114 = 114;
  private static final int METHOD_removePropertyChangeListener115 = 115;
  private static final int METHOD_repaint116 = 116;
  private static final int METHOD_repaint117 = 117;
  private static final int METHOD_repaint118 = 118;
  private static final int METHOD_repaint119 = 119;
  private static final int METHOD_repaint120 = 120;
  private static final int METHOD_requestDefaultFocus121 = 121;
  private static final int METHOD_requestFocus122 = 122;
  private static final int METHOD_requestFocus123 = 123;
  private static final int METHOD_requestFocusInWindow124 = 124;
  private static final int METHOD_resetKeyboardActions125 = 125;
  private static final int METHOD_reshape126 = 126;
  private static final int METHOD_resize127 = 127;
  private static final int METHOD_resize128 = 128;
  private static final int METHOD_revalidate129 = 129;
  private static final int METHOD_scrollRectToVisible130 = 130;
  private static final int METHOD_setBounds131 = 131;
  private static final int METHOD_setComponentZOrder132 = 132;
  private static final int METHOD_setDefaultLocale133 = 133;
  private static final int METHOD_setMnemonic134 = 134;
  private static final int METHOD_show135 = 135;
  private static final int METHOD_show136 = 136;
  private static final int METHOD_size137 = 137;
  private static final int METHOD_toString138 = 138;
  private static final int METHOD_transferFocus139 = 139;
  private static final int METHOD_transferFocusBackward140 = 140;
  private static final int METHOD_transferFocusDownCycle141 = 141;
  private static final int METHOD_transferFocusUpCycle142 = 142;
  private static final int METHOD_unregisterKeyboardAction143 = 143;
  private static final int METHOD_update144 = 144;
  private static final int METHOD_updateUI145 = 145;
  private static final int METHOD_validate146 = 146;
  private static MethodDescriptor[] getMdescriptor()
  {
    MethodDescriptor[] methods = new MethodDescriptor[''];
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
      methods[26] = new MethodDescriptor(javax.swing.AbstractButton.class.getMethod("doClick", new Class[0]));
      methods[26].setDisplayName("");
      methods[27] = new MethodDescriptor(javax.swing.AbstractButton.class.getMethod("doClick", new Class[] { Integer.TYPE }));
      methods[27].setDisplayName("");
      methods[28] = new MethodDescriptor(java.awt.Container.class.getMethod("doLayout", new Class[0]));
      methods[28].setDisplayName("");
      methods[29] = new MethodDescriptor(java.awt.Component.class.getMethod("enable", new Class[] { Boolean.TYPE }));
      methods[29].setDisplayName("");
      methods[30] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("enable", new Class[0]));
      methods[30].setDisplayName("");
      methods[31] = new MethodDescriptor(java.awt.Component.class.getMethod("enableInputMethods", new Class[] { Boolean.TYPE }));
      methods[31].setDisplayName("");
      methods[32] = new MethodDescriptor(java.awt.Container.class.getMethod("findComponentAt", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[32].setDisplayName("");
      methods[33] = new MethodDescriptor(java.awt.Container.class.getMethod("findComponentAt", new Class[] { java.awt.Point.class }));
      methods[33].setDisplayName("");
      methods[34] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] { String.class, Byte.TYPE, Byte.TYPE }));
      methods[34].setDisplayName("");
      methods[35] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] { String.class, Short.TYPE, Short.TYPE }));
      methods[35].setDisplayName("");
      methods[36] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] { String.class, Long.TYPE, Long.TYPE }));
      methods[36].setDisplayName("");
      methods[37] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] { String.class, Float.TYPE, Float.TYPE }));
      methods[37].setDisplayName("");
      methods[38] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] { String.class, Double.TYPE, Double.TYPE }));
      methods[38].setDisplayName("");
      methods[39] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("firePropertyChange", new Class[] { String.class, Boolean.TYPE, Boolean.TYPE }));
      methods[39].setDisplayName("");
      methods[40] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("firePropertyChange", new Class[] { String.class, Integer.TYPE, Integer.TYPE }));
      methods[40].setDisplayName("");
      methods[41] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("firePropertyChange", new Class[] { String.class, Character.TYPE, Character.TYPE }));
      methods[41].setDisplayName("");
      methods[42] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getActionForKeyStroke", new Class[] { javax.swing.KeyStroke.class }));
      methods[42].setDisplayName("");
      methods[43] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getBaseline", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[43].setDisplayName("");
      methods[44] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getBounds", new Class[] { java.awt.Rectangle.class }));
      methods[44].setDisplayName("");
      methods[45] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getClientProperty", new Class[] { Object.class }));
      methods[45].setDisplayName("");
      methods[46] = new MethodDescriptor(java.awt.Container.class.getMethod("getComponentAt", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[46].setDisplayName("");
      methods[47] = new MethodDescriptor(java.awt.Container.class.getMethod("getComponentAt", new Class[] { java.awt.Point.class }));
      methods[47].setDisplayName("");
      methods[48] = new MethodDescriptor(java.awt.Container.class.getMethod("getComponentZOrder", new Class[] { java.awt.Component.class }));
      methods[48].setDisplayName("");
      methods[49] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getConditionForKeyStroke", new Class[] { javax.swing.KeyStroke.class }));
      methods[49].setDisplayName("");
      methods[50] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getDefaultLocale", new Class[0]));
      methods[50].setDisplayName("");
      methods[51] = new MethodDescriptor(java.awt.Container.class.getMethod("getFocusTraversalKeys", new Class[] { Integer.TYPE }));
      methods[51].setDisplayName("");
      methods[52] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getFontMetrics", new Class[] { java.awt.Font.class }));
      methods[52].setDisplayName("");
      methods[53] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getInsets", new Class[] { java.awt.Insets.class }));
      methods[53].setDisplayName("");
      methods[54] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getListeners", new Class[] { Class.class }));
      methods[54].setDisplayName("");
      methods[55] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getLocation", new Class[] { java.awt.Point.class }));
      methods[55].setDisplayName("");
      methods[56] = new MethodDescriptor(javax.swing.AbstractButton.class.getMethod("getMnemonic", new Class[0]));
      methods[56].setDisplayName("");
      methods[57] = new MethodDescriptor(java.awt.Container.class.getMethod("getMousePosition", new Class[] { Boolean.TYPE }));
      methods[57].setDisplayName("");
      methods[58] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getPopupLocation", new Class[] { java.awt.event.MouseEvent.class }));
      methods[58].setDisplayName("");
      methods[59] = new MethodDescriptor(java.awt.Component.class.getMethod("getPropertyChangeListeners", new Class[] { String.class }));
      methods[59].setDisplayName("");
      methods[60] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getSize", new Class[] { java.awt.Dimension.class }));
      methods[60].setDisplayName("");
      methods[61] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getToolTipLocation", new Class[] { java.awt.event.MouseEvent.class }));
      methods[61].setDisplayName("");
      methods[62] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getToolTipText", new Class[] { java.awt.event.MouseEvent.class }));
      methods[62].setDisplayName("");
      methods[63] = new MethodDescriptor(java.awt.Component.class.getMethod("gotFocus", new Class[] { java.awt.Event.class, Object.class }));
      methods[63].setDisplayName("");
      methods[64] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("grabFocus", new Class[0]));
      methods[64].setDisplayName("");
      methods[65] = new MethodDescriptor(java.awt.Component.class.getMethod("handleEvent", new Class[] { java.awt.Event.class }));
      methods[65].setDisplayName("");
      methods[66] = new MethodDescriptor(java.awt.Component.class.getMethod("hasFocus", new Class[0]));
      methods[66].setDisplayName("");
      methods[67] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("hide", new Class[0]));
      methods[67].setDisplayName("");
      methods[68] = new MethodDescriptor(javax.swing.AbstractButton.class.getMethod("imageUpdate", new Class[] { java.awt.Image.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
      methods[68].setDisplayName("");
      methods[69] = new MethodDescriptor(java.awt.Container.class.getMethod("insets", new Class[0]));
      methods[69].setDisplayName("");
      methods[70] = new MethodDescriptor(java.awt.Component.class.getMethod("inside", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[70].setDisplayName("");
      methods[71] = new MethodDescriptor(java.awt.Container.class.getMethod("invalidate", new Class[0]));
      methods[71].setDisplayName("");
      methods[72] = new MethodDescriptor(java.awt.Container.class.getMethod("isAncestorOf", new Class[] { java.awt.Component.class }));
      methods[72].setDisplayName("");
      methods[73] = new MethodDescriptor(java.awt.Container.class.getMethod("isFocusCycleRoot", new Class[] { java.awt.Container.class }));
      methods[73].setDisplayName("");
      methods[74] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("isLightweightComponent", new Class[] { java.awt.Component.class }));
      methods[74].setDisplayName("");
      methods[75] = new MethodDescriptor(java.awt.Component.class.getMethod("keyDown", new Class[] { java.awt.Event.class, Integer.TYPE }));
      methods[75].setDisplayName("");
      methods[76] = new MethodDescriptor(java.awt.Component.class.getMethod("keyUp", new Class[] { java.awt.Event.class, Integer.TYPE }));
      methods[76].setDisplayName("");
      methods[77] = new MethodDescriptor(java.awt.Container.class.getMethod("layout", new Class[0]));
      methods[77].setDisplayName("");
      methods[78] = new MethodDescriptor(java.awt.Component.class.getMethod("list", new Class[0]));
      methods[78].setDisplayName("");
      methods[79] = new MethodDescriptor(java.awt.Component.class.getMethod("list", new Class[] { java.io.PrintStream.class }));
      methods[79].setDisplayName("");
      methods[80] = new MethodDescriptor(java.awt.Component.class.getMethod("list", new Class[] { java.io.PrintWriter.class }));
      methods[80].setDisplayName("");
      methods[81] = new MethodDescriptor(java.awt.Container.class.getMethod("list", new Class[] { java.io.PrintStream.class, Integer.TYPE }));
      methods[81].setDisplayName("");
      methods[82] = new MethodDescriptor(java.awt.Container.class.getMethod("list", new Class[] { java.io.PrintWriter.class, Integer.TYPE }));
      methods[82].setDisplayName("");
      methods[83] = new MethodDescriptor(java.awt.Container.class.getMethod("locate", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[83].setDisplayName("");
      methods[84] = new MethodDescriptor(java.awt.Component.class.getMethod("location", new Class[0]));
      methods[84].setDisplayName("");
      methods[85] = new MethodDescriptor(java.awt.Component.class.getMethod("lostFocus", new Class[] { java.awt.Event.class, Object.class }));
      methods[85].setDisplayName("");
      methods[86] = new MethodDescriptor(java.awt.Container.class.getMethod("minimumSize", new Class[0]));
      methods[86].setDisplayName("");
      methods[87] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseDown", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
      methods[87].setDisplayName("");
      methods[88] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseDrag", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
      methods[88].setDisplayName("");
      methods[89] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseEnter", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
      methods[89].setDisplayName("");
      methods[90] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseExit", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
      methods[90].setDisplayName("");
      methods[91] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseMove", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
      methods[91].setDisplayName("");
      methods[92] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseUp", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
      methods[92].setDisplayName("");
      methods[93] = new MethodDescriptor(java.awt.Component.class.getMethod("move", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[93].setDisplayName("");
      methods[94] = new MethodDescriptor(java.awt.Component.class.getMethod("nextFocus", new Class[0]));
      methods[94].setDisplayName("");
      methods[95] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("paint", new Class[] { java.awt.Graphics.class }));
      methods[95].setDisplayName("");
      methods[96] = new MethodDescriptor(java.awt.Component.class.getMethod("paintAll", new Class[] { java.awt.Graphics.class }));
      methods[96].setDisplayName("");
      methods[97] = new MethodDescriptor(java.awt.Container.class.getMethod("paintComponents", new Class[] { java.awt.Graphics.class }));
      methods[97].setDisplayName("");
      methods[98] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("paintImmediately", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
      methods[98].setDisplayName("");
      methods[99] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("paintImmediately", new Class[] { java.awt.Rectangle.class }));
      methods[99].setDisplayName("");
      methods[100] = new MethodDescriptor(java.awt.Component.class.getMethod("postEvent", new Class[] { java.awt.Event.class }));
      methods[100].setDisplayName("");
      methods[101] = new MethodDescriptor(java.awt.Container.class.getMethod("preferredSize", new Class[0]));
      methods[101].setDisplayName("");
      methods[102] = new MethodDescriptor(java.awt.Component.class.getMethod("prepareImage", new Class[] { java.awt.Image.class, java.awt.image.ImageObserver.class }));
      methods[102].setDisplayName("");
      methods[103] = new MethodDescriptor(java.awt.Component.class.getMethod("prepareImage", new Class[] { java.awt.Image.class, Integer.TYPE, Integer.TYPE, java.awt.image.ImageObserver.class }));
      methods[103].setDisplayName("");
      methods[104] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("print", new Class[] { java.awt.Graphics.class }));
      methods[104].setDisplayName("");
      methods[105] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("printAll", new Class[] { java.awt.Graphics.class }));
      methods[105].setDisplayName("");
      methods[106] = new MethodDescriptor(java.awt.Container.class.getMethod("printComponents", new Class[] { java.awt.Graphics.class }));
      methods[106].setDisplayName("");
      methods[107] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("putClientProperty", new Class[] { Object.class, Object.class }));
      methods[107].setDisplayName("");
      methods[108] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("registerKeyboardAction", new Class[] { java.awt.event.ActionListener.class, String.class, javax.swing.KeyStroke.class, Integer.TYPE }));
      methods[108].setDisplayName("");
      methods[109] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("registerKeyboardAction", new Class[] { java.awt.event.ActionListener.class, javax.swing.KeyStroke.class, Integer.TYPE }));
      methods[109].setDisplayName("");
      methods[110] = new MethodDescriptor(java.awt.Component.class.getMethod("remove", new Class[] { java.awt.MenuComponent.class }));
      methods[110].setDisplayName("");
      methods[111] = new MethodDescriptor(java.awt.Container.class.getMethod("remove", new Class[] { Integer.TYPE }));
      methods[111].setDisplayName("");
      methods[112] = new MethodDescriptor(java.awt.Container.class.getMethod("remove", new Class[] { java.awt.Component.class }));
      methods[112].setDisplayName("");
      methods[113] = new MethodDescriptor(java.awt.Container.class.getMethod("removeAll", new Class[0]));
      methods[113].setDisplayName("");
      methods[114] = new MethodDescriptor(javax.swing.JButton.class.getMethod("removeNotify", new Class[0]));
      methods[114].setDisplayName("");
      methods[115] = new MethodDescriptor(java.awt.Component.class.getMethod("removePropertyChangeListener", new Class[] { String.class, java.beans.PropertyChangeListener.class }));
      methods[115].setDisplayName("");
      methods[116] = new MethodDescriptor(java.awt.Component.class.getMethod("repaint", new Class[0]));
      methods[116].setDisplayName("");
      methods[117] = new MethodDescriptor(java.awt.Component.class.getMethod("repaint", new Class[] { Long.TYPE }));
      methods[117].setDisplayName("");
      methods[118] = new MethodDescriptor(java.awt.Component.class.getMethod("repaint", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
      methods[118].setDisplayName("");
      methods[119] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("repaint", new Class[] { Long.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
      methods[119].setDisplayName("");
      methods[120] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("repaint", new Class[] { java.awt.Rectangle.class }));
      methods[120].setDisplayName("");
      methods[121] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("requestDefaultFocus", new Class[0]));
      methods[121].setDisplayName("");
      methods[122] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("requestFocus", new Class[0]));
      methods[122].setDisplayName("");
      methods[123] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("requestFocus", new Class[] { Boolean.TYPE }));
      methods[123].setDisplayName("");
      methods[124] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("requestFocusInWindow", new Class[0]));
      methods[124].setDisplayName("");
      methods[125] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("resetKeyboardActions", new Class[0]));
      methods[125].setDisplayName("");
      methods[126] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("reshape", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
      methods[126].setDisplayName("");
      methods[127] = new MethodDescriptor(java.awt.Component.class.getMethod("resize", new Class[] { Integer.TYPE, Integer.TYPE }));
      methods[127].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("resize", new Class[] { java.awt.Dimension.class }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("revalidate", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("scrollRectToVisible", new Class[] { java.awt.Rectangle.class }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("setBounds", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Container.class.getMethod("setComponentZOrder", new Class[] { java.awt.Component.class, Integer.TYPE }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("setDefaultLocale", new Class[] { java.util.Locale.class }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(javax.swing.AbstractButton.class.getMethod("setMnemonic", new Class[] { Integer.TYPE }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("show", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("show", new Class[] { Boolean.TYPE }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("size", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("toString", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("transferFocus", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("transferFocusBackward", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Container.class.getMethod("transferFocusDownCycle", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Component.class.getMethod("transferFocusUpCycle", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("unregisterKeyboardAction", new Class[] { javax.swing.KeyStroke.class }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("update", new Class[] { java.awt.Graphics.class }));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(javax.swing.JButton.class.getMethod("updateUI", new Class[0]));
      methods[''].setDisplayName("");
      methods[''] = new MethodDescriptor(java.awt.Container.class.getMethod("validate", new Class[0]));
      methods[''].setDisplayName("");
    }
    catch (Exception localException) {}
    

    return methods; }
  
  private static java.awt.Image iconColor16 = null;
  private static java.awt.Image iconColor32 = null;
  private static java.awt.Image iconMono16 = null;
  private static java.awt.Image iconMono32 = null;
  private static String iconNameC16 = "/zTools/Images/paint-board-and-brush.png";
  private static String iconNameC32 = "/zTools/Images/paint-board-and-brush (3).png";
  private static String iconNameM16 = "/zTools/Images/paint-board-and-brush (1).png";
  private static String iconNameM32 = "/zTools/Images/paint-board-and-brush (2).png";
  




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
