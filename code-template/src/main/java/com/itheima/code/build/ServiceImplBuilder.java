package com.itheima.code.build;

import com.sun.org.glassfish.gmbal.Description;
import freemarker.template.Template;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ServiceImplBuilder {

    public static void builder(Map<String,Object> modelMap){
        //生成ServiceImpl层文件
        BuilderFactory.builder(modelMap,
                "/template/service/impl",
                "ServiceImpl.java",
                TemplateBuilder.PACKAGE_SERVICE_INTERFACE_IMPL,
                "ServiceImpl.java");
    }

}
