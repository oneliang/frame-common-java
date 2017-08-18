package com.oneliang.frame;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.oneliang.frame.configuration.ConfigurationBean;
import com.oneliang.frame.configuration.ConfigurationContext;
import com.oneliang.frame.ioc.IocBean;
import com.oneliang.frame.ioc.IocContext;
import com.oneliang.frame.jdbc.DatabaseContext;
import com.oneliang.frame.jdbc.MappingBean;
import com.oneliang.frame.jdbc.MappingContext;
import com.oneliang.frame.jxl.JxlMappingContext;
import com.oneliang.frame.servlet.action.ActionBean;
import com.oneliang.frame.servlet.action.ActionContext;
import com.oneliang.frame.servlet.action.Interceptor;
import com.oneliang.frame.servlet.action.InterceptorContext;
import com.oneliang.frame.workflow.TaskContext;
import com.oneliang.util.jxl.JxlMappingBean;

/**
 * ConfigurationFactory
 * 
 * @author Dandelion
 * @since 2009-03-12
 */
public class ConfigurationFactory {

    protected static ConfigurationContext configurationContext = new ConfigurationContext();

    /**
     * get singleton configuration context
     * 
     * @return ConfigurationContext
     */
    public static ConfigurationContext getSingletonConfigurationContext() {
        return configurationContext;
    }

    /**
     * get before global interceptor list
     * 
     * @return List<Interceptor>
     */
    public static List<Interceptor> getBeforeGlobalInterceptorList() {
        List<Interceptor> beforeGlobalInterceptorList = null;
        InterceptorContext interceptorContext = configurationContext.findContext(InterceptorContext.class);
        if (interceptorContext != null) {
            beforeGlobalInterceptorList = interceptorContext.getBeforeGlobalInterceptorList();
        }
        return beforeGlobalInterceptorList;
    }

    /**
     * get after global interceptor list
     * 
     * @return List<Interceptor>
     */
    public static List<Interceptor> getAfterGlobalInterceptorList() {
        List<Interceptor> afterGlobalInterceptorList = null;
        InterceptorContext interceptorContext = configurationContext.findContext(InterceptorContext.class);
        if (interceptorContext != null) {
            afterGlobalInterceptorList = interceptorContext.getAfterGlobalInterceptorList();
        }
        return afterGlobalInterceptorList;
    }

    /**
     * injection,include ioc injection and interceptor injection
     * 
     * @throws Exception
     */
    public static void inject() throws Exception {
        iocInject();
        interceptorInject();
        processorInject();
    }

    /**
     * ioc injection
     * 
     * @throws Exception
     */
    public static void iocInject() throws Exception {
        IocContext iocContext = configurationContext.findContext(IocContext.class);
        if (iocContext != null) {
            iocContext.inject();
        }
    }

    /**
     * after inject
     * 
     * @throws Exception
     */
    public static void afterInject() throws Exception {
        IocContext iocContext = configurationContext.findContext(IocContext.class);
        if (iocContext != null) {
            iocContext.afterInject();
        }
    }

    /**
     * interceptor inject
     */
    public static void interceptorInject() {
        ActionContext actionContext = configurationContext.findContext(ActionContext.class);
        if (actionContext != null) {
            actionContext.interceptorInject();
        }
    }

    /**
     * processor injection
     * 
     * @throws Exception
     */
    public static void processorInject() throws Exception {
        Iterator<Entry<String, ConfigurationBean>> iterator = configurationContext.getConfigurationBeanEntrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, ConfigurationBean> entry = iterator.next();
            ConfigurationBean configurationBean = entry.getValue();
            Context context = configurationBean.getContextInstance();
            if (context instanceof TaskContext) {
                TaskContext taskContext = (TaskContext) context;
                taskContext.processorInject();
            }
        }
    }

    /**
     * ioc auto inject object by id
     * 
     * @param id
     * @param object
     * @throws Exception
     */
    public static void iocAutoInjectObjectById(String id, Object object) throws Exception {
        if (object != null) {
            IocContext iocContext = configurationContext.findContext(IocContext.class);
            if (iocContext != null) {
                IocBean iocBean = new IocBean();
                iocBean.setId(id);
                iocBean.setInjectType(IocBean.INJECT_TYPE_AUTO_BY_ID);
                iocBean.setProxy(false);
                iocBean.setProxyInstance(object);
                iocBean.setBeanInstance(object);
                iocBean.setType(object.getClass().getName());
                iocContext.putToIocBeanMap(id, iocBean);
                iocContext.autoInjectObjectById(object);
            }
        }
    }

    /**
     * put object to ioc bean map
     * 
     * @param id
     * @param object
     */
    public static void putObjectToIocBeanMap(String id, Object object) {
        if (object != null) {
            IocContext iocContext = configurationContext.findContext(IocContext.class);
            if (iocContext != null) {
                IocBean iocBean = new IocBean();
                iocBean.setId(id);
                iocBean.setInjectType(IocBean.INJECT_TYPE_AUTO_BY_ID);
                iocBean.setProxy(false);
                iocBean.setProxyInstance(object);
                iocBean.setBeanInstance(object);
                iocBean.setType(object.getClass().getName());
                iocContext.putToIocBeanMap(id, iocBean);
            }
        }
    }

    /**
     * initial connection pools
     * 
     * @throws Exception
     */
    @Deprecated
    public static void initialConnectionPools() throws Exception {
        DatabaseContext dataBaseContext = configurationContext.findContext(DatabaseContext.class);
        if (dataBaseContext != null) {
            dataBaseContext.initialConnectionPools();
        }
    }

    /**
     * find global forward path with name
     * 
     * @param name
     * @return String
     */
    public static String findGlobalForwardPath(String name) {
        String path = null;
        if (name != null) {
            ActionContext actionContext = configurationContext.findContext(ActionContext.class);
            if (actionContext != null) {
                path = actionContext.findGlobalForwardPath(name);
            }
        }
        return path;
    }

    /**
     * find bean
     * 
     * @param id
     * @return T
     */
    public static <T extends Object> T findBean(String id) {
        return configurationContext.findBean(id);
    }

    /**
     * get global exception forward path
     * 
     * @return String
     */
    public static String getGlobalExceptionForwardPath() {
        String path = null;
        ActionContext actionContext = configurationContext.findContext(ActionContext.class);
        if (actionContext != null) {
            path = actionContext.getGlobalExceptionForwardPath();
        }
        return path;
    }

    /**
     * find ActionBean list
     * 
     * @param uri
     * @return List<ActionBean>
     */
    public static List<ActionBean> findActionBeanList(String uri) {
        List<ActionBean> actionBeanList = null;
        if (uri != null) {
            ActionContext actionContext = configurationContext.findContext(ActionContext.class);
            if (actionContext != null) {
                actionBeanList = actionContext.findActionBeanList(uri);
            }
        }
        return actionBeanList;
    }

    /**
     * find mappingBean
     * 
     * @param <T>
     * @param clazz
     * @return MappingBean
     */
    public static <T extends Object> MappingBean findMappingBean(Class<T> clazz) {
        MappingBean mappingBean = null;
        if (clazz != null) {
            MappingContext mappingContext = configurationContext.findContext(MappingContext.class);
            if (mappingContext != null) {
                mappingBean = mappingContext.findMappingBean(clazz);
            }
        }
        return mappingBean;
    }

    /**
     * find mappingBean
     * 
     * @param name
     * @return MappingBean
     */
    public static MappingBean findMappingBean(String name) {
        MappingBean mappingBean = null;
        if (name != null) {
            MappingContext mappingContext = configurationContext.findContext(MappingContext.class);
            if (mappingContext != null) {
                mappingBean = mappingContext.findMappingBean(name);
            }
        }
        return mappingBean;
    }

    /**
     * get mapping bean entry set
     * 
     * @return Set<Entry<String,MappingBean>>
     * @throws Exception
     */
    public static Set<Entry<String, MappingBean>> getMappingBeanEntrySet() {
        Set<Entry<String, MappingBean>> mappingBeanEntrySet = null;
        MappingContext mappingContext = configurationContext.findContext(MappingContext.class);
        if (mappingContext != null) {
            mappingBeanEntrySet = mappingContext.getMappingBeanEntrySet();
        }
        return mappingBeanEntrySet;
    }

    /**
     * find import jxlMappingBean
     * 
     * @param <T>
     * @param clazz
     * @return JxlMappingBean
     */
    public static <T extends Object> JxlMappingBean findImportJxlMappingBean(Class<T> clazz) {
        JxlMappingBean jxlMappingBean = null;
        if (clazz != null) {
            JxlMappingContext jxlMappingContext = configurationContext.findContext(JxlMappingContext.class);
            if (jxlMappingContext != null) {
                jxlMappingBean = jxlMappingContext.findImportJxlMappingBean(clazz);
            }
        }
        return jxlMappingBean;
    }

    /**
     * find import jxlMappingBean
     * 
     * @param name
     * @return JxlMappingBean
     * @throws Exception
     */
    public static JxlMappingBean findImportJxlMappingBean(String name) throws Exception {
        JxlMappingBean jxlMappingBean = null;
        if (name != null) {
            JxlMappingContext jxlMappingContext = configurationContext.findContext(JxlMappingContext.class);
            if (jxlMappingContext != null) {
                jxlMappingBean = jxlMappingContext.findImportJxlMappingBean(name);
            }
        }
        return jxlMappingBean;
    }

    /**
     * find export jxlMappingBean
     * 
     * @param <T>
     * @param clazz
     * @return JxlMappingBean
     */
    public static <T extends Object> JxlMappingBean findExportJxlMappingBean(Class<T> clazz) {
        JxlMappingBean jxlMappingBean = null;
        if (clazz != null) {
            JxlMappingContext jxlMappingContext = configurationContext.findContext(JxlMappingContext.class);
            if (jxlMappingContext != null) {
                jxlMappingBean = jxlMappingContext.findExportJxlMappingBean(clazz);
            }
        }
        return jxlMappingBean;
    }

    /**
     * find export jxlMappingBean
     * 
     * @param name
     * @return JxlMappingBean
     * @throws Exception
     */
    public static JxlMappingBean findExportJxlMappingBean(String name) throws Exception {
        JxlMappingBean jxlMappingBean = null;
        if (name != null) {
            JxlMappingContext jxlMappingContext = configurationContext.findContext(JxlMappingContext.class);
            if (jxlMappingContext != null) {
                jxlMappingBean = jxlMappingContext.findExportJxlMappingBean(name);
            }
        }
        return jxlMappingBean;
    }
}
