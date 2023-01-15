/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.events.PacketEvent;
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
/*     */ public class WrapperPlayServerRelEntityMove
/*     */   extends AbstractPacket
/*     */ {
/*  29 */   public static final PacketType TYPE = PacketType.Play.Server.REL_ENTITY_MOVE;
/*     */ 
/*     */   
/*     */   public WrapperPlayServerRelEntityMove() {
/*  33 */     super(new PacketContainer(TYPE), TYPE);
/*  34 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerRelEntityMove(PacketContainer packet) {
/*  38 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEntityID() {
/*  49 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityID(int value) {
/*  58 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(World world) {
/*  68 */     return (Entity)this.handle.getEntityModifier(world).read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(PacketEvent event) {
/*  78 */     return getEntity(event.getPlayer().getWorld());
/*     */   }
/*     */   
/*     */   public int getDx() {
/*  82 */     return ((Integer)this.handle.getIntegers().read(1)).intValue();
/*     */   }
/*     */   
/*     */   public void setDx(int value) {
/*  86 */     this.handle.getIntegers().write(1, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public int getDy() {
/*  90 */     return ((Integer)this.handle.getIntegers().read(2)).intValue();
/*     */   }
/*     */   
/*     */   public void setDy(int value) {
/*  94 */     this.handle.getIntegers().write(2, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public int getDz() {
/*  98 */     return ((Integer)this.handle.getIntegers().read(3)).intValue();
/*     */   }
/*     */   
/*     */   public void setDz(int value) {
/* 102 */     this.handle.getIntegers().write(3, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getOnGround() {
/* 111 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnGround(boolean value) {
/* 120 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerRelEntityMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */