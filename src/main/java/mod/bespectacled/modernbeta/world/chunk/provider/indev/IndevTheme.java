package mod.bespectacled.modernbeta.world.chunk.provider.indev;

public enum IndevTheme {
    NORMAL(0, "normal"),
    HELL(1, "hell"),
    PARADISE(2, "paradise"),
    WOODS(3, "woods");
    
    private final int id;
    private final String name;
    
    private IndevTheme(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static IndevTheme fromId(int id) {
        for (IndevTheme t : IndevTheme.values()) {
            if (t.id == id) {
                return t;
            }
        }
        
        throw new IllegalArgumentException("No Indev Theme matching id: " + id);
    }
    
    public static IndevTheme fromName(String name) {
        for (IndevTheme t : IndevTheme.values()) {
            if (t.name.equalsIgnoreCase(name)) {
                return t;
            }
        }
        
        throw new IllegalArgumentException("No Indev Theme matching name: " + name);
    }
    
    public static IndevTheme fromString(String str) {
        IndevTheme theme;
        
        if (str.matches("-?\\d+")) {
            theme = fromId(Integer.parseInt(str));
        } else {
            theme = fromName(str);
        }
        
        return theme;
    }
}