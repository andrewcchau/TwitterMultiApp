package lithium.university;

import dagger.Component;
import lithium.university.resources.TwitterResource;

import javax.inject.Singleton;

@Singleton
@Component(modules = TwitterProvider.class)
public interface TwitterSetUp {
    TwitterResource resource();
    TwitterHealthCheck healthCheck();
}
