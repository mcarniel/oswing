package org.openswing.swing.client;

import java.beans.*;

/**
 * <p>
 * Title: OpenSwing Framework
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (C) 2006 Mauro Carniel
 * </p>
 * <p>
 * </p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class CheckBoxControlBeanInfo extends SimpleBeanInfo {
    Class  beanClass              = CheckBoxControl.class;
    String iconColor16x16Filename = "CheckBoxControl16.png";
    String iconColor32x32Filename = "CheckBoxControl.png";
    String iconMono16x16Filename  = "CheckBoxControl16.png";
    String iconMono32x32Filename  = "CheckBoxControl.png";

    public CheckBoxControlBeanInfo() {
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor _allowNullValue = new PropertyDescriptor("allowNullValue", beanClass, "isAllowNullValue", "setAllowNullValue");
            PropertyDescriptor _attributeName = new PropertyDescriptor("attributeName", beanClass, "getAttributeName", "setAttributeName");
            _attributeName.setPropertyEditorClass(BooleanAttributeNameEditor.class);
            PropertyDescriptor _canCopy = new PropertyDescriptor("canCopy", beanClass, "isCanCopy", "setCanCopy");
            PropertyDescriptor _linkLabel = new PropertyDescriptor("linkLabel", beanClass, "getLinkLabel", "setLinkLabel");
            PropertyDescriptor _font = new PropertyDescriptor("font", beanClass, "getFont", "setFont");
            PropertyDescriptor _enabled = new PropertyDescriptor("enabled", beanClass, "isEnabled", "setEnabled");
            PropertyDescriptor _enabledOnInsert = new PropertyDescriptor("enabledOnInsert", beanClass, "isEnabledOnInsert", "setEnabledOnInsert");
            PropertyDescriptor _enabledOnEdit = new PropertyDescriptor("enabledOnEdit", beanClass, "isEnabledOnEdit", "setEnabledOnEdit");
            PropertyDescriptor _toolTipText = new PropertyDescriptor("toolTipText", beanClass, "getToolTipText", "setToolTipText");
            PropertyDescriptor[] pds = new PropertyDescriptor[] { _allowNullValue, _attributeName, _canCopy, _font, _linkLabel, _enabled, _enabledOnInsert, _enabledOnEdit,
                    _toolTipText };
            return pds;
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {
            case BeanInfo.ICON_COLOR_16x16:
                return iconColor16x16Filename != null ? loadImage(iconColor16x16Filename) : null;
            case BeanInfo.ICON_COLOR_32x32:
                return iconColor32x32Filename != null ? loadImage(iconColor32x32Filename) : null;
            case BeanInfo.ICON_MONO_16x16:
                return iconMono16x16Filename != null ? loadImage(iconMono16x16Filename) : null;
            case BeanInfo.ICON_MONO_32x32:
                return iconMono32x32Filename != null ? loadImage(iconMono32x32Filename) : null;
        }
        return null;
    }


    public BeanInfo[] getAdditionalBeanInfo() {
        Class superclass = beanClass.getSuperclass();
        try {
            BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
            return new BeanInfo[] { superBeanInfo };
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
