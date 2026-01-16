package auto2i.Enum;


public enum Energie {
    ESSENCE,
    DIESEL,
    ELECTRIQUE,
    HYBRIDE;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}