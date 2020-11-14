package eu.realmcompany.regna.providers.storage.store;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.moandjiezana.toml.Toml;
import eu.realmcompany.regna.providers.storage.data.FriendlyData;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public abstract class AStore {

    @Getter
    protected @NotNull JavaPlugin instance;

    @Getter
    protected File file;
    @Getter
    protected @NotNull String path;

    @Getter
    protected boolean hasDefault;

    /**
     * Default constructor
     *
     * @param instance   Instance to plugin
     * @param path       Relative path to file
     * @param hasDefault Default
     */
    public AStore(@NotNull JavaPlugin instance, @NotNull String path, boolean hasDefault) {
        this.instance = instance;
        this.path = path;
        this.hasDefault = hasDefault;
    }

    /**
     * Makes sure that store is usable.
     *
     * @throws Exception Exception
     */
    public AStore prepare() throws Exception {
        if (!file.exists()) {
            if (hasDefault)
                provideDefault();
        } else
            load();

        return this;
    }

    /**
     * Loads from disk
     */
    public abstract void load() throws Exception;

    /**
     * Creates file
     *
     * @throws Exception Exception
     */
    protected void create() throws Exception {
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    /**
     * Deletes file
     *
     * @throws Exception Exception
     */
    protected void delete() throws Exception {
        if (file.exists())
            file.delete();
    }


    /**
     * Saves on disk
     */
    public abstract void save() throws Exception;

    /**
     * Loads default
     */
    public void provideDefault() throws Exception {
        if (hasDefault) {
            create();
            // write default file
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(instance.getResource(path)), StandardCharsets.UTF_8));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(this.file, StandardCharsets.UTF_8))) {

                String line;
                do {
                    line = reader.readLine();
                    if (line != null)
                        writer.write(line + "\n");
                } while (line != null);
                // load it
                load();
            } catch (Exception x) {
                throw new Exception("Failed to load '" + path + "': " + x.getMessage(), x);
            }
        }
    }

    /**
     * @return Friendly Data
     */
    public abstract @NotNull FriendlyData getData();


    public static @NotNull AStore makeYaml(@NotNull JavaPlugin plugin, @NotNull String path, boolean hasDefault) {
        return new YamlImpl(plugin, path, hasDefault);
    }

    public static @NotNull AStore makeJson(@NotNull JavaPlugin plugin, @NotNull String path, boolean hasDefault) {
        return new JsonImpl(plugin, path, hasDefault);
    }

    public static @NotNull AStore makeToml(@NotNull JavaPlugin plugin, @NotNull String path, boolean hasDefault) {
        return new TomlImpl(plugin, path, hasDefault);
    }


    /**
     * JSON Implementation of store
     */
    private static class JsonImpl extends AStore {

        private FriendlyData data;
        private JsonObject jsonData;

        public JsonImpl(@NotNull JavaPlugin instance, @NotNull String path, boolean hasDefault) {
            super(instance, path, hasDefault);

            this.file = new File(instance.getDataFolder(), path);
        }

        @Override
        public void load() throws Exception {
            try (Reader reader = new FileReader(this.file)) {
                this.jsonData = new JsonParser().parse(reader).getAsJsonObject();
                this.data = FriendlyData.fromJson(this.jsonData);
            } catch (Exception x) {
                throw new Exception("Failed to load '" + path + "': " + x.getMessage(), x);
            }
        }

        @Override
        public void save() throws Exception {
            super.create();

            try (Writer writer = new FileWriter(this.file)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                String serialized = gson.toJson(this.jsonData);
                writer.write(serialized);

            } catch (Exception x) {
                throw new Exception("Failed to load '" + path + "': " + x.getMessage(), x);
            }
        }

        @Override
        public void provideDefault() throws Exception {
            if (hasDefault) {
                try (Reader reader = new InputStreamReader(Objects.requireNonNull(instance.getResource(path)))) {
                    this.jsonData = new JsonParser().parse(reader).getAsJsonObject();
                    this.data = FriendlyData.fromJson(this.jsonData);
                    save();
                } catch (Exception x) {
                    throw new Exception("Failed to load '" + path + "': " + x.getMessage(), x);
                }
            }
        }

        @Override
        public @NotNull FriendlyData getData() {
            return data;
        }
    }

    /**
     * YAML Implementation of store
     */
    private static class YamlImpl extends AStore {

        private FriendlyData data;
        private YamlConfiguration yamlData;

        public YamlImpl(@NotNull JavaPlugin instance, @NotNull String path, boolean hasDefault) {
            super(instance, path, hasDefault);

            this.file = new File(instance.getDataFolder(), path);
        }

        @Override
        public void load() throws Exception {
            try (Reader reader = new FileReader(this.file, StandardCharsets.UTF_8)) {
                this.yamlData = new YamlConfiguration();
                this.yamlData.load(reader);

                this.data = FriendlyData.fromYaml(this.yamlData);
            } catch (Exception x) {
                throw new Exception("Failed to load '" + path + "': " + x.getMessage(), x);
            }
        }

        @Override
        public void save() throws Exception {
            super.create();
            yamlData.save(file);
        }

        @Override
        public @NotNull FriendlyData getData() {
            return data;
        }
    }

    /**
     * TOML Implementation of store
     */
    private static class TomlImpl extends AStore {

        private FriendlyData data;
        private Toml tomlData;

        public TomlImpl(@NotNull JavaPlugin instance, @NotNull String path, boolean hasDefault) {
            super(instance, path, hasDefault);

            this.file = new File(instance.getDataFolder(), path);
        }

        @Override
        public void load() throws Exception {
            try (Reader reader = new FileReader(this.file)) {
                this.tomlData = tomlData.read(reader);
                this.data = FriendlyData.fromToml(this.tomlData);
            } catch (Exception x) {
                throw new Exception("Failed to load '" + path + "': " + x.getMessage(), x);
            }
        }

        @Override
        public void save() throws Exception {
            super.create();

            if (hasDefault) {
                InputStream defaulty = instance.getResource(path);
                if (defaulty == null)
                    throw new Exception("Default resource '" + path + "' not found");
            }
        }

        @Override
        public @NotNull FriendlyData getData() {
            return data;
        }
    }

}
