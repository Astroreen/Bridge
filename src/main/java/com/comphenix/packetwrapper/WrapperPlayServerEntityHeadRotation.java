/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.comphenix.protocol.events.PacketEvent;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Entity;
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
/*    */ public class WrapperPlayServerEntityHeadRotation
/*    */   extends AbstractPacket
/*    */ {
/* 29 */   public static final PacketType TYPE = PacketType.Play.Server.ENTITY_HEAD_ROTATION;
/*    */ 
/*    */   
/*    */   public WrapperPlayServerEntityHeadRotation() {
/* 33 */     super(new PacketContainer(TYPE), TYPE);
/* 34 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerEntityHeadRotation(PacketContainer packet) {
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
/*    */   public int getEntityID() {
/* 49 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setEntityID(int value) {
/* 58 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Entity getEntity(World world) {
/* 68 */     return (Entity)this.handle.getEntityModifier(world).read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Entity getEntity(PacketEvent event) {
/* 78 */     return getEntity(event.getPlayer().getWorld());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getHeadYaw() {
/* 89 */     return ((Byte)this.handle.getBytes().read(0)).byteValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setHeadYaw(byte value) {
/* 98 */     this.handle.getBytes().write(0, Byte.valueOf(value));
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerEntityHeadRotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */