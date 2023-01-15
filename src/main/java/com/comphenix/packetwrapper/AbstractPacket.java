/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Objects;
import org.bukkit.entity.Player;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPacket
/*     */ {
/*     */   protected PacketContainer handle;
/*     */   
/*     */   protected AbstractPacket(PacketContainer handle, PacketType type) {
/*  42 */     if (handle == null)
/*  43 */       throw new IllegalArgumentException("Packet handle cannot be NULL."); 
/*  44 */     if (!Objects.equal(handle.getType(), type)) {
/*  45 */       throw new IllegalArgumentException(handle.getHandle() + " is not a packet of type " + type);
/*     */     }
/*     */     
/*  48 */     this.handle = handle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PacketContainer getHandle() {
/*  57 */     return this.handle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendPacket(Player receiver) {
/*     */     try {
/*  68 */       ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, 
/*  69 */           getHandle());
/*  70 */     } catch (Exception e) {
/*  71 */       throw new RuntimeException("Cannot send packet.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void broadcastPacket() {
/*  79 */     ProtocolLibrary.getProtocolManager().broadcastServerPacket(getHandle());
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
/*     */   @Deprecated
/*     */   public void recievePacket(Player sender) {
/*     */     try {
/*  93 */       ProtocolLibrary.getProtocolManager().receiveClientPacket(sender,
/*  94 */           getHandle());
/*  95 */     } catch (Exception e) {
/*  96 */       throw new RuntimeException("Cannot recieve packet.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void receivePacket(Player sender) {
/*     */     try {
/* 108 */       ProtocolLibrary.getProtocolManager().receiveClientPacket(sender,
/* 109 */           getHandle());
/* 110 */     } catch (Exception e) {
/* 111 */       throw new RuntimeException("Cannot receive packet.", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\AbstractPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */