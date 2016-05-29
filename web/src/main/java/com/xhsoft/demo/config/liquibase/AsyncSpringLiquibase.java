package com.xhsoft.demo.config.liquibase;

import com.xhsoft.demo.config.Constants;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.StopWatch;

import javax.inject.Inject;

/**
 * Specific liquibase.integration.spring.SpringLiquibase that will update the database asynchronously.
 * <p>
 *     By default, this asynchronous version only works when using the "dev" profile.<br/>
 *     The standard liquibase.integration.spring.SpringLiquibase starts Liquibase in the current thread:
 *     <ul>
 *         <li>This is needed if you want to do some database requests at startup</li>
 *         <li>This ensure that the database is ready when the application starts</li>
 *     </ul>
 *     But as this is a rather slow process, we use this asynchronous version to speed up our start-up time:
 *     <ul>
 *         <li>On a recent MacBook Pro, start-up time is down from 14 seconds to 8 seconds</li>
 *         <li>In production, this can help your application run on platforms like Heroku, where it must start/restart very quickly</li>
 *     </ul>
 * </p>
 */
public class AsyncSpringLiquibase extends SpringLiquibase {

    private final Logger log = LoggerFactory.getLogger(AsyncSpringLiquibase.class);

    @Inject
    @Qualifier("taskExecutor")
    private TaskExecutor taskExecutor;

    @Inject
    private Environment env;

    @Override
    public void afterPropertiesSet() throws LiquibaseException {
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT, Constants.SPRING_PROFILE_HEROKU)) {
            taskExecutor.execute(() -> {
                try {
                    log.warn("异步启动 Liquibase 以缩短启动时间, 可能会导致一些些的延迟后数据库才被正确配置(开发环境无影响)!");
                    initDb();
                } catch (LiquibaseException e) {
                    log.error("Liquibase 运行出错, 数据库未正确配置: {}", e.getMessage(), e);
                }
            });
        } else {
            log.debug("同步运行 Liquibase");
            initDb();
        }
    }

    protected void initDb() throws LiquibaseException {
        StopWatch watch = new StopWatch();
        watch.start();
        super.afterPropertiesSet();
        watch.stop();
        log.debug("Started Liquibase in {} ms", watch.getTotalTimeMillis());
    }
}
