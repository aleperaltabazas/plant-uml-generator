package persistence;

import klass.Klass;

import java.util.List;

public interface TableBuilder {
    TableBuilder parse(Klass entity, List<Klass> others);
    TableBuilder takeForeignKeys(List<ForeignKey> foreignKeys);
}