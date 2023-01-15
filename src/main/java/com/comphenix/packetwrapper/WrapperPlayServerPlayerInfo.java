//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;

import java.util.EnumSet;
import java.util.List;

public class WrapperPlayServerPlayerInfo extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.PLAYER_INFO;

    public WrapperPlayServerPlayerInfo() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerPlayerInfo(PacketContainer packet) {
        super(packet, TYPE);
    }

    public EnumWrappers.PlayerInfoAction getAction() {
        return (EnumWrappers.PlayerInfoAction)this.handle.getPlayerInfoAction().read(0);
    }

    public void setAction(EnumWrappers.PlayerInfoAction value) {
        this.handle.getPlayerInfoActions().write(0, EnumSet.of(value));
    }

    public List<PlayerInfoData> getData() {
        return (List<PlayerInfoData>) this.handle.getPlayerInfoDataLists().read(1);
    }

    public void setData(List<PlayerInfoData> value) {
        this.handle.getPlayerInfoDataLists().write(1, value);
    }
}
