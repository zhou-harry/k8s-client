package com.harry.k8sclient.yaml;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClusterDomain {

    private String name;
    private String server;
    private String certificateAuthority;

    public ClusterDomain init(Map resource){
        if (null==resource){
            return this;
        }
        Map cluster = MapUtils.getMap(resource, "cluster");
        if (null==cluster){
            return this;
        }

        this.setName(MapUtils.getString(resource,"name"));

        this.setServer(MapUtils.getString(cluster,"server"));
        this.setCertificateAuthority(MapUtils.getString(cluster,"certificate-authority-data"));
        return this;
    }
}
