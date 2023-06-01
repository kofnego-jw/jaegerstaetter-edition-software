<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tei="http://www.tei-c.org/ns/1.0"
    xmlns:uibk="http://www.uibk.ac.at/brenner-archiv/xml/ns"
    exclude-result-prefixes="tei uibk"
    version="2.0">
        
    <xsl:output method="xml" indent="yes"/>
    
    <xsl:param name="fileName">B1_27.xml</xsl:param>
    
    <xsl:template match="/">
        <MetadataResult>
            <id>
                <xsl:value-of select="$fileName"/>
            </id>
            <title>
                <xsl:call-template name="createTitle"/>
            </title>
            <dating>
                <xsl:call-template name="getDating"/>
            </dating>
            <signature>
                <xsl:value-of select="//tei:msIdentifier/tei:idno"/>
            </signature>
            <altSignature>
                <xsl:value-of select="//tei:msIdentifier/tei:altIdentifier/tei:idno"/>
            </altSignature>
            <type>
                <xsl:choose>
                    <xsl:when test="matches($fileName, '^.*B\d+.*$')">
                        <xsl:text>LETTER</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>DOCUMENT</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </type>
            <summary>
                <xsl:call-template name="getSummary"/>
            </summary>
            <xsl:for-each select="//tei:body/tei:div[@type='Lesefassung']//tei:pb/@facs">
                <facsimileIds>
                    <xsl:call-template name="normalizeFacsimileId">
                        <xsl:with-param name="facId" select="normalize-space(.)"/>
                    </xsl:call-template>
                </facsimileIds>
            </xsl:for-each>
            <xsl:apply-templates select="//tei:correspDesc"/>
            <xsl:apply-templates select="//tei:msIdentifier"/>
            <xsl:apply-templates select="//tei:physDesc"/>
            <xsl:apply-templates select="//tei:titleStmt" mode="respMetadata"/>
            <xsl:apply-templates select="//tei:history" mode="respMetadata"/>
            <xsl:call-template name="createResourceFWFields"/>
            <xsl:call-template name="findRelatedLetters"/>
        </MetadataResult>
    </xsl:template>
    
    <xsl:template name="normalizeFacsimileId">
        <xsl:param name="facId"/>
        <xsl:choose>
            <xsl:when test="ends-with(lower-case($facId), '.jpg')">
                <xsl:value-of select="substring($facId, 1, string-length($facId) - 4)"/>
            </xsl:when>
            <xsl:when test="ends-with(lower-case($facId), '.jpeg')">
                <xsl:value-of select="substring($facId, 1, string-length($facId) - 5)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$facId"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="findRelatedLetters">
        <xsl:if test="//tei:correspContext">
            <xsl:for-each select="//tei:correspContext/tei:ref[@type='prev' and @target]">
                <xsl:if test="not(contains(@target, '#'))">
                    <prevLetterIds>
                        <xsl:value-of select="@target"/>
                    </prevLetterIds>
                </xsl:if>
            </xsl:for-each>
            <xsl:for-each select="//tei:correspContext/tei:ref[@type='next' and @target]">
                <xsl:if test="not(contains(@target, '#'))">
                    <nextLetterIds>
                        <xsl:value-of select="@target"/>
                    </nextLetterIds>
                </xsl:if>
            </xsl:for-each>
            <xsl:for-each select="//tei:correspContext/tei:ref[@type='relation' and @target]">
                <xsl:if test="not(contains(@target, '#'))">
                    <relatedLetterIds>
                        <xsl:value-of select="@target"/>
                    </relatedLetterIds>
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    
    
    
    <xsl:template name="getDating">
        <xsl:choose>
            <xsl:when test="//tei:correspAction[@type='sent']//tei:date">
                <xsl:variable name="dateElement" select="(//tei:correspAction[@type='sent']//tei:date)[1]"/>
                <xsl:choose>
                    <xsl:when test="$dateElement/@when">
                        <xsl:value-of select="$dateElement/@when"/>
                    </xsl:when>
                    <xsl:when test="$dateElement/@from">
                        <xsl:value-of select="$dateElement/@from"/>
                    </xsl:when>
                    <xsl:when test="$dateElement/@notBefore">
                        <xsl:value-of select="$dateElement/@notBefore"/>
                    </xsl:when>
                    <xsl:when test="$dateElement/@to">
                        <xsl:value-of select="$dateElement/@to"/>
                    </xsl:when>
                    <xsl:when test="$dateElement/@notAfter">
                        <xsl:value-of select="$dateElement/@notAfter"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$dateElement/@notAfter-iso"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when test="//tei:origin//tei:date">
                <xsl:variable name="dateElement" select="(//tei:origin//tei:date)[1]"/>
                <xsl:choose>
                    <xsl:when test="$dateElement/@when">
                        <xsl:value-of select="$dateElement/@when"/>
                    </xsl:when>
                    <xsl:when test="$dateElement/@from">
                        <xsl:value-of select="$dateElement/@from"/>
                    </xsl:when>
                    <xsl:when test="$dateElement/@notBefore">
                        <xsl:value-of select="$dateElement/@notBefore"/>
                    </xsl:when>
                    <xsl:when test="$dateElement/@to">
                        <xsl:value-of select="$dateElement/@to"/>
                    </xsl:when>
                    <xsl:when test="$dateElement/@notAfter">
                        <xsl:value-of select="$dateElement/@notAfter"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$dateElement/@notAfter-iso"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>9999-12-31</xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="createResourceFWFields">
        <xsl:if test="//tei:correspAction[@type = 'sent']//tei:rs[@ref = '#P_0001'] or not(//tei:correspDesc)">
            <corpus>
                <xsl:text>FROM_FRANZ</xsl:text>
            </corpus>
        </xsl:if>
        <xsl:if test="//tei:correspAction[@type = 'received']//tei:rs[@ref = '#P_0001']">
            <corpus>
                <xsl:text>TO_FRANZ</xsl:text>
            </corpus>
        </xsl:if>
        <xsl:if
            test="//tei:correspDesc and not(//tei:correspAction[@type = 'received']//tei:rs[@ref = '#P_0001']) and not(//tei:correspAction[@type = 'sent']//tei:rs[@ref = '#P_0001'])">
            <corpus>
                <xsl:text>ABOUT_FRANZ</xsl:text>
            </corpus>
        </xsl:if>
        <xsl:if test="//tei:correspDesc">
            <corpus>
                <xsl:text>CORRESPONDENCE</xsl:text>
            </corpus>
        </xsl:if>
        <!-- 
            Timeline
            1940-06-16: EARLY
            1941-09-04: MILITARY
            1943-03-02: TO_REFUSAL
            1943-08-09: PRISON
                        AFTERLIFE
        -->
        <xsl:variable name="notBefore">
            <xsl:call-template name="findEarliest"/>
        </xsl:variable>
        <xsl:variable name="notAfter">
            <xsl:call-template name="findLatest"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$notBefore &lt;= '1940-06-16'">
                <periods>
                    <xsl:text>EARLY</xsl:text>
                </periods>
                <xsl:if test="$notAfter &gt; '1940-06-16'">
                    <periods>
                        <xsl:text>MILITARY</xsl:text>
                    </periods>
                </xsl:if>
                <xsl:if test="$notAfter &gt; '1941-09-04'">
                    <periods>
                        <xsl:text>TO_REFUSAL</xsl:text>
                    </periods>
                </xsl:if>
                <xsl:if test="$notAfter &gt;= '1943-03-02'">
                    <periods>
                        <xsl:text>PRISON</xsl:text>
                    </periods>
                </xsl:if>
                <xsl:if test="$notAfter &gt; '1943-08-09'">
                    <periods>
                        <xsl:text>AFTERLIFE</xsl:text>
                    </periods>
                </xsl:if>
            </xsl:when>
            <xsl:when test="$notBefore &lt;= '1941-09-04'">
                <periods>
                    <xsl:text>MILITARY</xsl:text>
                </periods>
                <xsl:if test="$notAfter &gt; '1941-09-04'">
                    <periods>
                        <xsl:text>TO_REFUSAL</xsl:text>
                    </periods>
                </xsl:if>
                <xsl:if test="$notAfter &gt;= '1943-03-02'">
                    <periods>
                        <xsl:text>PRISON</xsl:text>
                    </periods>
                </xsl:if>
                <xsl:if test="$notAfter &gt; '1943-08-09'">
                    <periods>
                        <xsl:text>AFTERLIFE</xsl:text>
                    </periods>
                </xsl:if>
            </xsl:when>
            <xsl:when test="$notBefore &lt;= '1943-03-02'">
                <periods>
                    <xsl:text>TO_REFUSAL</xsl:text>
                </periods>
                <xsl:if test="$notAfter &gt;= '1943-03-02'">
                    <periods>
                        <xsl:text>PRISON</xsl:text>
                    </periods>
                </xsl:if>
                <xsl:if test="$notAfter &gt; '1943-08-09'">
                    <periods>
                        <xsl:text>AFTERLIFE</xsl:text>
                    </periods>
                </xsl:if>
            </xsl:when>
            <xsl:when test="$notBefore &lt;= '1943-08-09'">
                <periods>
                    <xsl:text>PRISON</xsl:text>
                </periods>
                <xsl:if test="$notAfter &gt; '1943-08-09'">
                    <periods>
                        <xsl:text>AFTERLIFE</xsl:text>
                    </periods>
                </xsl:if>
            </xsl:when>
            <xsl:when test="$notBefore &gt; '1943-08-09'">
                <periods>
                    <xsl:text>AFTERLIFE</xsl:text>
                </periods>
            </xsl:when>
        </xsl:choose>

        <xsl:for-each select="//tei:correspAction[@type = 'sent']/tei:rs[@type = 'person']">
            <authors>
                <xsl:choose>
                    <xsl:when test="@key">
                        <xsl:value-of select="uibk:keytoname(@key, 'person', 'true')"/>
                    </xsl:when>
                    <xsl:when test="@ref">
                        <xsl:variable name="key">
                            <xsl:choose>
                                <xsl:when test="contains(@ref, '#')">
                                    <xsl:value-of select="substring-after(@ref, '#')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="@ref"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:variable>
                        <xsl:value-of select="uibk:keytoname($key, 'person', 'true')"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>UNBEKANNT</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </authors>
        </xsl:for-each>
        <xsl:if test="not(//tei:correspAction[@type = 'sent'])">
            <authors>Jägerstätter, Franz</authors>
        </xsl:if>

        <xsl:for-each select="//tei:correspAction[@type = 'received']/tei:rs[@type = 'person']">
            <recipients>
                <xsl:choose>
                    <xsl:when test="@key">
                        <xsl:value-of select="uibk:keytoname(@key, 'person', 'true')"/>
                    </xsl:when>
                    <xsl:when test="@ref">
                        <xsl:variable name="key">
                            <xsl:choose>
                                <xsl:when test="contains(@ref, '#')">
                                    <xsl:value-of select="substring-after(@ref, '#')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="@ref"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:variable>
                        <xsl:value-of select="uibk:keytoname($key, 'person', 'true')"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>UNBEKANNT</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </recipients>
        </xsl:for-each>

        <xsl:for-each select="//tei:correspAction[@type = 'sent']/tei:rs[@type = 'place'] | //tei:origin//tei:rs[@type='place']">
            <places>
                <xsl:value-of select="uibk:keytoname(@key, 'place', 'true')"/>
            </places>
        </xsl:for-each>
        
        <!-- Briefe, Karten, lose Blätter, Hefte -->
        <objectTypes>
            <xsl:choose>
                <xsl:when test="starts-with($fileName, 'L1_H')">
                    <xsl:text>Hefte</xsl:text>
                </xsl:when>
                <xsl:when test="starts-with($fileName, 'L')">
                    <xsl:text>Lose Blätter</xsl:text>
                </xsl:when>
                <xsl:when test="//tei:p//tei:objectType">
                    <xsl:variable name="mainString">
                        <xsl:apply-templates select="//tei:p[.//tei:objectType]" mode="notInObjectType"/>
                    </xsl:variable>
                    <xsl:choose>
                        <xsl:when test="contains(lower-case($mainString), 'brief')">
                            <xsl:text>Briefe</xsl:text>
                        </xsl:when>
                        <xsl:when test="contains(lower-case($mainString), 'kart')">
                            <xsl:text>Karten</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>Briefe</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>Briefe</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </objectTypes>


    </xsl:template>
    
    <xsl:template match="tei:*" mode="notInObjectType">
        <xsl:apply-templates mode="notInObjectType"/>
    </xsl:template>
    <xsl:template match="tei:objectType" mode="notInObjectType">
        <!-- SKIP -->
    </xsl:template>
    
    <xsl:template name="findEarliest">
        <xsl:for-each select="(//tei:origin//tei:date/(@when|@notBefore) | //tei:correspDesc//tei:date/(@when|@notBefore))">
            <xsl:sort/>
            <xsl:if test="position()=1">
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template name="findLatest">
        <xsl:choose>
            <xsl:when test="//tei:correspAction[@type='sent']//tei:date[@when and not(parent::tei:note)]">
                <xsl:value-of select="(//tei:correspAction[@type='sent']/tei:date[@when])[1]/@when"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="(//tei:origin//tei:date/(@when|@notAfter) | //tei:correspDesc//tei:date/(@when|@notAfter))">
                    <xsl:sort order="descending"/>
                    <xsl:if test="position()=1">
                        <xsl:value-of select="."/>
                    </xsl:if>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
        
        
    </xsl:template>
    
    
    <xsl:template name="getSummary">
        <xsl:value-of select="//tei:msContents/tei:summary"/>
    </xsl:template>
    
    
    <xsl:template name="createTitle">
        <xsl:choose>
            <xsl:when test="starts-with($fileName, 'B') or //tei:correspDesc">
                <xsl:choose>
                    <xsl:when test="//tei:correspAction[@type='sent']/tei:rs[@type='person']">
                        <xsl:variable name="key">
                            <xsl:choose>
                                <xsl:when test="(//tei:correspAction[@type='sent']/tei:rs[@type='person'])[1]/@key">
                                    <xsl:value-of select="(//tei:correspAction[@type='sent']/tei:rs[@type='person'])[1]/@key"/>
                                </xsl:when>
                                <xsl:when test="(//tei:correspAction[@type='sent']/tei:rs[@type='person'])[1]/@ref">
                                    <xsl:value-of select="(//tei:correspAction[@type='sent']/tei:rs[@type='person'])[1]/@ref"/>
                                </xsl:when>
                            </xsl:choose>
                        </xsl:variable>
                        <xsl:value-of select="uibk:keytoname($key, 'person', 'false')"/>
                        <xsl:if test="count(//tei:correspAction[@type='sent']/tei:rs[@type='person']) &gt; 1">
                            <xsl:text> u.a.</xsl:text>
                        </xsl:if>
                    </xsl:when>
                    <xsl:when test="//tei:titleStmt/tei:author">
                        <xsl:value-of select="//tei:titleStmt/tei:author"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>Unbekannt</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text> an </xsl:text>
                <xsl:choose>
                    <xsl:when test="//tei:correspAction[@type='received']/tei:rs[@type='person']">
                        <xsl:variable name="key">
                            <xsl:choose>
                                <xsl:when test="(//tei:correspAction[@type='received']/tei:rs[@type='person'])[1]/@key">
                                    <xsl:value-of select="(//tei:correspAction[@type='received']/tei:rs[@type='person'])[1]/@key"/>
                                </xsl:when>
                                <xsl:when test="(//tei:correspAction[@type='received']/tei:rs[@type='person'])[1]/@ref">
                                    <xsl:value-of select="(//tei:correspAction[@type='received']/tei:rs[@type='person'])[1]/@ref"/>
                                </xsl:when>
                            </xsl:choose>
                        </xsl:variable>
                        <xsl:value-of select="uibk:keytoname($key, 'person', 'false')"/>
                        <xsl:if test="count(//tei:correspAction[@type='received']/tei:rs[@type='person']) &gt; 1">
                            <xsl:text> u.a.</xsl:text>
                        </xsl:if>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>unbekannt</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text>, </xsl:text>
                <xsl:choose>
                    <xsl:when test="//tei:correspAction[@type='sent']//tei:date[@when]">
                        <xsl:call-template name="toReadableDate">
                            <xsl:with-param name="date" select="(//tei:correspAction[@type='sent']//tei:date[@when])[1]"></xsl:with-param>
                        </xsl:call-template>                        
                    </xsl:when>
                    <xsl:when test="//tei:correspAction[@type='sent']//tei:date[@notBefore-iso or @notAfter-iso or @notBefore or @notAfter or @from or @to]">
                        <xsl:call-template name="toReadableDate">
                            <xsl:with-param name="date" select="(//tei:correspAction[@type='sent']//tei:date[@notBefore-iso or @notAfter-iso or @notBefore or @notAfter or @from or @to])[1]"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>[undatiert]</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>Von Franz Jägerstätter, </xsl:text>
                <xsl:choose>
                    <xsl:when test="//tei:origin//tei:date">
                        <xsl:call-template name="toReadableDate">
                            <xsl:with-param name="date" select="(//tei:origin//tei:date)[1]"></xsl:with-param>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>[undatiert]</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="contains($fileName, '_H')">
                        <xsl:text> (Heft)</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text> (loses Blatt)</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
        
    <xsl:template name="delete0AtBeginning">
        <xsl:param name="text"/>
        <xsl:choose>
            <xsl:when test="starts-with($text, '0')">
                <xsl:value-of select="substring-after($text, '0')"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="toSingleDate">
        <xsl:param name="dateString"/>
        <xsl:choose>
            <xsl:when test="matches($dateString, '^\d+-\d+-\d+$')">
                <xsl:analyze-string select="$dateString" regex="(\d+)-(\d+)-(\d+)">
                    <xsl:matching-substring>
                        <xsl:call-template name="delete0AtBeginning">
                            <xsl:with-param name="text" select="regex-group(3)"/>
                        </xsl:call-template>
                        <xsl:text>.</xsl:text>
                        <xsl:call-template name="delete0AtBeginning">
                            <xsl:with-param name="text" select="regex-group(2)"/>
                        </xsl:call-template>
                        <xsl:text>.</xsl:text>
                        <xsl:value-of select="regex-group(1)"/>
                    </xsl:matching-substring>
                </xsl:analyze-string>
            </xsl:when>
            <xsl:when test="matches($dateString, '^\d+-\d+$')">
                <xsl:analyze-string select="$dateString" regex="(\d+)-(\d+)">
                    <xsl:matching-substring>
                        <xsl:variable name="monthNumber">
                            <xsl:call-template name="delete0AtBeginning">
                                <xsl:with-param name="text" select="regex-group(2)"/>
                            </xsl:call-template>
                        </xsl:variable>
                        <xsl:call-template name="getMonthName">
                            <xsl:with-param name="monthNumber" select="$monthNumber"/>
                        </xsl:call-template>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="regex-group(1)"/>
                    </xsl:matching-substring>
                </xsl:analyze-string>
            </xsl:when>
            <xsl:when test="matches($dateString, '^\d+$')">
                <xsl:analyze-string select="$dateString" regex="(\d+)">
                    <xsl:matching-substring>
                        <xsl:value-of select="regex-group(1)"/>
                    </xsl:matching-substring>
                </xsl:analyze-string>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="toReadableDate">
        <xsl:param name="date"/>
        <xsl:param name="when" select="$date/@when"/>
        <xsl:param name="notBefore">
            <xsl:choose>
                <xsl:when test="$date/@notBefore-iso">
                    <xsl:value-of select="$date/@notBefore-iso"/>
                </xsl:when>
                <xsl:when test="$date/@notBefore">
                    <xsl:value-of select="$date/@notBefore"/>
                </xsl:when>
                <xsl:when test="$date/@from">
                    <xsl:value-of select="$date/@from"/>
                </xsl:when>
            </xsl:choose>
        </xsl:param>
        <xsl:param name="notAfter">
            <xsl:choose>
                <xsl:when test="$date/@notAfter-iso">
                    <xsl:value-of select="$date/@notAfter-iso"/>
                </xsl:when>
                <xsl:when test="$date/@notAfter">
                    <xsl:value-of select="$date/@notAfter"/>
                </xsl:when>
                <xsl:when test="$date/@to">
                    <xsl:value-of select="$date/@to"/>
                </xsl:when>
            </xsl:choose>
        </xsl:param>
        <xsl:choose>
            <xsl:when test="normalize-space($when) != ''">
                <xsl:call-template name="toSingleDate">
                    <xsl:with-param name="dateString" select="$when"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="string-length($notBefore) &gt; 0 and string-length($notAfter) &gt; 0">
                <xsl:text>zw. </xsl:text>
                <xsl:call-template name="toSingleDate">
                    <xsl:with-param name="dateString" select="$notBefore"/>
                </xsl:call-template>
                <xsl:text> u. </xsl:text>
                <xsl:call-template name="toSingleDate">
                    <xsl:with-param name="dateString" select="$notAfter"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="string-length($notBefore) &gt; 0">
                <xsl:text>nach </xsl:text>
                <xsl:call-template name="toSingleDate">
                    <xsl:with-param name="dateString" select="$notBefore"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="string-length($notAfter) &gt; 0">
                <xsl:text>vor </xsl:text>
                <xsl:call-template name="toSingleDate">
                    <xsl:with-param name="dateString" select="$notAfter"/>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="getMonthName">
        <xsl:param name="monthNumber"/>
        <xsl:choose>
            <xsl:when test="$monthNumber = '1'">Januar</xsl:when>
            <xsl:when test="$monthNumber = '2'">Februar</xsl:when>
            <xsl:when test="$monthNumber = '3'">März</xsl:when>
            <xsl:when test="$monthNumber = '4'">April</xsl:when>
            <xsl:when test="$monthNumber = '5'">Main</xsl:when>
            <xsl:when test="$monthNumber = '6'">Juni</xsl:when>
            <xsl:when test="$monthNumber = '7'">Juli</xsl:when>
            <xsl:when test="$monthNumber = '8'">August</xsl:when>
            <xsl:when test="$monthNumber = '9'">September</xsl:when>
            <xsl:when test="$monthNumber = '10'">Oktober</xsl:when>
            <xsl:when test="$monthNumber = '11'">November</xsl:when>
            <xsl:when test="$monthNumber = '12'">Dezember</xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="tei:p" mode="inPhysDesc">
        <xsl:apply-templates mode="inPhysDesc" />
    </xsl:template>
    
    <xsl:template match="text()" mode="inPhysDesc">
        <xsl:if test="matches(., '\^s+.*$')">
            <xsl:text> </xsl:text>
        </xsl:if>
        <xsl:value-of select="normalize-space()"/>
        <xsl:if test="matches(., '^.*\s+$')">
            <xsl:text> </xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template match="tei:objectType" mode="inPhysDesc">
        <xsl:text> (</xsl:text>
        <xsl:apply-templates mode="inPhysDesc"/>
        <xsl:text>)</xsl:text>
    </xsl:template>
    
    <xsl:template match="tei:date[string()='']" mode="inPhysDesc">
        <xsl:call-template name="toReadableDate">
            <xsl:with-param name="date" select="."/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="tei:note" mode="inPhysDesc">
        <xsl:apply-templates mode="inPhysDesc"/>
        <xsl:text>&lt;br /&gt;</xsl:text>
    </xsl:template>
    
    <xsl:template match="tei:physDesc">
        <metadataGroups>
            <groupKey>physDesc</groupKey>
            <xsl:if test="tei:p/tei:objectType">
                <records>
                    <fieldname>Schrifttyp</fieldname>
                    <content>
                        <xsl:apply-templates select="tei:p[tei:objectType]" mode="inPhysDesc"/>
                    </content>
                </records>
            </xsl:if>
            <xsl:if test="tei:p/tei:material">
                <records>
                    <fieldname>Materialität</fieldname>
                    <content>
                        <xsl:apply-templates select="tei:p[tei:material]" mode="inPhysDesc"/>
                    </content>
                </records>
            </xsl:if>
            <xsl:if test=".//tei:supportDesc/tei:extent">
                <records>
                    <fieldname>Umfang</fieldname>
                    <content>
                        <xsl:apply-templates select=".//tei:supportDesc/tei:extent" mode="inPhysDesc"/>
                    </content>
                </records>
            </xsl:if>
        </metadataGroups>
    </xsl:template>
    
    <xsl:template match="tei:titleStmt" mode="respMetadata">
        <xsl:if test="tei:respStmt[contains(tei:name, 'Würthinger')] or 
            tei:respStmt[contains(tei:name, 'Putz')] or 
            tei:respStmt[contains(tei:name, 'Schwung')]">
            <metadataGroups>
                <groupKey>respStmt</groupKey>
                    <records>
                        <fieldname>Editionshinweise</fieldname>
                        <content>
                            <xsl:apply-templates select="tei:respStmt" mode="respMetadata"/>
                        </content>
                    </records>
            </metadataGroups>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="tei:respStmt" mode="respMetadata">
        <xsl:if test="tei:name[contains(., 'Würthinger') or contains(., 'Putz') or contains(., 'Schwung')]">
            <xsl:text>&lt;b&gt;</xsl:text>
            <xsl:apply-templates select="tei:name" mode="inPhysDesc"/>
            <xsl:text>&lt;/b&gt;: </xsl:text>
            <xsl:apply-templates select="tei:resp" mode="inPhysDesc"/>
            <xsl:text>&lt;br/&gt;&lt;br/&gt;</xsl:text>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="tei:history" mode="respMetadata">
        <xsl:choose>
            <xsl:when test="tei:origin | tei:provenance">
                <metadataGroups>
                    <groupKey>history</groupKey>
                    <records>
                        <xsl:if test="tei:origin">
                            <fieldname>Anmerkungen</fieldname>
                            <content>
                                <xsl:apply-templates select="tei:origin" mode="respMetadata"/>
                            </content>
                        </xsl:if>
                        <xsl:if test="tei:provenance">
                            <fieldname>Provenienz</fieldname>
                            <content>
                                <xsl:apply-templates select="tei:provenance" mode="respMetadata"/>
                            </content>
                        </xsl:if>
                    </records>
                    <xsl:if test="tei:origin//tei:date">
                        <records>
                            <fieldname>Datierung</fieldname>
                            <content>
                                <xsl:call-template name="toReadableDate">
                                    <xsl:with-param name="date" select="(.//tei:date)[1]"></xsl:with-param>
                                </xsl:call-template>
                            </content>
                        </records>
                    </xsl:if>
                </metadataGroups>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="tei:correspDesc">
        <metadataGroups>
            <groupKey>correspDesc</groupKey>
            <xsl:apply-templates select="tei:correspAction[@type='sent']"/>
            <xsl:apply-templates select="tei:correspAction[@type='received']"/>
        </metadataGroups>
    </xsl:template>
    
    <xsl:template match="tei:correspAction[@type='sent']">
        <xsl:if test="tei:rs[@type='person']">
            <records>
                <fieldname>Absender:innen</fieldname>
                <content>
                    <xsl:value-of select="string-join(tei:rs[@type='person'], ' / ')"/>
                </content>
                <xsl:for-each select="tei:rs[@type='person']">
                    <externalLinks>
                        <xsl:choose>
                            <xsl:when test="@key">
                                <xsl:value-of select="@key"/>
                            </xsl:when>
                            <xsl:when test="@ref">
                                <xsl:value-of select="@ref"/>
                            </xsl:when>
                        </xsl:choose>
                    </externalLinks>
                </xsl:for-each>
            </records>
        </xsl:if>
        <xsl:if test=".//tei:date">
            <records>
                <fieldname>Absendedatum</fieldname>
                <content>
                    <xsl:choose>
                        <xsl:when test="//tei:correspAction[@type='sent']//tei:date[@when]">
                            <xsl:call-template name="toReadableDate">
                                <xsl:with-param name="date" select="(//tei:correspAction[@type='sent']//tei:date[@when])[1]"></xsl:with-param>
                            </xsl:call-template>                        
                        </xsl:when>
                        <xsl:when test="//tei:correspAction[@type='sent']//tei:date[(@notBefore-iso and @notAfter-iso) or (@notBefore and @notAfter) or (@from and @to)]">
                            <xsl:text>[</xsl:text>
                            <xsl:call-template name="toReadableDate">
                                <xsl:with-param name="date" select="(//tei:correspAction[@type='sent']//tei:date[(@notBefore-iso and @notAfter-iso) or (@notBefore and @notAfter) or (@from and @to)])[1]"></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>]</xsl:text>
                        </xsl:when>
                    </xsl:choose>
                </content>
            </records>
        </xsl:if>
        <xsl:if test="tei:rs[@type='place']">
            <records>
                <fieldname>Absendeort</fieldname>
                <content>
                    <xsl:value-of select="string-join(tei:rs[@type='place'], ' / ')"/>
                </content>
                <xsl:for-each select="tei:rs[@type='place']">
                    <externalLinks>
                        <xsl:choose>
                            <xsl:when test="@key">
                                <xsl:value-of select="@key"/>
                            </xsl:when>
                        </xsl:choose>
                    </externalLinks>
                </xsl:for-each>
            </records>
        </xsl:if>
        <xsl:if test="tei:note">
            <records>
                <fieldname>Zusatz</fieldname>
                <content>
                    <xsl:apply-templates select="tei:note" mode="inPhysDesc"/>
                </content>
                <xsl:for-each select="tei:note/tei:rs[@key]">
                    <externalLinks>
                        <xsl:choose>
                            <xsl:when test="@key">
                                <xsl:value-of select="@key"/>
                            </xsl:when>
                        </xsl:choose>
                    </externalLinks>
                </xsl:for-each>
            </records>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="tei:correspAction[@type='received']">
        <xsl:if test="tei:rs[@type='person']">
            <records>
                <fieldname>Empfänger:innen</fieldname>
                <content>
                    <xsl:value-of select="string-join(tei:rs[@type='person'], ' / ')"/>
                </content>
                <xsl:for-each select="tei:rs[@type='person']">
                    <externalLinks>
                        <xsl:choose>
                            <xsl:when test="@key">
                                <xsl:value-of select="@key"/>
                            </xsl:when>
                            <xsl:when test="@ref">
                                <xsl:value-of select="@ref"/>
                            </xsl:when>
                        </xsl:choose>
                    </externalLinks>
                </xsl:for-each>
            </records>
        </xsl:if>
        <xsl:if test="tei:date/@when">
            <xsl:for-each select="tei:date">
                <records>
                    <fieldname>Empfangsdatum</fieldname>
                    <content>
                        <xsl:value-of select="@when"/>
                    </content>
                </records>
            </xsl:for-each>
        </xsl:if>
        <xsl:if test="tei:rs[@type='place']">
            <records>
                <fieldname>Empfangsort</fieldname>
                <content>
                    <xsl:value-of select="string-join(tei:rs[@type='place'], ' / ')"/>
                </content>
                <xsl:for-each select="tei:rs[@type='place']">
                    <externalLinks>
                        <xsl:choose>
                            <xsl:when test="@key">
                                <xsl:value-of select="@key"/>
                            </xsl:when>
                        </xsl:choose>
                    </externalLinks>
                </xsl:for-each>
            </records>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="tei:msIdentifier">
        <metadataGroups>
            <groupKey>msIdentifier</groupKey>
            <records>
                <fieldname>RepositorySignatur</fieldname>
                <xsl:choose>
                    <xsl:when test="tei:institution and tei:repository">
                        <content>
                            <xsl:value-of select="tei:institution"/>
                            <xsl:text>, </xsl:text>
                            <xsl:value-of select="tei:repository"/>
                            <xsl:text> / </xsl:text>
                            <xsl:value-of select="tei:idno"/>
                        </content>
                    </xsl:when>
                    <xsl:when test="not(tei:institution) and tei:collection">
                        <content>
                            <xsl:value-of select="tei:collection"/>
                            <xsl:text> / </xsl:text>
                            <xsl:value-of select="tei:idno"/>
                        </content>
                    </xsl:when>
                    <xsl:when test="tei:institution">
                        <content>
                            <xsl:value-of select="tei:institution"/>
                            <xsl:text> / </xsl:text>
                            <xsl:value-of select="tei:idno"/>
                        </content>
                    </xsl:when>
                    <xsl:otherwise>
                        <content>
                            <xsl:text>Unbekannt / </xsl:text>
                            <xsl:value-of select="tei:idno"/>
                        </content>
                    </xsl:otherwise>
                </xsl:choose>
            </records>
            <xsl:if test="tei:institution">
                <records>
                    <fieldname>institution</fieldname>
                    <content>
                        <xsl:value-of select="tei:institution/normalize-space()"/>
                    </content>
                </records>
            </xsl:if>
            <xsl:if test="tei:repository">
                <records>
                    <fieldname>repository</fieldname>
                    <content>
                        <xsl:value-of select="tei:repository/normalize-space()"/>
                    </content>
                </records>
            </xsl:if>
            <xsl:if test="tei:collection">
                <records>
                    <fieldname>collection</fieldname>
                    <content>
                        <xsl:value-of select="tei:collection/normalize-space()"/>
                    </content>
                </records>
            </xsl:if>
            <xsl:if test="tei:idno">
                <records>
                    <fieldname>Signatur</fieldname>
                    <content>
                        <xsl:value-of select="tei:idno/normalize-space()"/>
                    </content>
                </records>
            </xsl:if>
            <xsl:if test="tei:altIdentifier">
                <records>
                    <fieldname>Alte Signatur</fieldname>
                    <content>
                        <xsl:value-of select="tei:altIdentifier/normalize-space()"/>
                    </content>
                </records>
            </xsl:if>
        </metadataGroups>
    </xsl:template>
    
</xsl:stylesheet>
