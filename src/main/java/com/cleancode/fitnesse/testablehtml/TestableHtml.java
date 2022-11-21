package com.cleancode.fitnesse.testablehtml;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class TestableHtml {

    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        return new TestableHtmlBuilder(pageData, includeSuiteSetup).invoke();
    }

    private class TestableHtmlBuilder {
        private PageData pageData;
        private boolean includeSuiteSetup;
        private WikiPage wikiPage;
            private StringBuffer buffer;

        public TestableHtmlBuilder(PageData pageData, boolean includeSuiteSetup) {
                this.pageData = pageData;
                this.includeSuiteSetup = includeSuiteSetup;
                wikiPage = pageData.getWikiPage();
                buffer = new StringBuffer();
            }

            public String invoke() throws Exception {
                if (isTestPage()) {
                    includeSetups();
                }

                buffer.append(pageData.getContent());
                if (isTestPage()) {
                    includeTearDowns();
                }

                pageData.setContent(buffer.toString());
                return pageData.getHtml();
            }

            private boolean isTestPage() throws Exception {
            return pageData.hasAttribute("Test");
        }

        private void includeTearDowns() throws Exception {
            includeInherited("teardown", "TearDown");
            if (includeSuiteSetup) {
                includeInherited("teardown", SuiteResponder.SUITE_TEARDOWN_NAME);
            }
        }

        private void includeSetups() throws Exception {
            if (includeSuiteSetup) {
                includeInherited("setup", SuiteResponder.SUITE_SETUP_NAME);
            }
            includeInherited("setup", "SetUp");
        }

        private void includeInherited(String mode, String pageName1) throws Exception {
            WikiPage suiteTeardown = PageCrawlerImpl.getInheritedPage(pageName1, wikiPage);
            if (suiteTeardown != null) {
                renderPage(mode, suiteTeardown);
            }
        }

        private void renderPage(String mode, WikiPage suiteSetup) throws Exception {
            WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(suiteSetup);
            String pagePathName = PathParser.render(pagePath);
            buffer.append("!include -" + mode + " .").append(pagePathName).append("\n");
        }
    }
}//1