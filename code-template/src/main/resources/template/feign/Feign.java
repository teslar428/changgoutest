package com.changgou.content.feign;
import com.changgou.entity.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="${serviceName}")
@RequestMapping("/${table}")
public interface ${Table}Feign {

    // ${Table}分页条件搜索实现
    @PostMapping(value = "/search/{page}/{size}" )
    Result<PageInfo> findPage(@RequestBody(required = false) ${Table} ${table}, @PathVariable("page")  int page, @PathVariable("size")  int size);

    // ${Table}分页搜索实现
    @GetMapping(value = "/search/{page}/{size}" )
    Result<PageInfo> findPage(@PathVariable("page")  int page, @PathVariable("size")  int size);

    // 多条件搜索品牌数据
    @PostMapping(value = "/search" )
    Result<List<${Table}>> findList(@RequestBody(required = false) ${Table} ${table});

    // 根据ID删除品牌数据
    @DeleteMapping(value = "/{id}" )
    Result delete(@PathVariable("id") ${keyType} id);

    // 修改${Table}数据
    @PutMapping(value="/{id}")
    Result update(@RequestBody ${Table} ${table},@PathVariable("id") ${keyType} id);

    // 新增${Table}数据
    @PostMapping
    Result add(@RequestBody ${Table} ${table});

    // 根据ID查询${Table}数据
    @GetMapping("/{id}")
    Result<${Table}> findById(@PathVariable("id") ${keyType} id);

    // 查询${Table}全部数据
    @GetMapping
    Result<List<${Table}>> findAll();
}