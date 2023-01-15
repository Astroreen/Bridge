/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.comphenix.protocol.utility.MinecraftReflection;
/*    */ import com.comphenix.protocol.wrappers.MinecraftKey;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
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
/*    */ public class WrapperPlayClientCustomPayload
/*    */   extends AbstractPacket
/*    */ {
/* 30 */   public static final PacketType TYPE = PacketType.Play.Client.CUSTOM_PAYLOAD;
/*    */   
/*    */   public WrapperPlayClientCustomPayload() {
/* 33 */     super(new PacketContainer(TYPE), TYPE);
/* 34 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayClientCustomPayload(PacketContainer packet) {
/* 38 */     super(packet, TYPE);
/*    */   }
/*    */   
/*    */   public MinecraftKey getChannel() {
/* 42 */     return (MinecraftKey)this.handle.getMinecraftKeys().readSafely(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setChannel(MinecraftKey value) {
/* 50 */     this.handle.getMinecraftKeys().writeSafely(0, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ByteBuf getContentsBuffer() {
/* 59 */     return (ByteBuf)this.handle.getModifier().withType(ByteBuf.class).read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getContents() {
/* 68 */     ByteBuf buffer = getContentsBuffer();
/* 69 */     byte[] array = new byte[buffer.readableBytes()];
/* 70 */     buffer.readBytes(array);
/* 71 */     return array;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setContentsBuffer(ByteBuf contents) {
/* 80 */     if (MinecraftReflection.is(MinecraftReflection.getPacketDataSerializerClass(), contents)) {
/* 81 */       this.handle.getModifier().withType(ByteBuf.class).write(0, contents);
/*    */     } else {
/* 83 */       Object serializer = MinecraftReflection.getPacketDataSerializer(contents);
/* 84 */       this.handle.getModifier().withType(ByteBuf.class).write(0, serializer);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setContents(byte[] content) {
/* 94 */     setContentsBuffer(Unpooled.copiedBuffer(content));
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientCustomPayload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */