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
/*    */ public class WrapperPlayClientAutoRecipe
/*    */   extends AbstractPacket
/*    */ {
/* 26 */   public static final PacketType TYPE = PacketType.Play.Client.AUTO_RECIPE;
/*    */   
/*    */   public WrapperPlayClientAutoRecipe() {
/* 29 */     super(new PacketContainer(TYPE), TYPE);
/* 30 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayClientAutoRecipe(PacketContainer packet) {
/* 34 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getWindowId() {
/* 44 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setWindowId(int value) {
/* 52 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isMakeAll() {
/* 58 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*    */   }
/*    */   
/*    */   public void setMakeAll(boolean value) {
/* 62 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientAutoRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */