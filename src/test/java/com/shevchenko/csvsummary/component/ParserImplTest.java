package com.shevchenko.csvsummary.component;

import com.shevchenko.csvsummary.component.impl.ParserImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

/**
 * @author Dmytro_Shevchenko4
 */
@RunWith(SpringRunner.class)
public class ParserImplTest {

    @InjectMocks
    private ParserImpl parser;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(ParserImplTest.class);
    }

    @Test
    public void testValidInput() throws Exception {
        Assert.assertTrue(parser.isValid(getFileWithName("valid.csv")));
    }

    @Test
    public void testInvalidInputData() throws Exception {
        Assert.assertFalse(parser.isValid(getFileWithName("invalidData.csv")));
    }

    @Test
    public void testInvalidInputHeaders() throws Exception {
        Assert.assertFalse(parser.isValid(getFileWithName("invalidHeaders.csv")));
    }

    @Test
    public void testParseHeaders() throws Exception {
        Assert.assertEquals("a, b", parser.parseHeaders(getFileWithName("valid.csv")));
    }

    @Test
    public void testSummaryForCorrectFile() throws Exception {
        Assert.assertEquals(new Double(3), parser.summarize(getFileWithName("valid.csv"), "a"));
        Assert.assertEquals(new Double(5), parser.summarize(getFileWithName("valid.csv"), "b"));
    }

    @Test(expected = NumberFormatException.class)
    public void testSummaryForWrongData() throws Exception {
        parser.summarize(getFileWithName("invalidData.csv"), "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSummaryForWrongHeader() throws Exception {
        parser.summarize(getFileWithName("valid.csv"), "c");
    }

    private File getFileWithName(String name) {
        return new File(getClass().getClassLoader().getResource(name).getFile());
    }
}
