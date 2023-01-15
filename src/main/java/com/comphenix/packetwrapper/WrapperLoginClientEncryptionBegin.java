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
/*    */ public class WrapperLoginClientEncryptionBegin
/*    */   extends AbstractPacket
/*    */ {
/* 25 */   public static final PacketType TYPE = PacketType.Login.Client.ENCRYPTION_BEGIN;
/*    */ 
/*    */   
/*    */   public WrapperLoginClientEncryptionBegin() {
/* 29 */     super(new PacketContainer(TYPE), TYPE);
/* 30 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperLoginClientEncryptionBegin(PacketContainer packet) {
/* 34 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getSharedSecret() {
/* 43 */     return (byte[])this.handle.getByteArrays().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSharedSecret(byte[] value) {
/* 52 */     this.handle.getByteArrays().write(0, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getVerifyToken() {
/* 61 */     return (byte[])this.handle.getByteArrays().read(1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setVerifyToken(byte[] value) {
/* 70 */     this.handle.getByteArrays().write(1, value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperLoginClientEncryptionBegin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */