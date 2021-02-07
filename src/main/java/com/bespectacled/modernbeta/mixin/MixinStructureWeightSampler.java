package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.StructurePiece;
import net.minecraft.world.gen.StructureWeightSampler;

@Mixin(StructureWeightSampler.class)
public interface MixinStructureWeightSampler {
    @Accessor("pieces")
    public ObjectList<StructurePiece> getPieces();
    
    @Accessor("junctions")
    public ObjectList<JigsawJunction> getJunctions();
    
    @Accessor("pieceIterator")
    public ObjectListIterator<StructurePiece> getPieceIterator();
    
    @Accessor("junctionIterator")
    public ObjectListIterator<JigsawJunction> getJunctionIterator();
}
