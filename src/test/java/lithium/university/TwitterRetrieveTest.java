package lithium.university;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import twitter4j.*;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TwitterRetrieveTest {
    @Mock
    private Twitter twitterTest;

    @InjectMocks
    private TwitterRetrieve twitterRetrieveTest;

    private Status mockStatus() {
        Status s = Mockito.mock(Status.class);
        Mockito.when(s.getText()).thenReturn("This is a mocked status!");
        return s;
    }

    @Test(expected = NegativeArraySizeException.class)
    public void testRetrieveNegative() throws TwitterException {
        List<Status> l = twitterRetrieveTest.retrieveFromTwitter(twitterTest, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveNothing() throws TwitterException {
        List<Status> l = twitterRetrieveTest.retrieveFromTwitter(twitterTest, 0);
    }

    @Test
    public void testRetrieveSomething() throws TwitterException {
        ResponseList<Status> fakeList = new FakeResponseList<>();
        fakeList.add(mockStatus());
        Mockito.when(twitterTest.getHomeTimeline(Mockito.any(Paging.class))).thenReturn(fakeList);
        List<Status> l = twitterRetrieveTest.retrieveFromTwitter(twitterTest, 1);
        Assert.assertEquals("This is a mocked status!", l.get(0).getText());
    }

    @Test
    public void testRetrieveMany() throws TwitterException {
        int size = 100;
        ResponseList<Status> fakeList = new FakeResponseList<>();
        for(int i = 0; i < 100; i++){
            fakeList.add(mockStatus());
        }
        Mockito.when(twitterTest.getHomeTimeline(Mockito.any(Paging.class))).thenReturn(fakeList);
        List<Status> l = twitterRetrieveTest.retrieveFromTwitter(twitterTest, size);
        for(int i = 0; i < 100; i++){
            Assert.assertEquals("This is a mocked status!", l.get(0).getText());
        }
    }
}
