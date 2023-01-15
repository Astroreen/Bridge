/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.wrappers.WrappedChatComponent;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.boss.BarColor;
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
/*     */ 
/*     */ public class WrapperPlayServerBoss
/*     */   extends AbstractPacket
/*     */ {
/*  31 */   public static final PacketType TYPE = PacketType.Play.Server.BOSS;
/*     */   
/*     */   public WrapperPlayServerBoss() {
/*  34 */     super(new PacketContainer(TYPE), TYPE);
/*  35 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerBoss(PacketContainer packet) {
/*  39 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UUID getUniqueId() {
/*  50 */     return (UUID)this.handle.getUUIDs().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniqueId(UUID value) {
/*  59 */     this.handle.getUUIDs().write(0, value);
/*     */   }
/*     */   
/*     */   public Action getAction() {
/*  63 */     return (Action)this.handle.getEnumModifier(Action.class, 1).read(0);
/*     */   }
/*     */   
/*     */   public void setAction(Action value) {
/*  67 */     this.handle.getEnumModifier(Action.class, 1).write(0, value);
/*     */   }
/*     */   
/*     */   public WrappedChatComponent getTitle() {
/*  71 */     return (WrappedChatComponent)this.handle.getChatComponents().read(0);
/*     */   }
/*     */   
/*     */   public void setTitle(WrappedChatComponent value) {
/*  75 */     this.handle.getChatComponents().write(0, value);
/*     */   }
/*     */   
/*     */   public float getHealth() {
/*  79 */     return ((Float)this.handle.getFloat().read(0)).floatValue();
/*     */   }
/*     */   
/*     */   public void setHealth(float value) {
/*  83 */     this.handle.getFloat().write(0, Float.valueOf(value));
/*     */   }
/*     */   
/*     */   public BarColor getColor() {
/*  87 */     return (BarColor)this.handle.getEnumModifier(BarColor.class, 4).read(0);
/*     */   }
/*     */   
/*     */   public void setColor(BarColor value) {
/*  91 */     this.handle.getEnumModifier(BarColor.class, 4).write(0, value);
/*     */   }
/*     */   
/*     */   public BarStyle getStyle() {
/*  95 */     return (BarStyle)this.handle.getEnumModifier(BarStyle.class, 5).read(0);
/*     */   }
/*     */   
/*     */   public void setStyle(BarStyle value) {
/*  99 */     this.handle.getEnumModifier(BarStyle.class, 5).write(0, value);
/*     */   }
/*     */   
/*     */   public boolean isDarkenSky() {
/* 103 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setDarkenSky(boolean value) {
/* 107 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean isPlayMusic() {
/* 111 */     return ((Boolean)this.handle.getBooleans().read(1)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setPlayMusic(boolean value) {
/* 115 */     this.handle.getBooleans().write(1, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean isCreateFog() {
/* 119 */     return ((Boolean)this.handle.getBooleans().read(2)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setCreateFog(boolean value) {
/* 123 */     this.handle.getBooleans().write(2, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   public enum Action {
/* 127 */     ADD, REMOVE, UPDATE_PCT, UPDATE_NAME, UPDATE_STYLE, UPDATE_PROPERTIES;
/*     */   }
/*     */   
/*     */   public enum BarStyle {
/* 131 */     PROGRESS, NOTCHED_6, NOTCHED_10, NOTCHED_12, NOTCHED_20;
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerBoss.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */