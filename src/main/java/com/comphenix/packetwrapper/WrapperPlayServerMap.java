/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
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
/*     */ public class WrapperPlayServerMap
/*     */   extends AbstractPacket
/*     */ {
/*  25 */   public static final PacketType TYPE = PacketType.Play.Server.MAP;
/*     */   
/*     */   public WrapperPlayServerMap() {
/*  28 */     super(new PacketContainer(TYPE), TYPE);
/*  29 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerMap(PacketContainer packet) {
/*  33 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getItemDamage() {
/*  44 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItemDamage(int value) {
/*  53 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getScale() {
/*  62 */     return ((Byte)this.handle.getBytes().read(0)).byteValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScale(byte value) {
/*  71 */     this.handle.getBytes().write(0, Byte.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean getTrackingPosition() {
/*  75 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setTrackingPosition(boolean value) {
/*  79 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   public Object[] getMapIcons() {
/*  83 */     return (Object[])this.handle.getModifier().read(3);
/*     */   }
/*     */   
/*     */   public void setMapIcons(Object[] value) {
/*  87 */     this.handle.getModifier().write(3, value);
/*     */   }
/*     */   
/*     */   public int getColumns() {
/*  91 */     return ((Integer)this.handle.getIntegers().read(3)).intValue();
/*     */   }
/*     */   
/*     */   public void setColumns(int value) {
/*  95 */     this.handle.getIntegers().write(3, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public int getRows() {
/*  99 */     return ((Integer)this.handle.getIntegers().read(4)).intValue();
/*     */   }
/*     */   
/*     */   public void setRows(int value) {
/* 103 */     this.handle.getIntegers().write(4, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public int getX() {
/* 107 */     return ((Integer)this.handle.getIntegers().read(1)).intValue();
/*     */   }
/*     */   
/*     */   public void setX(int value) {
/* 111 */     this.handle.getIntegers().write(1, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public int getZ() {
/* 115 */     return ((Integer)this.handle.getIntegers().read(2)).intValue();
/*     */   }
/*     */   
/*     */   public void setZ(int value) {
/* 119 */     this.handle.getIntegers().write(2, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public byte[] getData() {
/* 123 */     return (byte[])this.handle.getByteArrays().read(0);
/*     */   }
/*     */   
/*     */   public void setData(byte[] value) {
/* 127 */     this.handle.getByteArrays().write(0, value);
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */