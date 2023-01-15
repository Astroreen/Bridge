/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import org.bukkit.inventory.ItemStack;
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
/*     */ public class WrapperPlayClientWindowClick
/*     */   extends AbstractPacket
/*     */ {
/*  27 */   public static final PacketType TYPE = PacketType.Play.Client.WINDOW_CLICK;
/*     */   
/*     */   public WrapperPlayClientWindowClick() {
/*  30 */     super(new PacketContainer(TYPE), TYPE);
/*  31 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayClientWindowClick(PacketContainer packet) {
/*  35 */     super(packet, TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWindowId() {
/*  46 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWindowId(int value) {
/*  55 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSlot() {
/*  66 */     return ((Integer)this.handle.getIntegers().read(1)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSlot(int value) {
/*  75 */     this.handle.getIntegers().write(1, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getButton() {
/*  86 */     return ((Integer)this.handle.getIntegers().read(2)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setButton(int value) {
/*  95 */     this.handle.getIntegers().write(2, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getActionNumber() {
/* 107 */     return ((Short)this.handle.getShorts().read(0)).shortValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActionNumber(short value) {
/* 116 */     this.handle.getShorts().write(0, Short.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getClickedItem() {
/* 125 */     return (ItemStack)this.handle.getItemModifier().read(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClickedItem(ItemStack value) {
/* 134 */     this.handle.getItemModifier().write(0, value);
/*     */   }
/*     */   
/*     */   public InventoryClickType getShift() {
/* 138 */     return (InventoryClickType)this.handle.getEnumModifier(InventoryClickType.class, 5).read(0);
/*     */   }
/*     */   
/*     */   public void setShift(InventoryClickType value) {
/* 142 */     this.handle.getEnumModifier(InventoryClickType.class, 5).write(0, value);
/*     */   }
/*     */   
/*     */   public enum InventoryClickType {
/* 146 */     PICKUP, QUICK_MOVE, SWAP, CLONE, THROW, QUICK_CRAFT, PICKUP_ALL;
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientWindowClick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */