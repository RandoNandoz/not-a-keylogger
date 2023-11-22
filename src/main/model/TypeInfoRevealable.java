package model;

/**
 * Weird hacky way to get around Java's stupid fact that I can't get type at runtime.
 */
public interface TypeInfoRevealable {
    // EFFECTS: Returns type as capture
    String getType();
}
