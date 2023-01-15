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
/*     */ public class WrapperPlayServerEntityTeleport
/*     */   extends AbstractPacket
/*     */ {
/*  29 */   public static final PacketType TYPE = PacketType.Play.Server.ENTITY_TELEPORT;
/*     */ 
/*     */   
/*     */   public WrapperPlayServerEntityTeleport() {
/*  33 */     super(new PacketContainer(TYPE), TYPE);
/*  34 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerEntityTeleport(PacketContainer packet) {
/*  38 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEntityID() {
/*  47 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityID(int value) {
/*  56 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(World world) {
/*  66 */     return (Entity)this.handle.getEntityModifier(world).read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(PacketEvent event) {
/*  76 */     return getEntity(event.getPlayer().getWorld());
/*     */   }
/*     */   
/*     */   public double getX() {
/*  80 */     return ((Double)this.handle.getDoubles().read(0)).doubleValue();
/*     */   }
/*     */   
/*     */   public void setX(double value) {
/*  84 */     this.handle.getDoubles().write(0, Double.valueOf(value));
/*     */   }
/*     */   
/*     */   public double getY() {
/*  88 */     return ((Double)this.handle.getDoubles().read(1)).doubleValue();
/*     */   }
/*     */   
/*     */   public void setY(double value) {
/*  92 */     this.handle.getDoubles().write(1, Double.valueOf(value));
/*     */   }
/*     */   
/*     */   public double getZ() {
/*  96 */     return ((Double)this.handle.getDoubles().read(2)).doubleValue();
/*     */   }
/*     */   
/*     */   public void setZ(double value) {
/* 100 */     this.handle.getDoubles().write(2, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYaw() {
/* 109 */     return ((Byte)this.handle.getBytes().read(0)).byteValue() * 360.0F / 256.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setYaw(float value) {
/* 118 */     this.handle.getBytes().write(0, Byte.valueOf((byte)(int)(value * 256.0F / 360.0F)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPitch() {
/* 127 */     return ((Byte)this.handle.getBytes().read(1)).byteValue() * 360.0F / 256.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPitch(float value) {
/* 136 */     this.handle.getBytes().write(1, Byte.valueOf((byte)(int)(value * 256.0F / 360.0F)));
/*     */   }
/*     */   
/*     */   public boolean getOnGround() {
/* 140 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setOnGround(boolean value) {
/* 144 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerEntityTeleport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */