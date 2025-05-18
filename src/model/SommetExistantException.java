package model;

public class SommetExistantException extends Exception {
    public SommetExistantException(String nom) {
        super("Un sommet nommé \"" + nom + "\" existe déjà.");
    }
}
