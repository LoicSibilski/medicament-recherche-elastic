package app.m2i.medic.configs;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
@Configuration
//@EnableElasticsearchRepositories(basePackages = "app.m2i.medic.repositories.elastic")
public class ElasticConfig extends ElasticsearchConfigurationSupport {
    @Value("localhost:9200")
    private String host;
    @Value("elastic}")
    private String username;
    @Value("changeme")
    private String password;
    
    @Bean
    public RestHighLevelClient client() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
            .connectedTo(host)
        	.withBasicAuth("elastic","changeme")
            .build();
        return RestClients.create(clientConfiguration).rest();
    }
    
    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }
	
    
}
