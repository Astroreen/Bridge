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
/*    */ public class WrapperPlayServerKeepAlive
/*    */   extends AbstractPacket
/*    */ {
/* 25 */   public static final PacketType TYPE = PacketType.Play.Server.KEEP_ALIVE;
/*    */   
/*    */   public WrapperPlayServerKeepAlive() {
/* 28 */     super(new PacketContainer(TYPE), TYPE);
/* 29 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerKeepAlive(PacketContainer packet) {
/* 33 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getKeepAliveId() {
/* 42 */     return ((Long)this.handle.getLongs().read(0)).longValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setKeepAliveId(long value) {
/* 51 */     this.handle.getLongs().write(0, Long.valueOf(value));
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerKeepAlive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */