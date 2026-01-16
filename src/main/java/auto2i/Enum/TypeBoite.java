package auto2i.Enum;

public enum TypeBoite {
    MANUELLE,
    AUTOMATIQUE;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}