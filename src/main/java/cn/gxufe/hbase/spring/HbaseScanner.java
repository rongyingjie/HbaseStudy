package cn.gxufe.hbase.spring;

import cn.gxufe.hbase.core.HbaseRepository;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

public final class HbaseScanner extends ClassPathBeanDefinitionScanner {

    public HbaseScanner(BeanDefinitionRegistry registry){
        super(registry);
    }
    public void registerDefaultFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(HbaseRepository.class));
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions =   super.doScan(basePackages);
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definition.getPropertyValues().add("className", definition.getBeanClassName());
            definition.setBeanClass(HbaseFactoryBean.class);
        }

        return beanDefinitions;
    }

    public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return true;
    }
}
