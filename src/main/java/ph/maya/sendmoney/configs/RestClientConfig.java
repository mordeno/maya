package ph.maya.sendmoney.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import ph.maya.sendmoney.exceptions.ContactsProviderException;
import ph.maya.sendmoney.properties.ContactsProperties;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(ContactsProperties.class)
public class RestClientConfig {

    private final ContactsProperties contactsProperties;

    @Bean
    public RestClient recipientClient() {
        return RestClient.builder()
                .baseUrl(contactsProperties.baseUrl())
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        ((request, response) -> {
                            throw new ContactsProviderException("Provider error: " + response.getStatusCode());
                        }))
                .build();
    }
}
