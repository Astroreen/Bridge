/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ 
/*    */ public class WrapperPlayClientTradeSelect extends AbstractPacket {
/*  7 */   public static final PacketType TYPE = PacketType.Play.Client.TR_SEL;
/*    */   
/*    */   public WrapperPlayClientTradeSelect() {
/* 10 */     super(new PacketContainer(TYPE), TYPE);
/* 11 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public int getSlot() {
/* 15 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*    */   }
/*    */   
/*    */   public void setSlot(int value) {
/* 19 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayClientTradeSelect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */