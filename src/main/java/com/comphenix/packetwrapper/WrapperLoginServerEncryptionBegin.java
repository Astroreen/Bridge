/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import java.security.PublicKey;
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
/*    */ public class WrapperLoginServerEncryptionBegin
/*    */   extends AbstractPacket
/*    */ {
/* 27 */   public static final PacketType TYPE = PacketType.Login.Server.ENCRYPTION_BEGIN;
/*    */ 
/*    */   
/*    */   public WrapperLoginServerEncryptionBegin() {
/* 31 */     super(new PacketContainer(TYPE), TYPE);
/* 32 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperLoginServerEncryptionBegin(PacketContainer packet) {
/* 36 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getServerId() {
/* 47 */     return (String)this.handle.getStrings().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setServerId(String value) {
/* 56 */     this.handle.getStrings().write(0, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PublicKey getPublicKey() {
/* 65 */     return (PublicKey)this.handle.getSpecificModifier(PublicKey.class).read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPublicKey(PublicKey value) {
/* 74 */     this.handle.getSpecificModifier(PublicKey.class).write(0, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getVerifyToken() {
/* 83 */     return (byte[])this.handle.getByteArrays().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setVerifyToken(byte[] value) {
/* 92 */     this.handle.getByteArrays().write(0, value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperLoginServerEncryptionBegin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */