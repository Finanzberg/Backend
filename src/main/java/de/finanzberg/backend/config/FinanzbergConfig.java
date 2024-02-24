package de.finanzberg.backend.config;

import de.finanzberg.backend.util.CipherUtils;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@SuppressWarnings("FieldMayBeFinal")
@ConfigSerializable
public class FinanzbergConfig {

    public Web web = new Web();
    public MySql mysql = new MySql();

    @Comment("The secret key used to encrypt the passwords")
    public String key = CipherUtils.byteToString(CipherUtils.generateKey(), false);

    @ConfigSerializable
    public static class Web {
        public int port = 8080;
        public long sessionMaxAgeMinutes = 60 * 24 * 3; // 3 days

    }

    @ConfigSerializable
    public static class MySql {
        public String host = "127.0.0.1";
        public int port = 3306;
        public String database = "finanzberg";
        public String username = "";
        public String password = "";
    }
}
