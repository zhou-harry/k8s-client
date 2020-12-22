package com.harry.k8sclient.yaml;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.MapUtils;
import org.springframework.core.annotation.AliasFor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDomain {

    private String apiVersion;

    private String kind;

    private String currentContext;

    private List<ClusterDomain> clusters;

    private List<ContextDomain> contexts;

    private List<UserDomain> users;

    public ConfigDomain init(Map resource){
        if (null==resource){
            return this;
        }
        this.setApiVersion(MapUtils.getString(resource,"apiVersion"));
        this.setKind(MapUtils.getString(resource,"kind"));
        this.setCurrentContext(MapUtils.getString(resource,"current-context"));

        Object users = MapUtils.getObject(resource, "users");
        if (null!=users){
            this.users= Lists.newArrayList();
            ((List)users).forEach(map->{
                this.users.add(new UserDomain().init((Map)map));
            });
        }
        Object clusters = MapUtils.getObject(resource, "clusters");
        if (null!=clusters){
            this.clusters= Lists.newArrayList();
            ((List)clusters).forEach(map->{
                this.clusters.add(new ClusterDomain().init((Map)map));
            });
        }
        Object contexts = MapUtils.getObject(resource, "contexts");
        if (null!=contexts){
            this.contexts= Lists.newArrayList();
            ((List)contexts).forEach(map->{
                this.contexts.add(new ContextDomain().init((Map)map));
            });
        }
        return this;
    }
}
