package ma.projet.graph.config;

import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class GraphQLConfig {
    
    @Bean
    public GraphQLScalarType dateScalar() {
        return GraphQLScalarType.newScalar()
                .name("Date")
                .description("Date scalar type")
                .coercing(new Coercing<Date, String>() {
                    @Override
                    public String serialize(Object dataFetcherResult) {
                        if (dataFetcherResult instanceof Date) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            return dateFormat.format((Date) dataFetcherResult);
                        }
                        return null;
                    }
                    
                    @Override
                    public Date parseValue(Object input) {
                        if (input instanceof String) {
                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                return dateFormat.parse((String) input);
                            } catch (Exception e) {
                                throw new RuntimeException("Invalid date format", e);
                            }
                        }
                        return null;
                    }
                    
                    @Override
                    public Date parseLiteral(Object input) {
                        return parseValue(input);
                    }
                })
                .build();
    }
}

