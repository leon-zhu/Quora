<html>
    <body>
    include from top.ftl:
    <#include "top.ftl">

    <pre>
        var = ${var!"default value"}

        <#--遍历array-->
        <#list array as a>
            ${a_index}: ${a}
        </#list>

        <#--遍历list-->
        <#list list as li>
            ${li_index}: ${li}
        </#list>

        <#--第一种map遍历方式-->
        <#assign keys = map?keys> <#--先获取keyset, 再进行遍历-->
        <#list keys as key>
            index = ${key_index}, key = ${key}, value = ${map[key]}
        </#list>

        <#--第二种map遍历方式-->
        <#list map?keys as key>
            index = ${key_index}, key = ${key}, value = ${map[key]}
        </#list>

        <#--解析对象-->
        <#--${stu.age}, ${stu.name}
        ${stu.toString()}-->

        <#--自定义变量-->
        <#--
        <#assign desc = "This is description, ${stu.name}">
        ${desc!} -->

        <#--自定义函数-->
        <#function add num1 num2>
            <#return num1+num2>
        </#function>

        usage of add function: ${add(10, 20)}

        <#--学习控制流-->
        <#if 1 == 2>
            if_1
        <#else>
            if_2
        </#if>

        <#assign var1 = 1>
        <#switch var1 >
            <#case 1>   case_1 <#break>
            <#case 2>   case_2 <#break>
        </#switch>


    </pre>
    </body>
</html>