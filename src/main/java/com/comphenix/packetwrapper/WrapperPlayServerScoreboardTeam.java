/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.List;
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
/*     */ public class WrapperPlayServerScoreboardTeam
/*     */   extends AbstractPacket
/*     */ {
/*  33 */   public static final PacketType TYPE = PacketType.Play.Server.SCOREBOARD_TEAM;
/*     */ 
/*     */   
/*     */   public WrapperPlayServerScoreboardTeam() {
/*  37 */     super(new PacketContainer(TYPE), TYPE);
/*  38 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerScoreboardTeam(PacketContainer packet) {
/*  42 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Mode
/*     */
/*     */   {
/*     */     public static final int TEAM_CREATED = 0;
/*     */     
/*     */     public static final int TEAM_REMOVED = 1;
/*     */     
/*     */     public static final int TEAM_UPDATED = 2;
/*     */     
/*     */     public static final int PLAYERS_ADDED = 3;
/*     */     public static final int PLAYERS_REMOVED = 4;
/*  57 */     private static final Mode INSTANCE = new Mode();
/*     */     
/*     */     public static Mode getInstance() {
/*  60 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  72 */     return (String)this.handle.getStrings().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String value) {
/*  81 */     this.handle.getStrings().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WrappedChatComponent getDisplayName() {
/*  92 */     return (WrappedChatComponent)this.handle.getChatComponents().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisplayName(WrappedChatComponent value) {
/* 101 */     this.handle.getChatComponents().write(0, value);
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
/*     */   public WrappedChatComponent getPrefix() {
/* 113 */     return (WrappedChatComponent)this.handle.getChatComponents().read(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(WrappedChatComponent value) {
/* 122 */     this.handle.getChatComponents().write(1, value);
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
/*     */   public WrappedChatComponent getSuffix() {
/* 134 */     return (WrappedChatComponent)this.handle.getChatComponents().read(2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(WrappedChatComponent value) {
/* 143 */     this.handle.getChatComponents().write(2, value);
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
/*     */   public String getNameTagVisibility() {
/* 155 */     return (String)this.handle.getStrings().read(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNameTagVisibility(String value) {
/* 164 */     this.handle.getStrings().write(1, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChatColor getColor() {
/* 175 */     return (ChatColor)this.handle.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(ChatColor value) {
/* 184 */     this.handle.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCollisionRule() {
/* 193 */     return (String)this.handle.getStrings().read(2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCollisionRule(String value) {
/* 201 */     this.handle.getStrings().write(2, value);
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
/*     */   public List<String> getPlayers() {
/* 214 */     return (List<String>)this.handle.getSpecificModifier(Collection.class)
/* 215 */       .read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlayers(List<String> value) {
/* 224 */     this.handle.getSpecificModifier(Collection.class).write(0, value);
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
/*     */   public int getMode() {
/* 237 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMode(int value) {
/* 246 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPackOptionData() {
/* 267 */     return ((Integer)this.handle.getIntegers().read(1)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPackOptionData(int value) {
/* 277 */     this.handle.getIntegers().write(1, Integer.valueOf(value));
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerScoreboardTeam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */