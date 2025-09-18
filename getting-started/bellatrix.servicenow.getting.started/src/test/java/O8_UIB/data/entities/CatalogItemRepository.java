package O8_UIB.data.entities;

import O8_UIB.ProjectTables;
import solutions.bellatrix.servicenow.infrastructure.core.repositories.TableApiRepository;

public class CatalogItemRepository extends TableApiRepository<CatalogItem> {
    public CatalogItemRepository() {
        super(CatalogItem.class, ProjectTables.CATALOG_ITEM_TABLE);
    }
}