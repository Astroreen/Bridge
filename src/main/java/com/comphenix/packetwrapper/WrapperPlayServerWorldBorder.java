/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.wrappers.EnumWrappers;
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
/*     */ public class WrapperPlayServerWorldBorder
/*     */   extends AbstractPacket
/*     */ {
/*  26 */   public static final PacketType TYPE = PacketType.Play.Server.WORLD_BORDER;
/*     */   
/*     */   public WrapperPlayServerWorldBorder() {
/*  29 */     super(new PacketContainer(TYPE), TYPE);
/*  30 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerWorldBorder(PacketContainer packet) {
/*  34 */     super(packet, TYPE);
/*     */   }
/*     */   
/*     */   public EnumWrappers.WorldBorderAction getAction() {
/*  38 */     return (EnumWrappers.WorldBorderAction)this.handle.getWorldBorderActions().read(0);
/*     */   }
/*     */   
/*     */   public void setAction(EnumWrappers.WorldBorderAction value) {
/*  42 */     this.handle.getWorldBorderActions().write(0, value);
/*     */   }
/*     */   
/*     */   public int getPortalTeleportBoundary() {
/*  46 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */   
/*     */   public void setPortalTeleportBoundary(int value) {
/*  50 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public double getCenterX() {
/*  54 */     return ((Double)this.handle.getDoubles().read(0)).doubleValue();
/*     */   }
/*     */   
/*     */   public void setCenterX(double value) {
/*  58 */     this.handle.getDoubles().write(0, Double.valueOf(value));
/*     */   }
/*     */   
/*     */   public double getCenterZ() {
/*  62 */     return ((Double)this.handle.getDoubles().read(1)).doubleValue();
/*     */   }
/*     */   
/*     */   public void setCenterZ(double value) {
/*  66 */     this.handle.getDoubles().write(1, Double.valueOf(value));
/*     */   }
/*     */   
/*     */   public double getOldRadius() {
/*  70 */     return ((Double)this.handle.getDoubles().read(2)).doubleValue();
/*     */   }
/*     */   
/*     */   public void setOldRadius(double value) {
/*  74 */     this.handle.getDoubles().write(2, Double.valueOf(value));
/*     */   }
/*     */   
/*     */   public double getRadius() {
/*  78 */     return ((Double)this.handle.getDoubles().read(3)).doubleValue();
/*     */   }
/*     */   
/*     */   public void setRadius(double value) {
/*  82 */     this.handle.getDoubles().write(3, Double.valueOf(value));
/*     */   }
/*     */   
/*     */   public long getSpeed() {
/*  86 */     return ((Long)this.handle.getLongs().read(0)).longValue();
/*     */   }
/*     */   
/*     */   public void setSpeed(long value) {
/*  90 */     this.handle.getLongs().write(0, Long.valueOf(value));
/*     */   }
/*     */   
/*     */   public int getWarningTime() {
/*  94 */     return ((Integer)this.handle.getIntegers().read(1)).intValue();
/*     */   }
/*     */   
/*     */   public void setWarningTime(int value) {
/*  98 */     this.handle.getIntegers().write(1, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public int getWarningDistance() {
/* 102 */     return ((Integer)this.handle.getIntegers().read(2)).intValue();
/*     */   }
/*     */   
/*     */   public void setWarningDistance(int value) {
/* 106 */     this.handle.getIntegers().write(2, Integer.valueOf(value));
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerWorldBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */