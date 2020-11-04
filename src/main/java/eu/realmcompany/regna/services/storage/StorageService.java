package eu.realmcompany.regna.services.storage;

import eu.realmcompany.regna.abstraction.AService;
import eu.realmcompany.regna.abstraction.Service;
import eu.realmcompany.regna.services.RegnaServices;
import org.jetbrains.annotations.NotNull;

@Service("Storage")
public class StorageService extends AService {

    public StorageService(@NotNull RegnaServices services) {
        super(services);
    }

    @Override
    public void initialize() throws Exception {

    }

    @Override
    public void terminate() throws Exception {

    }


    public void flatfile() {

    }

    public void database() {

    }
}
