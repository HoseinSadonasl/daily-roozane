package com.abc.daily.Objects;

public class SpinnerObjects {

    int icon;
    String spinnerObjName;

    public SpinnerObjects(int icon, String name) {
        this.icon = icon;
        this.spinnerObjName = name;
    }

    public SpinnerObjects() {
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getSpinnerObjName() {
        return spinnerObjName;
    }

    public void setSpinnerObjName(String spinnerObjName) {
        this.spinnerObjName = spinnerObjName;
    }
}
