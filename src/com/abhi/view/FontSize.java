package com.abhi.view;

public enum FontSize {
    SMALL,
    MEDIUM,
    BIG;

    public static String getCssPath(FontSize fontSize){
        switch (fontSize){
            case MEDIUM:
                return "style/fontMedium.css";
            case SMALL:
                return "style/fontSmall.css";
            case BIG:
                return "style/fontBig.css";
            default:
                return null;
        }
    }
}
