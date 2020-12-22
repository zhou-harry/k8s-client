package com.harry.k8sclient.examples;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.batch.CronJobBuilder;
import io.fabric8.kubernetes.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/*
 * Creates a simple cronjob that runs every minute spinning a pod that echoes hello world.
 */
public class CronJobExample {
    private static final Logger logger = LoggerFactory.getLogger(CronJobExample.class);

    public static void main(String[] args) {
        Config config = ConfigUtil.getConfig();
        config.setNamespace("default");
        try(final KubernetesClient client = new DefaultKubernetesClient(config)) {
            final String namespace = client.getNamespace();

            CronJob cronJob1 = new CronJobBuilder()
                    .withApiVersion("batch/v1beta1")
                    .withNewMetadata()
                    .withName("hello")
                    .withLabels(Collections.singletonMap("foo", "bar"))
                    .endMetadata()
                    .withNewSpec()
                    .withSchedule("*/1 * * * *")
                    .withNewJobTemplate()
                    .withNewSpec()
                    .withNewTemplate()
                    .withNewSpec()
                    .addNewContainer()
                    .withName("hello")
                    .withImage("busybox")
                    .withArgs("/bin/sh", "-c", "date; echo Hello from Kubernetes")
                    .endContainer()
                    .withRestartPolicy("OnFailure")
                    .endSpec()
                    .endTemplate()
                    .endSpec()
                    .endJobTemplate()
                    .endSpec()
                    .build();

            log("Creating cron job from object");
            cronJob1 = client.batch().cronjobs().inNamespace(namespace).create(cronJob1);
            log("Successfully created cronjob with name ", cronJob1.getMetadata().getName());

            log("Watching over pod which would be created during cronjob execution...");
            final CountDownLatch watchLatch = new CountDownLatch(1);
            try (Watch watch = client.pods().inNamespace(namespace).withLabel("job-name").watch(new Watcher<Pod>() {
                @Override
                public void eventReceived(Action action, Pod aPod) {
                    logger.info("action: {}", action);
                    log(aPod.getMetadata().getName(), aPod.getStatus().getPhase());
                    if(aPod.getStatus().getPhase().equals("Succeeded")) {
                        log("Logs -> ", client.pods().inNamespace(namespace).withName(aPod.getMetadata().getName()).getLog());
                        watchLatch.countDown();
                    }
                }

                @Override
                public void onClose(KubernetesClientException e) {
                    // Ignore
                }
            })) {
                watchLatch.await(2, TimeUnit.MINUTES);
            } catch (KubernetesClientException | InterruptedException e) {
                log("Could not watch pod", e);
            }
        } catch (KubernetesClientException exception) {
            log("An error occured while processing cronjobs:", exception.getMessage());
        }
    }

    private static void log(String action, Object obj) {
        logger.info("{}: {}", action, obj);
    }

    private static void log(String action) {
        logger.info(action);
    }
}
