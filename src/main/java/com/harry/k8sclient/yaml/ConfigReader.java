package com.harry.k8sclient.yaml;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

@Component
public class ConfigReader {


    public void initConfig() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("config");

        // 获得File对象，当然也可以获取输入流对象
        File file = classPathResource.getFile();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        YamlReader reader = new YamlReader(bufferedReader);
        Map read = reader.read(Map.class);
        ConfigDomain domain = new ConfigDomain().init(read);
        System.out.println(domain);
    }
}
