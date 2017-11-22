package com.shevchenko.csvsummary.component.impl;

import com.shevchenko.csvsummary.component.Cache;
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

    private Map<File, Long> map;
    private List<File> toRemove;

    private ScheduledExecutorService executor;

    private static final long EVICTION_PERIOD = 1200L;
    private static final long JOB_PERIOD = 100L;

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
    }

    @PreDestroy
    public void finalize() {
        map.keySet().forEach(file -> file.delete());
    }

    @Override
    public void put(File file) {
        map.put(file, System.currentTimeMillis());
    }

    @Override
    public File get(String name) throws FileNotFoundException {
        extend(name);
        return map.keySet().stream().filter(file -> name.equals(file.getName())).findFirst().orElseThrow(() -> new FileNotFoundException());
    }

    @Override
    public boolean extend(String name) {
        Optional<File> expectedFile = map.keySet().stream().filter(file -> name.equals(file.getName())).findFirst();
        expectedFile.ifPresent(file -> map.put(file, System.currentTimeMillis()));
        return expectedFile.isPresent();
    }

    private void timeClean() {
        map.entrySet().forEach(entry -> {
            if (System.currentTimeMillis() - entry.getValue() >
                    TimeUnit.SECONDS.convert(EVICTION_PERIOD, TimeUnit.MILLISECONDS)) {
                toRemove.add(entry.getKey());
            }
        });
        for (File file : toRemove) {
            file.delete();
        }
    }
}
