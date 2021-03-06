/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.json.jsonlib.builder;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.MapUtil.newHashMap;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.feilong.json.SensitiveWords;
import com.feilong.json.jsonlib.JavaToJsonConfig;
import com.feilong.json.jsonlib.processor.SensitiveWordsJsonValueProcessor;

import net.sf.json.processors.JsonValueProcessor;

/**
 * The Class JavaToJsonConfigBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.3
 * @since 1.11.0 change package
 */
public final class JavaToJsonConfigBuilder{

    /** Don't let anyone instantiate this class. */
    private JavaToJsonConfigBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the default java to json config.
     *
     * @param javaBean
     *            the obj
     * @return 取到该javabean的Field,解析是否有 {@link SensitiveWords}注解,如果有,那么添加 {@link SensitiveWordsJsonValueProcessor}
     */
    public static JavaToJsonConfig buildDefaultJavaToJsonConfig(Object javaBean){
        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = buildSensitiveWordsPropertyNameAndJsonValueProcessorMap(
                        javaBean);
        return isNullOrEmpty(propertyNameAndJsonValueProcessorMap) ? null : new JavaToJsonConfig(propertyNameAndJsonValueProcessorMap);
    }

    //---------------------------------------------------------------

    /**
     * Builds the sensitive words property name and json value processor map.
     *
     * @param javaBean
     *            the java bean
     * @return the map
     * @since 1.10.3
     */
    public static Map<String, JsonValueProcessor> buildSensitiveWordsPropertyNameAndJsonValueProcessorMap(Object javaBean){
        List<Field> fieldsList = FieldUtils.getFieldsListWithAnnotation(javaBean.getClass(), SensitiveWords.class);
        if (isNullOrEmpty(fieldsList)){
            return null;
        }

        //---------------------------------------------------------------------------------------------------------

        //敏感字段
        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = newHashMap();
        for (Field field : fieldsList){
            propertyNameAndJsonValueProcessorMap.put(field.getName(), SensitiveWordsJsonValueProcessor.INSTANCE);
        }

        return propertyNameAndJsonValueProcessorMap;
    }
}
