package com.shevchenko.csvsummary.component.impl;

import com.shevchenko.csvsummary.component.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Dmytro_Shevchenko4
 */
@Component
public class CacheImpl implements Cache {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheImpl.class);

    private Map<File, Long> map;
    private List<File> toRemove;

    private ScheduledExecutorService executor;

    private static final long EVICTION_PERIOD = 1000L;
    private static final long JOB_PERIOD = 10L;

    @PostConstruct
    public void setUp() {
        this.map = Collections.synchronizedMap(new HashMap<>());
        this.toRemove = Collections.synchronizedList(new ArrayList<>());
        this.executor = Executors.newSingleThreadScheduledExecutor((runnable) -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
        executor.scheduleAtFixedRate(() -> this.timeClean(), JOB_PERIOD, JOB_PERIOD, TimeUnit.SECONDS);
        LOGGER.info("Cache was setup");
    }

    @PreDestroy
    public void finalize() {
        map.keySet().forEach(file -> file.delete());
        LOGGER.info("Cache was cleared");
    }

    @Override
    public void put(File file) {
        map.put(file, System.currentTimeMillis());
        LOGGER.debug("File was put to cache -> {}", file.getName());
    }

    @Override
    public File get(String name) throws FileNotFoundException {
        if (!extend(name)){
            throw new FileNotFoundException("File was already removed from cache");
        }
        return map.keySet().stream().filter(file -> name.equals(file.getName())).findFirst().get();
    }

    @Override
    public boolean extend(String name) {
        Optional<File> expectedFile = map.keySet().stream().filter(file -> name.equals(file.getName())).findFirst();
        expectedFile.ifPresent(file -> map.put(file, System.currentTimeMillis()));
        expectedFile.ifPresent(file -> LOGGER.debug("File was extended -> {}", file.getName()));
        return expectedFile.isPresent();
    }

    private void timeClean() {
        map.entrySet().forEach(entry -> {
            if (System.currentTimeMillis() - entry.getValue() >
                    TimeUnit.MILLISECONDS.convert(EVICTION_PERIOD, TimeUnit.SECONDS)) {
                toRemove.add(entry.getKey());
            }
        });
        for (File file : toRemove) {
            file.delete();
            map.remove(file);
            LOGGER.debug("File was deleted -> {}", file.getName());
        }
        toRemove.clear();
    }
}
