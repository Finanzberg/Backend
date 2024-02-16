package de.finanzberg.backend.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@SuppressWarnings("FieldMayBeFinal")
@ConfigSerializable
public class FinanzbergConfig {

    public Web web = new Web();
    public MySql mysql = new MySql();

    @ConfigSerializable
    public static class Web {
        public int port = 8080;

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
