package solutions.bellatrix.web.components.enums;

import lombok.Getter;

@Getter
public enum ScrollPosition {
    CENTER("center"),
    START("start"),
    END("end");

    private final String value;

    ScrollPosition(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
