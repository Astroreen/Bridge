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
/*    */ public class WrapperPlayClientRecipeDisplayed
/*    */   extends AbstractPacket
/*    */ {
/* 25 */   public static final PacketType TYPE = PacketType.Play.Client.RECIPE_DISPLAYED;
/*    */   
/*    */   public WrapperPlayClientRecipeDisplayed() {
/* 28 */     super(new PacketContainer(TYPE), TYPE);
/* 29 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayClientRecipeDisplayed(PacketContainer packet) {
/* 33 */     super(packet, TYPE);
/*    */   }
/*    */   
/*    */   public Status getStatus() {
/* 37 */     return (Status)this.handle.getEnumModifier(Status.class, 0).readSafely(0);
/*    */   }
/*    */   
/*    */   public void setStatus(Status value) {
/* 41 */     this.handle.getEnumModifier(Status.class, 0).writeSafely(0, value);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isBookOpen() {
/* 47 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*    */   }
/*    */   
/*    */   public void setBookOpen(boolean value) {
/* 51 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*    */   }
/*    */   
/*    */   public boolean isFilterActive() {
/* 55 */     return ((Boolean)this.handle.getBooleans().read(1)).booleanValue();
/*    */   }
/*    */   
/*    */   public void setFilterActive(boolean value) {
/* 59 */     this.handle.getBooleans().write(1, Boolean.valueOf(value));
/*    */   }
/*    */   
/*    */   public enum Status {
/* 63 */     SHOWN,
/* 64 */     SETTINGS;
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientRecipeDisplayed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */