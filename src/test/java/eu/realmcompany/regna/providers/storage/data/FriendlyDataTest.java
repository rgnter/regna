package eu.realmcompany.regna.providers.storage.data;

import com.google.gson.JsonParser;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FriendlyDataTest {

    @Test
    void testJsonData() {
        FriendlyData json = FriendlyData.fromJson(new JsonParser().parse("{\n" +
                "   \"foo\":\"bar\",\n" +
                "   \"numbers\":{\n" +
                "      \"byte_foo\":1,\n" +
                "      \"short_foo\":1,\n" +
                "      \"int_foo\":1,\n" +
                "      \"long_foo\":1,\n" +
                "      \"float_foo\":1.5,\n" +
                "      \"double_foo\":1e+5,\n" +
                "      \"bool_foo\":true\n" +
                "   },\n" +
                "   \"list_foo\":[\n" +
                "      \"bar\"\n" +
                "   ]\n" +
                "}").getAsJsonObject());
        json.setInt("numbers.int_foo", 2);
        json.setBool("tests.bool", false);

        System.out.println(json.getKeys("numbers"));

        testData(json);
    }

    @Test
    void testYamlData() throws InvalidConfigurationException {
        YamlConfiguration yamlData = new YamlConfiguration();
        yamlData.loadFromString(
                "foo: bar\n" +
                        "numbers: \n" +
                        "  byte_foo: 1\n" +
                        "  double_foo: 1e5\n" +
                        "  float_foo: 1.5\n" +
                        "  int_foo: 1\n" +
                        "  long_foo: 1\n" +
                        "  short_foo: 1\n" +
                        "  bool_foo: true\n" +
                        "list_foo: \n" +
                        "  - bar\n");
        FriendlyData yaml = FriendlyData.fromYaml(yamlData);
        yaml.setInt("numbers.int_foo", 2);
        yaml.setBool("tests.bool", false);
        testData(yaml);
    }


    void testData(@NotNull FriendlyData data) {
        Assertions.assertEquals("bar", data.getString("foo"));
        Assertions.assertEquals((byte) 1, data.getByte("numbers.byte_foo"));
        Assertions.assertEquals((short) 1, data.getShort("numbers.short_foo"));
        Assertions.assertEquals(2, data.getInt("numbers.int_foo"));
        Assertions.assertEquals(1, data.getLong("numbers.long_foo"));
        Assertions.assertEquals(1.5f, data.getFloat("numbers.float_foo"));
        Assertions.assertEquals(1e+5d, data.getDouble("numbers.double_foo"));
        Assertions.assertEquals("bar", data.getStringList("list_foo").get(0));

        Assertions.assertTrue(data.getBool("numbers.bool_foo", false));
        Assertions.assertTrue(data.getBoolOpt("numbers.bool_foo").isPresent());
        Assertions.assertFalse(data.getBool("tests.bool", true));
        Assertions.assertFalse(data.getBoolOpt("numbers.notexistingelement").isPresent());

        Assertions.assertTrue(data.getKeys("numbers").contains("byte_foo"));
    }

}