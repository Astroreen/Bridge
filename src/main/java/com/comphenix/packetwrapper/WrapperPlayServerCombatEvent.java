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
/*     */ public class WrapperPlayServerCombatEvent
/*     */   extends AbstractPacket
/*     */ {
/*  26 */   public static final PacketType TYPE = PacketType.Play.Server.COMBAT_EVENT;
/*     */   
/*     */   public WrapperPlayServerCombatEvent() {
/*  29 */     super(new PacketContainer(TYPE), TYPE);
/*  30 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerCombatEvent(PacketContainer packet) {
/*  34 */     super(packet, TYPE);
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
/*     */   public EnumWrappers.CombatEventType getEvent() {
/*  47 */     return (EnumWrappers.CombatEventType)this.handle.getCombatEvents().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEvent(EnumWrappers.CombatEventType value) {
/*  56 */     this.handle.getCombatEvents().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDuration() {
/*  62 */     if (getEvent() != EnumWrappers.CombatEventType.END_COMBAT) {
/*  63 */       throw new IllegalStateException("Duration only exists for END_COMBAT");
/*     */     }
/*     */     
/*  66 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */   
/*     */   public void setDuration(int value) {
/*  70 */     if (getEvent() != EnumWrappers.CombatEventType.END_COMBAT) {
/*  71 */       throw new IllegalStateException("Duration only exists for END_COMBAT");
/*     */     }
/*     */     
/*  74 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPlayerID() {
/*  80 */     if (getEvent() != EnumWrappers.CombatEventType.ENTITY_DIED) {
/*  81 */       throw new IllegalStateException("Player ID only exists for ENTITY_DEAD");
/*     */     }
/*     */     
/*  84 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */   
/*     */   public void setPlayerId(int value) {
/*  88 */     if (getEvent() != EnumWrappers.CombatEventType.ENTITY_DIED) {
/*  89 */       throw new IllegalStateException("Player ID only exists for ENTITY_DEAD");
/*     */     }
/*     */     
/*  92 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public int getEntityID() {
/*  96 */     EnumWrappers.CombatEventType event = getEvent();
/*  97 */     switch (event) {
/*     */       case END_COMBAT:
/*     */       case ENTITY_DIED:
/* 100 */         return ((Integer)this.handle.getIntegers().read(1)).intValue();
/*     */     } 
/* 102 */     throw new IllegalStateException("Entity ID does not exist for " + event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityId(int value) {
/* 109 */     EnumWrappers.CombatEventType event = getEvent();
/* 110 */     switch (event) {
/*     */       case END_COMBAT:
/*     */       case ENTITY_DIED:
/* 113 */         this.handle.getIntegers().write(1, Integer.valueOf(value)); break;
/*     */     } 
/* 115 */     throw new IllegalStateException("Entity ID does not exist for " + event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 122 */     if (getEvent() != EnumWrappers.CombatEventType.ENTITY_DIED) {
/* 123 */       throw new IllegalStateException("Message only exists for ENTITY_DEAD");
/*     */     }
/*     */     
/* 126 */     return (String)this.handle.getStrings().read(0);
/*     */   }
/*     */   
/*     */   public void setMessage(String value) {
/* 130 */     if (getEvent() != EnumWrappers.CombatEventType.ENTITY_DIED) {
/* 131 */       throw new IllegalStateException("Message only exists for ENTITY_DEAD");
/*     */     }
/*     */     
/* 134 */     this.handle.getStrings().write(0, value);
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerCombatEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */