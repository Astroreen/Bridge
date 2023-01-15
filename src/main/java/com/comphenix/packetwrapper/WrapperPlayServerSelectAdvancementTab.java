/*    */ package com.comphenix.packetwrapper;
/*    */ 
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.comphenix.protocol.wrappers.MinecraftKey;
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
/*    */ public class WrapperPlayServerSelectAdvancementTab
/*    */   extends AbstractPacket
/*    */ {
/* 27 */   public static final PacketType TYPE = PacketType.Play.Server.SELECT_ADVANCEMENT_TAB;
/*    */   
/*    */   public WrapperPlayServerSelectAdvancementTab() {
/* 30 */     super(new PacketContainer(TYPE), TYPE);
/* 31 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */   
/*    */   public WrapperPlayServerSelectAdvancementTab(PacketContainer packet) {
/* 35 */     super(packet, TYPE);
/*    */   }
/*    */   
/*    */   public MinecraftKey getKey() {
/* 39 */     return (MinecraftKey)this.handle.getMinecraftKeys().readSafely(0);
/*    */   }
/*    */   
/*    */   public void setKey(MinecraftKey key) {
/* 43 */     this.handle.getMinecraftKeys().writeSafely(0, key);
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerSelectAdvancementTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */