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
/*     */ public class WrapperPlayServerEntityLook
/*     */   extends AbstractPacket
/*     */ {
/*  29 */   public static final PacketType TYPE = PacketType.Play.Server.ENTITY_LOOK;
/*     */   
/*     */   public WrapperPlayServerEntityLook() {
/*  32 */     super(new PacketContainer(TYPE), TYPE);
/*  33 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerEntityLook(PacketContainer packet) {
/*  37 */     super(packet, TYPE);
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
/*  48 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityID(int value) {
/*  57 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(World world) {
/*  67 */     return (Entity)this.handle.getEntityModifier(world).read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(PacketEvent event) {
/*  77 */     return getEntity(event.getPlayer().getWorld());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYaw() {
/*  86 */     return ((Byte)this.handle.getBytes().read(0)).byteValue() * 360.0F / 256.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setYaw(float value) {
/*  95 */     this.handle.getBytes().write(0, Byte.valueOf((byte)(int)(value * 256.0F / 360.0F)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPitch() {
/* 104 */     return ((Byte)this.handle.getBytes().read(1)).byteValue() * 360.0F / 256.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPitch(float value) {
/* 113 */     this.handle.getBytes().write(1, Byte.valueOf((byte)(int)(value * 256.0F / 360.0F)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getOnGround() {
/* 122 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnGround(boolean value) {
/* 131 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerEntityLook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */