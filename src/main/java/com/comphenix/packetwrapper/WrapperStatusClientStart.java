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
/*    */ public class WrapperStatusClientStart
/*    */   extends AbstractPacket
/*    */ {
/* 25 */   public static final PacketType TYPE = PacketType.Status.Client.START;
/*    */   
/*    */   public WrapperStatusClientStart() {
/* 28 */     super(new PacketContainer(TYPE), TYPE);
/* 29 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperStatusClientStart(PacketContainer packet) {
/* 33 */     super(packet, TYPE);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperStatusClientStart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */