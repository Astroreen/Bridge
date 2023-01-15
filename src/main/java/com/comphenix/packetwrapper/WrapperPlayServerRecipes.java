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
/*    */ 
/*    */ public class WrapperPlayServerRecipes
/*    */   extends AbstractPacket
/*    */ {
/* 26 */   public static final PacketType TYPE = PacketType.Play.Server.RECIPES;
/*    */   
/*    */   public WrapperPlayServerRecipes() {
/* 29 */     super(new PacketContainer(TYPE), TYPE);
/* 30 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerRecipes(PacketContainer packet) {
/* 34 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Action getAction() {
/* 44 */     return (Action)this.handle.getEnumModifier(Action.class, 0).readSafely(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAction(Action value) {
/* 52 */     this.handle.getEnumModifier(Action.class, 0).writeSafely(0, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean getCraftingBookOpen() {
/* 62 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCraftingBookOpen(boolean value) {
/* 70 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean getFilteringCraftable() {
/* 80 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFilteringCraftable(boolean value) {
/* 88 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*    */   }
/*    */ 
/*    */   
/*    */   public enum Action
/*    */   {
/* 94 */     INIT,
/* 95 */     ADD,
/* 96 */     REMOVE;
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerRecipes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */