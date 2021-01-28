package org.etosha.contextualizer;

/**
 * Created by kamir on 29.08.17.
 *
 *
 * Each entity which is exposed needs a backlink to the original cluster, so that some query can be sent to
 * that cluster about this dataset.
 *
 * We have to track technical metadata about the original cluster, from which the facts have been exposed.
 *
 *
 * Examples:
 *
 * Hive Table:
 * -----------
 *
 *
 *
 * HBase Table:
 * ------------
 *
 *
 *
 * HDFS Folder:
 * ------------
 *
 */
public class BackLinkExposedThings {

    // TODO: Replace by ENUM !!!
    public static final String BACKLINK_TYPE_HIVE = "HIVE";
    public static final String BACKLINK_TYPE_HBASE = "HBASE";
    public static final String BACKLINK_TYPE_HDFS = "HDFS";






}
