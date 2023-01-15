/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.reflect.StructureModifier;
/*     */ import com.comphenix.protocol.utility.MinecraftReflection;
/*     */ import com.comphenix.protocol.wrappers.EnumWrappers;
/*     */ import java.util.Set;
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
/*     */ public class WrapperPlayServerPosition
/*     */   extends AbstractPacket
/*     */ {
/*  30 */   public static final PacketType TYPE = PacketType.Play.Server.POSITION;
/*     */   
/*     */   public WrapperPlayServerPosition() {
/*  33 */     super(new PacketContainer(TYPE), TYPE);
/*  34 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerPosition(PacketContainer packet) {
/*  38 */     super(packet, TYPE);
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
/*  49 */     return ((Double)this.handle.getDoubles().read(0)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setX(double value) {
/*  58 */     this.handle.getDoubles().write(0, Double.valueOf(value));
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
/*  69 */     return ((Double)this.handle.getDoubles().read(1)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setY(double value) {
/*  78 */     this.handle.getDoubles().write(1, Double.valueOf(value));
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
/*  89 */     return ((Double)this.handle.getDoubles().read(2)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZ(double value) {
/*  98 */     this.handle.getDoubles().write(2, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYaw() {
/* 109 */     return ((Float)this.handle.getFloat().read(0)).floatValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setYaw(float value) {
/* 118 */     this.handle.getFloat().write(0, Float.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPitch() {
/* 129 */     return ((Float)this.handle.getFloat().read(1)).floatValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPitch(float value) {
/* 138 */     this.handle.getFloat().write(1, Float.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/* 142 */   private static final Class<?> FLAGS_CLASS = MinecraftReflection.getMinecraftClass("EnumPlayerTeleportFlags", new String[] { "PacketPlayOutPosition$EnumPlayerTeleportFlags" });
/*     */   
/*     */   public enum PlayerTeleportFlag
/*     */   {
/* 146 */     X, Y, Z, Y_ROT, X_ROT;
/*     */   }
/*     */   
/*     */   private StructureModifier<Set<PlayerTeleportFlag>> getFlagsModifier() {
/* 150 */     return this.handle.getSets(
/* 151 */         EnumWrappers.getGenericConverter(FLAGS_CLASS, PlayerTeleportFlag.class));
/*     */   }
/*     */   
/*     */   public Set<PlayerTeleportFlag> getFlags() {
/* 155 */     return (Set<PlayerTeleportFlag>)getFlagsModifier().read(0);
/*     */   }
/*     */   
/*     */   public void setFlags(Set<PlayerTeleportFlag> value) {
/* 159 */     getFlagsModifier().write(0, value);
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerPosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */