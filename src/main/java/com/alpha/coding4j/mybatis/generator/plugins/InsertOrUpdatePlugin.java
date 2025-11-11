package com.alpha.coding4j.mybatis.generator.plugins;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * InsertOrUpdatePlugin
 *
 * @version 1.0
 * Date: 2020-03-10
 */
public class InsertOrUpdatePlugin extends PluginAdapter {

    private static final String ENABLE_TABLES = "enableTables";
    private final Set<String> enableTables = new HashSet<>();

    @Override
    public boolean validate(List<String> warnings) {
        final String includeTables = properties.getProperty(ENABLE_TABLES);
        Optional.ofNullable(includeTables).filter(StringUtility::stringHasValue)
                .map(p -> Arrays.stream(p.split(",")).collect(Collectors.toSet()))
                .ifPresent(enableTables::addAll);
        return true;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze,
                                               IntrospectedTable introspectedTable) {
        final boolean ret = super.clientInsertMethodGenerated(method, interfaze, introspectedTable);
        if (ret && enableTables.contains(introspectedTable.getFullyQualifiedTableNameAtRuntime())) {
            addMethodToInterface(interfaze, introspectedTable);
        }
        return ret;
    }

    private void addMethodToInterface(Interface interfaze, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType recordType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        imports.add(recordType);
        imports.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Insert"));
        imports.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        imports.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Options"));
        interfaze.addImportedTypes(imports);

        Parameter param = new Parameter(recordType, "row");
        param.addAnnotation("@Param(\"row\")");

        Method method = new Method("insertOrUpdate");
        method.setAbstract(true);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(param);
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"<script>\",\n");
        sb.append("            \"INSERT INTO `");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime()).append("`\",\n");
        sb.append("            \"  <trim prefix='(' suffix=')' suffixOverrides=','>\",\n");
        String tpl1 = "            \"    <if test='row.%s != null'>`%s`,</if>\",\n";
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            final String name = column.getJavaProperty();
            sb.append(String.format(tpl1, name, column.getActualColumnName()));
        }
        sb.append("            \"  </trim>\",\n");
        sb.append("            \"VALUES\",\n");
        sb.append("            \"  <trim prefix='(' suffix=')' suffixOverrides=','>\",\n");
        String tpl2 = "            \"    <if test='row.%s != null'>#{row.%s,jdbcType=%s},</if>\",\n";
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            final String name = column.getJavaProperty();
            sb.append(String.format(tpl2, name, name, column.getJdbcTypeName()));
        }
        sb.append("            \"  </trim>\",\n");
        sb.append("            \"ON DUPLICATE KEY UPDATE \",\n");
        sb.append("            \"  <trim suffixOverrides=','>\",\n");
        String tpl3 = "            \"    <if test='row.%s != null'>`%s` = #{row.%s,jdbcType=%s},</if>\",\n";
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            final String name = column.getJavaProperty();
            sb.append(String.format(tpl3, name, column.getActualColumnName(), name, column.getJdbcTypeName()));
        }
        sb.append("            \"  </trim>\",\n");
        sb.append("            \"</script>\"");
        sb.append("}");

        method.addAnnotation("@Insert(" + sb + ")");

        final GeneratedKey generatedKey = introspectedTable.getGeneratedKey().orElse(null);
        if (generatedKey != null) {
            introspectedTable.getColumn(generatedKey.getColumn()).ifPresent(introspectedColumn -> {
                if (generatedKey.isJdbcStandard()) {
                    // only jdbc standard keys are supported for multiple insert
                    StringBuilder ksb = new StringBuilder();
                    ksb.append("@Options(useGeneratedKeys=true, keyProperty=\"row.");
                    ksb.append(introspectedColumn.getJavaProperty());
                    ksb.append("\")");
                    method.addAnnotation(ksb.toString());
                }
            });
        }

        interfaze.addMethod(method);

    }

}
