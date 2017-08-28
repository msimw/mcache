package com.msimw.mcache.handler.impl.memcached.util;

import com.whalin.MemCached.MemCachedClient;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * memcached 工具类
 */
public abstract class MemCachedUtil {

    private static final Logger logger = Logger.getLogger(MemCachedUtil.class);


    /**
     * 获取所有key
     * @param memCachedClient
     * @return
     */
    public static Map<String, Object> getKeySet(MemCachedClient memCachedClient) {
        Map<String, Object> keys = new HashMap();
        Map<String, Map<String, String>> statsItems = memCachedClient.statsItems();
        Map<String, String> statsItems_sub = null;
        String statsItems_sub_key = null;
        int items_number = 0;
        String server = null;
        Map<String, Map<String, String>> statsCacheDump = null;
        Map<String, String> statsCacheDump_sub = null;
        String statsCacheDumpsub_key = null;
        String statsCacheDumpsub_key_value = null;
        Iterator iterator = statsItems.keySet().iterator();

        label50:
        while (iterator.hasNext()) {
            server = (String) iterator.next();
            statsItems_sub = (Map) statsItems.get(server);
            Iterator iterator_item = statsItems_sub.keySet().iterator();

            while (true) {
                do {
                    do {
                        if (!iterator_item.hasNext()) {
                            continue label50;
                        }

                        statsItems_sub_key = (String) iterator_item.next();
                    } while (!statsItems_sub_key.toUpperCase().startsWith("items:".toUpperCase()));
                } while (!statsItems_sub_key.toUpperCase().endsWith(":number".toUpperCase()));

                items_number = Integer.parseInt((statsItems_sub.get(statsItems_sub_key)).trim());
                statsCacheDump = memCachedClient.statsCacheDump(new String[]{server}, Integer.parseInt(statsItems_sub_key.split(":")[1].trim()), items_number);
                Iterator statsCacheDump_iterator = statsCacheDump.keySet().iterator();

                while (statsCacheDump_iterator.hasNext()) {
                    statsCacheDump_sub =  statsCacheDump.get(statsCacheDump_iterator.next());
                    Iterator iterator_keys = statsCacheDump_sub.keySet().iterator();

                    while (iterator_keys.hasNext()) {
                        statsCacheDumpsub_key = (String) iterator_keys.next();
                        statsCacheDumpsub_key_value = statsCacheDump_sub.get(statsCacheDumpsub_key);

                        try {
                            keys.put(URLDecoder.decode(statsCacheDumpsub_key, "UTF-8"), null);
                            if (logger.isDebugEnabled()) {
                                logger.debug("key->" + statsCacheDumpsub_key + ": " + "value->" + statsCacheDumpsub_key_value);
                            }
                        } catch (UnsupportedEncodingException var16) {
                            var16.printStackTrace();
                        }
                    }
                }
            }
        }

        return keys;
    }
}
