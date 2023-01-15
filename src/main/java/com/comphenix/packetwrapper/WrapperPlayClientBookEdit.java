/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import org.bukkit.inventory.ItemStack;
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
/*    */ 
/*    */ public class WrapperPlayClientBookEdit
/*    */   extends AbstractPacket
/*    */ {
/* 28 */   public static final PacketType TYPE = PacketType.Play.Client.B_EDIT;
/*    */   
/*    */   public WrapperPlayClientBookEdit() {
/* 31 */     super(new PacketContainer(TYPE), TYPE);
/* 32 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayClientBookEdit(PacketContainer packet) {
/* 36 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack getNewBook() {
/* 44 */     return (ItemStack)this.handle.getItemModifier().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setNewBook(ItemStack value) {
/* 52 */     this.handle.getItemModifier().write(0, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean getIsSigning() {
/* 62 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setIsSigning(boolean value) {
/* 70 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientBookEdit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */