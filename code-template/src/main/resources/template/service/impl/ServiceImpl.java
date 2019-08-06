package ${package_service_impl};
import ${package_mapper}.${Table}Mapper;
import ${package_pojo}.${Table};
import ${package_service}.${Table}Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import java.util.List;
@Service
public class ${Table}ServiceImpl implements ${Table}Service {

    @Autowired
    private ${Table}Mapper ${table}Mapper;

    // ${Table}条件+分页查询
    @Override
    public PageInfo<${Table}> findPage(${Table} ${table}, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(${table});
        //执行搜索
        return new PageInfo<${Table}>(${table}Mapper.selectByExample(example));
    }

    // ${Table}分页查询
    @Override
    public PageInfo<${Table}> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<${Table}>(${table}Mapper.selectAll());
    }

    // ${Table}条件查询
    @Override
    public List<${Table}> findList(${Table} ${table}){
        //构建查询条件
        Example example = createExample(${table});
        //根据构建的条件查询数据
        return ${table}Mapper.selectByExample(example);
    }

    // ${Table}构建查询对象
    public Example createExample(${Table} ${table}){
        Example example=new Example(${Table}.class);
        Example.Criteria criteria = example.createCriteria();
        if(${table}!=null){
            <#list models as md>
            // ${md.desc}
            if(!StringUtils.isEmpty(${table}.get${md.upperName}())){
                <#if (md.name?contains("title") || md.name?contains("name"))>
                    criteria.andLike("${md.name}","%"+${table}.get${md.upperName}()+"%");
                <#else>
                    criteria.andEqualTo("${md.name}",${table}.get${md.upperName}());
                </#if>
            }
            </#list>
        }
        return example;
    }

    // 删除
    @Override
    public void delete(${keyType} id){
        ${table}Mapper.deleteByPrimaryKey(id);
    }

    // 修改${Table}
    @Override
    public void update(${Table} ${table}){
        ${table}Mapper.updateByPrimaryKeySelective(${table});
    }

    // 增加${Table}
    @Override
    public void add(${Table} ${table}){
        ${table}Mapper.insertSelective(${table});
    }

    // 根据ID查询${Table}
    @Override
    public ${Table} findById(${keyType} id){
        return  ${table}Mapper.selectByPrimaryKey(id);
    }

    // 查询${Table}全部数据
    @Override
    public List<${Table}> findAll() {
        return ${table}Mapper.selectAll();
    }
}
