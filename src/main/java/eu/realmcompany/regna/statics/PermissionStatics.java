package eu.realmcompany.regna.statics;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PermissionStatics {

    public static @Nullable Permission permissionFromString(@Nullable String permission) {
        if(permission == null)
            return null;
        if(permission.trim().isEmpty())
            return null;
        return Bukkit.getPluginManager().getPermission(permission);
    }

}
