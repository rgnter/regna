package eu.realmcompany.regna.abstraction.game;

import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.abstraction.AAbstract;
import eu.realmcompany.regna.game.mechanics.RegnaMechanics;
import eu.realmcompany.regna.game.services.RegnaServices;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public abstract class AGameService extends AAbstract {

    @Getter @Setter(AccessLevel.PROTECTED)
    public String serviceName;

    /**
     * Default constructor
     * @param services Service instance
     */
    public AGameService(@NotNull RegnaServices services) {
        super(services.getRegna().getKaryon());
    }
}
