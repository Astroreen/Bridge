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
/*    */ public class WrapperPlayClientBoatMove
/*    */   extends AbstractPacket
/*    */ {
/* 26 */   public static final PacketType TYPE = PacketType.Play.Client.BOAT_MOVE;
/*    */   
/*    */   public WrapperPlayClientBoatMove() {
/* 29 */     super(new PacketContainer(TYPE), TYPE);
/* 30 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayClientBoatMove(PacketContainer packet) {
/* 34 */     super(packet, TYPE);
/*    */   }
/*    */   
/*    */   public boolean getLeftOar() {
/* 38 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*    */   }
/*    */   
/*    */   public void setLeftOar(boolean value) {
/* 42 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*    */   }
/*    */   
/*    */   public boolean getRightOar() {
/* 46 */     return ((Boolean)this.handle.getBooleans().read(1)).booleanValue();
/*    */   }
/*    */   
/*    */   public void setRightOar(boolean value) {
/* 50 */     this.handle.getBooleans().write(1, Boolean.valueOf(value));
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientBoatMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */