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
/*    */ public class WrapperPlayServerServerDifficulty
/*    */   extends AbstractPacket
/*    */ {
/* 26 */   public static final PacketType TYPE = PacketType.Play.Server.SERVER_DIFFICULTY;
/*    */ 
/*    */   
/*    */   public WrapperPlayServerServerDifficulty() {
/* 30 */     super(new PacketContainer(TYPE), TYPE);
/* 31 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerServerDifficulty(PacketContainer packet) {
/* 35 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EnumWrappers.Difficulty getDifficulty() {
/* 46 */     return (EnumWrappers.Difficulty)this.handle.getDifficulties().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDifficulty(EnumWrappers.Difficulty value) {
/* 55 */     this.handle.getDifficulties().write(0, value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerServerDifficulty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */