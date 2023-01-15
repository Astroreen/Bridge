/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.wrappers.EnumWrappers;
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
/*     */ public class WrapperPlayServerScoreboardScore
/*     */   extends AbstractPacket
/*     */ {
/*  26 */   public static final PacketType TYPE = PacketType.Play.Server.SCOREBOARD_SCORE;
/*     */ 
/*     */   
/*     */   public WrapperPlayServerScoreboardScore() {
/*  30 */     super(new PacketContainer(TYPE), TYPE);
/*  31 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerScoreboardScore(PacketContainer packet) {
/*  35 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScoreName() {
/*  46 */     return (String)this.handle.getStrings().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScoreName(String value) {
/*  55 */     this.handle.getStrings().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjectiveName() {
/*  66 */     return (String)this.handle.getStrings().read(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjectiveName(String value) {
/*  75 */     this.handle.getStrings().write(1, value);
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
/*     */   public int getValue() {
/*  87 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(int value) {
/*  96 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public EnumWrappers.ScoreboardAction getAction() {
/* 100 */     return (EnumWrappers.ScoreboardAction)this.handle.getScoreboardActions().read(0);
/*     */   }
/*     */   
/*     */   public void setScoreboardAction(EnumWrappers.ScoreboardAction value) {
/* 104 */     this.handle.getScoreboardActions().write(0, value);
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerScoreboardScore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */