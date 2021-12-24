package shared.util;

public class Pair {
    private long key;
    private int value;

    public Pair(Long key, int value) {
        this.key = key;
        this.value = value;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
