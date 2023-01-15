/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.wrappers.BlockPosition;
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
/*     */ 
/*     */ 
/*     */ public class WrapperPlayClientUseItem
/*     */   extends AbstractPacket
/*     */ {
/*  29 */   public static final PacketType TYPE = PacketType.Play.Client.USE_ITEM;
/*     */   
/*     */   public WrapperPlayClientUseItem() {
/*  32 */     super(new PacketContainer(TYPE), TYPE);
/*  33 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayClientUseItem(PacketContainer packet) {
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
/*     */   public BlockPosition getLocation() {
/*  48 */     return (BlockPosition)this.handle.getBlockPositionModifier().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocation(BlockPosition value) {
/*  57 */     this.handle.getBlockPositionModifier().write(0, value);
/*     */   }
/*     */   
/*     */   public EnumWrappers.Direction getFace() {
/*  61 */     return (EnumWrappers.Direction)this.handle.getDirections().read(0);
/*     */   }
/*     */   
/*     */   public void setFace(EnumWrappers.Direction value) {
/*  65 */     this.handle.getDirections().write(0, value);
/*     */   }
/*     */   
/*     */   public EnumWrappers.Hand getHand() {
/*  69 */     return (EnumWrappers.Hand)this.handle.getHands().read(0);
/*     */   }
/*     */   
/*     */   public void setHand(EnumWrappers.Hand value) {
/*  73 */     this.handle.getHands().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCursorPositionX() {
/*  85 */     return ((Float)this.handle.getFloat().read(0)).floatValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCursorPositionX(float value) {
/*  94 */     this.handle.getFloat().write(0, Float.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCursorPositionY() {
/* 106 */     return ((Float)this.handle.getFloat().read(1)).floatValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCursorPositionY(float value) {
/* 115 */     this.handle.getFloat().write(1, Float.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCursorPositionZ() {
/* 127 */     return ((Float)this.handle.getFloat().read(2)).floatValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCursorPositionZ(float value) {
/* 136 */     this.handle.getFloat().write(2, Float.valueOf(value));
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientUseItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */