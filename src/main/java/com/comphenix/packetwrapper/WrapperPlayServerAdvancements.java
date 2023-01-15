/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.reflect.EquivalentConverter;
/*     */ import com.comphenix.protocol.utility.MinecraftReflection;
/*     */ import com.comphenix.protocol.wrappers.AutoWrapper;
/*     */ import com.comphenix.protocol.wrappers.BukkitConverters;
/*     */ import com.comphenix.protocol.wrappers.Converters;
/*     */ import com.comphenix.protocol.wrappers.EnumWrappers;
/*     */ import com.comphenix.protocol.wrappers.MinecraftKey;
/*     */ import com.comphenix.protocol.wrappers.WrappedChatComponent;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import org.bukkit.advancement.Advancement;
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
/*     */ public class WrapperPlayServerAdvancements
/*     */   extends AbstractPacket
/*     */ {
/*  37 */   public static final PacketType TYPE = PacketType.Play.Server.ADVANCEMENTS;
/*     */   
/*     */   public WrapperPlayServerAdvancements() {
/*  40 */     super(new PacketContainer(TYPE), TYPE);
/*  41 */     this.handle.getModifier().writeDefaults();
/*     */   }
/*     */   
/*     */   public WrapperPlayServerAdvancements(PacketContainer packet) {
/*  45 */     super(packet, TYPE);
/*     */   }
/*     */   
/*     */   public static class SerializedAdvancement {
/*     */     public MinecraftKey key;
/*     */     public Advancement advancement;
/*     */     public WrapperPlayServerAdvancements.AdvancementDisplay display;
/*     */     public Object rewards;
/*     */     public Map<String, Object> criteria;
/*     */     public String[][] requirements;
/*     */   }
/*     */   
/*     */   public static class AdvancementDisplay {
/*     */     public WrappedChatComponent title;
/*     */     public WrappedChatComponent description;
/*     */     public ItemStack icon;
/*     */     public MinecraftKey background;
/*     */     public WrapperPlayServerAdvancements.FrameType frame;
/*     */     public boolean showToast;
/*     */     public boolean announceToChat;
/*     */     public boolean hidden;
/*     */     public float xCoord;
/*     */     public float yCoord;
/*     */   }
/*     */   
/*     */   public enum FrameType {
/*  71 */     TASK,
/*  72 */     CHALLENGE,
/*  73 */     GOAL;
/*     */   }
/*     */   
/*     */   public static class AdvancementProgress
/*     */   {
/*     */     public Map<String, WrapperPlayServerAdvancements.CriterionProgress> progress;
/*     */     public String[][] array2d;
/*     */   }
/*     */   
/*     */   public static class CriterionProgress
/*     */   {
/*     */     public WrapperPlayServerAdvancements.AdvancementProgress progress;
/*     */     public Date date;
/*     */   }
/*  87 */   private static final AutoWrapper<AdvancementDisplay> DISPLAY = AutoWrapper.wrap(AdvancementDisplay.class, "AdvancementDisplay")
/*  88 */     .field(0, BukkitConverters.getWrappedChatComponentConverter())
/*  89 */     .field(1, BukkitConverters.getWrappedChatComponentConverter())
/*  90 */     .field(2, BukkitConverters.getItemStackConverter())
/*  91 */     .field(3, MinecraftKey.getConverter())
/*  92 */     .field(4, EnumWrappers.getGenericConverter(MinecraftReflection.getMinecraftClass("AdvancementFrameType"), FrameType.class));
/*     */ 
/*     */   
/*  95 */   private static final AutoWrapper<SerializedAdvancement> WRAPPER = AutoWrapper.wrap(SerializedAdvancement.class, "Advancement$SerializedAdvancement")
/*  96 */     .field(0, MinecraftKey.getConverter())
/*  97 */     .field(1, BukkitConverters.getAdvancementConverter())
/*  98 */     .field(2, (EquivalentConverter)DISPLAY);
/*     */ 
/*     */   
/* 101 */   private static final AutoWrapper<CriterionProgress> CRITERION = AutoWrapper.wrap(CriterionProgress.class, "CriterionProgress");
/*     */ 
/*     */   
/* 104 */   private static final AutoWrapper<AdvancementProgress> PROGRESS = AutoWrapper.wrap(AdvancementProgress.class, "AdvancementProgress")
/* 105 */     .field(0, BukkitConverters.getMapConverter(Converters.passthrough(String.class), (EquivalentConverter)CRITERION));
/*     */   
/*     */   static {
/* 108 */     CRITERION.field(0, (EquivalentConverter)PROGRESS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReset() {
/* 118 */     return ((Boolean)this.handle.getBooleans().read(0)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReset(boolean value) {
/* 126 */     this.handle.getBooleans().write(0, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   public Optional<Map<MinecraftKey, SerializedAdvancement>> getAdvancements() {
/* 130 */     return this.handle.getMaps(MinecraftKey.getConverter(), (EquivalentConverter)WRAPPER).optionRead(0);
/*     */   }
/*     */   
/*     */   public void setAdvancements(Map<MinecraftKey, SerializedAdvancement> value) {
/* 134 */     this.handle.getMaps(MinecraftKey.getConverter(), (EquivalentConverter)WRAPPER).writeSafely(0, value);
/*     */   }
/*     */   
/*     */   public Optional<Set<MinecraftKey>> getKeys() {
/* 138 */     return this.handle.getSets(MinecraftKey.getConverter()).optionRead(0);
/*     */   }
/*     */   
/*     */   public void setKeys(Set<MinecraftKey> value) {
/* 142 */     this.handle.getSets(MinecraftKey.getConverter()).writeSafely(0, value);
/*     */   }
/*     */   
/*     */   public Optional<Map<MinecraftKey, AdvancementProgress>> getProgress() {
/* 146 */     return this.handle.getMaps(MinecraftKey.getConverter(), (EquivalentConverter)PROGRESS).optionRead(1);
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\WrapperPlayServerAdvancements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */