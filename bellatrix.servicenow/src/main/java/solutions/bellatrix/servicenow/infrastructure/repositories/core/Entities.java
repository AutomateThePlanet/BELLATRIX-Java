package solutions.bellatrix.servicenow.infrastructure.repositories.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entities<TEntity extends ApiEntity> {
    private List<TEntity> entities;
}