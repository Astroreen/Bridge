/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.events.PacketEvent;
/*     */ import com.comphenix.protocol.wrappers.EnumWrappers;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.WorldType;
/*     */ import org.bukkit.entity.Entity;
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
/*     */ public class WrapperPlayServerLogin
/*     */   extends AbstractPacket
/*     */ {
/*  32 */   public static final PacketType TYPE = PacketType.Play.Server.LOGIN;
/*     */   
/*     */   public WrapperPlayServerLogin() {
/*  35 */     super(new PacketContainer(TYPE), TYPE);
/*  36 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerLogin(PacketContainer packet) {
/*  40 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEntityID() {
/*  51 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityID(int value) {
/*  60 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(World world) {
/*  70 */     return (Entity)this.handle.getEntityModifier(world).read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(PacketEvent event) {
/*  80 */     return getEntity(event.getPlayer().getWorld());
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
/*  92 */     return (EnumWrappers.NativeGameMode)this.handle.getGameModes().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGamemode(EnumWrappers.NativeGameMode value) {
/* 101 */     this.handle.getGameModes().write(0, value);
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
/* 112 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDimension(int value) {
/* 121 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
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
/* 132 */     return (EnumWrappers.Difficulty)this.handle.getDifficulties().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDifficulty(EnumWrappers.Difficulty value) {
/* 141 */     this.handle.getDifficulties().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxPlayers() {
/* 152 */     return ((Integer)this.handle.getIntegers().read(1)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxPlayers(int value) {
/* 161 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
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
/* 172 */     return (WorldType)this.handle.getWorldTypeModifier().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevelType(WorldType value) {
/* 181 */     this.handle.getWorldTypeModifier().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getReducedDebugInfo() {
/* 190 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReducedDebugInfo(boolean value) {
/* 199 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerLogin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */