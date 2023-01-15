package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

import java.util.List;
import java.util.UUID;

public class WrapperPlayServerPlayerInfoRemove extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.PLAYER_INFO_REMOVE;

    public WrapperPlayServerPlayerInfoRemove() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerPlayerInfoRemove(final PacketContainer packet) {
        super(packet, TYPE);
    }

    public void setUUIDList(final List<UUID> uuids){
        this.handle.getUUIDLists().write(0, uuids);
    }

    public List<UUID> getUUIDList () {
        return this.handle.getUUIDLists().read(0);
    }

}
