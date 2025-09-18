package O8_UIB.data.user;

import solutions.bellatrix.servicenow.infrastructure.core.repositories.TableApiRepository;

public class UserRepository extends TableApiRepository <User> {
    public UserRepository() {
        super(User.class);
    }
}