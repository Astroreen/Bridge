package bridge.utils;

import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfoRemove;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ProtocolLibUtils {

    private final static Set<UUID> uuids = new HashSet<>();

    //skin by name/word: https://ru.namemc.com/
    //search for uuid: https://minecraftuuid.com/
    //https://sessionserver.mojang.com/session/minecraft/profile/{Trimmed uuid}?unsigned=false
    private final static String texture = "ewogICJ0aW1lc3RhbXAiIDogMTY3MTQ3OTc5OTM5NCwKICAicHJvZmlsZUlkIiA6ICI3NDZmZTllY2EyNWM0ZWNlYjAxYTFjNDQxY2ZiZTAzZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJVU0VSX1ZBUEVWNCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yYjc4Zjg2ZDU3YWY0NWUwNjg0MjdjZTc1MGRkYmE3ZjNmZmJkYjJmZTE4MTNmNDE2OTRhODdjZjlhZWZlNzI2IgogICAgfQogIH0KfQ==";
    private final static String signature = "YLuRmmLiuWpXUcRTvhRDez+D6L1XwCSedtq8l46b7pwfNGjcGVAOrCvtjAl2vE1lyR+p8bB04zueULG8ucTt7gatCER/dMyrKmEz0QGRrplhBZtBYB2i+UsdM3iTZWaz6iLK7GpUneh17au2chOZtmQnq6aeG4y/vKV4QjsutWk7X53vBvvtWZotLzWESaIHBil46+pB1u2pJrqMCAcE3wXyzyeeAom4t+txz4U8PJ1CM2d6TBz03JvPn6++WOiYqqLHTwOet8wWTNVJwHrpeR/0ij5as2YYQVeO/ZpmhAjvSXvbtPinvaC2jkhxR1CkzFeFKob9SezVkCGC5129gfa9bDicAHYUMdJgdJHvrOFr4uL3npcgkovTrPn6xVQ6FkvNwBmvpgotKyKKKsuNjvPhiECv6e0vHJeVXaOYTDqWAnB2I0rfVt8RWI+5TOvLPb7qXfd5hx7enI79klDiRRcFda9yl5doutq5UKJUY7cyOdoj7XEyRBjan43/+zHGdjWJc6h/k60PK4PkPFoc1p9XGFOWScxhSy1J0ELOruPLIRP2EZQIHUy2CF2JfyNY5ise9NkYRr3toQ/HxVLqcu6VNGadVlw0hLdrmHD5/RQAmX1RhdXpipEm/ANCIq1JK7YAjSuEFd+jWGXyx205bg/rsvinCV5Bjd7YMxWnGHA=";


    /**
     * Remove players on packet level from TAB and tab-completion
     * @param singleDataList list of {@link PlayerInfoData} where will be all players for deleting
     * @param chunks max UUID's to be put in one list to then send
     * @return {@link PacketContainer}
     */
    @SuppressWarnings("deprecation")
    public static @NotNull List<PacketContainer> removePlayerInfoData(final @NotNull List<PlayerInfoData> singleDataList, final int chunks) {
        //split list into chunks with 20 items in it
        final List<List<PlayerInfoData>> temp = Lists.partition(singleDataList, chunks);

        final List<PacketContainer> packets = new ArrayList<>();
        if(NMS.isYoungerThan(NMS.Version.v1_19_3)) {
            for(final List<PlayerInfoData> data : temp) {
                final PacketContainer container = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
                final WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo(container);

                //setting data
                info.setData(data);
                info.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
                packets.add(container);
            }
        } else {
            for(final List<PlayerInfoData> data : temp) {
                final PacketContainer container = new PacketContainer(PacketType.Play.Server.PLAYER_INFO_REMOVE);
                final WrapperPlayServerPlayerInfoRemove info = new WrapperPlayServerPlayerInfoRemove(container);

                //getting uuids from PlayerInfoData
                final List<UUID> uuids = new ArrayList<>();
                data.forEach(d -> uuids.add(d.getProfileId()));

                //setting data
                info.setUUIDList(uuids);
                packets.add(container);
            }

        }
        return packets;
    }

    public static @NotNull List<PacketContainer> fullTabPacket(final int chunks) {
        if(uuids.isEmpty()) {
            for (int i = 0; i <= 80; i++) uuids.add(UUID.randomUUID());
        }
        //creating packet
        final List<PlayerInfoData> data = new ArrayList<>();
        for (final @NotNull UUID uuid : uuids) {
            final GameProfile profile = new GameProfile(uuid, " ");
            profile.getProperties().put("textures", ProtocolLibUtils.getUnknownProperty());
            final WrappedGameProfile prof = WrappedGameProfile.fromHandle(profile);
            data.add(new PlayerInfoData(prof, 10, EnumWrappers.NativeGameMode.SPECTATOR, WrappedChatComponent.fromText(" ")));
        }
        //split list into chunks with 20 items in it
        final List<List<PlayerInfoData>> temp = Lists.partition(data, chunks);
        final List<PacketContainer> packets = new ArrayList<>();
        for (final @NotNull List<PlayerInfoData> list : temp) {
            //creating packet
            final PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
            final WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo(packet);
            //setting data;
            info.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
            info.setData(list);
            packets.add(packet);
        }
        return packets;
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Property getUnknownProperty() {
        return new Property("textures", texture, signature);
    }
}
