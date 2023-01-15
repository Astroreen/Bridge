/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.events.PacketEvent;
/*     */ import com.comphenix.protocol.wrappers.BlockPosition;
/*     */ import com.comphenix.protocol.wrappers.EnumWrappers;
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
/*     */ public class WrapperPlayServerSpawnEntityPainting
/*     */   extends AbstractPacket
/*     */ {
/*  31 */   public static final PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY_PAINTING;
/*     */ 
/*     */   
/*     */   public WrapperPlayServerSpawnEntityPainting() {
/*  35 */     super(new PacketContainer(TYPE), TYPE);
/*  36 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerSpawnEntityPainting(PacketContainer packet) {
/*  40 */     super(packet, TYPE);
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
/*  51 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityID(int value) {
/*  60 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(World world) {
/*  70 */     return (Entity)this.handle.getEntityModifier(world).read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(PacketEvent event) {
/*  80 */     return getEntity(event.getPlayer().getWorld());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPosition getLocation() {
/*  91 */     return (BlockPosition)this.handle.getBlockPositionModifier().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocation(BlockPosition value) {
/* 100 */     this.handle.getBlockPositionModifier().write(0, value);
/*     */   }
/*     */   
/*     */   public EnumWrappers.Direction getDirection() {
/* 104 */     return (EnumWrappers.Direction)this.handle.getDirections().read(0);
/*     */   }
/*     */   
/*     */   public void setDirection(EnumWrappers.Direction value) {
/* 108 */     this.handle.getDirections().write(0, value);
/*     */   }
/*     */   
/*     */   public int getPaintingId() {
/* 112 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */   
/*     */   public void setPaintingId(int value) {
/* 116 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerSpawnEntityPainting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */