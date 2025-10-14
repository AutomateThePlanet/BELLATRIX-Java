package O7_DataCreationHttp.data.incident;

import solutions.bellatrix.servicenow.infrastructure.core.repositories.TableApiRepository;

public class IncidentRepository extends TableApiRepository<Incident> {
    public IncidentRepository() {
        super(Incident.class);
    }
}