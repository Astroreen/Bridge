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
/*    */ public class WrapperPlayClientSteerVehicle
/*    */   extends AbstractPacket
/*    */ {
/* 25 */   public static final PacketType TYPE = PacketType.Play.Client.STEER_VEHICLE;
/*    */   
/*    */   public WrapperPlayClientSteerVehicle() {
/* 28 */     super(new PacketContainer(TYPE), TYPE);
/* 29 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayClientSteerVehicle(PacketContainer packet) {
/* 33 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float getSideways() {
/* 44 */     return ((Float)this.handle.getFloat().read(0)).floatValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSideways(float value) {
/* 53 */     this.handle.getFloat().write(0, Float.valueOf(value));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float getForward() {
/* 64 */     return ((Float)this.handle.getFloat().read(1)).floatValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setForward(float value) {
/* 73 */     this.handle.getFloat().write(1, Float.valueOf(value));
/*    */   }
/*    */   
/*    */   public boolean isJump() {
/* 77 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*    */   }
/*    */   
/*    */   public void setJump(boolean value) {
/* 81 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*    */   }
/*    */   
/*    */   public boolean isUnmount() {
/* 85 */     return ((Boolean)this.handle.getBooleans().read(1)).booleanValue();
/*    */   }
/*    */   
/*    */   public void setUnmount(boolean value) {
/* 89 */     this.handle.getBooleans().write(1, Boolean.valueOf(value));
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientSteerVehicle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */