package eu.realmcompany.regna.services.chat;

import eu.realmcompany.regna.abstraction.AService;
import eu.realmcompany.regna.services.RegnaServices;
import org.jetbrains.annotations.NotNull;

public class ChatService extends AService {

    /**
     * Super constructor
     * @param services Services instance
     */
    public ChatService(@NotNull RegnaServices services) {
        super(services);
    }

    @Override
    public void initialize() throws Exception {
        this.getLogger().info("wee");
    }

    @Override
    public void terminate() throws Exception {

    }
}
