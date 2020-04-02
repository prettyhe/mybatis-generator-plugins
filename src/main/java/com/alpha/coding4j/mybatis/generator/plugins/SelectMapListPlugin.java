package com.alpha.coding4j.mybatis.generator.plugins;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

/**
 * SelectResultMapPlugin
 *
 * @version 1.0
 * Date: 2020/4/2
 */
public class SelectMapListPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientBasicSelectManyMethodGenerated(Method method, Interface interfaze,
                                                        IntrospectedTable introspectedTable) {
        final boolean result = super.clientBasicSelectManyMethodGenerated(method, interfaze, introspectedTable);
        addMethodToInterface(interfaze, introspectedTable);
        return result;
    }

    private void addMethodToInterface(Interface interfaze, IntrospectedTable introspectedTable) {

        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        FullyQualifiedJavaType parameterType =
                new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.render.SelectStatementProvider");
        FullyQualifiedJavaType adapter =
                new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.SqlProviderAdapter");
        FullyQualifiedJavaType annotation =
                new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider");

        imports.add(parameterType);
        imports.add(adapter);
        imports.add(annotation);
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        imports.add(returnType);
        imports.add(FullyQualifiedJavaType.getNewMapInstance());
        interfaze.addImportedTypes(imports);

        returnType.addTypeArgument(FullyQualifiedJavaType.getNewMapInstance());

        Method method = new Method("selectMapList");
        method.setAbstract(true);
        method.setReturnType(returnType);
        method.addParameter(new Parameter(parameterType, "selectStatement"));
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.addAnnotation("@SelectProvider(type=SqlProviderAdapter.class, method=\"select\")");

        interfaze.addMethod(method);
    }
}
