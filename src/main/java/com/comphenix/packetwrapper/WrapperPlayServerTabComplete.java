/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
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
/*    */ public class WrapperPlayServerTabComplete
/*    */   extends AbstractPacket
/*    */ {
/* 26 */   public static final PacketType TYPE = PacketType.Play.Server.TAB_COMPLETE;
/*    */   
/*    */   public WrapperPlayServerTabComplete() {
/* 29 */     super(new PacketContainer(TYPE), TYPE);
/* 30 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerTabComplete(PacketContainer packet) {
/* 34 */     super(packet, TYPE);
/*    */   }
/*    */   
/*    */   public int getTransactionId() {
/* 38 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*    */   }
/*    */   
/*    */   public void setTransactionId(int value) {
/* 42 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*    */   }
/*    */   
/*    */   public Suggestions getSuggestions() {
/* 46 */     return (Suggestions)this.handle.getSpecificModifier(Suggestions.class).read(0);
/*    */   }
/*    */   
/*    */   public void setSuggestions(Suggestions value) {
/* 50 */     this.handle.getSpecificModifier(Suggestions.class).write(0, value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerTabComplete.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */