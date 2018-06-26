package lithium.university;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;


import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TwitterRetrieveTest {
    @Mock
    private Twitter twitterTest;

    @InjectMocks
    private TwitterRetrieve twitterRetrieveTest;

    private Status mockStatus(String message){
        Status s = Mockito.mock(Status.class);
        Mockito.when(s.getText()).thenReturn(message);
        return s;
    }

    private Status mockStatus() {
        return mockStatus("This is a mocked status!");
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
        Assert.assertEquals(1, l.size());
        Assert.assertEquals(mockStatus().getText(), l.get(0).getText());
    }

    @Test
    public void testRetrieveMany() throws TwitterException {
        int size = 10;
        String testMessage = "Many num ";
        ResponseList<Status> fakeList = new FakeResponseList<>();
        for(int i = 0; i < size; i++){
            fakeList.add(mockStatus(testMessage + i));
        }
        Mockito.when(twitterTest.getHomeTimeline(Mockito.any(Paging.class))).thenReturn(fakeList);
        List<Status> l = twitterRetrieveTest.retrieveFromTwitter(twitterTest, size);
        Assert.assertEquals(size, l.size());
        for(int i = 0; i < size; i++){
            Assert.assertEquals(testMessage + i, l.get(i).getText());
        }
    }
}
