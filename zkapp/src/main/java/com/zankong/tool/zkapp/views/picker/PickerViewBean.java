package com.zankong.tool.zkapp.views.picker;

/**
 *
 */
public class PickerViewBean {
    public String[] text;
    public String textColor;
    public float textSize;
    public boolean textBold;
    public boolean line=true;
    public String lineColor;
    public int lineHeight;
    public int lineWidth;
    public int width;
    public int  height;
    public String onClick;
    public PickerViewBean() {
    }

    public PickerViewBean(String[] text, String textColor, float textSize, boolean textBold, boolean line, String lineColor, int  lineHeight, int lineWidth) {
        this.text = text;
        this.textColor = textColor;
        this.textSize = textSize;
        this.textBold = textBold;
        this.line = line;
        this.lineColor = lineColor;
        this.lineHeight = lineHeight;
        this.lineWidth = lineWidth;
    }

    public String[] getText() {
        return text;
    }

    public void setText(String[] text) {
        this.text = text;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public boolean isTextBold() {
        return textBold;
    }

    public void setTextBold(boolean textBold) {
        this.textBold = textBold;
    }

    public boolean isLine() {
        return line;
    }

    public void setLine(boolean line) {
        this.line = line;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getOnClick() {
        return onClick;
    }

    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }
}
