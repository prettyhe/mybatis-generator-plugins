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
 * SelectByStatementPlugin
 *
 * @version 1.0
 * Date: 2020/4/2
 */
public class SelectByStatementPlugin extends PluginAdapter {

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

        imports.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select"));
        imports.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        imports.add(returnType);
        imports.add(FullyQualifiedJavaType.getNewMapInstance());
        interfaze.addImportedTypes(imports);

        returnType.addTypeArgument(FullyQualifiedJavaType.getNewMapInstance());

        Parameter param = new Parameter(FullyQualifiedJavaType.getStringInstance(), "selectStatement");
        param.addAnnotation("@Param(\"selectStatement\")");

        Method method = new Method("selectByStatement");
        method.setAbstract(true);
        method.setReturnType(returnType);
        method.addParameter(param);
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.addAnnotation("@Select({\"${selectStatement}\"})");

        interfaze.addMethod(method);
    }
}