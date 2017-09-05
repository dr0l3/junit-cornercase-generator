package example;

import java.awt.*;

public class Jacket {
    private Integer buttons;
    private Color color;

    public Integer getButtons() {
        return buttons;
    }

    public void setButtons(Integer buttons) {
        this.buttons = buttons;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Jacket{" +
                "buttons=" + buttons +
                ", color=" + color +
                '}';
    }
}
