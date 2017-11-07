package org.apache.xerces.util;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;

public class XMLAttributesImpl
  implements XMLAttributes
{
  protected static final int TABLE_SIZE = 101;
  protected static final int SIZE_LIMIT = 20;
  protected boolean fNamespaces = true;
  protected int fLargeCount = 1;
  protected int fLength;
  protected Attribute[] fAttributes = new Attribute[4];
  protected Attribute[] fAttributeTableView;
  protected int[] fAttributeTableViewChainState;
  protected int fTableViewBuckets;
  protected boolean fIsTableViewConsistent;
  
  public XMLAttributesImpl()
  {
    this(101);
  }
  
  public XMLAttributesImpl(int paramInt)
  {
    fTableViewBuckets = paramInt;
    for (int i = 0; i < fAttributes.length; i++) {
      fAttributes[i] = new Attribute();
    }
  }
  
  public void setNamespaces(boolean paramBoolean)
  {
    fNamespaces = paramBoolean;
  }
  
  public int addAttribute(QName paramQName, String paramString1, String paramString2)
  {
    int i;
    if (fLength < 20)
    {
      i = (uri != null) && (uri.length() != 0) ? getIndexFast(uri, localpart) : getIndexFast(rawname);
      if (i == -1)
      {
        i = fLength;
        if (fLength++ == fAttributes.length)
        {
          Attribute[] arrayOfAttribute1 = new Attribute[fAttributes.length + 4];
          System.arraycopy(fAttributes, 0, arrayOfAttribute1, 0, fAttributes.length);
          for (int k = fAttributes.length; k < arrayOfAttribute1.length; k++) {
            arrayOfAttribute1[k] = new Attribute();
          }
          fAttributes = arrayOfAttribute1;
        }
      }
    }
    else if ((uri == null) || (uri.length() == 0) || ((i = getIndexFast(uri, localpart)) == -1))
    {
      if ((!fIsTableViewConsistent) || (fLength == 20))
      {
        prepareAndPopulateTableView();
        fIsTableViewConsistent = true;
      }
      int j = getTableViewBucket(rawname);
      Object localObject;
      if (fAttributeTableViewChainState[j] != fLargeCount)
      {
        i = fLength;
        if (fLength++ == fAttributes.length)
        {
          localObject = new Attribute[fAttributes.length << 1];
          System.arraycopy(fAttributes, 0, localObject, 0, fAttributes.length);
          for (int m = fAttributes.length; m < localObject.length; m++) {
            localObject[m] = new Attribute();
          }
          fAttributes = ((Attribute[])localObject);
        }
        fAttributeTableViewChainState[j] = fLargeCount;
        fAttributes[i].next = null;
        fAttributeTableView[j] = fAttributes[i];
      }
      else
      {
        for (localObject = fAttributeTableView[j]; localObject != null; localObject = next) {
          if (name.rawname == rawname) {
            break;
          }
        }
        if (localObject == null)
        {
          i = fLength;
          if (fLength++ == fAttributes.length)
          {
            Attribute[] arrayOfAttribute2 = new Attribute[fAttributes.length << 1];
            System.arraycopy(fAttributes, 0, arrayOfAttribute2, 0, fAttributes.length);
            for (int n = fAttributes.length; n < arrayOfAttribute2.length; n++) {
              arrayOfAttribute2[n] = new Attribute();
            }
            fAttributes = arrayOfAttribute2;
          }
          fAttributes[i].next = fAttributeTableView[j];
          fAttributeTableView[j] = fAttributes[i];
        }
        else
        {
          i = getIndexFast(rawname);
        }
      }
    }
    Attribute localAttribute = fAttributes[i];
    name.setValues(paramQName);
    type = paramString1;
    value = paramString2;
    nonNormalizedValue = paramString2;
    specified = false;
    augs.removeAllItems();
    return i;
  }
  
  public void removeAllAttributes()
  {
    fLength = 0;
  }
  
  public void removeAttributeAt(int paramInt)
  {
    fIsTableViewConsistent = false;
    if (paramInt < fLength - 1)
    {
      Attribute localAttribute = fAttributes[paramInt];
      System.arraycopy(fAttributes, paramInt + 1, fAttributes, paramInt, fLength - paramInt - 1);
      fAttributes[(fLength - 1)] = localAttribute;
    }
    fLength -= 1;
  }
  
  public void setName(int paramInt, QName paramQName)
  {
    fAttributes[paramInt].name.setValues(paramQName);
  }
  
  public void getName(int paramInt, QName paramQName)
  {
    paramQName.setValues(fAttributes[paramInt].name);
  }
  
  public void setType(int paramInt, String paramString)
  {
    fAttributes[paramInt].type = paramString;
  }
  
  public void setValue(int paramInt, String paramString)
  {
    Attribute localAttribute = fAttributes[paramInt];
    value = paramString;
    nonNormalizedValue = paramString;
  }
  
  public void setNonNormalizedValue(int paramInt, String paramString)
  {
    if (paramString == null) {
      paramString = fAttributes[paramInt].value;
    }
    fAttributes[paramInt].nonNormalizedValue = paramString;
  }
  
  public String getNonNormalizedValue(int paramInt)
  {
    String str = fAttributes[paramInt].nonNormalizedValue;
    return str;
  }
  
  public void setSpecified(int paramInt, boolean paramBoolean)
  {
    fAttributes[paramInt].specified = paramBoolean;
  }
  
  public boolean isSpecified(int paramInt)
  {
    return fAttributes[paramInt].specified;
  }
  
  public int getLength()
  {
    return fLength;
  }
  
  public String getType(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fLength)) {
      return null;
    }
    return getReportableType(fAttributes[paramInt].type);
  }
  
  public String getType(String paramString)
  {
    int i = getIndex(paramString);
    return i != -1 ? getReportableType(fAttributes[i].type) : null;
  }
  
  public String getValue(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fLength)) {
      return null;
    }
    return fAttributes[paramInt].value;
  }
  
  public String getValue(String paramString)
  {
    int i = getIndex(paramString);
    return i != -1 ? fAttributes[i].value : null;
  }
  
  public String getName(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fLength)) {
      return null;
    }
    return fAttributes[paramInt].name.rawname;
  }
  
  public int getIndex(String paramString)
  {
    for (int i = 0; i < fLength; i++)
    {
      Attribute localAttribute = fAttributes[i];
      if ((name.rawname != null) && (name.rawname.equals(paramString))) {
        return i;
      }
    }
    return -1;
  }
  
  public int getIndex(String paramString1, String paramString2)
  {
    for (int i = 0; i < fLength; i++)
    {
      Attribute localAttribute = fAttributes[i];
      if ((name.localpart != null) && (name.localpart.equals(paramString2)) && ((paramString1 == name.uri) || ((paramString1 != null) && (name.uri != null) && (name.uri.equals(paramString1))))) {
        return i;
      }
    }
    return -1;
  }
  
  public String getLocalName(int paramInt)
  {
    if (!fNamespaces) {
      return "";
    }
    if ((paramInt < 0) || (paramInt >= fLength)) {
      return null;
    }
    return fAttributes[paramInt].name.localpart;
  }
  
  public String getQName(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fLength)) {
      return null;
    }
    String str = fAttributes[paramInt].name.rawname;
    return str != null ? str : "";
  }
  
  public String getType(String paramString1, String paramString2)
  {
    if (!fNamespaces) {
      return null;
    }
    int i = getIndex(paramString1, paramString2);
    return i != -1 ? getReportableType(fAttributes[i].type) : null;
  }
  
  public String getPrefix(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fLength)) {
      return null;
    }
    String str = fAttributes[paramInt].name.prefix;
    return str != null ? str : "";
  }
  
  public String getURI(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fLength)) {
      return null;
    }
    String str = fAttributes[paramInt].name.uri;
    return str;
  }
  
  public String getValue(String paramString1, String paramString2)
  {
    int i = getIndex(paramString1, paramString2);
    return i != -1 ? getValue(i) : null;
  }
  
  public Augmentations getAugmentations(String paramString1, String paramString2)
  {
    int i = getIndex(paramString1, paramString2);
    return i != -1 ? fAttributes[i].augs : null;
  }
  
  public Augmentations getAugmentations(String paramString)
  {
    int i = getIndex(paramString);
    return i != -1 ? fAttributes[i].augs : null;
  }
  
  public Augmentations getAugmentations(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fLength)) {
      return null;
    }
    return fAttributes[paramInt].augs;
  }
  
  public void setAugmentations(int paramInt, Augmentations paramAugmentations)
  {
    fAttributes[paramInt].augs = paramAugmentations;
  }
  
  public void setURI(int paramInt, String paramString)
  {
    fAttributes[paramInt].name.uri = paramString;
  }
  
  public int getIndexFast(String paramString)
  {
    for (int i = 0; i < fLength; i++)
    {
      Attribute localAttribute = fAttributes[i];
      if (name.rawname == paramString) {
        return i;
      }
    }
    return -1;
  }
  
  public void addAttributeNS(QName paramQName, String paramString1, String paramString2)
  {
    int i = fLength;
    if (fLength++ == fAttributes.length)
    {
      if (fLength < 20) {
        localObject = new Attribute[fAttributes.length + 4];
      } else {
        localObject = new Attribute[fAttributes.length << 1];
      }
      System.arraycopy(fAttributes, 0, localObject, 0, fAttributes.length);
      for (int j = fAttributes.length; j < localObject.length; j++) {
        localObject[j] = new Attribute();
      }
      fAttributes = ((Attribute[])localObject);
    }
    Object localObject = fAttributes[i];
    name.setValues(paramQName);
    type = paramString1;
    value = paramString2;
    nonNormalizedValue = paramString2;
    specified = false;
    augs.removeAllItems();
  }
  
  public QName checkDuplicatesNS()
  {
    int k;
    Attribute localAttribute3;
    if (fLength <= 20)
    {
      for (int i = 0; i < fLength - 1; i++)
      {
        Attribute localAttribute2 = fAttributes[i];
        for (k = i + 1; k < fLength; k++)
        {
          localAttribute3 = fAttributes[k];
          if ((name.localpart == name.localpart) && (name.uri == name.uri)) {
            return name;
          }
        }
      }
    }
    else
    {
      fIsTableViewConsistent = false;
      prepareTableView();
      for (k = fLength - 1; k >= 0; k--)
      {
        Attribute localAttribute1 = fAttributes[k];
        int j = getTableViewBucket(name.localpart, name.uri);
        if (fAttributeTableViewChainState[j] != fLargeCount)
        {
          fAttributeTableViewChainState[j] = fLargeCount;
          next = null;
          fAttributeTableView[j] = localAttribute1;
        }
        else
        {
          for (localAttribute3 = fAttributeTableView[j]; localAttribute3 != null; localAttribute3 = next) {
            if ((name.localpart == name.localpart) && (name.uri == name.uri)) {
              return name;
            }
          }
          next = fAttributeTableView[j];
          fAttributeTableView[j] = localAttribute1;
        }
      }
    }
    return null;
  }
  
  public int getIndexFast(String paramString1, String paramString2)
  {
    for (int i = 0; i < fLength; i++)
    {
      Attribute localAttribute = fAttributes[i];
      if ((name.localpart == paramString2) && (name.uri == paramString1)) {
        return i;
      }
    }
    return -1;
  }
  
  private String getReportableType(String paramString)
  {
    if (paramString.charAt(0) == '(') {
      return "NMTOKEN";
    }
    return paramString;
  }
  
  protected int getTableViewBucket(String paramString)
  {
    return (paramString.hashCode() & 0x7FFFFFFF) % fTableViewBuckets;
  }
  
  protected int getTableViewBucket(String paramString1, String paramString2)
  {
    if (paramString2 == null) {
      return (paramString1.hashCode() & 0x7FFFFFFF) % fTableViewBuckets;
    }
    return (paramString1.hashCode() + paramString2.hashCode() & 0x7FFFFFFF) % fTableViewBuckets;
  }
  
  protected void cleanTableView()
  {
    if (++fLargeCount < 0)
    {
      if (fAttributeTableViewChainState != null) {
        for (int i = fTableViewBuckets - 1; i >= 0; i--) {
          fAttributeTableViewChainState[i] = 0;
        }
      }
      fLargeCount = 1;
    }
  }
  
  protected void prepareTableView()
  {
    if (fAttributeTableView == null)
    {
      fAttributeTableView = new Attribute[fTableViewBuckets];
      fAttributeTableViewChainState = new int[fTableViewBuckets];
    }
    else
    {
      cleanTableView();
    }
  }
  
  protected void prepareAndPopulateTableView()
  {
    prepareTableView();
    for (int j = 0; j < fLength; j++)
    {
      Attribute localAttribute = fAttributes[j];
      int i = getTableViewBucket(name.rawname);
      if (fAttributeTableViewChainState[i] != fLargeCount)
      {
        fAttributeTableViewChainState[i] = fLargeCount;
        next = null;
        fAttributeTableView[i] = localAttribute;
      }
      else
      {
        next = fAttributeTableView[i];
        fAttributeTableView[i] = localAttribute;
      }
    }
  }
  
  static class Attribute
  {
    public final QName name = new QName();
    public String type;
    public String value;
    public String nonNormalizedValue;
    public boolean specified;
    public Augmentations augs = new AugmentationsImpl();
    public Attribute next;
    
    Attribute() {}
  }
}
