package eu.realmcompany.regna.services.admin.justice.model;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PunishDetail {

    @Getter
    private @NotNull PunishDetail.Type type;
    @Getter
    private @NotNull UUID punished;
    @Getter
    private @NotNull UUID punisher;

    @Getter
    private @NotNull String reason;

    private long created;
    private long expires;



    public enum Type {
        BAN, KICK, MUTE, DARKNESS_EVERYWHERE
    }

}
