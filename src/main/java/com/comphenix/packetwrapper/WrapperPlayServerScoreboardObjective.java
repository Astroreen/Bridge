/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
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
/*     */ public class WrapperPlayServerScoreboardObjective
/*     */   extends AbstractPacket
/*     */ {
/*  27 */   public static final PacketType TYPE = PacketType.Play.Server.SCOREBOARD_OBJECTIVE;
/*     */ 
/*     */   
/*     */   public WrapperPlayServerScoreboardObjective() {
/*  31 */     super(new PacketContainer(TYPE), TYPE);
/*  32 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerScoreboardObjective(PacketContainer packet) {
/*  36 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Mode
/*     */
/*     */   {
/*     */     public static final int ADD_OBJECTIVE = 0;
/*     */     
/*     */     public static final int REMOVE_OBJECTIVE = 1;
/*     */     
/*     */     public static final int UPDATE_VALUE = 2;
/*     */     
/*  49 */     private static final Mode INSTANCE = new Mode();
/*     */     
/*     */     public static Mode getInstance() {
/*  52 */       return INSTANCE;
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
/*  64 */     return (String)this.handle.getStrings().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String value) {
/*  73 */     this.handle.getStrings().write(0, value);
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
/*  84 */     return (WrappedChatComponent)this.handle.getChatComponents().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisplayName(WrappedChatComponent value) {
/*  93 */     this.handle.getChatComponents().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HealthDisplay getHealthDisplay() {
/* 104 */     return (HealthDisplay)this.handle.getEnumModifier(HealthDisplay.class, 2).read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHealthDisplay(HealthDisplay value) {
/* 114 */     this.handle.getEnumModifier(HealthDisplay.class, 2).write(0, value);
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
/*     */   public int getMode() {
/* 126 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMode(int value) {
/* 135 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public enum HealthDisplay {
/* 139 */     INTEGER, HEARTS;
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerScoreboardObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */