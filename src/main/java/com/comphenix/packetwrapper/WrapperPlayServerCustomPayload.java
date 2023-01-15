/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.utility.MinecraftReflection;
/*     */ import com.comphenix.protocol.wrappers.MinecraftKey;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
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
/*     */ public class WrapperPlayServerCustomPayload
/*     */   extends AbstractPacket
/*     */ {
/*  30 */   public static final PacketType TYPE = PacketType.Play.Server.CUSTOM_PAYLOAD;
/*     */   
/*     */   public WrapperPlayServerCustomPayload() {
/*  33 */     super(new PacketContainer(TYPE), TYPE);
/*  34 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerCustomPayload(PacketContainer packet) {
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
/*     */   public MinecraftKey getChannel() {
/*  49 */     return (MinecraftKey)this.handle.getMinecraftKeys().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChannel(MinecraftKey value) {
/*  57 */     this.handle.getMinecraftKeys().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuf getContentsBuffer() {
/*  66 */     return (ByteBuf)this.handle.getModifier().withType(ByteBuf.class).read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getContents() {
/*  75 */     ByteBuf buffer = getContentsBuffer();
/*  76 */     byte[] array = new byte[buffer.readableBytes()];
/*  77 */     buffer.readBytes(array);
/*  78 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentsBuffer(ByteBuf contents) {
/*  87 */     if (MinecraftReflection.is(MinecraftReflection.getPacketDataSerializerClass(), contents)) {
/*  88 */       this.handle.getModifier().withType(ByteBuf.class).write(0, contents);
/*     */     } else {
/*  90 */       Object serializer = MinecraftReflection.getPacketDataSerializer(contents);
/*  91 */       this.handle.getModifier().withType(ByteBuf.class).write(0, serializer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContents(byte[] content) {
/* 101 */     setContentsBuffer(Unpooled.copiedBuffer(content));
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerCustomPayload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */