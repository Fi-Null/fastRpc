package com.fast.rpc.config.springsupport;

import com.fast.rpc.config.ReferenceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

/**
 * @ClassName ReferenceConfigBean
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/23 23:41
 * @Version 1.0
 **/
public class ReferenceConfigBean<T> extends ReferenceConfig<T> implements
        FactoryBean<T>, BeanFactoryAware,
        InitializingBean, DisposableBean {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private transient BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public T getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
