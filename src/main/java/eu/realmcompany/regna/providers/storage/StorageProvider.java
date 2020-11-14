package eu.realmcompany.regna.providers.storage;

import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.providers.storage.store.AStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StorageProvider {

    private final @NotNull RegnaKaryon instance;

    /**
     * Default constructor
     * @param instance Instance of Regna karyon
     */
    public StorageProvider(@NotNull RegnaKaryon instance) {
        this.instance = instance;
    }

    /**
     * Constructs YAML Store, and prepares it
     * @param path       Path to resource
     * @param hasDefault Load default if not available in data folder
     * @return YAML Store
     * @throws Exception When something goes wrong
     */
    public @NotNull AStore provideYaml(@NotNull String path, boolean hasDefault) throws Exception {
        return AStore.makeYaml(this.instance, path, hasDefault).prepare();
    }


    /**
     * Constructs JSON Store, and prepares it
     * @param path       Path to resource
     * @param hasDefault Load default if not available in data folder
     * @return YAML Store
     * @throws Exception When something goes wrong
     */
    public @NotNull AStore provideJson(@NotNull String path, boolean hasDefault) throws Exception {
        return AStore.makeJson(this.instance, path, hasDefault).prepare();
    }


    /**
     * Constructs TOML Store, and prepares it
     * @param path       Path to resource
     * @param hasDefault Load default if not available in data folder
     * @return YAML Store
     * @throws Exception When something goes wrong
     */
    public @NotNull AStore provideToml(@NotNull String path, boolean hasDefault) throws Exception {
        return AStore.makeToml(this.instance, path, hasDefault).prepare();
    }
}
