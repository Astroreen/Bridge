/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.wrappers.EnumWrappers;
/*     */ import org.bukkit.Sound;
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
/*     */ public class WrapperPlayServerNamedSoundEffect
/*     */   extends AbstractPacket
/*     */ {
/*  28 */   public static final PacketType TYPE = PacketType.Play.Server.NAMED_SOUND_EFFECT;
/*     */ 
/*     */   
/*     */   public WrapperPlayServerNamedSoundEffect() {
/*  32 */     super(new PacketContainer(TYPE), TYPE);
/*  33 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerNamedSoundEffect(PacketContainer packet) {
/*  37 */     super(packet, TYPE);
/*     */   }
/*     */   
/*     */   public Sound getSoundEffect() {
/*  41 */     return (Sound)this.handle.getSoundEffects().read(0);
/*     */   }
/*     */   
/*     */   public void setSoundEffect(Sound value) {
/*  45 */     this.handle.getSoundEffects().write(0, value);
/*     */   }
/*     */   
/*     */   public EnumWrappers.SoundCategory getSoundCategory() {
/*  49 */     return (EnumWrappers.SoundCategory)this.handle.getSoundCategories().read(0);
/*     */   }
/*     */   
/*     */   public void setSoundCategory(EnumWrappers.SoundCategory value) {
/*  53 */     this.handle.getSoundCategories().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEffectPositionX() {
/*  64 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEffectPositionX(int value) {
/*  73 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEffectPositionY() {
/*  84 */     return ((Integer)this.handle.getIntegers().read(1)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEffectPositionY(int value) {
/*  93 */     this.handle.getIntegers().write(1, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEffectPositionZ() {
/* 104 */     return ((Integer)this.handle.getIntegers().read(2)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEffectPositionZ(int value) {
/* 113 */     this.handle.getIntegers().write(2, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getVolume() {
/* 124 */     return ((Float)this.handle.getFloat().read(0)).floatValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVolume(float value) {
/* 133 */     this.handle.getFloat().write(0, Float.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPitch() {
/* 144 */     return ((Float)this.handle.getFloat().read(1)).floatValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPitch(float value) {
/* 153 */     this.handle.getFloat().write(1, Float.valueOf(value));
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerNamedSoundEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */