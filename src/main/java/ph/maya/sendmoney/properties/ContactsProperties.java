package ph.maya.sendmoney.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "contacts.jsonplaceholder")
public record ContactsProperties(String baseUrl) {}
