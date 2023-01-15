/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.wrappers.EnumWrappers;
/*     */ import org.bukkit.WorldType;
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
/*     */ public class WrapperPlayServerRespawn
/*     */   extends AbstractPacket
/*     */ {
/*  29 */   public static final PacketType TYPE = PacketType.Play.Server.RESPAWN;
/*     */   
/*     */   public WrapperPlayServerRespawn() {
/*  32 */     super(new PacketContainer(TYPE), TYPE);
/*  33 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerRespawn(PacketContainer packet) {
/*  37 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDimension() {
/*  48 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDimension(int value) {
/*  57 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumWrappers.Difficulty getDifficulty() {
/*  68 */     return (EnumWrappers.Difficulty)this.handle.getDifficulties().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDifficulty(EnumWrappers.Difficulty value) {
/*  77 */     this.handle.getDifficulties().write(0, value);
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
/*     */   public EnumWrappers.NativeGameMode getGamemode() {
/*  89 */     return (EnumWrappers.NativeGameMode)this.handle.getGameModes().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGamemode(EnumWrappers.NativeGameMode value) {
/*  98 */     this.handle.getGameModes().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WorldType getLevelType() {
/* 109 */     return (WorldType)this.handle.getWorldTypeModifier().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevelType(WorldType value) {
/* 118 */     this.handle.getWorldTypeModifier().write(0, value);
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerRespawn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */