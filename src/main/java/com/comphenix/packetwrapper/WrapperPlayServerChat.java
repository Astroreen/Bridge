/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.wrappers.EnumWrappers;
/*     */ import com.comphenix.protocol.wrappers.WrappedChatComponent;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ public class WrapperPlayServerChat
/*     */   extends AbstractPacket
/*     */ {
/*  30 */   public static final PacketType TYPE = PacketType.Play.Server.CHAT;
/*     */   
/*     */   public WrapperPlayServerChat() {
/*  33 */     super(new PacketContainer(TYPE), TYPE);
/*  34 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerChat(PacketContainer packet) {
/*  38 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WrappedChatComponent getMessage() {
/*  49 */     return (WrappedChatComponent)this.handle.getChatComponents().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessage(WrappedChatComponent value) {
/*  58 */     this.handle.getChatComponents().write(0, value);
/*     */   }
/*     */   
/*     */   public EnumWrappers.ChatType getChatType() {
/*  62 */     return (EnumWrappers.ChatType)this.handle.getChatTypes().read(0);
/*     */   }
/*     */   
/*     */   public void setChatType(EnumWrappers.ChatType type) {
/*  66 */     this.handle.getChatTypes().write(0, type);
/*     */   }
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
/*     */   @Deprecated
/*     */   public byte getPosition() {
/*  80 */     Byte position = (Byte)this.handle.getBytes().readSafely(0);
/*  81 */     if (position != null) {
/*  82 */       return position.byteValue();
/*     */     }
/*  84 */     return getChatType().getId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setPosition(byte value) {
/*  96 */     this.handle.getBytes().writeSafely(0, Byte.valueOf(value));
/*     */     
/*  98 */     if (EnumWrappers.getChatTypeClass() != null)
/*     */     {
/* 100 */       Arrays.<EnumWrappers.ChatType>stream(EnumWrappers.ChatType.values()).filter(t -> (t.getId() == value)).findAny()
/* 101 */         .ifPresent(t -> this.handle.getChatTypes().writeSafely(0, t));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerChat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */