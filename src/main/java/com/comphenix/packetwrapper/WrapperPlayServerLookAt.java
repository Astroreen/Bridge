/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.events.PacketEvent;
/*     */ import com.comphenix.protocol.utility.MinecraftReflection;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Entity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WrapperPlayServerLookAt
/*     */   extends AbstractPacket
/*     */ {
/*  31 */   public static final PacketType TYPE = PacketType.Play.Server.LOOK_AT;
/*     */   
/*     */   public WrapperPlayServerLookAt() {
/*  34 */     super(new PacketContainer(TYPE), TYPE);
/*  35 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerLookAt(PacketContainer packet) {
/*  39 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Anchor getAnchor() {
/*  49 */     return (Anchor)this.handle.getEnumModifier(Anchor.class, MinecraftReflection.getMinecraftClass("ArgumentAnchor$Anchor"))
/*  50 */       .readSafely(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAnchor(Anchor value) {
/*  58 */     this.handle.getEnumModifier(Anchor.class, MinecraftReflection.getMinecraftClass("ArgumentAnchor$Anchor"))
/*  59 */       .writeSafely(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTargetX() {
/*  69 */     return ((Double)this.handle.getDoubles().read(0)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetX(double value) {
/*  77 */     this.handle.getDoubles().write(0, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTargetY() {
/*  87 */     return ((Double)this.handle.getDoubles().read(1)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetY(double value) {
/*  95 */     this.handle.getDoubles().write(1, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTargetZ() {
/* 105 */     return ((Double)this.handle.getDoubles().read(2)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetZ(double value) {
/* 113 */     this.handle.getDoubles().write(2, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIsEntity() {
/* 123 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsEntity(boolean value) {
/* 131 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEntityID() {
/* 141 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityID(int value) {
/* 149 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(World world) {
/* 158 */     return (Entity)this.handle.getEntityModifier(world).read(6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(PacketEvent event) {
/* 167 */     return getEntity(event.getPlayer().getWorld());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Anchor getEntityAnchor() {
/* 177 */     return (Anchor)this.handle.getEnumModifier(Anchor.class, MinecraftReflection.getMinecraftClass("ArgumentAnchor$Anchor"))
/* 178 */       .readSafely(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityAnchor(Anchor value) {
/* 186 */     this.handle.getEnumModifier(Anchor.class, MinecraftReflection.getMinecraftClass("ArgumentAnchor$Anchor"))
/* 187 */       .writeSafely(1, value);
/*     */   }
/*     */   
/*     */   public enum Anchor {
/* 191 */     FEET,
/* 192 */     EYES;
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerLookAt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */