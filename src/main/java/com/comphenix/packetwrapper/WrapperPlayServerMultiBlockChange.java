/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
/*    */ import com.comphenix.protocol.wrappers.MultiBlockChangeInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WrapperPlayServerMultiBlockChange
/*    */   extends AbstractPacket
/*    */ {
/* 27 */   public static final PacketType TYPE = PacketType.Play.Server.MULTI_BLOCK_CHANGE;
/*    */ 
/*    */   
/*    */   public WrapperPlayServerMultiBlockChange() {
/* 31 */     super(new PacketContainer(TYPE), TYPE);
/* 32 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerMultiBlockChange(PacketContainer packet) {
/* 36 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ChunkCoordIntPair getChunk() {
/* 45 */     return (ChunkCoordIntPair)this.handle.getChunkCoordIntPairs().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setChunk(ChunkCoordIntPair value) {
/* 54 */     this.handle.getChunkCoordIntPairs().write(0, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MultiBlockChangeInfo[] getRecords() {
/* 63 */     return (MultiBlockChangeInfo[])this.handle.getMultiBlockChangeInfoArrays().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRecords(MultiBlockChangeInfo[] value) {
/* 72 */     this.handle.getMultiBlockChangeInfoArrays().write(0, value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerMultiBlockChange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */