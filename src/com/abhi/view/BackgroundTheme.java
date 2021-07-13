package com.abhi.view;

public enum BackgroundTheme {
    LIGHT,
    DEFAULT,
    DARK;

    public static String getCssPath(BackgroundTheme backgroundTheme){
        return switch (backgroundTheme) {
            case LIGHT -> "style/themeLight.css";
            case DARK -> "style/themeDark.css";
            case DEFAULT -> "style/themeDefault.css";
        };
    }
}
