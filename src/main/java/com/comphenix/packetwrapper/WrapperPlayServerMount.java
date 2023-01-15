/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.ProtocolLibrary;
/*     */ import com.comphenix.protocol.ProtocolManager;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.events.PacketEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Entity;
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
/*     */ public class WrapperPlayServerMount
/*     */   extends AbstractPacket
/*     */ {
/*  35 */   public static final PacketType TYPE = PacketType.Play.Server.MOUNT;
/*     */   
/*     */   public WrapperPlayServerMount() {
/*  38 */     super(new PacketContainer(TYPE), TYPE);
/*  39 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerMount(PacketContainer packet) {
/*  43 */     super(packet, TYPE);
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
/*  54 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(World world) {
/*  64 */     return (Entity)this.handle.getEntityModifier(world).read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(PacketEvent event) {
/*  74 */     return getEntity(event.getPlayer().getWorld());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityID(int value) {
/*  83 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public int[] getPassengerIds() {
/*  87 */     return (int[])this.handle.getIntegerArrays().read(0);
/*     */   }
/*     */   
/*     */   public void setPassengerIds(int[] value) {
/*  91 */     this.handle.getIntegerArrays().write(0, value);
/*     */   }
/*     */   
/*     */   public List<Entity> getPassengers(PacketEvent event) {
/*  95 */     return getPassengers(event.getPlayer().getWorld());
/*     */   }
/*     */   
/*     */   public List<Entity> getPassengers(World world) {
/*  99 */     int[] ids = getPassengerIds();
/* 100 */     List<Entity> passengers = new ArrayList<>();
/* 101 */     ProtocolManager manager = ProtocolLibrary.getProtocolManager();
/*     */     
/* 103 */     for (int id : ids) {
/* 104 */       Entity entity = manager.getEntityFromID(world, id);
/* 105 */       if (entity != null) {
/* 106 */         passengers.add(entity);
/*     */       }
/*     */     } 
/*     */     
/* 110 */     return passengers;
/*     */   }
/*     */   
/*     */   public void setPassengers(List<Entity> value) {
/* 114 */     int[] array = new int[value.size()];
/* 115 */     for (int i = 0; i < value.size(); i++) {
/* 116 */       array[i] = ((Entity)value.get(i)).getEntityId();
/*     */     }
/*     */     
/* 119 */     setPassengerIds(array);
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerMount.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */