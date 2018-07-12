package lithium.university;

import dagger.Component;
import lithium.university.resources.TwitterResource;

import javax.inject.Singleton;

@Singleton
@Component
public interface TwitterSetUp {
    TwitterResource resource();
    TwitterHealthCheck healthCheck();
}
