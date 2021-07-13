package com.abhi.view;

public enum FontSize {
    SMALL,
    MEDIUM,
    BIG;

    public static String getCssPath(FontSize fontSize){
        return switch (fontSize) {
            case MEDIUM -> "style/fontMedium.css";
            case SMALL -> "style/fontSmall.css";
            case BIG -> "style/fontBig.css";
        };
    }
}
