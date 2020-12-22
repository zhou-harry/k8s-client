package com.harry.k8sclient.controller;

import com.esotericsoftware.yamlbeans.YamlException;
import com.harry.k8sclient.examples.ListExamples;
import com.harry.k8sclient.yaml.ConfigReader;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.DaemonSetList;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.ReplicaSetList;
import io.fabric8.kubernetes.api.model.apps.StatefulSetList;
import io.fabric8.kubernetes.api.model.batch.CronJobList;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Autowired
    private ConfigReader configReader;

    @GetMapping("/home")
    public String getFeign() {
        Config config = new ConfigBuilder().build();
        try (final KubernetesClient client = new DefaultKubernetesClient(config)) {

            /**
             * 节点
             */
            System.out.println();
            System.out.println("Nodes================================================================");
            NodeList nodes = client.nodes().list();
            nodes.getItems().forEach(node -> {
                ObjectMeta metadata = node.getMetadata();
                System.out.println("Node:" + metadata.getName() +
                        "-->Namespace:" + metadata.getNamespace() +
                        "-->ClusterName:" + metadata.getClusterName());
                List<NodeAddress> addresses = node.getStatus().getAddresses();
                addresses.forEach(address -> {
                    System.out.println("=======>" + address.getType() + ": " + address.getAddress());
                });
            });
            /**
             * Namespace
             */
            System.out.println();
            System.out.println("Namespaces================================================================");
            NamespaceList namespaces = client.namespaces().list();
            namespaces.getItems().forEach(ns -> {
                ObjectMeta metadata = ns.getMetadata();
                System.out.println("Namespace:" + metadata.getName() + "-->ClusterName:" + metadata.getClusterName());
                //Namespace下的Pod
                PodList pods = client.pods().inNamespace(metadata.getName()).list();
                pods.getItems().forEach(pod -> {
                    ObjectMeta podMetadata = pod.getMetadata();
                    System.out.println("Pod=======>" + podMetadata.getName());
                });
                //Namespace下的Service
                ServiceList services = client.services().inNamespace(metadata.getName()).list();
                services.getItems().forEach(svc -> {
                    ObjectMeta svcMetadata = svc.getMetadata();
                    System.out.println("Service=======>" + svcMetadata.getName());
                });
                //Namespace下的Deployment
                DeploymentList deployments = client.apps().deployments().inNamespace(metadata.getName()).list();
                deployments.getItems().forEach(deploy -> {
                    ObjectMeta deployMetadata = deploy.getMetadata();
                    System.out.println("Deployment=======>" + deployMetadata.getName());
                });
                //Namespace下的DaemonSet
                DaemonSetList daemonSets = client.apps().daemonSets().inNamespace(metadata.getName()).list();
                daemonSets.getItems().forEach(ds -> {
                    ObjectMeta dsMetadata = ds.getMetadata();
                    System.out.println("DaemonSet=======>" + dsMetadata.getName());
                });
                //Namespace下的ReplicaSet
                ReplicaSetList replicaSets = client.apps().replicaSets().inNamespace(metadata.getName()).list();
                replicaSets.getItems().forEach(rs -> {
                    ObjectMeta rsMetadata = rs.getMetadata();
                    System.out.println("ReplicaSet=======>" + rsMetadata.getName());
                });
                //Namespace下的StatefulSet
                StatefulSetList statefulSets = client.apps().statefulSets().inNamespace(metadata.getName()).list();
                statefulSets.getItems().forEach(sts -> {
                    ObjectMeta stsMetadata = sts.getMetadata();
                    System.out.println("StatefulSet=======>" + stsMetadata.getName());
                });
                //Namespace下的CronJob
                CronJobList cronJobs = client.batch().cronjobs().inNamespace(metadata.getName()).list();
                cronJobs.getItems().forEach(cj -> {
                    ObjectMeta jobMetadata = cj.getMetadata();
                    System.out.println("CronJob=======>" + jobMetadata.getName());
                });
            });
            /**
             * DaemonSet
             */
            System.out.println();
            System.out.println("DaemonSet================================================================");
            DaemonSetList daemonSets = client.apps().daemonSets().list();
            daemonSets.getItems().forEach(ds -> {
                ObjectMeta metadata = ds.getMetadata();
                System.out.println("DaemonSet:" + metadata.getName() +
                        "-->Namespace:" + metadata.getNamespace() +
                        "-->ClusterName:" + metadata.getClusterName());
                //DaemonSet下的Pods
                Map<String, String> matchLabels = ds.getSpec().getSelector().getMatchLabels();
                ListExamples.showPods(client, metadata.getNamespace(), matchLabels);
            });
            /**
             * Deployment
             */
            System.out.println();
            System.out.println("Deployment================================================================");
            DeploymentList deployments = client.apps().deployments().list();
            deployments.getItems().forEach(deploy -> {
                ObjectMeta metadata = deploy.getMetadata();
                System.out.println("Deployment:" + metadata.getName() + "-->Namespace:" + metadata.getNamespace() + "-->ClusterName:" + metadata.getClusterName());
                //Deployment下的Pods
                Map<String, String> matchLabels = deploy.getSpec().getSelector().getMatchLabels();
                ListExamples.showPods(client, metadata.getNamespace(), matchLabels);
            });
            /**
             * StatefulSet
             */
            System.out.println();
            System.out.println("StatefulSet================================================================");
            StatefulSetList statefulSets = client.apps().statefulSets().list();
            statefulSets.getItems().forEach(sts -> {
                ObjectMeta metadata = sts.getMetadata();
                System.out.println("StatefulSet:" + metadata.getName() + "-->Namespace:" + metadata.getNamespace() + "-->ClusterName:" + metadata.getClusterName());
                //StatefulSet下的Pods
                Map<String, String> matchLabels = sts.getSpec().getSelector().getMatchLabels();
                ListExamples.showPods(client, metadata.getNamespace(), matchLabels);
            });
            /**
             * Service
             */
            System.out.println();
            System.out.println("Service================================================================");
            ServiceList services = client.services().list();
            services.getItems().forEach(svc -> {
                ObjectMeta metadata = svc.getMetadata();
                System.out.println("Service=======>" + metadata.getName() + "-->Namespace:" + metadata.getNamespace() + "-->ClusterName:" + metadata.getClusterName());
                //Service下的Pods
                Map<String, String> matchLabels = svc.getSpec().getSelector();
                ListExamples.showPods(client, metadata.getNamespace(), matchLabels);
            });
            /**
             * Pod
             */
            System.out.println();
            System.out.println("Pod================================================================");
            PodList pods = client.pods().list();
            pods.getItems().forEach(pod -> {
                ObjectMeta metadata = pod.getMetadata();
                String hostIP = pod.getStatus().getHostIP();
                System.out.println("Pod=======>" + metadata.getName() + "-->Namespace:" + metadata.getNamespace() + "-->ClusterName:" + metadata.getClusterName() + "-->主机IP:" + hostIP);
                //Pods下的容器
                List<Container> containers = pod.getSpec().getContainers();
                containers.forEach(container -> {
                    System.out.println("Container=======>" + container.getName() + "-->Image:" + container.getImage());
                });
            });
        } catch (KubernetesClientException e) {
            logger.error(e.getMessage(), e);
        }
        try {
            configReader.initConfig();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (YamlException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "hello";
    }

}
