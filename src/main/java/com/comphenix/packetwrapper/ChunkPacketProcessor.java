/*     */ package com.comphenix.packetwrapper;
/*     */ 
/*     */ import com.comphenix.protocol.PacketType;
/*     */ import com.comphenix.protocol.events.PacketContainer;
/*     */ import com.comphenix.protocol.reflect.StructureModifier;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkPacketProcessor
/*     */ {
/*     */   protected static final int BYTES_PER_NIBBLE_PART = 2048;
/*     */   protected static final int CHUNK_SEGMENTS = 16;
/*     */   protected static final int NIBBLES_REQUIRED = 4;
/*     */   @Deprecated
/*     */   public static final int BLOCK_ID_LENGHT = 4096;
/*     */   public static final int BLOCK_ID_LENGTH = 4096;
/*     */   @Deprecated
/*     */   public static final int DATA_LENGHT = 2048;
/*     */   public static final int DATA_LENGTH = 2048;
/*     */   public static final int BIOME_ARRAY_LENGTH = 256;
/*     */   private int chunkX;
/*     */   private int chunkZ;
/*     */   private int chunkMask;
/*     */   private int extraMask;
/*     */   private int chunkSectionNumber;
/*     */   private int extraSectionNumber;
/*     */   
/*     */   public static interface ChunkletProcessor
/*     */   {
/*     */     void processChunklet(Location param1Location, byte[] param1ArrayOfbyte, ChunkPacketProcessor.ChunkOffsets param1ChunkOffsets);
/*     */     
/*     */     void processBiomeArray(Location param1Location, byte[] param1ArrayOfbyte, int param1Int);
/*     */   }
/*     */   
/*     */   public static class ChunkOffsets
/*     */   {
/*     */     private int blockIdOffset;
/*     */     private int dataOffset;
/*     */     private int lightOffset;
/*     */     private int skylightOffset;
/*     */     private int extraOffset;
/*     */     
/*     */     private ChunkOffsets(int blockIdOffset, int dataOffset, int lightOffset, int skylightOffset, int extraOffset) {
/*  49 */       this.blockIdOffset = blockIdOffset;
/*  50 */       this.dataOffset = dataOffset;
/*  51 */       this.lightOffset = lightOffset;
/*  52 */       this.skylightOffset = skylightOffset;
/*  53 */       this.extraOffset = extraOffset;
/*     */     }
/*     */     
/*     */     private void incrementIdIndex() {
/*  57 */       this.blockIdOffset += 4096;
/*  58 */       this.dataOffset += 2048;
/*  59 */       this.dataOffset += 2048;
/*     */       
/*  61 */       if (this.skylightOffset >= 0) {
/*  62 */         this.skylightOffset += 2048;
/*     */       }
/*     */     }
/*     */     
/*     */     private void incrementExtraIndex() {
/*  67 */       if (this.extraOffset >= 0) {
/*  68 */         this.extraOffset += 2048;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getBlockIdOffset() {
/*  81 */       return this.blockIdOffset;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getDataOffset() {
/*  93 */       return this.dataOffset;
/*     */     }
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
/*     */     public int getLightOffset() {
/* 106 */       return this.lightOffset;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getSkylightOffset() {
/* 118 */       return this.skylightOffset;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasSkylightOffset() {
/* 127 */       return (this.skylightOffset >= 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getExtraOffset() {
/* 139 */       return this.extraOffset;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasExtraOffset() {
/* 148 */       return (this.extraOffset > 0);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasContinuous = true;
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
/*     */   private int startIndex;
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
/*     */   private int size;
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
/*     */   private byte[] data;
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
/*     */   private World world;
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
/*     */   public static ChunkPacketProcessor fromMapPacket(PacketContainer packet, World world) {
/* 233 */     if (!packet.getType().equals(PacketType.Play.Server.MAP_CHUNK)) {
/* 234 */       throw new IllegalArgumentException(packet + " must be a MAP_CHUNK packet.");
/*     */     }
/*     */     
/* 237 */     StructureModifier<Integer> ints = packet.getIntegers();
/* 238 */     StructureModifier<byte[]> byteArray = packet.getByteArrays();
/*     */ 
/*     */     
/* 241 */     ChunkPacketProcessor processor = new ChunkPacketProcessor();
/* 242 */     processor.world = world;
/* 243 */     processor.chunkX = ((Integer)ints.read(0)).intValue();
/* 244 */     processor.chunkZ = ((Integer)ints.read(1)).intValue();
/* 245 */     processor.chunkMask = ((Integer)ints.read(2)).intValue();
/* 246 */     processor.extraMask = ((Integer)ints.read(3)).intValue();
/* 247 */     processor.data = (byte[])byteArray.read(1);
/* 248 */     processor.startIndex = 0;
/*     */     
/* 250 */     if (packet.getBooleans().size() > 0) {
/* 251 */       processor.hasContinuous = ((Boolean)packet.getBooleans().read(0)).booleanValue();
/*     */     }
/* 253 */     return processor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(ChunkletProcessor processor) {
/* 309 */     for (int i = 0; i < 16; i++) {
/* 310 */       if ((this.chunkMask & 1 << i) > 0) {
/* 311 */         this.chunkSectionNumber++;
/*     */       }
/* 313 */       if ((this.extraMask & 1 << i) > 0) {
/* 314 */         this.extraSectionNumber++;
/*     */       }
/*     */     } 
/*     */     
/* 318 */     int skylightCount = getSkylightCount();
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
/* 334 */     this.size = 2048 * ((4 + skylightCount) * this.chunkSectionNumber + this.extraSectionNumber) + (this.hasContinuous ? 256 : 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 340 */     if (getOffset(2) - this.startIndex > this.data.length) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 345 */     if (isChunkLoaded(this.world, this.chunkX, this.chunkZ)) {
/* 346 */       translate(processor);
/*     */     }
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
/*     */ 
/*     */   
/*     */   protected int getSkylightCount() {
/* 361 */     return (this.world.getEnvironment() == World.Environment.NORMAL) ? 1 : 0;
/*     */   }
/*     */   
/*     */   private int getOffset(int nibbles) {
/* 365 */     return this.startIndex + nibbles * this.chunkSectionNumber * 2048;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void translate(ChunkletProcessor processor) {
/* 371 */     int current = 4;
/*     */ 
/*     */ 
/*     */     
/* 375 */     ChunkOffsets offsets = new ChunkOffsets(getOffset(0), getOffset(2), getOffset(3), (getSkylightCount() > 0) ? getOffset(current++) : -1, (this.extraSectionNumber > 0) ? getOffset(current++) : -1);
/*     */     
/* 377 */     for (int i = 0; i < 16; i++) {
/*     */       
/* 379 */       if ((this.chunkMask & 1 << i) > 0) {
/*     */         
/* 381 */         Location origin = new Location(this.world, (this.chunkX << 4), (i * 16), (this.chunkZ << 4));
/*     */ 
/*     */         
/* 384 */         processor.processChunklet(origin, this.data, offsets);
/* 385 */         offsets.incrementIdIndex();
/*     */       } 
/* 387 */       if ((this.extraMask & 1 << i) > 0) {
/* 388 */         offsets.incrementExtraIndex();
/*     */       }
/*     */     } 
/*     */     
/* 392 */     if (this.hasContinuous) {
/* 393 */       processor.processBiomeArray(new Location(this.world, (this.chunkX << 4), 0.0D, (this.chunkZ << 4)), this.data, this.startIndex + this.size - 256);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isChunkLoaded(World world, int x, int z) {
/* 399 */     return world.isChunkLoaded(x, z);
/*     */   }
/*     */ }


/* Location:              D:\GitHub Projects\Anicloud\Bridge\libs\PacketWrapper.jar!\com\comphenix\packetwrapper\ChunkPacketProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */