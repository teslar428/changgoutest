package com.itheima.code.build;

import com.sun.org.glassfish.gmbal.Description;
import freemarker.template.Template;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PojoBuilder {

    public static void builder(Map<String,Object> dataModel){
        //生成Pojo层文件
        BuilderFactory.builder(dataModel,
                "/template/pojo",
                "Pojo.java",
                TemplateBuilder.PACKAGE_POJO,
                ".java");
    }

}
