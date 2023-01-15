/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WrapperHandshakingClientSetProtocol
/*     */   extends AbstractPacket
/*     */ {
/*  26 */   public static final PacketType TYPE = PacketType.Handshake.Client.SET_PROTOCOL;
/*     */ 
/*     */   
/*     */   public WrapperHandshakingClientSetProtocol() {
/*  30 */     super(new PacketContainer(TYPE), TYPE);
/*  31 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperHandshakingClientSetProtocol(PacketContainer packet) {
/*  35 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getProtocolVersion() {
/*  46 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProtocolVersion(int value) {
/*  55 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServerAddressHostnameOrIp() {
/*  66 */     return (String)this.handle.getStrings().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServerAddressHostnameOrIp(String value) {
/*  75 */     this.handle.getStrings().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getServerPort() {
/*  86 */     return ((Integer)this.handle.getIntegers().read(1)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServerPort(int value) {
/*  95 */     this.handle.getIntegers().write(1, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PacketType.Protocol getNextState() {
/* 106 */     return (PacketType.Protocol)this.handle.getProtocols().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNextState(PacketType.Protocol value) {
/* 115 */     this.handle.getProtocols().write(0, value);
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperHandshakingClientSetProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */