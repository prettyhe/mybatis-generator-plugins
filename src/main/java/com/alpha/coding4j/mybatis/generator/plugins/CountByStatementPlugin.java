package com.alpha.coding4j.mybatis.generator.plugins;

import java.util.Arrays;
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
 * CountByStatementPlugin
 *
 * @version 1.0
 * Date: 2020/4/14
 */
public class CountByStatementPlugin extends PluginAdapter {

    private List<String> methodDocLines = Arrays.asList(
            "/**",
            " * execute count by custom statement",
            " *",
            " * @param countStatement select sql statement, like: "
                    + "select count(*) from table_a where column_a = #{params.a}",
            " * @param params         params for sql, like: (a=xxx)",
            " * @return count of select result",
            " */"
    );

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientBasicCountMethodGenerated(Method method, Interface interfaze,
                                                   IntrospectedTable introspectedTable) {
        final boolean result = super.clientBasicCountMethodGenerated(method, interfaze, introspectedTable);
        addMethodToInterface(interfaze, introspectedTable);
        return result;
    }

    private void addMethodToInterface(Interface interfaze, IntrospectedTable introspectedTable) {

        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        imports.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select"));
        imports.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        imports.add(FullyQualifiedJavaType.getNewMapInstance());
        interfaze.addImportedTypes(imports);

        Parameter param = new Parameter(FullyQualifiedJavaType.getStringInstance(), "countStatement");
        param.addAnnotation("@Param(\"countStatement\")");

        Method method = new Method("countByStatement");
        method.setAbstract(true);
        method.setReturnType(new FullyQualifiedJavaType("long"));
        method.addParameter(param);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getNewMapInstance(), "params"));
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.addAnnotation("@Select({\"${countStatement}\"})");

        methodDocLines.forEach(x -> method.addJavaDocLine(x));

        interfaze.addMethod(method);
    }
}
