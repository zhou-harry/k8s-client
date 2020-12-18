package com.harry.k8sclient.examples;

import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.DaemonSetList;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.ReplicaSetList;
import io.fabric8.kubernetes.api.model.apps.StatefulSetList;
import io.fabric8.kubernetes.api.model.batch.CronJobList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

public class ListExamples {
    private static final Logger logger = LoggerFactory.getLogger(ListExamples.class);

    public static void main(String[] args) {
        String master = "https://192.168.148.128:6443/";
        String caCert = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUN5RENDQWJDZ0F3SUJBZ0lCQURBTkJna3Foa2lHOXcwQkFRc0ZBREFWTVJNd0VRWURWUVFERXdwcmRXSmwKY201bGRHVnpNQjRYRFRJd01EY3lOekUyTURZek1sb1hEVE13TURjeU5URTJNRFl6TWxvd0ZURVRNQkVHQTFVRQpBeE1LYTNWaVpYSnVaWFJsY3pDQ0FTSXdEUVlKS29aSWh2Y05BUUVCQlFBRGdnRVBBRENDQVFvQ2dnRUJBS3dnCklkNzkrMjJ2bHVKQ3BleGpYN2NYRUNSLzdrMUxLUjhpQ0NRcUFNaktiamJmWTFLNUo4TlBwTW9henhRdWltS0sKYndZWjBvNEx6WU9KTEtRZDN4SlloZ05obHNqS04xaDY4SzMzSVd4V08xRFljUnAyTTk2UlhEZDl3Znl3VlFUQgo0b2tFZk1nOVVOaGlrSGs1eE5ha1lyaHFKTWxsSHozTmpPNGRxeENzUndxZUh4REViVnBGQWliQXdCU1JxTFJ4CldnK0VJRHpTdE1Gbk14YlRlUU1Ld0xyREErOTlkcFZTMitIZEdpSFh2TjdhNUl0ZWdoMm5sMXVRN3BBZDN3K3EKUm1vWUVoNVdmTXRHeXloYjdIQjVkWEVrb1A3MWRkRHdPckRRRUVNaEZLUGc3ZWRVekxZcnpzRUhkTklzd2NIRwpHMXlSSXU4OEZtUkx2eGpOVWpVQ0F3RUFBYU1qTUNFd0RnWURWUjBQQVFIL0JBUURBZ0trTUE4R0ExVWRFd0VCCi93UUZNQU1CQWY4d0RRWUpLb1pJaHZjTkFRRUxCUUFEZ2dFQkFBVHl4dFRBeW1WbXVwQlI4WHpRZEQ1NFYyaDUKaDBWNFA0YStRd3NhcGsvb1F2dEhwV3BLVmM2bDdWc3V2Y3U0Nm9Yd0hoc0g4b3RDMDNzNFo1d0RGYkFmRDh1SApaanN5c25RKzk3UTRLc0FHemdiM3EwWlVoRVdxdHBlK25KaWZtczd0L1Mzclc2NG9ycVdTZjF1THZ4b09QVTdMCklCTGw3cHN5OVVMM3IwbHdSUkFGcUtvSzQ5TjN2YjRTTmVnOFh4dkd3b1BDK2ZaNXc2VzJSR0kwclhUNTlkSWgKUmFMRHJxOGpoUkUvZDJub3A2bUtkMHNzYTFuT001L0hCK29SSk1KSHRJYTNIMFNJSUFYOUZMZjZUQkhSbFE0QwpQRmlsVSt3elowWERkZzFjUTMrR0ZPZzdrSElPb1JwSlRlMjVCQmtxUnBhZGRHL3dPOWUzZmZIeTIyTT0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo=";
        String clientCert = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUM4akNDQWRxZ0F3SUJBZ0lJZjREY1dNM1Y0c293RFFZSktvWklodmNOQVFFTEJRQXdGVEVUTUJFR0ExVUUKQXhNS2EzVmlaWEp1WlhSbGN6QWVGdzB5TURBM01qY3hOakEyTXpKYUZ3MHlNVEEzTWpjeE5qQTJNelphTURReApGekFWQmdOVkJBb1REbk41YzNSbGJUcHRZWE4wWlhKek1Sa3dGd1lEVlFRREV4QnJkV0psY201bGRHVnpMV0ZrCmJXbHVNSUlCSWpBTkJna3Foa2lHOXcwQkFRRUZBQU9DQVE4QU1JSUJDZ0tDQVFFQXNCM29sN1M5aDdBZWtzNjcKS1RaNzQvL0txdm5PQ1B0MGg1YVNXTWR1ekovbGZBNmZxZ3E3bUtjY25qUTFMbTV0b29NSEhkUmpkRUlxNXpXNAppeUJEcm9FZ0FuWmxadmVuRXRxUW9zRGxHdXl5ZUlKazFoZVptSUxxWnlrYjdTUlNaYmw4NDd0TG54cTE0bmRvCll5ZUZCRzFBTlNNNjlscUR0UEN1UzBDSkp4M3VCQTNOYzJaZU1pWFdCOS81dFZFbXBDYVd5MlF4SmRkUmFKYmoKWFpzL2xBaUJRTFYyU1ZudnJrWGhEYnk4WlNDL0k3T1FJbjQ1QXRqNVVrQm5RdThqZ3NVMHpYeTVyKyt6ZzRkTQpvNG1xd0JFbGdMTnEzc1dxRDhxRU1USTRIK1JIYjhhOHlFa2lTY2d5d3RETDg2aHpVMFlwZlgvUVQrTjh2R2paClUvd3hKUUlEQVFBQm95Y3dKVEFPQmdOVkhROEJBZjhFQkFNQ0JhQXdFd1lEVlIwbEJBd3dDZ1lJS3dZQkJRVUgKQXdJd0RRWUpLb1pJaHZjTkFRRUxCUUFEZ2dFQkFKVnpIWDNNQXduTWhOTzNkVmwzWjAzYUZGNmxEOC9HdTdMZgpSYlpEbUxyWnQ3YkRuR2tnT0k4bmVlQnJNQzZUVUhiSUJHLzFyTmRDOXdJbk1VdUg5WDFsQjl3RTY3NkhDOVdyCnlwNysyUGRiWEVBU2NmM0laS25HL1pmNzVIUnNyeHlZOFoxTXQ5b08vMTZHakc3eTJYOXZCMHprMXE2enJmaVoKbVFqQ1U3Q0NWRXVuWGdkbnkrTlc4SU5MQ2xrY3FzR1FEVi9zMXBabHhvdUVOczRNSnFwY0p3QldnZnlvRkNFKwovcnMvelZJWnMzQVBYWm1zdkxEb05XdW1veWNBcXd2WmhieGZsU0paV2VOL2Era3JiQkZNMWFUb3ppaGJFUDhvCnFWWWtCZUxwU051emxncGxKL2tNUFJrT2V1S0xDVHBnaTNJQ1o4d2lYQmFOdnRNSG8yTT0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo=";
        String clientKey = "LS0tLS1CRUdJTiBSU0EgUFJJVkFURSBLRVktLS0tLQpNSUlFb2dJQkFBS0NBUUVBc0Izb2w3UzloN0Fla3M2N0tUWjc0Ly9LcXZuT0NQdDBoNWFTV01kdXpKL2xmQTZmCnFncTdtS2NjbmpRMUxtNXRvb01ISGRSamRFSXE1elc0aXlCRHJvRWdBblpsWnZlbkV0cVFvc0RsR3V5eWVJSmsKMWhlWm1JTHFaeWtiN1NSU1pibDg0N3RMbnhxMTRuZG9ZeWVGQkcxQU5TTTY5bHFEdFBDdVMwQ0pKeDN1QkEzTgpjMlplTWlYV0I5LzV0VkVtcENhV3kyUXhKZGRSYUpialhacy9sQWlCUUxWMlNWbnZya1hoRGJ5OFpTQy9JN09RCkluNDVBdGo1VWtCblF1OGpnc1Uwelh5NXIrK3pnNGRNbzRtcXdCRWxnTE5xM3NXcUQ4cUVNVEk0SCtSSGI4YTgKeUVraVNjZ3l3dERMODZoelUwWXBmWC9RVCtOOHZHalpVL3d4SlFJREFRQUJBb0lCQUIxUzNZZGs4S25IQ1ZBbwpXOEZnOFAvbGR6bEFucTJnTUtiaXl6czgrWlhZaWIwK01oZDk4ZjVZMU1zQ3BsZzB5c3hrUktzUzBmeHBoL3MzClZxTFRuSEZSenFLYVZBZnc5RUNQWThXMW1IQURPVFFYZ2ZLanUwOUpmUDN3T2xZaDFoWlo3QkYrL2ZoZjJpajYKK01hSnJmd3V0S0lrVzR6cFJVUzdUa241Vk56T09DSC8xRDZ4ZFFQNVdMeXQvNG4xMjRMaXRhTXI1c2s1eEdZOApaeVVJNm9mR2RkWDJwRGRPZ29vWHZveEVFdEgxVDNxMEZYcTI5ZUdSRi9ieENYaThNNythWXNJYkdSWG5FdDJpClJIeEdPbnM0enFYTHhCS0plUTVHUm95cWFtVDNvdEpESnBLeGtOcWNHemg3dDM0c3BlOFZxQ0Fjc3o3ZXhVK2gKaERlaUs0RUNnWUVBNEdqcWlzbGZ6bTM2empzckpaYzMwVHJCTlRQTkpVT2F5WXRWZjQ3TEFvYVRNb00xQktjawpzYnB2amVHN1BMM2g3UXRXSHIrOTdMRkNVQmphQVBvK0JmUVV6Umh2dHJWcDM2OHRMY2Z1Zk0xa0I5UkNkMy9OCi9Zcy84RzZ3VWpMblRrVHlaSFI1dnJ4RlY1VDlTa21odWhzc2Fld2V6NW11RWcvQmpYRDlURmNDZ1lFQXlPaW0KM2huQVIvR2dPTlRPY1RXZTFmVVMrZHBlZ2dFQTV6VVNoODh2OTVmeC9ENVZ5ZVNWZXU5M2g2RVBDSVZGaXlhTAptOVNTSFRLbEU2UEsvUE81MU5XdFdNWmVOQ0RPY1Y5K0d5TnMyZFR4ODZwUjhHaG93enU4WTBTOHFrSEYvTEJSCjRyTldjQWVUbkdLWEVyTHdHdW9ndGd4aXpRUGN1MnB0eG1xNmdPTUNnWUFrbVBlbktyQTFPanBzNTdsaHFBZXkKVjZLcXlvZnFTOXd5Z2thdlJ0cFV6eDJ5WHpPR1RydnRRRHB4S1I4Z0NOZVBrUW02ZWdxL1R5bEJac3dtM0tqQQp6ODJVcFNlUzVJTWZ0N0htaFhTZlpkK3Faek52eWFnT2NXYzhEdEV5bHBxaWFSd2V4ZUFVK3g5VDZUaGxwUng2Ci9YT0Ewc0V6bWh5SlZZaDdTTElHZ1FLQmdDRVVwYjAyVWtIR1dGSmpQK2JHSkhRb1dVcUltVDBndXl4V3djbFkKNVJZSWo5Q3YxdUphWC82UkJZWndqNUFnbVpYUzcvUGxxWFZqVEdrU2RDNzRWcVhqMVJvajk0anZ5ZTVEQWZtbgorSmxaaGNsUVJ4T2xyWEpRR0xIanRJajRNWWhzNDk3SEZybDFsZW1QRmo3KzlhZ0kydi8reEJucncrR2NJWjBsCkg3WnZBb0dBTjdidlVncmxOaWp6STArNUFLYXRIaXBSR3U2SCtWN3MwR1MrOWJHZUp3elZWaS9JQ28yTDFiUm8KUlJtWTI5Z1M1KzNrWDFHc0F5SklIM3QrRFhzVThyaDBqTjYxTForK2swNE5SeUVZSXIrVldmMU9BVTFmZUFpOApBYVF5aGtNTXJhSmIzWGFzcndSZEM5WVhFVlRScUNnN0lKa2R6dlRlT1FOZXA5d3BjU0k9Ci0tLS0tRU5EIFJTQSBQUklWQVRFIEtFWS0tLS0tCg==";

        String namespace = "kube-system";
        if (args.length == 1) {
            master = args[0];
        }
        Config config = new ConfigBuilder().withMasterUrl(master).withTrustCerts(true)
                .withCaCertData(caCert)
                .withClientCertData(clientCert)
                .withClientKeyData(clientKey)
                .build();
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
                showPods(client, metadata.getNamespace(), matchLabels);
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
                showPods(client, metadata.getNamespace(), matchLabels);
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
                showPods(client, metadata.getNamespace(), matchLabels);
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
                showPods(client, metadata.getNamespace(), matchLabels);
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
    }

    private static void showPods(KubernetesClient client, String ns, Map<String, String> matchLabels) {
        if (ObjectUtils.isEmpty(matchLabels)) {
            matchLabels = Maps.newHashMap();
        }
        PodList pods = client.pods().inNamespace(ns).withLabels(matchLabels).list();
        pods.getItems().forEach(pod -> {
            ObjectMeta podMetadata = pod.getMetadata();
            System.out.println("Pod=======>" + podMetadata.getName());
        });
    }
}
