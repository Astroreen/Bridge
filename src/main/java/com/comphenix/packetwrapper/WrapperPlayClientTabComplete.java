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
/*    */ 
/*    */ 
/*    */ public class WrapperPlayClientTabComplete
/*    */   extends AbstractPacket
/*    */ {
/* 27 */   public static final PacketType TYPE = PacketType.Play.Client.TAB_COMPLETE;
/*    */   
/*    */   public WrapperPlayClientTabComplete() {
/* 30 */     super(new PacketContainer(TYPE), TYPE);
/* 31 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayClientTabComplete(PacketContainer packet) {
/* 35 */     super(packet, TYPE);
/*    */   }
/*    */   
/*    */   public int getTransactionId() {
/* 39 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*    */   }
/*    */   
/*    */   public void setTransactionId(int value) {
/* 43 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*    */   }
/*    */   
/*    */   public String getInput() {
/* 47 */     return (String)this.handle.getStrings().read(0);
/*    */   }
/*    */   
/*    */   public void setInput(String value) {
/* 51 */     this.handle.getStrings().write(0, value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientTabComplete.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */