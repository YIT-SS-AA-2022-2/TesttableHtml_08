package com.cleancode.fitnesse.testablehtml;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class TestableHtml {

    public static final String INCLUDE_SETUP = "!include -setup .";
    public static final String INCLUDE_TEARDOWN = "!include -teardown .";

    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        WikiPage wikiPage = pageData.getWikiPage();
        StringBuffer pagebuffer = new StringBuffer();

        if (pageData.hasAttribute("Test")) {
            if (includeSuiteSetup) {
                WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_SETUP_NAME, wikiPage);
                if (suiteSetup != null) {
                    randerpage(wikiPage, suiteSetup, pagebuffer, INCLUDE_SETUP);
                }
            }
            WikiPage setup = PageCrawlerImpl.getInheritedPage("SetUp", wikiPage);
            if (setup != null) {
                randerpage(wikiPage, setup, pagebuffer,INCLUDE_SETUP);
            }
        }

        pagebuffer.append(pageData.getContent());
        if (pageData.hasAttribute("Test")) {
            WikiPage teardown = PageCrawlerImpl.getInheritedPage("TearDown", wikiPage);
            if (teardown != null) {
                randerpage(wikiPage, teardown, pagebuffer, INCLUDE_TEARDOWN);
            }
            if (includeSuiteSetup) {
                WikiPage suiteTeardown = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_TEARDOWN_NAME, wikiPage);
                if (suiteTeardown != null) {
                    randerpage(wikiPage, suiteTeardown, pagebuffer, INCLUDE_TEARDOWN);
                }
            }
        }

        pageData.setContent(pagebuffer.toString());
        return pageData.getHtml();
    }

    private static void randerpage(WikiPage wikiPage, WikiPage suiteSetup, StringBuffer pagebuffer, String str) throws Exception {
        WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(suiteSetup);
        String pagePathName = PathParser.render(pagePath);
        pagebuffer.append(str).append(pagePathName).append("\n");
    }
}
