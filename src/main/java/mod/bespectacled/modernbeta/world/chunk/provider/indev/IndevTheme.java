package mod.bespectacled.modernbeta.world.chunk.provider.indev;

import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public enum IndevTheme {
    NORMAL(0, "normal", ModernBetaBiomes.INDEV_NORMAL_ID, Formatting.GREEN),
    HELL(1, "hell", ModernBetaBiomes.INDEV_HELL_ID, Formatting.RED),
    PARADISE(2, "paradise", ModernBetaBiomes.INDEV_PARADISE_ID, Formatting.YELLOW),
    WOODS(3, "woods", ModernBetaBiomes.INDEV_WOODS_ID, Formatting.AQUA);
    
    private static final String PREFIX = "createWorld.customize.indev.levelTheme";
    
    private final int id;
    private final String name;
    private final Identifier defaultBiome;
    private final Formatting color;
    
    private IndevTheme(int id, String name, Identifier defaultBiome, Formatting color) {
        this.id = id;
        this.name = name;
        this.defaultBiome = defaultBiome;
        this.color = color;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Identifier getDefaultBiome() {
        return this.defaultBiome;
    }
    
    public Formatting getColor() {
        return this.color;
    }
    
    public String getDescription() {
        return String.format("%s.%s.description", PREFIX, this.name);
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