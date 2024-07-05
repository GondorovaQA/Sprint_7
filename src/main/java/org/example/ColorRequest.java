// ColorRequest.java
package org.example;

import java.util.List;

public class ColorRequest {
    private List<String> colors;

    public ColorRequest(List<String> colors) {
        this.colors = colors;
    }

    // Геттеры и сеттеры
    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }
}
