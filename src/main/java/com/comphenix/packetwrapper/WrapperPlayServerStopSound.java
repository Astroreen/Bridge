/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.comphenix.protocol.wrappers.EnumWrappers;
/*    */ import com.comphenix.protocol.wrappers.MinecraftKey;
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
/*    */ public class WrapperPlayServerStopSound
/*    */   extends AbstractPacket
/*    */ {
/* 28 */   public static final PacketType TYPE = PacketType.Play.Server.STOP_SOUND;
/*    */   
/*    */   public WrapperPlayServerStopSound() {
/* 31 */     super(new PacketContainer(TYPE), TYPE);
/* 32 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerStopSound(PacketContainer packet) {
/* 36 */     super(packet, TYPE);
/*    */   }
/*    */   
/*    */   public MinecraftKey getSoundEffect() {
/* 40 */     return (MinecraftKey)this.handle.getMinecraftKeys().readSafely(0);
/*    */   }
/*    */   
/*    */   public void setSoundEffect(MinecraftKey value) {
/* 44 */     this.handle.getMinecraftKeys().writeSafely(0, value);
/*    */   }
/*    */   
/*    */   public EnumWrappers.SoundCategory getCategory() {
/* 48 */     return (EnumWrappers.SoundCategory)this.handle.getSoundCategories().readSafely(0);
/*    */   }
/*    */   
/*    */   public void setCategory(EnumWrappers.SoundCategory value) {
/* 52 */     this.handle.getSoundCategories().writeSafely(0, value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerStopSound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */