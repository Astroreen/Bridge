/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.events.PacketEvent;
/*     */ import com.comphenix.protocol.wrappers.WrappedDataWatcher;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.util.Vector;
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
/*     */ public class WrapperPlayServerNamedEntitySpawn
/*     */   extends AbstractPacket
/*     */ {
/*  33 */   public static final PacketType TYPE = PacketType.Play.Server.NAMED_ENTITY_SPAWN;
/*     */ 
/*     */   
/*     */   public WrapperPlayServerNamedEntitySpawn() {
/*  37 */     super(new PacketContainer(TYPE), TYPE);
/*  38 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerNamedEntitySpawn(PacketContainer packet) {
/*  42 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEntityID() {
/*  53 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityID(int value) {
/*  62 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(World world) {
/*  72 */     return (Entity)this.handle.getEntityModifier(world).read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(PacketEvent event) {
/*  82 */     return getEntity(event.getPlayer().getWorld());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UUID getPlayerUUID() {
/*  93 */     return (UUID)this.handle.getUUIDs().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlayerUUID(UUID value) {
/* 102 */     this.handle.getUUIDs().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getPosition() {
/* 111 */     return new Vector(getX(), getY(), getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosition(Vector position) {
/* 120 */     setX(position.getX());
/* 121 */     setY(position.getY());
/* 122 */     setZ(position.getZ());
/*     */   }
/*     */   
/*     */   public double getX() {
/* 126 */     return ((Double)this.handle.getDoubles().read(0)).doubleValue();
/*     */   }
/*     */   
/*     */   public void setX(double value) {
/* 130 */     this.handle.getDoubles().write(0, Double.valueOf(value));
/*     */   }
/*     */   
/*     */   public double getY() {
/* 134 */     return ((Double)this.handle.getDoubles().read(1)).doubleValue();
/*     */   }
/*     */   
/*     */   public void setY(double value) {
/* 138 */     this.handle.getDoubles().write(1, Double.valueOf(value));
/*     */   }
/*     */   
/*     */   public double getZ() {
/* 142 */     return ((Double)this.handle.getDoubles().read(2)).doubleValue();
/*     */   }
/*     */   
/*     */   public void setZ(double value) {
/* 146 */     this.handle.getDoubles().write(2, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYaw() {
/* 155 */     return ((Byte)this.handle.getBytes().read(0)).byteValue() * 360.0F / 256.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setYaw(float value) {
/* 164 */     this.handle.getBytes().write(0, Byte.valueOf((byte)(int)(value * 256.0F / 360.0F)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPitch() {
/* 173 */     return ((Byte)this.handle.getBytes().read(1)).byteValue() * 360.0F / 256.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPitch(float value) {
/* 182 */     this.handle.getBytes().write(1, Byte.valueOf((byte)(int)(value * 256.0F / 360.0F)));
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
/* 193 */     return (WrappedDataWatcher)this.handle.getDataWatcherModifier().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetadata(WrappedDataWatcher value) {
/* 202 */     this.handle.getDataWatcherModifier().write(0, value);
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerNamedEntitySpawn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */