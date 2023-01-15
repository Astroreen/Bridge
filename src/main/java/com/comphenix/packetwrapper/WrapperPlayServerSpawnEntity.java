/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.PacketConstructor;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.UUID;
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
/*     */ public class WrapperPlayServerSpawnEntity
/*     */   extends AbstractPacket
/*     */ {
/*  34 */   public static final PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY;
/*     */   
/*     */   private static PacketConstructor entityConstructor;
/*     */ 
/*     */   
/*     */   public static class ObjectTypes
/*     */
/*     */   {
/*     */     public static final int BOAT = 1;
/*     */     
/*     */     public static final int ITEM_STACK = 2;
/*     */     
/*     */     public static final int AREA_EFFECT_CLOUD = 3;
/*     */     
/*     */     public static final int MINECART = 10;
/*     */     
/*     */     public static final int ACTIVATED_TNT = 50;
/*     */     
/*     */     public static final int ENDER_CRYSTAL = 51;
/*     */     
/*     */     public static final int TIPPED_ARROW_PROJECTILE = 60;
/*     */     public static final int SNOWBALL_PROJECTILE = 61;
/*     */     public static final int EGG_PROJECTILE = 62;
/*     */     public static final int GHAST_FIREBALL = 63;
/*     */     public static final int BLAZE_FIREBALL = 64;
/*     */     public static final int THROWN_ENDERPEARL = 65;
/*     */     public static final int WITHER_SKULL_PROJECTILE = 66;
/*     */     public static final int SHULKER_BULLET = 67;
/*     */     public static final int FALLING_BLOCK = 70;
/*     */     public static final int ITEM_FRAME = 71;
/*     */     public static final int EYE_OF_ENDER = 72;
/*     */     public static final int THROWN_POTION = 73;
/*     */     public static final int THROWN_EXP_BOTTLE = 75;
/*     */     public static final int FIREWORK_ROCKET = 76;
/*     */     public static final int LEASH_KNOT = 77;
/*     */     public static final int ARMORSTAND = 78;
/*     */     public static final int FISHING_FLOAT = 90;
/*     */     public static final int SPECTRAL_ARROW = 91;
/*     */     public static final int DRAGON_FIREBALL = 93;
/*  73 */     private static ObjectTypes INSTANCE = new ObjectTypes();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static ObjectTypes getInstance() {
/*  81 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */   
/*     */   public WrapperPlayServerSpawnEntity() {
/*  86 */     super(new PacketContainer(TYPE), TYPE);
/*  87 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerSpawnEntity(PacketContainer packet) {
/*  91 */     super(packet, TYPE);
/*     */   }
/*     */   
/*     */   public WrapperPlayServerSpawnEntity(Entity entity, int type, int objectData) {
/*  95 */     super(fromEntity(entity, type, objectData), TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static PacketContainer fromEntity(Entity entity, int type, int objectData) {
/* 101 */     if (entityConstructor == null)
/*     */     {
/*     */       
/* 104 */       entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(TYPE, new Object[] { entity, Integer.valueOf(type), 
/* 105 */             Integer.valueOf(objectData) }); } 
/* 106 */     return entityConstructor.createPacket(new Object[] { entity, Integer.valueOf(type), Integer.valueOf(objectData) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEntityID() {
/* 115 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(World world) {
/* 125 */     return (Entity)this.handle.getEntityModifier(world).read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(PacketEvent event) {
/* 135 */     return getEntity(event.getPlayer().getWorld());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityID(int value) {
/* 144 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public UUID getUniqueId() {
/* 148 */     return (UUID)this.handle.getUUIDs().read(0);
/*     */   }
/*     */   
/*     */   public void setUniqueId(UUID value) {
/* 152 */     this.handle.getUUIDs().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getX() {
/* 163 */     return ((Double)this.handle.getDoubles().read(0)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setX(double value) {
/* 172 */     this.handle.getDoubles().write(0, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getY() {
/* 183 */     return ((Double)this.handle.getDoubles().read(1)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setY(double value) {
/* 192 */     this.handle.getDoubles().write(1, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getZ() {
/* 203 */     return ((Double)this.handle.getDoubles().read(2)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZ(double value) {
/* 212 */     this.handle.getDoubles().write(2, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getOptionalSpeedX() {
/* 223 */     return ((Integer)this.handle.getIntegers().read(1)).intValue() / 8000.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptionalSpeedX(double value) {
/* 232 */     this.handle.getIntegers().write(1, Integer.valueOf((int)(value * 8000.0D)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getOptionalSpeedY() {
/* 243 */     return ((Integer)this.handle.getIntegers().read(2)).intValue() / 8000.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptionalSpeedY(double value) {
/* 252 */     this.handle.getIntegers().write(2, Integer.valueOf((int)(value * 8000.0D)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getOptionalSpeedZ() {
/* 263 */     return ((Integer)this.handle.getIntegers().read(3)).intValue() / 8000.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptionalSpeedZ(double value) {
/* 272 */     this.handle.getIntegers().write(3, Integer.valueOf((int)(value * 8000.0D)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPitch() {
/* 281 */     return ((Integer)this.handle.getIntegers().read(4)).intValue() * 360.0F / 256.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPitch(float value) {
/* 290 */     this.handle.getIntegers().write(4, Integer.valueOf((int)(value * 256.0F / 360.0F)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYaw() {
/* 299 */     return ((Integer)this.handle.getIntegers().read(5)).intValue() * 360.0F / 256.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setYaw(float value) {
/* 308 */     this.handle.getIntegers().write(5, Integer.valueOf((int)(value * 256.0F / 360.0F)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/* 317 */     return ((Integer)this.handle.getIntegers().read(6)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(int value) {
/* 326 */     this.handle.getIntegers().write(6, Integer.valueOf(value));
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
/*     */   public int getObjectData() {
/* 364 */     return ((Integer)this.handle.getIntegers().read(7)).intValue();
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
/*     */   public void setObjectData(int value) {
/* 376 */     this.handle.getIntegers().write(7, Integer.valueOf(value));
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerSpawnEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */