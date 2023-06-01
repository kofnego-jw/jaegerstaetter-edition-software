<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:tei="http://www.tei-c.org/ns/1.0"
                exclude-result-prefixes="tei"
                version="2.0">

    <xsl:output method="xml" indent="yes"/>

    <xsl:param name="documentId"/>
    <xsl:param name="filePath"/>

    <xsl:template match="/">
        <IndexDocument>
            <documentId>
                <xsl:value-of select="$documentId"/>
            </documentId>
            <resourceId>
                <xsl:value-of select="$filePath"/>
            </resourceId>
            <xsl:call-template name="createAllField"/>
            <xsl:call-template name="createTranscriptField"/>
            <xsl:call-template name="createCommentField"/>
            <xsl:call-template name="createDatingField"/>
            <xsl:call-template name="createRegistryCorporationField"/>
            <xsl:call-template name="createRegistryPersonField"/>
            <xsl:call-template name="createRegistryPlaceField"/>
            <xsl:call-template name="createRegistrySaintField"/>
            
            <!-- <xsl:call-template name="createResourceFWFields"/> -->
        </IndexDocument>
    </xsl:template>

    <xsl:template name="createTranscriptField">
        <fields>
            <fieldname>TRANSCRIPT</fieldname>
            <xsl:for-each select="//tei:div[@type='Lesefassung']">
                <xsl:variable name="content">
                    <xsl:apply-templates select="." mode="getNormalizedText"/>
                </xsl:variable>
                <xsl:if test="$content!=''">
                    <contents>
                        <xsl:value-of select="normalize-space($content)"/>
                    </contents>
                </xsl:if>
            </xsl:for-each>
        </fields>
    </xsl:template>

    <xsl:template name="createCommentField">
        <fields>
            <fieldname>COMMENTARY</fieldname>
            <xsl:for-each select="//tei:div[@type='Lesefassung']">
                <xsl:variable name="content">
                    <xsl:apply-templates select="." mode="getCommentText"/>
                </xsl:variable>
                <xsl:if test="$content!=''">
                    <contents>
                        <xsl:value-of select="normalize-space($content)"/>
                    </contents>
                </xsl:if>
            </xsl:for-each>
            <xsl:for-each select="//tei:teiHeader//tei:summary">
                <xsl:variable name="summary">
                    <xsl:apply-templates select="." mode="#default"/>
                </xsl:variable>
                <contents>
                    <xsl:value-of select="normalize-space($summary)"/>
                </contents>
            </xsl:for-each>
            <xsl:for-each select="//tei:teiHeader//tei:origin">
                <xsl:variable name="origin">
                    <xsl:apply-templates select="." mode="addSpaceAroundNote"/>
                </xsl:variable>
                <contents>
                    <xsl:value-of select="normalize-space($origin)"/>
                </contents>
            </xsl:for-each>
            <xsl:for-each select="//tei:physDesc">
                <xsl:variable name="content">
                    <xsl:apply-templates select="." mode="addSpaceAroundNote"/>
                </xsl:variable>
                <xsl:if test="$content!=''">
                    <contents>
                        <xsl:value-of select="normalize-space($content)"/>
                    </contents>
                </xsl:if>
            </xsl:for-each>
        </fields>
    </xsl:template>

    <xsl:template name="createAllField">
        <fields>
            <fieldname>ALL</fieldname>
            <xsl:for-each select="//tei:div[@type='Lesefassung']">
                <xsl:variable name="content">
                    <xsl:apply-templates select="." mode="getNormalizedText"/>
                </xsl:variable>
                <xsl:if test="$content!=''">
                    <contents>
                        <xsl:value-of select="normalize-space($content)"/>
                    </contents>
                </xsl:if>
                <xsl:variable name="content2">
                    <xsl:apply-templates select="." mode="getCommentText"/>
                </xsl:variable>
                <xsl:if test="$content2!=''">
                    <contents>
                        <xsl:value-of select="normalize-space($content2)"/>
                    </contents>
                </xsl:if>
            </xsl:for-each>
            <xsl:for-each select="//tei:teiHeader//tei:summary">
                <xsl:variable name="summary">
                    <xsl:apply-templates select="." mode="#default"/>
                </xsl:variable>
                <contents>
                    <xsl:value-of select="normalize-space($summary)"/>
                </contents>
            </xsl:for-each>
            <xsl:for-each select="//tei:teiHeader//tei:origin">
                <xsl:variable name="origin">
                    <xsl:apply-templates select="." mode="addSpaceAroundNote"/>
                </xsl:variable>
                <contents>
                    <xsl:value-of select="normalize-space($origin)"/>
                </contents>
            </xsl:for-each>
            <xsl:for-each select="//tei:physDesc">
                <xsl:variable name="content">
                    <xsl:apply-templates select="." mode="addSpaceAroundNote"/>
                </xsl:variable>
                <xsl:if test="$content!=''">
                    <contents>
                        <xsl:value-of select="normalize-space($content)"/>
                    </contents>
                </xsl:if>
            </xsl:for-each>
        </fields>
    </xsl:template>
    
    <xsl:template name="getDate">
        <xsl:param name="dateElement"/>
        <xsl:choose>
            <xsl:when test="$dateElement/@when">
                <xsl:value-of select="$dateElement/@when"/>
            </xsl:when>
            <xsl:when test="$dateElement/@notBefore">
                <xsl:value-of select="$dateElement/@notBefore"/>
            </xsl:when>
            <xsl:when test="$dateElement/@notBefore-iso">
                <xsl:value-of select="$dateElement/@notBefore-iso"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="createResourceFWFields">
        <!-- <fields>
            <fieldname>RESOURCE_CORPUS</fieldname>
            <xsl:if test="//tei:correspAction[@type = 'sent']//tei:rs[@ref = '#P_0001'] or not(//tei:correspDesc)">
                <contents>
                    <xsl:text>FROM_FRANZ</xsl:text>
                </contents>
            </xsl:if>
            <xsl:if test="//tei:correspAction[@type='received']//tei:rs[@ref='#P_0001']">
                <contents>
                    <xsl:text>TO_FRANZ</xsl:text>
                </contents>
            </xsl:if>
            <xsl:if
                test="//tei:correspDesc and not(//tei:correspAction[@type = 'received']//tei:rs[@ref = '#P_0001']) and not(//tei:correspAction[@type = 'sent']//tei:rs[@ref = '#P_0001'])">
                <contents>
                    <xsl:text>ABOUT_FRANZ</xsl:text>
                </contents>
            </xsl:if>
            <xsl:if test="//tei:correspDesc">
                <contents>
                    <xsl:text>CORRESPONDENCE</xsl:text>
                </contents>
            </xsl:if>
        </fields> -->
        <!-- 
            Timeline
            1940-06-16: EARLY
            1941-09-04: MILITARY
            1943-02-27: TO_REFUSAL
            1943-08-09: PRISON
                        AFTERLIFE
        -->
        <!-- <fields>
            <fieldname>RESOURCE_PERIOD</fieldname>
            <xsl:variable name="notBefore">
                <xsl:call-template name="findEarliest"/>
            </xsl:variable>
            <xsl:variable name="notAfter">
                <xsl:call-template name="findLatest"/>
            </xsl:variable>
            <xsl:choose>
                <xsl:when test="$notBefore &lt;= '1940-06-16'">
                    <contents>
                        <xsl:text>EARLY</xsl:text>
                    </contents>
                    <xsl:if test="$notAfter &gt; '1940-06-16'">
                        <contents>
                            <xsl:text>MILITARY</xsl:text>
                        </contents>
                    </xsl:if>
                    <xsl:if test="$notAfter &gt; '1941-09-04'">
                        <contents>
                            <xsl:text>TO_REFUSAL</xsl:text>
                        </contents>
                    </xsl:if>
                    <xsl:if test="$notAfter &gt; '1943-02-27'">
                        <contents>
                            <xsl:text>PRISON</xsl:text>
                        </contents>
                    </xsl:if>
                    <xsl:if test="$notAfter &gt; '1943-08-09'">
                        <contents>
                            <xsl:text>AFTERLIFE</xsl:text>
                        </contents>
                    </xsl:if>
                </xsl:when>
                <xsl:when test="$notBefore &lt;= '1941-09-04'">
                    <contents>
                        <xsl:text>MILITARY</xsl:text>
                    </contents>
                    <xsl:if test="$notAfter &gt; '1941-09-04'">
                        <contents>
                            <xsl:text>TO_REFUSAL</xsl:text>
                        </contents>
                    </xsl:if>
                    <xsl:if test="$notAfter &gt; '1943-02-27'">
                        <contents>
                            <xsl:text>PRISON</xsl:text>
                        </contents>
                    </xsl:if>
                    <xsl:if test="$notAfter &gt; '1943-08-09'">
                        <contents>
                            <xsl:text>AFTERLIFE</xsl:text>
                        </contents>
                    </xsl:if>
                </xsl:when>
                <xsl:when test="$notBefore &lt;= '1943-02-27'">
                    <contents>
                        <xsl:text>TO_REFUSAL</xsl:text>
                    </contents>
                    <xsl:if test="$notAfter &gt; '1943-02-27'">
                        <contents>
                            <xsl:text>PRISON</xsl:text>
                        </contents>
                    </xsl:if>
                    <xsl:if test="$notAfter &gt; '1943-08-09'">
                        <contents>
                            <xsl:text>AFTERLIFE</xsl:text>
                        </contents>
                    </xsl:if>
                </xsl:when>
                <xsl:when test="$notBefore &lt;= '1943-08-09'">
                    <contents>
                        <xsl:text>PRISON</xsl:text>
                    </contents>
                    <xsl:if test="$notAfter &gt; '1943-08-09'">
                        <contents>
                            <xsl:text>AFTERLIFE</xsl:text>
                        </contents>
                    </xsl:if>                    
                </xsl:when>
                <xsl:when test="$notBefore &gt; '1943-08-09'">
                    <contents>
                        <xsl:text>AFTERLIFE</xsl:text>
                    </contents>
                </xsl:when>
            </xsl:choose>
        </fields> -->
        
        <!-- <fields>
            <fieldname>RESOURCE_AUTHOR</fieldname>
            <xsl:for-each select="//tei:correspAction[@type='sent']//tei:rs[@type='person']">
                <contents>
                    <xsl:value-of select="normalize-space(.)"/>
                </contents>
            </xsl:for-each>
            <xsl:if test="not(//tei:correspAction[@type='sent'])">
                <contents>Jägerstätter, Franz</contents>
            </xsl:if>
        </fields> -->
       
        <!-- <fields>
            <fieldname>RESOURCE_RECIPIENT</fieldname>
            <xsl:for-each select="//tei:correspAction[@type='received']//tei:rs[@type='person']">
                <contents>
                    <xsl:value-of select="normalize-space(.)"/>
                </contents>
            </xsl:for-each>
        </fields> -->
        
        <!-- <fields>
            <fieldname>RESOURCE_PLACE</fieldname>
            <xsl:for-each select="//tei:correspAction[@type='sent']//tei:rs[@type='place']">
                <contents>
                    <xsl:value-of select="normalize-space(.)"/>
                </contents>
            </xsl:for-each>
        </fields> -->

        <!-- <fields>
            <fieldname>RESOURCE_OBJECTTYPE</fieldname>
            <xsl:for-each select="//tei:physDesc//tei:p[.//tei:objectType]">
                <contents>
                    <xsl:variable name="mainString">
                        <xsl:apply-templates mode="notInObjectType"/>
                    </xsl:variable>
                    <xsl:value-of select="normalize-space($mainString)"/>
                </contents>
            </xsl:for-each>
            <contents>
                <xsl:value-of select="//tei:objectType"/>
            </contents>
        </fields> -->
        
        
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
        <xsl:for-each select="(//tei:origin//tei:date/(@when|@notAfter) | //tei:correspDesc//tei:date/(@when|@notAfter))">
            <xsl:sort order="descending"/>
            <xsl:if test="position()=1">
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
    

    <xsl:template name="createDatingField">
        <fields>
            <fieldname>DATING</fieldname>
            <contents>
                <xsl:choose>
                    <xsl:when test="//tei:correspAction[@type='sent']//tei:date">
                        <xsl:variable name="dateElement" select="(//tei:correspAction[@type='sent']//tei:date)[1]"/>
                        <xsl:call-template name="getDate">
                            <xsl:with-param name="dateElement" select="$dateElement"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:when test="//tei:history/tei:date">
                        <xsl:variable name="dateElement" select="(//tei:history//tei:date)[1]"/>
                        <xsl:call-template name="getDate">
                            <xsl:with-param name="dateElement" select="$dateElement"/>
                        </xsl:call-template>
                    </xsl:when>
                </xsl:choose>
            </contents>
        </fields>
    </xsl:template>

    <xsl:template name="createRegistryCorporationField">
        <fields>
            <fieldname>REGISTRY_CORPORATION</fieldname>
            <xsl:for-each select="//tei:rs[@type='org' and @key]/@key">
                <xsl:variable name="content" select="normalize-space()"/>
                <xsl:if test="$content!=''">
                    <contents>
                        <xsl:value-of select="$content"/>
                    </contents>
                </xsl:if>
            </xsl:for-each>
        </fields>
    </xsl:template>
    <xsl:template name="createRegistryPersonField">
        <fields>
            <fieldname>REGISTRY_PERSON</fieldname>
            <xsl:for-each select="//tei:rs[@type='person' and @ref]/@ref">
                <xsl:variable name="content" select="tokenize(., '\s+')"/>
                <xsl:for-each select="$content">
                    <contents>
                        <xsl:value-of select="normalize-space(substring-after(., '#'))"/>
                    </contents>
                </xsl:for-each>
            </xsl:for-each>
            <xsl:for-each select="//tei:rs[@type='person' and @key[not(starts-with(., 'H_'))]]/@key">
                <xsl:variable name="content" select="tokenize(., '\s*;\s*')"/>
                <xsl:for-each select="$content">
                    <contents>
                        <xsl:value-of select="normalize-space()"/>
                    </contents>
                </xsl:for-each>
            </xsl:for-each>
        </fields>
    </xsl:template>
    <xsl:template name="createRegistryPlaceField">
        <fields>
            <fieldname>REGISTRY_PLACE</fieldname>
            <xsl:for-each select="//tei:rs[@type='place' and @key]/@key">
                <xsl:variable name="content" select="normalize-space()"/>
                <xsl:if test="$content!=''">
                    <contents>
                        <xsl:value-of select="$content"/>
                    </contents>
                </xsl:if>
            </xsl:for-each>
        </fields>
    </xsl:template>
    <xsl:template name="createRegistrySaintField">
        <fields>
            <fieldname>REGISTRY_SAINT</fieldname>
            <xsl:for-each select="//tei:rs[@type='person' and @key[starts-with(.,'H_')]]/@key">
                <xsl:variable name="content" select="normalize-space()"/>
                <xsl:if test="$content!=''">
                    <contents>
                        <xsl:value-of select="$content"/>
                    </contents>
                </xsl:if>
            </xsl:for-each>
        </fields>
    </xsl:template>
    
    <xsl:template match="tei:*" mode="getNormalizedText">
        <xsl:apply-templates mode="getNormalizedText"/>
    </xsl:template>
    
    <xsl:template match="tei:note" mode="getNormalizedText">
        <!-- Ignored -->
    </xsl:template>
    
    <xsl:template match="tei:*" mode="getCommentText">
        <xsl:apply-templates mode="getCommentText"/>
    </xsl:template>
    
    <xsl:template match="text()" mode="getCommentText">
        <!-- Ignored -->
    </xsl:template>
    
    <xsl:template match="tei:note"  mode="getCommentText">
        <xsl:text> </xsl:text>
        <xsl:apply-templates mode="getCommentText"/>
        <xsl:text> </xsl:text>
    </xsl:template>
    
    <xsl:template match="text()[ancestor::tei:note]" mode="getCommentText">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="tei:*" mode="addSpaceAroundNote">
        <xsl:apply-templates mode="addSpaceAroundNote"/>
    </xsl:template>
    
    <xsl:template match="tei:note|tei:objectType" mode="addSpaceAroundNote">
        <xsl:text> </xsl:text>
        <xsl:apply-templates mode="addSpaceAroundNote"/>
        <xsl:text> </xsl:text>
    </xsl:template>


</xsl:stylesheet>
