/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.comphenix.protocol.wrappers.BlockPosition;
/*    */ import com.comphenix.protocol.wrappers.WrappedBlockData;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
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
/*    */ public class WrapperPlayServerBlockChange
/*    */   extends AbstractPacket
/*    */ {
/* 30 */   public static final PacketType TYPE = PacketType.Play.Server.BLOCK_CHANGE;
/*    */   
/*    */   public WrapperPlayServerBlockChange() {
/* 33 */     super(new PacketContainer(TYPE), TYPE);
/* 34 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerBlockChange(PacketContainer packet) {
/* 38 */     super(packet, TYPE);
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
/* 49 */     return (BlockPosition)this.handle.getBlockPositionModifier().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLocation(BlockPosition value) {
/* 58 */     this.handle.getBlockPositionModifier().write(0, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Location getBukkitLocation(World world) {
/* 68 */     return getLocation().toVector().toLocation(world);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WrappedBlockData getBlockData() {
/* 77 */     return (WrappedBlockData)this.handle.getBlockData().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBlockData(WrappedBlockData value) {
/* 86 */     this.handle.getBlockData().write(0, value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerBlockChange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */