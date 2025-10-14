package O7_DataCreationHttp.data.user;

import solutions.bellatrix.data.http.contracts.EntityFactory;

public class UserRepositoryFactory implements EntityFactory<User> {
    public UserRepositoryFactory() {
    }

    @Override
    public User buildDefault() {
        var identifierPart = Long.toString(System.currentTimeMillis());
        return User.builder()
                .firstName("Default Test User")
                .lastName(identifierPart)
                .userName("Default Test User " + identifierPart)
                .build();
    }
}
