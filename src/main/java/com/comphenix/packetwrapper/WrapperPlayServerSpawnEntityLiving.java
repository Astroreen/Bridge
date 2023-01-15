/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.ProtocolLibrary;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.events.PacketEvent;
/*     */ import com.comphenix.protocol.injector.PacketConstructor;
/*     */ import com.comphenix.protocol.wrappers.WrappedDataWatcher;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.EntityType;
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
/*     */ public class WrapperPlayServerSpawnEntityLiving
/*     */   extends AbstractPacket
/*     */ {
/*  35 */   public static final PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY_LIVING;
/*     */   
/*     */   private static PacketConstructor entityConstructor;
/*     */ 
/*     */   
/*     */   public WrapperPlayServerSpawnEntityLiving() {
/*  41 */     super(new PacketContainer(TYPE), TYPE);
/*  42 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerSpawnEntityLiving(PacketContainer packet) {
/*  46 */     super(packet, TYPE);
/*     */   }
/*     */   
/*     */   public WrapperPlayServerSpawnEntityLiving(Entity entity) {
/*  50 */     super(fromEntity(entity), TYPE);
/*     */   }
/*     */ 
/*     */   
/*     */   private static PacketContainer fromEntity(Entity entity) {
/*  55 */     if (entityConstructor == null)
/*     */     {
/*     */       
/*  58 */       entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(TYPE, new Object[] { entity }); } 
/*  59 */     return entityConstructor.createPacket(new Object[] { entity });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEntityID() {
/*  68 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(World world) {
/*  78 */     return (Entity)this.handle.getEntityModifier(world).read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(PacketEvent event) {
/*  88 */     return getEntity(event.getPlayer().getWorld());
/*     */   }
/*     */   
/*     */   public UUID getUniqueId() {
/*  92 */     return (UUID)this.handle.getUUIDs().read(0);
/*     */   }
/*     */   
/*     */   public void setUniqueId(UUID value) {
/*  96 */     this.handle.getUUIDs().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityID(int value) {
/* 105 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityType getType() {
/* 115 */     return EntityType.fromId(((Integer)this.handle.getIntegers().read(1)).intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(EntityType value) {
/* 125 */     this.handle.getIntegers().write(1, Integer.valueOf(value.getTypeId()));
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
/* 136 */     return ((Double)this.handle.getDoubles().read(0)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setX(double value) {
/* 145 */     this.handle.getDoubles().write(0, Double.valueOf(value));
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
/* 156 */     return ((Double)this.handle.getDoubles().read(1)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setY(double value) {
/* 165 */     this.handle.getDoubles().write(1, Double.valueOf(value));
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
/* 176 */     return ((Double)this.handle.getDoubles().read(2)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZ(double value) {
/* 185 */     this.handle.getDoubles().write(2, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYaw() {
/* 194 */     return ((Byte)this.handle.getBytes().read(0)).byteValue() * 360.0F / 256.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setYaw(float value) {
/* 203 */     this.handle.getBytes().write(0, Byte.valueOf((byte)(int)(value * 256.0F / 360.0F)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPitch() {
/* 212 */     return ((Byte)this.handle.getBytes().read(1)).byteValue() * 360.0F / 256.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPitch(float value) {
/* 221 */     this.handle.getBytes().write(1, Byte.valueOf((byte)(int)(value * 256.0F / 360.0F)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getHeadPitch() {
/* 230 */     return ((Byte)this.handle.getBytes().read(2)).byteValue() * 360.0F / 256.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeadPitch(float value) {
/* 239 */     this.handle.getBytes().write(2, Byte.valueOf((byte)(int)(value * 256.0F / 360.0F)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getVelocityX() {
/* 248 */     return ((Integer)this.handle.getIntegers().read(2)).intValue() / 8000.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVelocityX(double value) {
/* 257 */     this.handle.getIntegers().write(2, Integer.valueOf((int)(value * 8000.0D)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getVelocityY() {
/* 266 */     return ((Integer)this.handle.getIntegers().read(3)).intValue() / 8000.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVelocityY(double value) {
/* 275 */     this.handle.getIntegers().write(3, Integer.valueOf((int)(value * 8000.0D)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getVelocityZ() {
/* 284 */     return ((Integer)this.handle.getIntegers().read(4)).intValue() / 8000.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVelocityZ(double value) {
/* 293 */     this.handle.getIntegers().write(4, Integer.valueOf((int)(value * 8000.0D)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WrappedDataWatcher getMetadata() {
/* 304 */     return (WrappedDataWatcher)this.handle.getDataWatcherModifier().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetadata(WrappedDataWatcher value) {
/* 313 */     this.handle.getDataWatcherModifier().write(0, value);
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerSpawnEntityLiving.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */