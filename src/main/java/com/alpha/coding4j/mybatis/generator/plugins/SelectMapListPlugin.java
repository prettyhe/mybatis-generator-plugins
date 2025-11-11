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
 * SelectMapListPlugin
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
        addMoreMethodToInterface(interfaze, introspectedTable);
        return result;
    }

    private void addMethodToInterface(Interface interfaze, IntrospectedTable introspectedTable) {

        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        // 方法入参、注解需要用到的class
        FullyQualifiedJavaType parameterType =
                new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.render.SelectStatementProvider");
        FullyQualifiedJavaType adapter =
                new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.SqlProviderAdapter");
        FullyQualifiedJavaType annotation =
                new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider");
        imports.add(parameterType);
        imports.add(adapter);
        imports.add(annotation);

        // 方法出参需要用到的class
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        imports.add(returnType);
        imports.add(FullyQualifiedJavaType.getNewMapInstance());
        // 完善出参
        returnType.addTypeArgument(FullyQualifiedJavaType.getNewMapInstance());

        // 方法体需要用到的class
        interfaze.addImportedTypes(imports);

        Method method = new Method("selectMapList");
        method.setAbstract(true);
        method.setReturnType(returnType);
        method.addParameter(new Parameter(parameterType, "selectStatement"));
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.addAnnotation("@SelectProvider(type=SqlProviderAdapter.class, method=\"select\")");

        interfaze.addMethod(method);
    }

    private void addMoreMethodToInterface(Interface interfaze, IntrospectedTable introspectedTable) {

        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        // 方法入参需要用到的class
        FullyQualifiedJavaType parameterType1 =
                new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.QueryExpressionDSL<org.mybatis.dynamic.sql.select.SelectModel>");
        FullyQualifiedJavaType parameterType2 =
                new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.SelectDSLCompleter");
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.QueryExpressionDSL"));
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.SelectModel"));
        imports.add(parameterType2);

        // 方法出参需要用到的class
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        imports.add(returnType);
        imports.add(FullyQualifiedJavaType.getNewMapInstance());
        // 完善出参
        returnType.addTypeArgument(FullyQualifiedJavaType.getNewMapInstance());

        // 方法体需要用到的class
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.render.RenderingStrategies"));

        // 接口添加import
        interfaze.addImportedTypes(imports);

        Method method = new Method("selectMapList");
        method.setDefault(true);
        method.addParameter(new Parameter(parameterType1, "select"));
        method.addParameter(new Parameter(parameterType2, "completer"));
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.setReturnType(returnType);
        method.addBodyLine("return this.selectMapList(completer.apply(select).build().render(RenderingStrategies.MYBATIS3));");

        interfaze.addMethod(method);
    }

}
