package tobis.showtracker;

import org.joda.time.LocalDate;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.mockito.cglib.core.Local;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class FunctionTests {
    private WatchListFunctions wlf;
    private List<Episode> watchList;

    /*
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass(){
    }
    */

    @Before
    public void setUp() {
        wlf = new WatchListFunctions();
        watchList = new ArrayList<>();
    }

    @After
    public void tearDown() {
        wlf = null;
    }

    @Test
    public void test_getEpisodesfromSeasonString() throws Exception {
        String seasonArray[] = {"Game Of Thrones", "5", "3", "02.04.16", "7"};
        watchList = wlf.getEpisodesfromSeasonString(seasonArray);

        assertEquals(3, watchList.size());

        Episode result1 = new Episode("Game Of Thrones", 5, 1, new LocalDate(2016, 4, 2), false);
        Episode result2 = new Episode("Game Of Thrones", 5, 2, new LocalDate(2016, 4, 9), false);
        Episode result3 = new Episode("Game Of Thrones", 5, 3, new LocalDate(2016, 4, 16), false);
        List<Episode> testList = new ArrayList<>();
        testList.add(result1);
        testList.add(result2);
        testList.add(result3);

        for (int i = 0; i < 3; i++) {
            Episode act = watchList.get(i);
            Episode exp = testList.get(i);
            assertEquals(exp.getShowName(), act.getShowName());
            assertEquals(exp.getSeasonNumber(), act.getSeasonNumber());
            assertEquals(exp.getEpisodeNumber(), act.getEpisodeNumber());
            assertEquals(exp.getDate(), act.getDate());
            assertEquals(exp.isWatchedStatus(), act.isWatchedStatus());
        }
    }

    //List<Episode> getReleasedEpisodeList(List<Episode> watchList)
    @Test
    public void test_getReleasedEpisodeList() throws Exception {
        LocalDate yesterday = new LocalDate().minusDays(1);
        String yStr = yesterday.toString("dd.MM.yy");
        String seasonArray[] = {"Test Show123", "2", "4", yStr, "1"};
        watchList = wlf.getEpisodesfromSeasonString(seasonArray);
        List<Episode> releasedList = wlf.getReleasedEpisodeList(watchList);

        assertEquals(2, releasedList.size());
        assertEquals(4, watchList.size());

        Episode result1 = new Episode("Test Show123", 2, 1, yesterday, false);
        Episode result2 = new Episode("Test Show123", 2, 2, yesterday.plusDays(1), false);
        List<Episode> testList = new ArrayList<>();
        testList.add(result1);
        testList.add(result2);

        for (int i = 0; i < 2; i++) {
            Episode act = watchList.get(i);
            Episode exp = testList.get(i);
            assertEquals(exp.getShowName(), act.getShowName());
            assertEquals(exp.getSeasonNumber(), act.getSeasonNumber());
            assertEquals(exp.getEpisodeNumber(), act.getEpisodeNumber());
            assertEquals(exp.getDate(), act.getDate());
            assertEquals(exp.isWatchedStatus(), act.isWatchedStatus());
        }
    }
}