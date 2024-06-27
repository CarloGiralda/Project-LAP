package LAP.Blockchain.service;

import lombok.AllArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DiscoveryClientService {

    private final DiscoveryClient discoveryClient;

    public String getServiceUrl(String serviceName){
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);

        if (serviceInstances.isEmpty()){
            throw new IllegalStateException("No instances available for " + serviceName);
        }

        // TODO here I get the first instance but with more than one instance one could implement load balancing algorithms
        return serviceInstances.get(0).getUri().toString();
    }

    public List<String> getServiceInstances(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        return instances.stream()
                .map(ServiceInstance::getHost)
                .collect(Collectors.toList());
    }
}
