package lithium.university;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TwitterRetrieveTest {
    @Mock
    private Twitter twitterTest;

    @InjectMocks
    private TwitterRetrieve twitterRetrieveTest;

    private Status mockStatus() {
        return Mockito.mock(Status.class);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void testRetrieveNegative() throws TwitterException {
        List<Status> l = twitterRetrieveTest.retrieveFromTwitter(twitterTest, -1);
        System.out.println("testRetrieveNegative pass");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveNothing() throws TwitterException {
        List<Status> l = twitterRetrieveTest.retrieveFromTwitter(twitterTest, 0);
        System.out.println("testRetrieveNothing pass");
    }

    @Test
    public void testRetrieveSomething() throws TwitterException {
        ResponseList<Status> fakeList = new FakeResponseList<>();
        fakeList.add(mockStatus());
        Mockito.when(twitterTest.getHomeTimeline(Mockito.any(Paging.class))).thenReturn(fakeList);
        List<Status> l = twitterRetrieveTest.retrieveFromTwitter(twitterTest, 1);
        System.out.println("Size Expected: 1, Size Actual: " + l.size());
        if(l.size() == 1){
            System.out.println("testRetrieveSomething pass");
        }
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
        System.out.println("Size Expected: " + size + ", Size Actual: " + l.size());
        if(size == l.size()) {
            System.out.println("testRetrieveMany pass");
        }
    }
}
