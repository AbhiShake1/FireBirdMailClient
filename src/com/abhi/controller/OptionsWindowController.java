package com.abhi.controller;

import com.abhi.EmailManager;
import com.abhi.controller.persistence.Preference;
import com.abhi.view.BackgroundTheme;
import com.abhi.view.FontSize;
import com.abhi.view.ViewFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class OptionsWindowController extends BaseController implements Initializable {

    public OptionsWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    private Slider fontSizePicker;

    @FXML
    private ChoiceBox<BackgroundTheme> themePicker;

    @FXML
    private void applyButtonAction() {
        Preference pref = Preference.getInstance();
        pref.setValue("pref_background_key",String.valueOf(themePicker.getValue().ordinal()));
        viewFactory.setBackgroundTheme(themePicker.getValue());
        pref.setValue("pref_font_size_key",String.valueOf((int)fontSizePicker.getValue()));
        viewFactory.setFontSize(FontSize.values()[(int)fontSizePicker.getValue()]);
        viewFactory.updateAllStyles();
    }

    @FXML
    private void cancelButtonAction() {
        Stage stage = (Stage) fontSizePicker.getScene().getWindow(); //random element of stage to find its parent.
                                                                //any element belonging to the required stage should work
        viewFactory.close(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpBackgroundThemePicker();
        setUpSizePicker();
    }

    private void setUpSizePicker() {
        fontSizePicker.setMin(0);
        fontSizePicker.setMax(FontSize.values().length-1); //maximum/last value of FontSize enum
        fontSizePicker.setValue(viewFactory.getFontSize().ordinal()); //ordinal to get order of the respective enum constant
        fontSizePicker.setMajorTickUnit(1);
        fontSizePicker.setMinorTickCount(0);
        fontSizePicker.setBlockIncrement(1);
        fontSizePicker.setSnapToTicks(true); //snap/break to tick marks
        fontSizePicker.setShowTickMarks(true);
        fontSizePicker.setShowTickLabels(true);
        fontSizePicker.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double aDouble) {
                int i = aDouble.intValue();
                return FontSize.values()[i].toString();
            }

            @Override
            public Double fromString(String s) {
                return null;
            }
        });
        fontSizePicker.valueProperty().addListener((o, oldVal, newVal)->{
            fontSizePicker.setValue(newVal.intValue()); //snap to values of enum directly and set
        });
    }

    private void setUpBackgroundThemePicker() {
        themePicker.setItems(FXCollections.observableArrayList(BackgroundTheme.values()));
        themePicker.setValue(viewFactory.getBackgroundTheme());
    }
}

