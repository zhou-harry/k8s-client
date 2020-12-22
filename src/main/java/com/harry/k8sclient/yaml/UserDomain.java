package com.harry.k8sclient.yaml;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDomain {
    private String name;
    private String userName;
    private String password;
    private String clientCertificate;
    private String clientKey;

    public UserDomain init(Map resource){
        if (null==resource){
            return this;
        }
        Map user = MapUtils.getMap(resource, "user");
        if (null==user){
            return this;
        }

        this.setName(MapUtils.getString(resource,"name"));

        this.setUserName(MapUtils.getString(user,"username"));
        this.setPassword(MapUtils.getString(user,"password"));
        this.setClientCertificate(MapUtils.getString(user,"client-certificate-data"));
        this.setClientKey(MapUtils.getString(user,"client-key-data"));
        return this;
    }
}
