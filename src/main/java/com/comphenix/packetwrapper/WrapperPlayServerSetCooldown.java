/*    */ package com.comphenix.packetwrapper;
/*    */
/*    */ import com.comphenix.protocol.PacketType;
/*    */ import com.comphenix.protocol.events.PacketContainer;
/*    */ import com.comphenix.protocol.reflect.EquivalentConverter;
/*    */ import com.comphenix.protocol.reflect.accessors.Accessors;
/*    */ import com.comphenix.protocol.reflect.accessors.MethodAccessor;
/*    */ import com.comphenix.protocol.utility.MinecraftReflection;
/*    */ import org.bukkit.Material;
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
/*    */ public class WrapperPlayServerSetCooldown
/*    */   extends AbstractPacket
/*    */ {
/* 32 */   private static final Class<?> ITEM_CLASS = MinecraftReflection.getMinecraftClass("Item");
/* 33 */   public static final PacketType TYPE = PacketType.Play.Server.SET_COOLDOWN;
/*    */
/*    */   public WrapperPlayServerSetCooldown() {
/* 36 */     super(new PacketContainer(TYPE), TYPE);
/* 37 */     this.handle.getModifier().writeDefaults();
/*    */   }
/*    */
/*    */   public WrapperPlayServerSetCooldown(PacketContainer packet) {
/* 41 */     super(packet, TYPE);
/*    */   }
/*    */
/*    */   public Material getItem() {
/* 45 */     return (Material)this.handle.getModifier()
/* 46 */       .withType(ITEM_CLASS, new ItemConverter()).read(0);
/*    */   }
/*    */
/*    */   public void setItem(Material value) {
/* 50 */     this.handle.getModifier()
/* 51 */       .withType(ITEM_CLASS, new ItemConverter())
/* 52 */       .write(0, value);
/*    */   }
/*    */
/*    */   public int getTicks() {
/* 56 */     return ((Integer)this.handle.getIntegers().read(0)).intValue();
/*    */   }
/*    */
/*    */   public void setTicks(int value) {
/* 60 */     this.handle.getIntegers().write(0, Integer.valueOf(value));
/*    */   }
/*    */
/*    */   private static class ItemConverter implements EquivalentConverter<Material> {
/* 64 */     private static MethodAccessor getMaterial = null; private ItemConverter() {}
/* 65 */     private static MethodAccessor getItem = null;
/*    */
/*    */
/*    */     public Material getSpecific(Object generic) {
    if (getMaterial == null) {
        getMaterial = Accessors.getMethodAccessor(MinecraftReflection.getCraftBukkitClass("util.CraftMagicNumbers"), "getMaterial", new Class[]{WrapperPlayServerSetCooldown.class});
    }

    return (Material)getMaterial.invoke((Object)null, new Object[]{generic});
}
/*    */
/*    */
/*    */     public Object getGeneric(Material specific) {
/* 81 */       if (getItem == null)
/*    */       {
/* 83 */         getItem = Accessors.getMethodAccessor(
/* 84 */             MinecraftReflection.getCraftBukkitClass("util.CraftMagicNumbers"), "getItem", new Class[] { Material.class });
/*    */       }
/*    */
/*    */
/* 88 */       return getItem.invoke(null, new Object[] { specific });
/*    */     }
/*    */
/*    */
/*    */     public Class<Material> getSpecificType() {
/* 93 */       return Material.class;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerSetCooldown.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */