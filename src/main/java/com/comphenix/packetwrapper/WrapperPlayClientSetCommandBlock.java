/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.utility.MinecraftReflection;
/*     */ import com.comphenix.protocol.wrappers.BlockPosition;
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
/*     */ public class WrapperPlayClientSetCommandBlock
/*     */   extends AbstractPacket
/*     */ {
/*  28 */   public static final PacketType TYPE = PacketType.Play.Client.SET_COMMAND_BLOCK;
/*     */   
/*     */   public WrapperPlayClientSetCommandBlock() {
/*  31 */     super(new PacketContainer(TYPE), TYPE);
/*  32 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayClientSetCommandBlock(PacketContainer packet) {
/*  36 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPosition getLocation() {
/*  44 */     return (BlockPosition)this.handle.getBlockPositionModifier().readSafely(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocation(BlockPosition value) {
/*  52 */     this.handle.getBlockPositionModifier().writeSafely(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommand() {
/*  60 */     return (String)this.handle.getStrings().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommand(String value) {
/*  68 */     this.handle.getStrings().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTrackOutput() {
/*  75 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setTrackOutput(boolean value) {
/*  79 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean isConditional() {
/*  83 */     return ((Boolean)this.handle.getBooleans().read(1)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setConditional(boolean value) {
/*  87 */     this.handle.getBooleans().write(1, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean isAutomatic() {
/*  91 */     return ((Boolean)this.handle.getBooleans().read(2)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setAutomatic(boolean value) {
/*  95 */     this.handle.getBooleans().write(2, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   public Mode getMode() {
/*  99 */     return (Mode)this.handle.getEnumModifier(Mode.class, MinecraftReflection.getMinecraftClass("TileEntityCommand$Type")).readSafely(0);
/*     */   }
/*     */   
/*     */   public void setMode(Mode mode) {
/* 103 */     this.handle.getEnumModifier(Mode.class, MinecraftReflection.getMinecraftClass("TileEntityCommand$Type")).writeSafely(0, mode);
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 107 */     SEQUENCE,
/* 108 */     AUTO,
/* 109 */     REDSTONE;
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientSetCommandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */