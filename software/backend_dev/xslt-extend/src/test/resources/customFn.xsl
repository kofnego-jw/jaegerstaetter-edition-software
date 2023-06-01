<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:uibk="http://www.uibk.ac.at/brenner-archiv/xml/ns"
                exclude-result-prefixes="xs"
                version="2.0">

    <xsl:param name="searchJson"/>
    <xsl:param name="startTag">&lt;span class="highlight"&gt;</xsl:param>
    <xsl:param name="endTag">&lt;/span&gt;</xsl:param>

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <result>
            <xsl:apply-templates/>
        </result>
    </xsl:template>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="text()">
        <xsl:copy-of select="parse-xml-fragment(uibk:highlight($searchJson, ., $startTag, $endTag))"/>
    </xsl:template>

    <xsl:template match="key">
        <a>
            <xsl:attribute name="name" select="uibk:keytoname(@key, 'person', 'true')"/>
            <xsl:apply-templates/>
        </a>
    </xsl:template>


</xsl:stylesheet>
