/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.comphenix.protocol.wrappers.WrappedServerPing;
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
/*    */ public class WrapperStatusServerServerInfo
/*    */   extends AbstractPacket
/*    */ {
/* 26 */   public static final PacketType TYPE = PacketType.Status.Server.SERVER_INFO;
/*    */   
/*    */   public WrapperStatusServerServerInfo() {
/* 29 */     super(new PacketContainer(TYPE), TYPE);
/* 30 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperStatusServerServerInfo(PacketContainer packet) {
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
/*    */   public WrappedServerPing getJsonResponse() {
/* 45 */     return (WrappedServerPing)this.handle.getServerPings().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setJsonResponse(WrappedServerPing value) {
/* 54 */     this.handle.getServerPings().write(0, value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperStatusServerServerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */