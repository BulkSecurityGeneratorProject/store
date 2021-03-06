package io.github.jhipster.application.config;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.config.CouchbaseConfigurer;
import org.testcontainers.couchbase.CouchbaseContainer;

import java.util.List;

@Configuration
public class DatabaseTestConfiguration extends AbstractCouchbaseConfiguration {

    private static String name;

    private static CouchbaseContainer couchbaseContainer;

    public DatabaseTestConfiguration(@Value("${spring.couchbase.bucket.name}") String name) {
        DatabaseTestConfiguration.name = name;
    }

    @Override
    @Bean(destroyMethod = "", name = BeanNames.COUCHBASE_ENV)
    public CouchbaseEnvironment couchbaseEnvironment() {
        return getCouchbaseContainer().getCouchbaseEnvironment();
    }

    @Override
    public Cluster couchbaseCluster() throws Exception {
        return getCouchbaseContainer().getCouchbaseCluster();
    }

    @Override
    protected List<String> getBootstrapHosts() {
        return Lists.newArrayList(getCouchbaseContainer().getContainerIpAddress());
    }

    @Override
    protected String getBucketName() {
        return name;
    }

    @Override
    protected String getBucketPassword() {
        return "";
    }

    @Override
    protected CouchbaseConfigurer couchbaseConfigurer() {
        return this;
    }

    private static CouchbaseContainer getCouchbaseContainer() {
        if (couchbaseContainer != null) {
            return couchbaseContainer;
        }
        couchbaseContainer = new CouchbaseContainer("couchbase/server:4.6.2")
            .withNewBucket(DefaultBucketSettings.builder()
                .name(name)
                .type(BucketType.COUCHBASE)
                .quota(100)
                .build());
        couchbaseContainer.start();
        return couchbaseContainer;
    }
}
