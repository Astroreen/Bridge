/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.events.PacketEvent;
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
/*     */ public class WrapperPlayClientSetCommandMinecart
/*     */   extends AbstractPacket
/*     */ {
/*  30 */   public static final PacketType TYPE = PacketType.Play.Client.SET_COMMAND_MINECART;
/*     */   
/*     */   public WrapperPlayClientSetCommandMinecart() {
/*  33 */     super(new PacketContainer(TYPE), TYPE);
/*  34 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayClientSetCommandMinecart(PacketContainer packet) {
/*  38 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEntityID() {
/*  46 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(World world) {
/*  55 */     return (Entity)this.handle.getEntityModifier(world).read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(PacketEvent event) {
/*  64 */     return getEntity(event.getPlayer().getWorld());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityID(int value) {
/*  72 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommand() {
/*  80 */     return (String)this.handle.getStrings().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommand(String value) {
/*  88 */     this.handle.getStrings().write(0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getTrackOutput() {
/*  98 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrackOutput(boolean value) {
/* 106 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientSetCommandMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */