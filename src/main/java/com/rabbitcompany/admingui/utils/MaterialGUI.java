package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.XMaterial;

public class MaterialGUI {

    public static String getMaterial(String material){
        return XMaterial.matchXMaterial(material).get().parseMaterial().name();
    }

}
