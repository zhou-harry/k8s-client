package com.harry.k8sclient.yaml;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContextDomain {
    private String name;
    private String cluster;
    private String user;

    public ContextDomain init(Map resource){
        if (null==resource){
            return this;
        }
        Map context = MapUtils.getMap(resource, "context");
        if (null==context){
            return this;
        }

        this.setName(MapUtils.getString(resource,"name"));

        this.setCluster(MapUtils.getString(context,"cluster"));
        this.setUser(MapUtils.getString(context,"user"));
        return this;
    }
}
