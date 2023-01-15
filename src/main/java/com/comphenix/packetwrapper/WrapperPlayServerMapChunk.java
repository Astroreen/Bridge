/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.wrappers.nbt.NbtBase;
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
/*     */ 
/*     */ public class WrapperPlayServerMapChunk
/*     */   extends AbstractPacket
/*     */ {
/*  29 */   public static final PacketType TYPE = PacketType.Play.Server.MAP_CHUNK;
/*     */   
/*     */   public WrapperPlayServerMapChunk() {
/*  32 */     super(new PacketContainer(TYPE), TYPE);
/*  33 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerMapChunk(PacketContainer packet) {
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
/*     */   public int getChunkX() {
/*  48 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChunkX(int value) {
/*  57 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getChunkZ() {
/*  68 */     return ((Integer)this.handle.getIntegers().read(1)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChunkZ(int value) {
/*  77 */     this.handle.getIntegers().write(1, Integer.valueOf(value));
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
/*     */   public boolean getGroundUpContinuous() {
/*  90 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGroundUpContinuous(boolean value) {
/*  99 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBitmask() {
/* 109 */     return ((Integer)this.handle.getIntegers().read(2)).intValue();
/*     */   }
/*     */   
/*     */   public void setBitmask(int value) {
/* 113 */     this.handle.getIntegers().write(2, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getData() {
/* 121 */     return (byte[])this.handle.getByteArrays().read(0);
/*     */   }
/*     */   
/*     */   public void setData(byte[] value) {
/* 125 */     this.handle.getByteArrays().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<NbtBase<?>> getTileEntities() {
/* 132 */     return (List<NbtBase<?>>)this.handle.getListNbtModifier().read(0);
/*     */   }
/*     */   
/*     */   public void setTileEntities(List<NbtBase<?>> value) {
/* 136 */     this.handle.getListNbtModifier().write(0, value);
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerMapChunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */