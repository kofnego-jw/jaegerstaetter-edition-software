<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:tei="http://www.tei-c.org/ns/1.0"
    xmlns:uibk="http://www.uibk.ac.at/brenner-archiv/xml/ns"
    xmlns="http://www.tei-c.org/ns/1.0"
    exclude-result-prefixes="xs tei uibk"
    version="2.0">
    
    <xsl:output method="xml" indent="no"/>
    
    <xsl:param name="includeHeader">false</xsl:param>
    
    <xsl:param name="versions">&lt;VersionInfoList&gt;&lt;list&gt;&lt;list&gt;&lt;versionNumber&gt;2&lt;/versionNumber&gt;&lt;creationTimestamp&gt;2023-04-07T10:22&lt;/creationTimestamp&gt;&lt;/list&gt;&lt;list&gt;&lt;versionNumber&gt;1&lt;/versionNumber&gt;&lt;creationTimestamp&gt;2023-04-05T10:22&lt;/creationTimestamp&gt;&lt;/list&gt;&lt;/list&gt;&lt;/VersionInfoList&gt;</xsl:param>
    
    <xsl:variable name="versionList" select="parse-xml($versions)"/>
    
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="@*|tei:*|text()">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="comment()">
        <!-- Skip this -->
    </xsl:template>
    
    <xsl:template match="processing-instruction()">
        <!-- Skip this -->
    </xsl:template>
    
    <xsl:template match="tei:teiHeader">
        <xsl:choose>
            <xsl:when test="$includeHeader='true'">
                <xsl:copy>
                    <xsl:apply-templates select="@*"/>
                    <xsl:apply-templates />
                </xsl:copy>
            </xsl:when>
            <xsl:otherwise>
                <teiHeader>
                    <xsl:comment>Die gesamte XML-TEI Datei inklusive TEI header kann im Downloadbereich abgerufen werden.</xsl:comment>
                </teiHeader>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="tei:rs[@type='person' and @ref]">
        <xsl:copy>
            <xsl:variable name="additional">
                <xsl:if test="@key">
                    <xsl:value-of select="@key"/>
                    <xsl:text>; </xsl:text>
                </xsl:if>
                <xsl:for-each select="tokenize(@ref, ' ')">
                    <xsl:variable name="key" select="substring-after(., '#')"/>
                    <xsl:value-of select="uibk:keytoname($key, 'person', 'true')"/>
                    <xsl:if test="not(position()=last())">
                        <xsl:text>; </xsl:text>
                    </xsl:if>
                </xsl:for-each>
            </xsl:variable>
            <xsl:attribute name="key" select="$additional"/>
            <xsl:apply-templates select="@*[name()!='ref' and name()!='key']"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="tei:encodingDesc">
        <!-- Ignore -->
    </xsl:template>
    
    <xsl:template match="tei:revisionDesc">
        <xsl:copy>
            <xsl:apply-templates select="$versionList"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="VersionInfoList">
        <xsl:for-each select=".//list/list">
            <change>
                <xsl:attribute name="n" select="versionNumber"/>
                <xsl:text>Document changed on </xsl:text>
                <date>
                    <xsl:attribute name="when" select="creationTimestamp"/>
                    <xsl:value-of select="creationTimestamp"/>
                </date>
            </change>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="tei:opener[ancestor::tei:div[@type='diplomatische_Umschrift']]|tei:closer[ancestor::tei:div[@type='diplomatische_Umschrift']]">
        <p>
            <xsl:apply-templates mode="noSalute"/>
        </p>
    </xsl:template>
    
    <xsl:template match="tei:dateline" mode="noSalute">
        <xsl:apply-templates/>
        <lb/>
    </xsl:template>
    
    <xsl:template match="tei:salute" mode="noSalute">
        <xsl:apply-templates/>
    </xsl:template>
        
</xsl:stylesheet>
