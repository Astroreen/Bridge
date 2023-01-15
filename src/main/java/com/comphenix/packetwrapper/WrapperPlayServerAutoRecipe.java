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
/*    */ public class WrapperPlayServerAutoRecipe
/*    */   extends AbstractPacket
/*    */ {
/* 26 */   public static final PacketType TYPE = PacketType.Play.Server.AUTO_RECIPE;
/*    */   
/*    */   public WrapperPlayServerAutoRecipe() {
/* 29 */     super(new PacketContainer(TYPE), TYPE);
/* 30 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerAutoRecipe(PacketContainer packet) {
/* 34 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getWindowId() {
/* 42 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setWindowId(int value) {
/* 50 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerAutoRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */