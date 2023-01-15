/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
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
/*    */ public class WrapperPlayServerCollect
/*    */   extends AbstractPacket
/*    */ {
/* 25 */   public static final PacketType TYPE = PacketType.Play.Server.COLLECT;
/*    */   
/*    */   public WrapperPlayServerCollect() {
/* 28 */     super(new PacketContainer(TYPE), TYPE);
/* 29 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerCollect(PacketContainer packet) {
/* 33 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCollectedEntityId() {
/* 42 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCollectedEntityId(int value) {
/* 51 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCollectorEntityId() {
/* 60 */     return ((Integer)this.handle.getIntegers().read(1)).intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCollectorEntityId(int value) {
/* 69 */     this.handle.getIntegers().write(1, Integer.valueOf(value));
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerCollect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */