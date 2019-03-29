package persistence;

import java.util.List;

public interface Table {
    String getName();

    String getPk();

    List<TableAttribute> getAttributes();

    List<ForeignKey> getFks();
}