package com.cleancode.fitnesse.testablehtml;

import fitnesse.wiki.*;
import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestableHtmlTest {
    private PageData pageData;
    private PageCrawler crawler;
    private WikiPage root;
    private WikiPage testPage;

    private final String expectedResultNonTestPage = "클린 코드 실습 페이지입니다.<br/>결과를 비교해 보세요.";

    // For window system
    private final String expectedResultWithoutSuiteAndSetup = "<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content";
    private final String expectedResultWithSetupAndNonIgnoredSuite = "<div class=\"setup\">\r\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\r\n\t<a href=\"javascript:toggleCollapsable('');\">\r\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\r\n\t</a>\r\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SuiteSetUp\">.SuiteSetUp</a> <a href=\"SuiteSetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\r\n\t<div class=\"collapsable\" id=\"\">suiteSetUp</div>\r\n</div>\r\n<div class=\"setup\">\r\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\r\n\t<a href=\"javascript:toggleCollapsable('');\">\r\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\r\n\t</a>\r\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\r\n\t<div class=\"collapsable\" id=\"\">setup</div>\r\n</div>\r\n<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href=\"TearDown\">.TearDown</a><br/><div class=\"teardown\">\r\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\r\n\t<a href=\"javascript:toggleCollapsable('');\">\r\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\r\n\t</a>\r\n&nbsp;<span class=\"meta\">Tear Down: <a href=\"SuiteTearDown\">.SuiteTearDown</a> <a href=\"SuiteTearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\r\n\t<div class=\"collapsable\" id=\"\">suiteTearDown</div>\r\n</div>\r\n";
    private final String expectedResultWithSetupAndIgnoredSuite = "<div class=\"setup\">\r\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\r\n\t<a href=\"javascript:toggleCollapsable('');\">\r\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\r\n\t</a>\r\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\r\n\t<div class=\"collapsable\" id=\"\">setup</div>\r\n</div>\r\n<div class=\"setup\">\r\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\r\n\t<a href=\"javascript:toggleCollapsable('');\">\r\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\r\n\t</a>\r\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SuiteSetUp\">.SuiteSetUp</a> <a href=\"SuiteSetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\r\n\t<div class=\"collapsable\" id=\"\">suiteSetUp</div>\r\n</div>\r\n<div class=\"setup\">\r\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\r\n\t<a href=\"javascript:toggleCollapsable('');\">\r\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\r\n\t</a>\r\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\r\n\t<div class=\"collapsable\" id=\"\">setup</div>\r\n</div>\r\n<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href=\"TearDown\">.TearDown</a><br/><div class=\"teardown\">\r\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\r\n\t<a href=\"javascript:toggleCollapsable('');\">\r\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\r\n\t</a>\r\n&nbsp;<span class=\"meta\">Tear Down: <a href=\"SuiteTearDown\">.SuiteTearDown</a> <a href=\"SuiteTearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\r\n\t<div class=\"collapsable\" id=\"\">suiteTearDown</div>\r\n</div>\r\n<div class=\"teardown\">\r\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\r\n\t<a href=\"javascript:toggleCollapsable('');\">\r\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\r\n\t</a>\r\n&nbsp;<span class=\"meta\">Tear Down: <a href=\"TearDown\">.TearDown</a> <a href=\"TearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\r\n\t<div class=\"collapsable\" id=\"\">teardown</div>\r\n</div>\r\n";
    private final String expectedResultOnlySetupNonIgnoredSuite = "<div class=\"setup\">\r\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\r\n\t<a href=\"javascript:toggleCollapsable('');\">\r\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\r\n\t</a>\r\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\r\n\t<div class=\"collapsable\" id=\"\">setup</div>\r\n</div>\r\n<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href=\"TearDown\">.TearDown</a><br/>";
    private final String expectedResultOnlySetupIgnoredSuite = "<div class=\"setup\">\r\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\r\n\t<a href=\"javascript:toggleCollapsable('');\">\r\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\r\n\t</a>\r\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\r\n\t<div class=\"collapsable\" id=\"\">setup</div>\r\n</div>\r\n<div class=\"setup\">\r\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\r\n\t<a href=\"javascript:toggleCollapsable('');\">\r\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\r\n\t</a>\r\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\r\n\t<div class=\"collapsable\" id=\"\">setup</div>\r\n</div>\r\n<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href=\"TearDown\">.TearDown</a><br/><div class=\"teardown\">\r\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\r\n\t<a href=\"javascript:toggleCollapsable('');\">\r\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\r\n\t</a>\r\n&nbsp;<span class=\"meta\">Tear Down: <a href=\"TearDown\">.TearDown</a> <a href=\"TearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\r\n\t<div class=\"collapsable\" id=\"\">teardown</div>\r\n</div>\r\n";

    // For linux system
//    private final String expectedResultWithoutSuiteAndSetup = "<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content";
//    private final String expectedResultWithSetupAndNonIgnoredSuite = "<div class=\"setup\">\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n\t<a href=\"javascript:toggleCollapsable('');\">\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n\t</a>\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SuiteSetUp\">.SuiteSetUp</a> <a href=\"SuiteSetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n\t<div class=\"collapsable\" id=\"\">suiteSetUp</div>\n</div>\n<div class=\"setup\">\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n\t<a href=\"javascript:toggleCollapsable('');\">\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n\t</a>\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n\t<div class=\"collapsable\" id=\"\">setup</div>\n</div>\n<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href=\"TearDown\">.TearDown</a><br/><div class=\"teardown\">\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n\t<a href=\"javascript:toggleCollapsable('');\">\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n\t</a>\n&nbsp;<span class=\"meta\">Tear Down: <a href=\"SuiteTearDown\">.SuiteTearDown</a> <a href=\"SuiteTearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n\t<div class=\"collapsable\" id=\"\">suiteTearDown</div>\n</div>\n";
//    private final String expectedResultWithSetupAndIgnoredSuite = "<div class=\"setup\">\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n\t<a href=\"javascript:toggleCollapsable('');\">\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n\t</a>\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n\t<div class=\"collapsable\" id=\"\">setup</div>\n</div>\n<div class=\"setup\">\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n\t<a href=\"javascript:toggleCollapsable('');\">\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n\t</a>\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SuiteSetUp\">.SuiteSetUp</a> <a href=\"SuiteSetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n\t<div class=\"collapsable\" id=\"\">suiteSetUp</div>\n</div>\n<div class=\"setup\">\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n\t<a href=\"javascript:toggleCollapsable('');\">\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n\t</a>\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n\t<div class=\"collapsable\" id=\"\">setup</div>\n</div>\n<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href=\"TearDown\">.TearDown</a><br/><div class=\"teardown\">\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n\t<a href=\"javascript:toggleCollapsable('');\">\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n\t</a>\n&nbsp;<span class=\"meta\">Tear Down: <a href=\"SuiteTearDown\">.SuiteTearDown</a> <a href=\"SuiteTearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n\t<div class=\"collapsable\" id=\"\">suiteTearDown</div>\n</div>\n<div class=\"teardown\">\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n\t<a href=\"javascript:toggleCollapsable('');\">\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n\t</a>\n&nbsp;<span class=\"meta\">Tear Down: <a href=\"TearDown\">.TearDown</a> <a href=\"TearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n\t<div class=\"collapsable\" id=\"\">teardown</div>\n</div>\n";
//    private final String expectedResultOnlySetupNonIgnoredSuite = "<div class=\"setup\">\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n\t<a href=\"javascript:toggleCollapsable('');\">\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n\t</a>\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n\t<div class=\"collapsable\" id=\"\">setup</div>\n</div>\n<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href=\"TearDown\">.TearDown</a><br/>";
//    private final String expectedResultOnlySetupIgnoredSuite = "<div class=\"setup\">\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n\t<a href=\"javascript:toggleCollapsable('');\">\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n\t</a>\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n\t<div class=\"collapsable\" id=\"\">setup</div>\n</div>\n<div class=\"setup\">\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n\t<a href=\"javascript:toggleCollapsable('');\">\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n\t</a>\n&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n\t<div class=\"collapsable\" id=\"\">setup</div>\n</div>\n<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href=\"TearDown\">.TearDown</a><br/><div class=\"teardown\">\n\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n\t<a href=\"javascript:toggleCollapsable('');\">\n\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n\t</a>\n&nbsp;<span class=\"meta\">Tear Down: <a href=\"TearDown\">.TearDown</a> <a href=\"TearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n\t<div class=\"collapsable\" id=\"\">teardown</div>\n</div>\n";

    @BeforeEach
    public void setUp() throws Exception {
        root = InMemoryPage.makeRoot("RooT");
        crawler = root.getPageCrawler();
        testPage = addPageToContent("TestPage", "!define TEST_SYSTEM {slim}\nthe content");
    }


    @Test
    @DisplayName("Suite Setup, setup, tear-down, suite tear down 페이지가 모두 포함되어 있는 테스트 페이지")
    public void testableHtmlSurroundedSuiteSetupPages() throws Exception {
        addSetupTeardownPage();
        addSuiteSetupSuiteTeardownPage();

        pageData = testPage.getData();

        generateHtmlAndAssert(true, expectedResultWithSetupAndNonIgnoredSuite);
        generateHtmlAndAssert(false, expectedResultWithSetupAndIgnoredSuite);
    }

    @Test
    @DisplayName("Setup, tear-down 페이지만 포함되어 있는 테스트 페이지")
    public void testableHtmlSurroundedSetupPages() throws Exception {
        addSetupTeardownPage();

        pageData = testPage.getData();

        generateHtmlAndAssert(true, expectedResultOnlySetupNonIgnoredSuite);
        generateHtmlAndAssert(false, expectedResultOnlySetupIgnoredSuite);
    }


    @Test
    @DisplayName("Setup, Tear-down 페이지가 하나도 포함되어 있지 않는 테스트 페이지")
    public void testableHtmlwithoutSetupPages() throws Exception {
        pageData = testPage.getData();

        generateHtmlAndAssert(true, expectedResultWithoutSuiteAndSetup);
        generateHtmlAndAssert(false, expectedResultWithoutSuiteAndSetup);
    }

    @Test
    @DisplayName("일반 페이지의 테스트")
    public void testableHtmlwithNonTestPages() throws Exception {
        WikiPage nonTestPage = addPageToContent("CleanCodeExercise", "클린 코드 실습 페이지입니다.\n결과를 비교해 보세요.");
        generateHtmlAndAssertForCommonPage(nonTestPage);

        addSetupTeardownPage();
        generateHtmlAndAssertForCommonPage(nonTestPage);

        addSuiteSetupSuiteTeardownPage();
        generateHtmlAndAssertForCommonPage(nonTestPage);

    }

    private void addSetupTeardownPage() throws Exception {
        addPageToContent("SetUp", "setup");
        addPageToContent("TearDown", "teardown");
    }

    private void addSuiteSetupSuiteTeardownPage() throws Exception {
        addPageToContent("SuiteSetUp", "suiteSetUp");
        addPageToContent("SuiteTearDown", "suiteTearDown");
    }

    private WikiPage addPageToContent(String pageName, String content) throws Exception {
        return crawler.addPage(root, PathParser.parse(pageName), content);
    }

    private String removeSpecialCharacters(String expectedResult) {
        return expectedResult.replaceAll("[-]*\\d+", "");
    }

    private void generateHtmlAndAssert(boolean includeSuiteSetup, String expectedResult) throws Exception {
        String testableHtml = new TestableHtml().testableHtml(pageData, includeSuiteSetup);
        assertThat(removeSpecialCharacters(testableHtml), is(removeSpecialCharacters(expectedResult)));
    }

    private void generateHtmlAndAssertForCommonPage(WikiPage commonPage) throws Exception {
        pageData = commonPage.getData();

        generateHtmlAndAssert(true, expectedResultNonTestPage);
        generateHtmlAndAssert(false, expectedResultNonTestPage);

    }
}
