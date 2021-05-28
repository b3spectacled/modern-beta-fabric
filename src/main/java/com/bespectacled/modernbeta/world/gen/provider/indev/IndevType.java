package com.bespectacled.modernbeta.world.gen.provider.indev;

public enum IndevType {
    ISLAND(0, "island"),
    FLOATING(1, "floating"),
    INLAND(2, "inland");
    
    private final int id;
    private final String name;
    
    private IndevType(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static IndevType fromId(int id) {
        for (IndevType t : IndevType.values()) {
            if (t.id == id) {
                return t;
            }
        }
        
        throw new IllegalArgumentException("No Indev Type matching id: " + id);
    }
    
    public static IndevType fromName(String name) {
        for (IndevType t : IndevType.values()) {
            if (t.name.equalsIgnoreCase(name)) {
                return t;
            }
        }
        
        throw new IllegalArgumentException("No Indev Type matching name: " + name);
    }
    
    public static IndevType fromString(String str) {
        IndevType type;
        
        if (str.matches("-?\\d+")) {
            type = fromId(Integer.parseInt(str));
        } else {
            type = fromName(str);
        }
        
        return type;
    }
}