<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:tei="http://www.tei-c.org/ns/1.0"
                xmlns:uibk="http://www.uibk.ac.at/brenner-archiv/xml/ns"
                xmlns="http://www.w3.org/1999/xhtml"
                exclude-result-prefixes="xs tei uibk"
                version="2.0">
    
    <xsl:param name="searchJson"/>
    <xsl:param name="startTag">&lt;span class="highlight"&gt;</xsl:param>
    <xsl:param name="endTag">&lt;/span&gt;</xsl:param>

    <xsl:include href="shared.xsl"/>
    
    <xsl:variable name="displayMode">norm</xsl:variable>

    <xsl:output method="html" indent="no"/>

    <xsl:template match="/">
        <div class="tei">
            <h2>Lesefassung</h2>
            <div class="text">
                <xsl:apply-templates select="//tei:div[@type='Lesefassung']"/>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:copy-of select="parse-xml-fragment(uibk:highlight($searchJson, ., $startTag, $endTag))"/>
    </xsl:template>


</xsl:stylesheet>
