<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:tei="http://www.tei-c.org/ns/1.0"
    exclude-result-prefixes="xs tei"
    version="2.0">
    
    <xsl:template name="createScrollAnchor">
        <xsl:param name="id"/>
        <ffji-anchor>
            <xsl:attribute name="id" select="$id"/>
        </ffji-anchor>
    </xsl:template>
    
    <xsl:template name="createScrollButton">
        <xsl:param name="aimId"/>
        <xsl:param name="content"/>
        <ffji-viewanchor>
            <xsl:attribute name="aim" select="$aimId"/>
            <xsl:value-of select="$content"/>
        </ffji-viewanchor>
    </xsl:template>
    
    <xsl:template name="createDivId">
        <xsl:param name="div"/>
        <xsl:choose>
            <xsl:when test="$div/@xml:id">
                <xsl:value-of select="$div/@xml:id"/>
            </xsl:when>
            <xsl:when test="$div/@corresp">
                <xsl:value-of select="replace($div/@corresp, '[^A-Za-z0-9]', '_')"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>ID_</xsl:text>
                <xsl:value-of select="count($div/preceding::tei:div) + 1"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
        
</xsl:stylesheet>
