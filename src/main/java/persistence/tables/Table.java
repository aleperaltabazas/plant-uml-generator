package persistence.tables;

import persistence.attributes.ForeignKey;
import persistence.attributes.TableAttribute;

import java.util.List;

public interface Table {
    String getName();

    String getPk();

    List<TableAttribute> getAttributes();

    List<ForeignKey> getFks();
}