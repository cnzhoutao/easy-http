package com.github.easy_http.registry;

import com.github.easy_http.annotation.Api;
import com.github.easy_http.annotation.EnableApi;
import com.github.easy_http.fatory.ApiFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Set;

/**
 * @Author: zt
 * @Date: 2021/1/1 10:17 下午
 */
public class ApiImportBeanDefinitionRegistry implements ImportBeanDefinitionRegistrar, ResourceLoaderAware,
        EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(EnableApi.class.getName());
        String[] basePackages = (String[]) annotationAttributes.get("basePackages");
        if (basePackages == null || basePackages.length <= 0) {
            return;
        }
        ClassPathScanningCandidateComponentProvider scanner = this.getScanner();
        scanner.setResourceLoader(resourceLoader);
        AnnotationTypeFilter apiTypeFilter = new AnnotationTypeFilter(Api.class);
        scanner.addIncludeFilter(apiTypeFilter);
        for (String packageName : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(packageName);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    //限制次此注解只能用在接口上
                    Assert.isTrue(annotationMetadata.isInterface(), "@Api can only be specified on an interface");

                    Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(
                            Api.class.getCanonicalName());
                    registerFeignClient(registry, annotationMetadata, attributes);
                }
            }
        }
    }

    /**
     * 注册beanDefinition
     *
     * @param registry           注册器
     * @param annotationMetadata @Api注解中的元数据
     * @param attributes         @Api注解中的源数据
     */
    private void registerFeignClient(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata,
                                     Map<String, Object> attributes) {
        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ApiFactoryBean.class);
        definitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        definitionBuilder.addPropertyValue("hostName", attributes.get("hostName"));
        definitionBuilder.addPropertyValue("https", attributes.get("https"));
        definitionBuilder.addPropertyValue("url", attributes.get("url"));
        String className = annotationMetadata.getClassName();
        definitionBuilder.addPropertyValue("type", className);
        definitionBuilder.setLazyInit(false);
        AbstractBeanDefinition beanDefinition = definitionBuilder.getBeanDefinition();

        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, className);
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, registry);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
