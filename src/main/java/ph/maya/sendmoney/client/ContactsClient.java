package ph.maya.sendmoney.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ph.maya.sendmoney.properties.ContactsProperties;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ContactsClient {

    private final RestClient contactsClient;

    public List<JsonPlaceholderUsersResponse> getContacts() {
        try {
            return contactsClient.get()
                    .uri("/users")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            // return empty and use the default names of users
            return new ArrayList<>();
        }
    }
}
