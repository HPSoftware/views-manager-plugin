import org.kohsuke.stapler.Stapler

/*
<!--
The MIT License

Copyright (c) 2004-2009, Sun Microsystems, Inc., Kohsuke Kawaguchi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
-->

<!--
  Render build histories.
-->

<?jelly escape-by-default='true'?>
<j:jelly xmlns:j="jelly:core" xmlns:st="jelly:stapler" xmlns:d="jelly:define" xmlns:l="/lib/layout" xmlns:t="/lib/hudson" xmlns:f="/lib/form" xmlns:i="jelly:fmt">
    <j:set target="${it}" property="nextBuildNumberToFetch" value="${it.owner.nextBuildNumber}"/>
    <!-- build history -->
    <j:forEach var="build" items="${it.renderList}">
        <st:include page="/hudson/widgets/HistoryWidget/entry.jelly" />
    </j:forEach>
</j:jelly>
*/


//<!--

def st = namespace("jelly:stapler")
def j = namespace("jelly:core")
def l = namespace(lib.LayoutTagLib)

//l.div(Stapler.getCurrentRequest().getParameter("buildsFilter"))

my.nextBuildNumberToFetch = my.owner.nextBuildNumber

my.renderList.each { build ->
    context.setVariable("build", build)
    st.include(page:"/hudson/widgets/HistoryWidget/entry.jelly")
}



//-->