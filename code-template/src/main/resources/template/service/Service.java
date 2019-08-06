package ${package_service};
import ${package_pojo}.${Table};
import com.github.pagehelper.PageInfo;
import java.util.List;
public interface ${Table}Service {

    PageInfo<${Table}> findPage(${Table} ${table}, int page, int size);

    PageInfo<${Table}> findPage(int page, int size);

    List<${Table}> findList(${Table} ${table});

    void delete(${keyType} id);

    void update(${Table} ${table});

    void add(${Table} ${table});

    ${Table} findById(${keyType} id);

    List<${Table}> findAll();
}
