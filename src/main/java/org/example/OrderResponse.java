// OrderResponse.java
package org.example;

public class OrderResponse {
    private String track;

    public OrderResponse(String track) {
        this.track = track;
    }

    // Геттеры и сеттеры
    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }
}
