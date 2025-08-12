package O7_DataCreationHttp.data.user;

import solutions.bellatrix.servicenow.infrastructure.core.repositories.TableApiRepository;

public class UserRepository extends TableApiRepository <User> {
    public UserRepository() {
        super(User.class);
    }
}