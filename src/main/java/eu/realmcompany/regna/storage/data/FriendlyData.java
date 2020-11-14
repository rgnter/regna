package eu.realmcompany.regna.storage.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.moandjiezana.toml.Toml;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Provides functionality to easily work with different types of storage formats
 */
public abstract class FriendlyData {

    /**
     * Creates empty FriendlyData from JSON source.
     *
     * @return FriendlyData
     */
    public static @NotNull FriendlyData fromEmptyJson() {
        return new JsonImpl(new JsonObject());
    }

    /**
     * Creates empty FriendlyData from YAML source.
     *
     * @return FriendlyData
     */
    public static @NotNull FriendlyData fromEmptyYaml() {
        return new YamlImpl(new YamlConfiguration());
    }

    /**
     * Creates empty FriendlyData from TOML source.
     *
     * @return FriendlyData
     */
    public static @NotNull FriendlyData fromEmptyToml() {
        return new TomlImpl(new Toml());
    }

    /**
     * Creates FriendlyData from JSON source.
     *
     * @param source JSON source
     * @return FriendlyData
     */
    public static @NotNull FriendlyData fromJson(@NotNull JsonObject source) {
        return new JsonImpl(source);
    }

    /**
     * Creates FriendlyData from JSON source.
     *
     * @param source YAML source
     * @return FriendlyData
     */
    public static @NotNull FriendlyData fromYaml(@NotNull YamlConfiguration source) {
        return new YamlImpl(source);
    }

    /**
     * Creates FriendlyData from JSON source.
     *
     * @param source TOML source
     * @return FriendlyData
     */
    public static @NotNull FriendlyData fromToml(@NotNull Toml source) {
        return new TomlImpl(source);
    }

    /**
     * Gets Object value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Child value as Object. <br> If path is invalid returns null. <br> If child value is not convertible to Object, returns null.
     */
    public abstract @Nullable Object get(@NotNull String path);

    /**
     * Gets Object value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @param def  Default value
     * @return Child value as Object. <br> If path is invalid returns default value. <br> If child value is not convertible to Object, returns default value.
     */
    public abstract @NotNull Object get(@NotNull String path, @NotNull Object def);

    /**
     * Gets Object value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Optional child value as Object. <br> If path is invalid returns Optional with null. <br> If child value is not convertible to Object, returns Optional with null.
     */
    public @Nullable Optional<Object> getOpt(@NotNull String path) {
        return Optional.ofNullable(get(path));
    }

    /**
     * Sets Object value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     */
    public abstract void set(@NotNull String path, @Nullable Object value);

    /**
     * Gets String value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Child value as String. <br> If path is invalid returns null. <br> If child value is not convertible to String, returns null.
     */
    public abstract @Nullable String getString(@NotNull String path);

    /**
     * Gets String value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Optional child value as String. <br> If path is invalid returns Optional with null value. <br> If child value is not convertible to String, returns Optional with null value.
     */
    public @NotNull Optional<String> getStringOpt(@NotNull String path) {
        return Optional.ofNullable(this.getString(path));
    }

    /**
     * Gets String value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @param def  Default value
     * @return Child value as String. <br> If path is invalid returns default value. <br> If child value is not convertible to String, returns default value.
     */
    public abstract @NotNull String getString(@NotNull String path, @NotNull String def);

    /**
     * Sets String value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     */
    public abstract void setString(@NotNull String path, @Nullable String value);

    /**
     * Gets Byte value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Child value as Byte. <br> If path is invalid returns null. <br> If child value is not convertible to Byte, returns null.
     */
    public abstract @Nullable Byte getByte(@NotNull String path);

    /**
     * Gets Byte value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @param def  Default value
     * @return Child value as Byte. <br> If path is invalid returns default value. <br> If child value is not convertible to Byte, returns default value.
     */
    public abstract @NotNull Byte getByte(@NotNull String path, @NotNull Byte def);


    /**
     * Gets Byte value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Optional child value as Byte. <br> If path is invalid returns Optional with null value. <br> If child value is not convertible to Byte, returns Optional with null value.
     */
    public @NotNull Optional<Byte> getByteOpt(@NotNull String path) {
        return Optional.ofNullable(this.getByte(path));
    }


    /**
     * Sets Byte value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     */
    public abstract void setByte(@NotNull String path, @Nullable Byte value);

    /**
     * Gets Short value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Child value as Short. <br> If path is invalid returns null. <br> If child value is not convertible to Short, returns null.
     */
    public abstract @Nullable Short getShort(@NotNull String path);

    /**
     * Gets Short value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @param def  Default value
     * @return Child value as Short. <br> If path is invalid returns default value. <br> If child value is not convertible to Short, returns default value.
     */
    public abstract @NotNull Short getShort(@NotNull String path, @NotNull Short def);

    /**
     * Gets Short value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Optional child value as Short. <br> If path is invalid returns Optional with null value. <br> If child value is not convertible to Short, returns Optional with null value.
     */
    public @NotNull Optional<Short> getShortOpt(@NotNull String path) {
        return Optional.ofNullable(this.getShort(path));
    }

    /**
     * Sets Short value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     */
    public abstract void setShort(@NotNull String path, @Nullable Short value);

    /**
     * Gets Integer value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Child value as Integer. <br> If path is invalid returns null. <br> If child value is not convertible to Integer, returns null.
     */
    public abstract @Nullable Integer getInt(@NotNull String path);

    /**
     * Gets Integer value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Optional child value as Integer. <br> If path is invalid returns Optional with null value. <br> If child value is not convertible to Integer, returns Optional with null value.
     */
    public @NotNull Optional<Integer> getIntOpt(@NotNull String path) {
        return Optional.ofNullable(this.getInt(path));
    }

    /**
     * Gets Integer value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @param def  Default value
     * @return Child value as Integer. <br> If path is invalid returns default value. <br> If child value is not convertible to Integer, returns default value.
     */
    public abstract @NotNull Integer getInt(@NotNull String path, @NotNull Integer def);

    /**
     * Sets Integer value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     */
    public abstract void setInt(@NotNull String path, @Nullable Integer value);

    /**
     * Gets Integer value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Child value as Integer. <br> If path is invalid returns null. <br> If child value is not convertible to Integer, returns null.
     */
    public abstract @Nullable Long getLong(@NotNull String path);

    /**
     * Gets Long value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Optional child value as Long. <br> If path is invalid returns Optional with null value. <br> If child value is not convertible to Long, returns Optional with null value.
     */
    public @NotNull Optional<Long> getLongOpt(@NotNull String path) {
        return Optional.ofNullable(this.getLong(path));
    }

    /**
     * Gets Integer value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @param def  Default value
     * @return Child value as Integer. <br> If path is invalid returns default value. <br> If child value is not convertible to Integer, returns default value.
     */
    public abstract @NotNull Long getLong(@NotNull String path, @NotNull Long def);

    /**
     * Sets Long value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     */
    public abstract void setLong(@NotNull String path, @Nullable Long value);

    /**
     * Gets Float value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Child value as Float. <br> If path is invalid returns null. <br> If child value is not convertible to Float, returns null.
     */
    public abstract @Nullable Float getFloat(@NotNull String path);

    /**
     * Gets Float value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Optional child value as Float. <br> If path is invalid returns Optional with null value. <br> If child value is not convertible to Float, returns Optional with null value.
     */
    public @NotNull Optional<Float> getFloatOpt(@NotNull String path) {
        return Optional.ofNullable(this.getFloat(path));
    }

    /**
     * Gets Float value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @param def  Default value
     * @return Child value as Float. <br> If path is invalid returns default value. <br> If child value is not convertible to Float, returns default value.
     */
    public abstract @NotNull Float getFloat(@NotNull String path, @NotNull Float def);

    /**
     * Sets Float value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     */
    public abstract void setFloat(@NotNull String path, @Nullable Float value);

    /**
     * Gets Double value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Child value as Double. <br> If path is invalid returns null. <br> If child value is not convertible to Double, returns null.
     */
    public abstract @Nullable Double getDouble(@NotNull String path);

    /**
     * Gets Double value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Optional child value as Double. <br> If path is invalid returns Optional with null value. <br> If child value is not convertible to Double, returns Optional with null value.
     */
    public @NotNull Optional<Double> getDoubleOpt(@NotNull String path) {
        return Optional.ofNullable(this.getDouble(path));
    }

    /**
     * Gets Double value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @param def  Default value
     * @return Child value as Double. <br> If path is invalid returns default value. <br> If child value is not convertible to Double, returns default value.
     */
    public abstract @NotNull Double getDouble(@NotNull String path, @NotNull Double def);

    /**
     * Sets Double value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     */
    public abstract void setDouble(@NotNull String path, @Nullable Double value);

    /**
     * Gets Boolean value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Child value as Boolean. <br> If path is invalid returns null. <br> If child value is not convertible to Boolean, returns null.
     */
    public abstract @Nullable Boolean getBool(@NotNull String path);

    /**
     * Gets Boolean value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Optional child value as Boolean. <br> If path is invalid returns Optional with null value. <br> If child value is not convertible to Boolean, returns Optional with null value.
     */
    public @NotNull Optional<Boolean> getBoolOpt(@NotNull String path) {
        return Optional.ofNullable(this.getBool(path));
    }

    /**
     * Gets Boolean value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @param def  Default value
     * @return Child value as Boolean. <br> If path is invalid returns default value. <br> If child value is not convertible to Boolean, returns default value.
     */
    public abstract @NotNull Boolean getBool(@NotNull String path, @NotNull Boolean def);

    /**
     * Sets Boolean value.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     */
    public abstract void setBool(@NotNull String path, @Nullable Boolean value);

    /**
     * Gets String list.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Child value as String list. <br> If path is invalid returns null. <br> If child value is not convertible to Double, returns null.
     */
    public abstract @Nullable List<String> getStringList(@NotNull String path);

    /**
     * Gets String list.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @param def  Default value
     * @return Child value as String list. <br> If path is invalid returns default value. <br> If child value is not convertible to Double, returns default value.
     */
    public abstract @NotNull List<String> getStringList(@NotNull String path, @NotNull List<String> def);

    /**
     * Gets String list.
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @param def  Default value
     * @return Child value as String list. <br> If path is invalid returns default value. <br> If child value is not convertible to Double, returns default value.
     */
    public abstract @NotNull List<String> getStringList(@NotNull String path, @NotNull String... def);

    /**
     * Sets String list.
     *
     * @param path  Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @param value Value
     */
    public abstract void setStringList(@NotNull String path, @Nullable List<String> value);

    /**
     * Gets all children of specified parent path.
     *
     * @param path Path to parent
     * @return List of all children. <br> If Parent path is invalid returns null. If Parent is not a child, returns null.
     */
    public abstract @NotNull Set<String> getKeys(@NotNull String path);

    /**
     * Checks if child is set
     *
     * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
     * @return Boolean true if is set, else false.
     */
    public abstract boolean isSet(@NotNull String path);


    public abstract @NotNull String toString();

    /**
     * Implements FriendlyData for JSON
     */
    private static class JsonImpl extends FriendlyData {

        protected final JsonObject jsonData;

        private JsonImpl(@NotNull JsonObject jsonData) {
            this.jsonData = jsonData;
        }

        /***
         *
         * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
         * @return JsonElement, null if something fucked up.
         */
        public @Nullable JsonElement getJsonElement(@NotNull String path) {
            String[] members = path.split("\\.");
            if (path.endsWith(".") && path.trim().isEmpty())
                throw new IllegalArgumentException("Path incomplete!");

            JsonElement result = null;
            List<String> memberList = Arrays.asList(members);
            Iterator<String> iterator = memberList.iterator();

            // get first parent
            if (iterator.hasNext())
                result = jsonData.get(iterator.next());


            while (iterator.hasNext()) {
                if (result == null)
                    return null;
                String member = iterator.next();
                result = result.getAsJsonObject().get(member);

                // if is last, return result
                if (!iterator.hasNext())
                    return result;
            }
            return result;

        }


        /***
         *
         * @param path Path to child. Path is delimited with dots('.'). <br>Example: <code>parent0.parent1.child</code>
         */
        public void setJsonElement(@NotNull String path, @Nullable JsonElement value) {
            if (value == null)
                value = new JsonPrimitive("null");

            String[] members = path.split("\\.");
            if (path.endsWith(".") && path.trim().isEmpty())
                throw new IllegalArgumentException("Path incomplete!");


            List<String> memberList = Arrays.asList(members);
            Iterator<String> iterator = memberList.iterator();

            JsonObject lastParent = this.jsonData;
            while (iterator.hasNext()) {
                String member = iterator.next();

                if (!iterator.hasNext()) {
                    lastParent.add(member, value);
                } else {
                    JsonObject parent = new JsonObject();
                    if (lastParent.has(member))
                        parent = lastParent.get(member).getAsJsonObject();

                    lastParent.add(member, parent);
                    lastParent = parent;
                }
            }
        }


        @Override
        public @Nullable Object get(@NotNull String path) {
            var result = getJsonElement(path);
            var val = "null";
            if (result == null)
                val = null;
            else
                val = result.toString();
            return val;
        }

        @Override
        public @NotNull Object get(@NotNull String path, @NotNull Object def) {
            var result = get(path);
            if (result == null)
                result = def;
            return result;
        }

        @Override
        public void set(@NotNull String path, @Nullable Object value) {
            var val = value != null ? new JsonPrimitive(value.toString()) : null;
            setJsonElement(path, val);
        }

        @Override
        public @Nullable String getString(@NotNull String path) {
            JsonElement result = getJsonElement(path);
            return result != null ? result.getAsString() : null;
        }

        @Override
        public @NotNull String getString(@NotNull String path, @NotNull String def) {
            var result = getString(path);
            return result != null ? result : def;
        }

        @Override
        public void setString(@NotNull String path, @Nullable String value) {
            var val = value != null ? new JsonPrimitive(value) : new JsonPrimitive("null");
            setJsonElement(path, val);
        }

        @Override
        public @Nullable Byte getByte(@NotNull String path) {
            JsonElement result = getJsonElement(path);
            return result != null ? result.getAsByte() : null;
        }

        @Override
        public @NotNull Byte getByte(@NotNull String path, @NotNull Byte def) {
            var result = getByte(path);
            return result != null ? result : def;
        }

        @Override
        public void setByte(@NotNull String path, @Nullable Byte value) {
            var val = value != null ? new JsonPrimitive(value) : new JsonPrimitive("null");
            setJsonElement(path, val);
        }

        @Override
        public @Nullable Short getShort(@NotNull String path) {
            JsonElement result = getJsonElement(path);
            return result != null ? result.getAsShort() : null;
        }

        @Override
        public @NotNull Short getShort(@NotNull String path, @NotNull Short def) {
            var result = getShort(path);
            return result != null ? result : def;
        }

        @Override
        public void setShort(@NotNull String path, @Nullable Short value) {
            var val = value != null ? new JsonPrimitive(value) : new JsonPrimitive("null");
            setJsonElement(path, val);
        }

        @Override
        public @Nullable Integer getInt(@NotNull String path) {
            JsonElement result = getJsonElement(path);
            return result != null ? result.getAsInt() : null;
        }

        @Override
        public @NotNull Integer getInt(@NotNull String path, @NotNull Integer def) {
            var result = getInt(path);
            return result != null ? result : def;
        }

        @Override
        public void setInt(@NotNull String path, @Nullable Integer value) {
            var val = value != null ? new JsonPrimitive(value) : new JsonPrimitive("null");
            setJsonElement(path, val);
        }

        @Override
        public @Nullable Long getLong(@NotNull String path) {
            JsonElement result = getJsonElement(path);
            return result != null ? result.getAsLong() : null;
        }

        @Override
        public @NotNull Long getLong(@NotNull String path, @NotNull Long def) {
            var result = getLong(path);
            return result != null ? result : def;
        }

        @Override
        public void setLong(@NotNull String path, @Nullable Long value) {
            var val = value != null ? new JsonPrimitive(value) : new JsonPrimitive("null");
            setJsonElement(path, val);
        }

        @Override
        public @Nullable Float getFloat(@NotNull String path) {
            JsonElement result = getJsonElement(path);
            return result != null ? result.getAsFloat() : null;
        }

        @Override
        public @NotNull Float getFloat(@NotNull String path, @NotNull Float def) {
            var result = getFloat(path);
            return result != null ? result : def;
        }

        @Override
        public void setFloat(@NotNull String path, @Nullable Float value) {
            var val = value != null ? new JsonPrimitive(value) : new JsonPrimitive("null");
            setJsonElement(path, val);
        }

        @Override
        public @Nullable Double getDouble(@NotNull String path) {
            JsonElement result = getJsonElement(path);
            return result != null ? result.getAsDouble() : null;
        }

        @Override
        public @NotNull Double getDouble(@NotNull String path, @NotNull Double def) {
            var result = getDouble(path);
            return result != null ? result : def;
        }

        @Override
        public void setDouble(@NotNull String path, @Nullable Double value) {
            var val = value != null ? new JsonPrimitive(value) : new JsonPrimitive("null");
            setJsonElement(path, val);
        }

        @Override
        public @Nullable Boolean getBool(@NotNull String path) {
            JsonElement result = getJsonElement(path);
            return result != null ? result.getAsBoolean() : null;
        }

        @Override
        public @NotNull Boolean getBool(@NotNull String path, @NotNull Boolean def) {
            var result = getBool(path);
            return result != null ? result : def;
        }

        @Override
        public void setBool(@NotNull String path, @Nullable Boolean value) {
            var val = value != null ? new JsonPrimitive(value) : new JsonPrimitive("null");
            setJsonElement(path, val);
        }

        @Override
        public @Nullable List<String> getStringList(@NotNull String path) {
            JsonElement element = getJsonElement(path);
            if (element == null)
                return null;
            JsonArray array = null;
            try {
                array = element.getAsJsonArray();
            } catch (Exception x) {
                throw new IllegalArgumentException(x);
            }
            if (array == null)
                return null;

            List<String> result = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                result.add(i, array.get(i).getAsString());
            }

            return result.size() == 0 ? null : result;
        }

        @Override
        public @NotNull List<String> getStringList(@NotNull String path, @NotNull List<String> def) {
            var result = getStringList(path);
            return result != null ? result : def;
        }

        @Override
        public @NotNull List<String> getStringList(@NotNull String path, @NotNull String... def) {
            return getStringList(path, Arrays.asList(def));
        }

        @Override
        public void setStringList(@NotNull String path, @Nullable List<String> value) {
            var val = value != null ? value.toString() : "[null]";
            setJsonElement(path, new JsonPrimitive(val));
        }

        @Override
        public @NotNull Set<String> getKeys(@NotNull String path) {
            if (path.isEmpty())
                return null;

            JsonElement element = getJsonElement(path);
            if (element == null)
                return null;
            JsonObject section = element.getAsJsonObject();
            if (section == null)
                return null;

            Set<String> keys = new HashSet<>();
            for (Map.Entry<String, JsonElement> entry : section.entrySet()) {
                keys.add(entry.getKey());
            }
            return keys;
        }

        @Override
        public boolean isSet(@NotNull String path) {
            return getJsonElement(path) != null;
        }

        @Override
        public @NotNull String
        toString() {
            return jsonData.toString();
        }
    }

    /**
     * Implements FriendlyData for YAML
     */
    private static class YamlImpl extends FriendlyData {

        protected final YamlConfiguration yamlData;

        public YamlImpl(@NotNull YamlConfiguration yamlData) {
            this.yamlData = yamlData;
        }

        @Override
        public @Nullable Object get(@NotNull String path) {
            return yamlData.get(path);
        }

        @Override
        public @NotNull Object get(@NotNull String path, @NotNull Object def) {
            return yamlData.get(path, def);
        }

        @Override
        public void set(@NotNull String path, @Nullable Object value) {
            var val = value == null ? "null" : value;
            yamlData.set(path, val);
        }

        @Override

        public @Nullable String getString(@NotNull String path) {
            if (yamlData.isSet(path))
                return yamlData.getString(path);
            return null;
        }

        @Override
        public @NotNull String getString(@NotNull String path, @NotNull String def) {
            var result = getString(path);
            return result == null ? def : result;
        }

        @Override
        public void setString(@NotNull String path, @Nullable String value) {
            set(path, value);
        }

        @Override
        public @Nullable Byte getByte(@NotNull String path) {
            if (yamlData.isSet(path))
                return (byte) yamlData.getInt(path);
            return null;
        }

        @Override
        public @NotNull Byte getByte(@NotNull String path, @NotNull Byte def) {
            var result = getByte(path);
            return result == null ? def : result;
        }

        @Override
        public void setByte(@NotNull String path, @Nullable Byte value) {
            set(path, value);
        }

        @Override
        public @Nullable Short getShort(@NotNull String path) {
            if (yamlData.isSet(path))
                return (short) yamlData.getInt(path);
            return null;
        }

        @Override
        public @NotNull Short getShort(@NotNull String path, @NotNull Short def) {
            var result = getShort(path);
            return result == null ? def : result;
        }

        @Override
        public void setShort(@NotNull String path, @Nullable Short value) {
            set(path, value);
        }

        @Override
        public @Nullable Integer getInt(@NotNull String path) {
            if (yamlData.isSet(path))
                return yamlData.getInt(path);
            return null;
        }

        @Override
        public @NotNull Integer getInt(@NotNull String path, @NotNull Integer def) {
            var result = getInt(path);
            return result == null ? def : result;
        }

        @Override
        public void setInt(@NotNull String path, @Nullable Integer value) {
            set(path, value);
        }

        @Override
        public @Nullable Long getLong(@NotNull String path) {
            if (yamlData.isSet(path))
                return yamlData.getLong(path);
            return null;
        }

        @Override
        public @NotNull Long getLong(@NotNull String path, @NotNull Long def) {
            var result = getLong(path);
            return result == null ? def : result;
        }

        @Override
        public void setLong(@NotNull String path, @Nullable Long value) {
            set(path, value);
        }

        @Override
        public @Nullable Float getFloat(@NotNull String path) {
            if (yamlData.isSet(path))
                return (float) yamlData.getDouble(path);
            return null;
        }

        @Override
        public @NotNull Float getFloat(@NotNull String path, @NotNull Float def) {
            var result = getFloat(path);
            return result == null ? def : result;
        }

        @Override
        public void setFloat(@NotNull String path, @Nullable Float value) {
            set(path, value);
        }

        @Override
        public @Nullable Double getDouble(@NotNull String path) {
            if (yamlData.isSet(path))
                return yamlData.getDouble(path);
            return null;
        }

        @Override
        public @NotNull Double getDouble(@NotNull String path, @NotNull Double def) {
            var result = getDouble(path);
            return result == null ? def : result;
        }

        @Override
        public void setDouble(@NotNull String path, @Nullable Double value) {
            set(path, value);
        }

        @Override
        public @Nullable List<String> getStringList(@NotNull String path) {
            if (yamlData.isSet(path))
                return yamlData.getStringList(path);
            return null;
        }

        @Override
        public @NotNull List<String> getStringList(@NotNull String path, @NotNull List<String> def) {
            List<String> result = getStringList(path);
            return result == null ? def : result;
        }

        @Override
        public @NotNull List<String> getStringList(@NotNull String path, @NotNull String... def) {
            return getStringList(path, Arrays.asList(def));
        }

        @Override
        public void setStringList(@NotNull String path, @Nullable List<String> value) {
            set(path, value);
        }

        @Override
        public @Nullable Boolean getBool(@NotNull String path) {
            if (yamlData.isSet(path))
                return yamlData.getBoolean(path);
            return null;
        }

        @Override
        public @NotNull Boolean getBool(@NotNull String path, @NotNull Boolean def) {
            var result = getBool(path);
            return result != null ? result : def;
        }

        @Override
        public void setBool(@NotNull String path, @Nullable Boolean value) {
            set(path, value);
        }

        @Override
        public @NotNull Set<String> getKeys(@NotNull String path) {
            ConfigurationSection section = yamlData.getConfigurationSection(path);
            if (section == null)
                return Collections.emptySet();

            return section.getKeys(false);
        }

        @Override
        public boolean isSet(@NotNull String path) {
            return yamlData.isSet(path);
        }

        @Override
        public @NotNull String
        toString() {
            return yamlData.saveToString();
        }

    }

    /**
     * todo implement toml
     */
    private static class TomlImpl extends FriendlyData {

        private final Toml tomlData;

        public TomlImpl(@NotNull Toml tomlData) {
            this.tomlData = tomlData;
        }

        @Override
        public @Nullable Object get(@NotNull String path) {
            return tomlData.getString(path);
        }

        @Override
        public @NotNull Object get(@NotNull String path, @NotNull Object def) {
            var result = tomlData.getString(path);
            return result != null ? result : def;
        }

        @Override
        public void set(@NotNull String path, @Nullable Object value) {
            throw new NotImplementedException("TOML Doesn't support set... yet.");
        }

        @Override
        public @Nullable String getString(@NotNull String path) {
            return tomlData.getString(path);
        }

        @Override
        public @NotNull String getString(@NotNull String path, @NotNull String def) {
            var result = tomlData.getString(path);
            return result != null ? result : def;
        }

        @Override
        public void setString(@NotNull String path, @Nullable String value) {
            set(path, value);
        }

        @Override
        public @Nullable Byte getByte(@NotNull String path) {
            var result = tomlData.getLong(path);
            return result != null ? result.byteValue() : null;
        }

        @Override
        public @NotNull Byte getByte(@NotNull String path, @NotNull Byte def) {
            var result = tomlData.getLong(path);
            return result != null ? result.byteValue() : def;
        }

        @Override
        public void setByte(@NotNull String path, @Nullable Byte value) {
            set(path, value);
        }

        @Override
        public @Nullable Short getShort(@NotNull String path) {
            var result = tomlData.getLong(path);
            return result != null ? result.shortValue() : null;
        }

        @Override
        public @NotNull Short getShort(@NotNull String path, @NotNull Short def) {
            var result = tomlData.getLong(path);
            return result != null ? result.shortValue() : def;
        }

        @Override
        public void setShort(@NotNull String path, @Nullable Short value) {
            set(path, value);
        }

        @Override
        public @Nullable Integer getInt(@NotNull String path) {
            var result = tomlData.getLong(path);
            return result != null ? result.intValue() : null;
        }

        @Override
        public @NotNull Integer getInt(@NotNull String path, @NotNull Integer def) {
            var result = tomlData.getLong(path);
            return result != null ? result.intValue() : def;
        }

        @Override
        public void setInt(@NotNull String path, @Nullable Integer value) {
            set(path, value);
        }

        @Override
        public @Nullable Long getLong(@NotNull String path) {
            return tomlData.getLong(path);
        }

        @Override
        public @NotNull Long getLong(@NotNull String path, @NotNull Long def) {
            var result = tomlData.getLong(path);
            return result != null ? result : def;
        }

        @Override
        public void setLong(@NotNull String path, @Nullable Long value) {
            set(path, value);
        }

        @Override
        public @Nullable Float getFloat(@NotNull String path) {
            var result = tomlData.getDouble(path);
            return result != null ? result.floatValue() : null;
        }

        @Override
        public @NotNull Float getFloat(@NotNull String path, @NotNull Float def) {
            var result = tomlData.getDouble(path);
            return result != null ? result.floatValue() : def;
        }

        @Override
        public void setFloat(@NotNull String path, @Nullable Float value) {
            set(path, value);
        }

        @Override
        public @Nullable Double getDouble(@NotNull String path) {
            return tomlData.getDouble(path);
        }

        @Override
        public @NotNull Double getDouble(@NotNull String path, @NotNull Double def) {
            var result = tomlData.getDouble(path);
            return result != null ? result : def;
        }

        @Override
        public void setDouble(@NotNull String path, @Nullable Double value) {
            set(path, value);
        }

        @Override
        public @Nullable Boolean getBool(@NotNull String path) {
            return tomlData.getBoolean(path);
        }

        @Override
        public @NotNull Boolean getBool(@NotNull String path, @NotNull Boolean def) {
            var result = tomlData.getBoolean(path);
            return result != null ? result : def;
        }

        @Override
        public void setBool(@NotNull String path, @Nullable Boolean value) {
            set(path, value);
        }

        @Override
        public @Nullable List<String> getStringList(@NotNull String path) {
            return tomlData.getList(path);
        }

        @Override
        public @NotNull List<String> getStringList(@NotNull String path, @NotNull List<String> def) {
            return tomlData.getList(path, def);
        }

        @Override
        public @NotNull List<String> getStringList(@NotNull String path, @NotNull String... def) {
            return getStringList(path, Arrays.asList(def));
        }

        @Override
        public void setStringList(@NotNull String path, @Nullable List<String> value) {
            set(path, value);
        }

        @Override
        public @NotNull Set<String> getKeys(@NotNull String path) {
            Toml table = tomlData.getTable(path);
            if (table != null)
                return table.toMap().keySet();
            return Collections.emptySet();
        }

        @Override
        public boolean isSet(@NotNull String path) {
            return tomlData.contains(path);
        }

        @Override
        public @NotNull String
        toString() {
            return tomlData.toString();
        }
    }

}
