/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.mojang.brigadier.tree.RootCommandNode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WrapperPlayServerCommands
/*    */   extends AbstractPacket
/*    */ {
/* 27 */   public static final PacketType TYPE = PacketType.Play.Server.COMMANDS;
/*    */   
/*    */   public WrapperPlayServerCommands() {
/* 30 */     super(new PacketContainer(TYPE), TYPE);
/* 31 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerCommands(PacketContainer packet) {
/* 35 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RootCommandNode getRoot() {
/* 43 */     return (RootCommandNode)this.handle.getModifier().read(0);
/*    */   }
/*    */   
/*    */   public void setRoot(RootCommandNode node) {
/* 47 */     this.handle.getModifier().write(0, node);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */