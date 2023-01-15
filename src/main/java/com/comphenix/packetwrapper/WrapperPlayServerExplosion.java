/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.wrappers.BlockPosition;
/*     */ import java.util.List;
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
/*     */ public class WrapperPlayServerExplosion
/*     */   extends AbstractPacket
/*     */ {
/*  28 */   public static final PacketType TYPE = PacketType.Play.Server.EXPLOSION;
/*     */   
/*     */   public WrapperPlayServerExplosion() {
/*  31 */     super(new PacketContainer(TYPE), TYPE);
/*  32 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerExplosion(PacketContainer packet) {
/*  36 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getX() {
/*  45 */     return ((Double)this.handle.getDoubles().read(0)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setX(double value) {
/*  54 */     this.handle.getDoubles().write(0, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getY() {
/*  63 */     return ((Double)this.handle.getDoubles().read(1)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setY(double value) {
/*  72 */     this.handle.getDoubles().write(1, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getZ() {
/*  81 */     return ((Double)this.handle.getDoubles().read(2)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZ(double value) {
/*  90 */     this.handle.getDoubles().write(2, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getRadius() {
/* 101 */     return ((Float)this.handle.getFloat().read(0)).floatValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRadius(float value) {
/* 110 */     this.handle.getFloat().write(0, Float.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BlockPosition> getRecords() {
/* 121 */     return (List<BlockPosition>)this.handle.getBlockPositionCollectionModifier().read(0);
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
/*     */   
/*     */   @Deprecated
/*     */   public List<BlockPosition> getRecors() {
/* 135 */     return (List<BlockPosition>)this.handle.getBlockPositionCollectionModifier().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecords(List<BlockPosition> value) {
/* 144 */     this.handle.getBlockPositionCollectionModifier().write(0, value);
/*     */   }
/*     */   
/*     */   public float getPlayerVelocityX() {
/* 148 */     return ((Float)this.handle.getFloat().read(0)).floatValue();
/*     */   }
/*     */   
/*     */   public void setPlayerVelocityX(float value) {
/* 152 */     this.handle.getFloat().write(0, Float.valueOf(value));
/*     */   }
/*     */   
/*     */   public float getPlayerVelocityY() {
/* 156 */     return ((Float)this.handle.getFloat().read(1)).floatValue();
/*     */   }
/*     */   
/*     */   public void setPlayerVelocityY(float value) {
/* 160 */     this.handle.getFloat().write(1, Float.valueOf(value));
/*     */   }
/*     */   
/*     */   public float getPlayerVelocityZ() {
/* 164 */     return ((Float)this.handle.getFloat().read(2)).floatValue();
/*     */   }
/*     */   
/*     */   public void setPlayerVelocityZ(float value) {
/* 168 */     this.handle.getFloat().write(2, Float.valueOf(value));
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerExplosion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */