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
/*    */ public class WrapperPlayClientSetCreativeSlot
/*    */   extends AbstractPacket
/*    */ {
/* 27 */   public static final PacketType TYPE = PacketType.Play.Client.SET_CREATIVE_SLOT;
/*    */ 
/*    */   
/*    */   public WrapperPlayClientSetCreativeSlot() {
/* 31 */     super(new PacketContainer(TYPE), TYPE);
/* 32 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayClientSetCreativeSlot(PacketContainer packet) {
/* 36 */     super(packet, TYPE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getSlot() {
/* 47 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSlot(int value) {
/* 56 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack getClickedItem() {
/* 65 */     return (ItemStack)this.handle.getItemModifier().read(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClickedItem(ItemStack value) {
/* 74 */     this.handle.getItemModifier().write(0, value);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientSetCreativeSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */