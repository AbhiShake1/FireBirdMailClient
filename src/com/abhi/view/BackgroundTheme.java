package com.abhi.view;

public enum BackgroundTheme {
    LIGHT,
    DEFAULT,
    DARK;

    public static String getCssPath(BackgroundTheme backgroundTheme){
        switch (backgroundTheme){
            case LIGHT:
                return "style/themeLight.css";
            case DARK:
                return "style/themeDark.css";
            case DEFAULT:
                return "style/themeDefault.css";
            default:
                return null;
        }
    }
}
