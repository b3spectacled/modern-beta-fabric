package mod.bespectacled.modernbeta.world.chunk.provider.indev;

public enum IndevType {
    ISLAND("island"),
    FLOATING("floating"),
    INLAND("inland");
    
    private final String id;
    
    private IndevType(String id) {
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
    
    public static IndevType fromId(String id) {
        for (IndevType type : IndevType.values()) {
            if (type.id.equalsIgnoreCase(id)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("No Indev Type matching id: " + id);
    }
}