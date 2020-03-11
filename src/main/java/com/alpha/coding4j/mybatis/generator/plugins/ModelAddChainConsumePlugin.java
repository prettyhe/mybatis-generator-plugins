package com.alpha.coding4j.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * ModelAddChainConsumePlugin
 *
 * @version 1.0
 * Date: 2020-03-10
 */
public class ModelAddChainConsumePlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        generateInChain(introspectedTable, topLevelClass);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateInChain(introspectedTable, topLevelClass);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        generateInChain(introspectedTable, topLevelClass);
        return true;
    }

    private void generateInChain(IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {
        Method method = new Method("inChain");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(topLevelClass.getType());
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType("java.util.function.Consumer<"
                + topLevelClass.getType().getFullyQualifiedName() + ">");
        method.addParameter(new Parameter(parameterType, "consumer", false));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.function.Consumer"));

        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3_DSQL) {
            context.getCommentGenerator().addGeneralMethodAnnotation(method,
                    introspectedTable, topLevelClass.getImportedTypes());
        } else {
            context.getCommentGenerator().addGeneralMethodComment(method,
                    introspectedTable);
        }

        method.addBodyLine("if (consumer != null) {");
        method.addBodyLine("consumer.accept(this);");
        method.addBodyLine("}");
        method.addBodyLine("return this;");

        topLevelClass.addMethod(method);
    }
}
