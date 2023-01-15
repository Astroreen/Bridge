/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
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
/*    */ public class WrapperPlayClientAbilities
/*    */   extends AbstractPacket
/*    */ {
/* 25 */   public static final PacketType TYPE = PacketType.Play.Client.ABILITIES;
/*    */   
/*    */   public WrapperPlayClientAbilities() {
/* 28 */     super(new PacketContainer(TYPE), TYPE);
/* 29 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayClientAbilities(PacketContainer packet) {
/* 33 */     super(packet, TYPE);
/*    */   }
/*    */   
/*    */   public boolean isInvulnerable() {
/* 37 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*    */   }
/*    */   
/*    */   public void setInvulnerable(boolean value) {
/* 41 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public boolean isInvulnurable() {
/* 49 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public void setInvulnurable(boolean value) {
/* 57 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*    */   }
/*    */   
/*    */   public boolean isFlying() {
/* 61 */     return ((Boolean)this.handle.getBooleans().read(1)).booleanValue();
/*    */   }
/*    */   
/*    */   public void setFlying(boolean value) {
/* 65 */     this.handle.getBooleans().write(1, Boolean.valueOf(value));
/*    */   }
/*    */   
/*    */   public boolean canFly() {
/* 69 */     return ((Boolean)this.handle.getBooleans().read(2)).booleanValue();
/*    */   }
/*    */   
/*    */   public void setCanFly(boolean value) {
/* 73 */     this.handle.getBooleans().write(2, Boolean.valueOf(value));
/*    */   }
/*    */   
/*    */   public boolean canInstantlyBuild() {
/* 77 */     return ((Boolean)this.handle.getBooleans().read(3)).booleanValue();
/*    */   }
/*    */   
/*    */   public void setCanInstantlyBuild(boolean value) {
/* 81 */     this.handle.getBooleans().write(3, Boolean.valueOf(value));
/*    */   }
/*    */   
/*    */   public float getFlyingSpeed() {
/* 85 */     return ((Float)this.handle.getFloat().read(0)).floatValue();
/*    */   }
/*    */   
/*    */   public void setFlyingSpeed(float value) {
/* 89 */     this.handle.getFloat().write(0, Float.valueOf(value));
/*    */   }
/*    */   
/*    */   public float getWalkingSpeed() {
/* 93 */     return ((Float)this.handle.getFloat().read(1)).floatValue();
/*    */   }
/*    */   
/*    */   public void setWalkingSpeed(float value) {
/* 97 */     this.handle.getFloat().write(1, Float.valueOf(value));
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientAbilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */