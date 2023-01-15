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
/*    */ 
/*    */ public class WrapperPlayClientEntityNbtQuery
/*    */   extends AbstractPacket
/*    */ {
/* 30 */   public static final PacketType TYPE = PacketType.Play.Client.ENTITY_NBT_QUERY;
/*    */   
/*    */   public WrapperPlayClientEntityNbtQuery() {
/* 33 */     super(new PacketContainer(TYPE), TYPE);
/* 34 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayClientEntityNbtQuery(PacketContainer packet) {
/* 38 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTransactionId() {
/* 48 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTransactionId(int value) {
/* 56 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getEntityID() {
/* 66 */     return ((Integer)this.handle.getIntegers().read(1)).intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Entity getEntity(World world) {
/* 75 */     return (Entity)this.handle.getEntityModifier(world).read(1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Entity getEntity(PacketEvent event) {
/* 84 */     return getEntity(event.getPlayer().getWorld());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setEntityID(int value) {
/* 92 */     this.handle.getIntegers().write(1, Integer.valueOf(value));
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientEntityNbtQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */