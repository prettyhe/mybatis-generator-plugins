package com.alpha.coding4j.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * ClientAddAnnotationPlugin
 *
 * @version 1.0
 * Date: 2020-03-07
 */
public class ClientAddAnnotationPlugin extends ModelAddAnnotationPlugin {

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        for (String className : getAnnotationClass().split(",")) {
            interfaze.addImportedType(new FullyQualifiedJavaType(className));
        }
        interfaze.addAnnotation(getAnnotationValue());
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

}
