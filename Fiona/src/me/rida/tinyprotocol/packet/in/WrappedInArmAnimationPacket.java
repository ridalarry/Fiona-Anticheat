package com.ngxdev.tinyprotocol.packet.in;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.data.PlayerData;
import com.ngxdev.tinyprotocol.api.NMSObject;
import com.ngxdev.tinyprotocol.api.ProtocolVersion;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
public class WrappedInArmAnimationPacket extends NMSObject {
    private static final String packet = Client.ARM_ANIMATION;
    private boolean punchingBlock;

    public WrappedInArmAnimationPacket(Object object, Player player) {
        super(object, player);
    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        PlayerData data = AntiCheat.getInstance().getDataManager().getPlayerData(player);

        punchingBlock = data != null && (data.breakingBlock || player.getItemInHand().getType().equals(Material.FISHING_ROD));
    }
}
