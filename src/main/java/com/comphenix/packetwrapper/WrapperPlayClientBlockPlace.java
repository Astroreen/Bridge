/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.comphenix.protocol.wrappers.EnumWrappers;
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
/*    */ public class WrapperPlayClientBlockPlace
/*    */   extends AbstractPacket
/*    */ {
/* 26 */   public static final PacketType TYPE = PacketType.Play.Client.BLOCK_PLACE;
/*    */   
/*    */   public WrapperPlayClientBlockPlace() {
/* 29 */     super(new PacketContainer(TYPE), TYPE);
/* 30 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayClientBlockPlace(PacketContainer packet) {
/* 34 */     super(packet, TYPE);
/*    */   }
/*    */   
/*    */   public EnumWrappers.Hand getHand() {
/* 38 */     return (EnumWrappers.Hand)this.handle.getHands().read(0);
/*    */   }
/*    */   
/*    */   public void setHand(EnumWrappers.Hand value) {
/* 42 */     this.handle.getHands().write(0, value);
/*    */   }
/*    */   
/*    */   public long getTimestamp() {
/* 46 */     return ((Long)this.handle.getLongs().read(0)).longValue();
/*    */   }
/*    */   
/*    */   public void setTimestamp(long value) {
/* 50 */     this.handle.getLongs().write(0, Long.valueOf(value));
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientBlockPlace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */