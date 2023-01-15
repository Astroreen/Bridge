/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.comphenix.protocol.wrappers.BlockPosition;
/*    */ import com.comphenix.protocol.wrappers.EnumWrappers;
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
/*    */ public class WrapperPlayClientBlockDig
/*    */   extends AbstractPacket
/*    */ {
/* 28 */   public static final PacketType TYPE = PacketType.Play.Client.BLOCK_DIG;
/*    */   
/*    */   public WrapperPlayClientBlockDig() {
/* 31 */     super(new PacketContainer(TYPE), TYPE);
/* 32 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayClientBlockDig(PacketContainer packet) {
/* 36 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockPosition getLocation() {
/* 47 */     return (BlockPosition)this.handle.getBlockPositionModifier().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLocation(BlockPosition value) {
/* 56 */     this.handle.getBlockPositionModifier().write(0, value);
/*    */   }
/*    */   
/*    */   public EnumWrappers.Direction getDirection() {
/* 60 */     return (EnumWrappers.Direction)this.handle.getDirections().read(0);
/*    */   }
/*    */   
/*    */   public void setDirection(EnumWrappers.Direction value) {
/* 64 */     this.handle.getDirections().write(0, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EnumWrappers.PlayerDigType getStatus() {
/* 75 */     return (EnumWrappers.PlayerDigType)this.handle.getPlayerDigTypes().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setStatus(EnumWrappers.PlayerDigType value) {
/* 84 */     this.handle.getPlayerDigTypes().write(0, value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientBlockDig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */