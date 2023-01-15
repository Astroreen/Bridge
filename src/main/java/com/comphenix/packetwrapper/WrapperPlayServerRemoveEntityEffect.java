/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.comphenix.protocol.events.PacketEvent;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.potion.PotionEffectType;
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
/*    */ public class WrapperPlayServerRemoveEntityEffect
/*    */   extends AbstractPacket
/*    */ {
/* 30 */   public static final PacketType TYPE = PacketType.Play.Server.REMOVE_ENTITY_EFFECT;
/*    */ 
/*    */   
/*    */   public WrapperPlayServerRemoveEntityEffect() {
/* 34 */     super(new PacketContainer(TYPE), TYPE);
/* 35 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerRemoveEntityEffect(PacketContainer packet) {
/* 39 */     super(packet, TYPE);
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
/* 50 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setEntityID(int value) {
/* 59 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Entity getEntity(World world) {
/* 69 */     return (Entity)this.handle.getEntityModifier(world).read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Entity getEntity(PacketEvent event) {
/* 79 */     return getEntity(event.getPlayer().getWorld());
/*    */   }
/*    */   
/*    */   public PotionEffectType getEffect() {
/* 83 */     return (PotionEffectType)this.handle.getEffectTypes().read(0);
/*    */   }
/*    */   
/*    */   public void setEffect(PotionEffectType value) {
/* 87 */     this.handle.getEffectTypes().write(0, value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerRemoveEntityEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */