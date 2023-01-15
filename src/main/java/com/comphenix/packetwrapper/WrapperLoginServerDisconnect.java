/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.comphenix.protocol.wrappers.WrappedChatComponent;
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
/*    */ public class WrapperLoginServerDisconnect
/*    */   extends AbstractPacket
/*    */ {
/* 26 */   public static final PacketType TYPE = PacketType.Login.Server.DISCONNECT;
/*    */   
/*    */   public WrapperLoginServerDisconnect() {
/* 29 */     super(new PacketContainer(TYPE), TYPE);
/* 30 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperLoginServerDisconnect(PacketContainer packet) {
/* 34 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WrappedChatComponent getReason() {
/* 43 */     return (WrappedChatComponent)this.handle.getChatComponents().read(0);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public WrappedChatComponent getJsonData() {
/* 48 */     return getReason();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setReason(WrappedChatComponent value) {
/* 57 */     this.handle.getChatComponents().write(0, value);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public void setJsonData(WrappedChatComponent value) {
/* 62 */     setReason(value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperLoginServerDisconnect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */