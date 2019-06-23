package com.fast.rpc.config;

import com.fast.rpc.common.URL;
import com.fast.rpc.rpc.Exporter;
import com.google.common.collect.ArrayListMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName ServiceConfig
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/23 23:18
 * @Version 1.0
 **/
public class ServiceConfig<T> extends AbstractInterfaceConfig {

    private static final long serialVersionUID = -6784362174923673740L;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private volatile boolean exported = false;

    private List<Exporter<T>> exporters = new CopyOnWriteArrayList();

    private ArrayListMultimap<URL, URL> registeredUrls = ArrayListMultimap.create();

    private Class<T> interfaceClass;

    private T ref;



    protected void destroy0() throws Exception {
        exporters.clear();
        registeredUrls.clear();
    }

}
