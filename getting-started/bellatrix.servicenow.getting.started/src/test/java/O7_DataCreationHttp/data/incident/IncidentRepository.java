package O7_DataCreationHttp.data.incident;

import O7_DataCreationHttp.data.ProjectTables;
import solutions.bellatrix.servicenow.infrastructure.core.repositories.TableApiRepository;

public class IncidentRepository extends TableApiRepository<Incident> {
    public IncidentRepository() {
        super(Incident.class, ProjectTables.INCIDENT_TABLE);
    }
}