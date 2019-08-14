package persistence.tables;

import io.vavr.collection.List;
import persistence.attributes.ForeignKey;
import persistence.attributes.TableAttribute;

public interface Table {
    String getName();

    String getPk();

    List<TableAttribute> getAttributes();

    List<ForeignKey> getFks();

    String write();
}