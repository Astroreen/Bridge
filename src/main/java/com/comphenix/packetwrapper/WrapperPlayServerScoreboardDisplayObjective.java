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
/*    */ public class WrapperPlayServerScoreboardDisplayObjective
/*    */   extends AbstractPacket
/*    */ {
/* 25 */   public static final PacketType TYPE = PacketType.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE;
/*    */ 
/*    */   
/*    */   public WrapperPlayServerScoreboardDisplayObjective() {
/* 29 */     super(new PacketContainer(TYPE), TYPE);
/* 30 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerScoreboardDisplayObjective(PacketContainer packet) {
/* 34 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getPosition() {
/* 46 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPosition(int value) {
/* 55 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getScoreName() {
/* 66 */     return (String)this.handle.getStrings().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setScoreName(String value) {
/* 75 */     this.handle.getStrings().write(0, value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerScoreboardDisplayObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */