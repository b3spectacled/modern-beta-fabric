package mod.bespectacled.modernbeta.world.chunk.provider.indev;

public enum IndevTheme {
    NORMAL("normal"),
    HELL("hell"),
    PARADISE("paradise"),
    WOODS("woods");
    
    private final String id;
    
    private IndevTheme(String id) {
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
    
    public static IndevTheme fromId(String id) {
        for (IndevTheme theme : IndevTheme.values()) {
            if (theme.id.equalsIgnoreCase(id)) {
                return theme;
            }
        }
        
        throw new IllegalArgumentException("No Indev Theme matching id: " + id);
    }
}